package com.github.ethanicuss.astraladditions.blocks.customhopper;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomHopperBlockEntity extends LootableContainerBlockEntity implements Hopper {
    private DefaultedList<ItemStack> inventory;
    private int transferCooldown;
    private long lastTickTime;
    private int maxCooldown;
    private boolean acceptsItemEntities;
    private int itemRate;

    public CustomHopperBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, getCustomHopperBlock(state).getBlockEntityTypeId(), getCustomHopperBlock(state).getCooldown(), getCustomHopperBlock(state).getItemRate());
    }

    public CustomHopperBlockEntity(BlockPos pos, BlockState state, String blockEntityTypeId, int cooldown, int itemRate) {
        super(Registry.BLOCK_ENTITY_TYPE.get(new Identifier(AstralAdditions.MOD_ID, blockEntityTypeId)), pos, state);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.transferCooldown = -1;
        this.maxCooldown = cooldown;
        this.acceptsItemEntities = true;
        this.itemRate = itemRate;
    }

    private static CustomHopperBlock getCustomHopperBlock(BlockState state) {
        if (!(state.getBlock() instanceof CustomHopperBlock)) {
            throw new IllegalArgumentException("CustomHopperBlockEntity created for non-CustomHopperBlock: " + state.getBlock());
        }
        return (CustomHopperBlock) state.getBlock();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);

        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }

        if (nbt.contains("TransferCooldown")) {
            this.transferCooldown = nbt.getInt("TransferCooldown");
        }
        if (nbt.contains("AcceptsItemEntities")) {
            this.acceptsItemEntities = nbt.getBoolean("AcceptsItemEntities");
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }

        nbt.putInt("TransferCooldown", this.transferCooldown);
        nbt.putBoolean("AcceptsItemEntities", this.acceptsItemEntities);
    }

    public boolean getAcceptsItemEntities() {
        return this.acceptsItemEntities;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        this.checkLootInteraction((PlayerEntity) null);
        return Inventories.splitStack(this.getInvStackList(), slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.checkLootInteraction((PlayerEntity) null);
        this.getInvStackList().set(slot, stack);

        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.hopper");
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, CustomHopperBlockEntity blockEntity) {
        --blockEntity.transferCooldown;
        blockEntity.lastTickTime = world.getTime();

        if (!blockEntity.needsCooldown()) {
            blockEntity.setTransferCooldown(0);
            insertAndExtract(world, pos, state, blockEntity, () -> extract(world, blockEntity, blockEntity.itemRate));
        }
    }

    private static boolean insertAndExtract(World world, BlockPos pos, BlockState state, CustomHopperBlockEntity blockEntity, BooleanSupplier extractSupplier) {
        if (world.isClient || blockEntity.needsCooldown()) {
            return false;
        }

        boolean moved = false;

        if (!blockEntity.isEmpty()) {
            moved = insert(world, pos, state, blockEntity, blockEntity.itemRate);
        }

        if (!blockEntity.isFull()) {
            moved |= extractSupplier.getAsBoolean();
        }

        if (moved) {
            blockEntity.setTransferCooldown(blockEntity.maxCooldown);
            markDirty(world, pos, state);
            return true;
        }

        return false;
    }

    private boolean isFull() {
        for (ItemStack stack : this.inventory) {
            if (stack.isEmpty() || stack.getCount() < Math.min(stack.getMaxCount(), this.getMaxCountPerStack())) {
                return false;
            }
        }
        return true;
    }

    private static boolean insert(World world, BlockPos pos, BlockState state, CustomHopperBlockEntity hopper, int itemRate) {
        Direction outputDirection = state.get(HopperBlock.FACING);
        Direction targetSide = outputDirection.getOpposite();
        BlockPos targetPos = pos.offset(outputDirection);

        Storage<ItemVariant> targetStorage = findBlockItemStorage(world, targetPos, targetSide);

        if (targetStorage != null) {
            Storage<ItemVariant> sourceStorage = InventoryStorage.of(hopper, null);

            BlockEntity targetBlockEntity = world.getBlockEntity(targetPos);
            boolean destinationCustomHopperWasEmpty = targetBlockEntity instanceof CustomHopperBlockEntity && ((CustomHopperBlockEntity) targetBlockEntity).isEmpty();

            long moved = moveOneResourceBatch(sourceStorage, targetStorage, itemRate);

            if (moved > 0) {
                if (destinationCustomHopperWasEmpty && targetBlockEntity instanceof CustomHopperBlockEntity) {
                    applyDestinationHopperCooldown(hopper, (CustomHopperBlockEntity) targetBlockEntity);
                }
                return true;
            }
            return false;
        }

        Inventory targetInventory = getOutputInventory(world, pos, state);
        if (targetInventory == null) {
            return false;
        }

        return insertIntoVanillaInventory(hopper, targetInventory, targetSide, itemRate);
    }

    private static long moveOneResourceBatch(Storage<ItemVariant> source, Storage<ItemVariant> target, long maxAmount) {
        if (source == null || target == null || maxAmount <= 0) {
            return 0;
        }

        try (Transaction outer = Transaction.openOuter()) {
            Set<ItemVariant> triedResources = new HashSet<>();

            for (StorageView<ItemVariant> view : source.iterable(outer)) {
                if (view.isResourceBlank()) {
                    continue;
                }

                ItemVariant resource = view.getResource();
                if (!triedResources.add(resource)) {
                    continue;
                }

                long extractable;

                try (Transaction extractionTest = outer.openNested()) {
                    extractable = source.extract(resource, maxAmount, extractionTest);
                    extractionTest.abort();
                }

                if (extractable <= 0) {
                    continue;
                }

                try (Transaction transfer = outer.openNested()) {
                    long accepted = target.insert(resource, extractable, transfer);
                    if (accepted <= 0) {
                        continue;
                    }

                    long extracted = source.extract(resource, accepted, transfer);

                    if (extracted != accepted) {
                        continue;
                    }

                    transfer.commit();
                    outer.commit();
                    return accepted;
                }
            }
        }

        return 0;
    }

    private static boolean insertIntoVanillaInventory(Inventory source, Inventory target, Direction targetSide, int itemRate) {
        if (itemRate <= 0) {
            return false;
        }

        boolean movedAny = false;
        int remaining = itemRate;

        while (remaining > 0 && !isInventoryFull(target, targetSide)) {
            boolean movedOne = false;

            for (int slot = 0; slot < source.size(); ++slot) {
                if (source.getStack(slot).isEmpty()) {
                    continue;
                }

                ItemStack original = source.getStack(slot).copy();
                ItemStack leftover = transfer(source, target, source.removeStack(slot, 1), targetSide);

                if (leftover.isEmpty()) {
                    target.markDirty();
                    movedOne = true;
                    movedAny = true;
                    --remaining;
                    break;
                }

                source.setStack(slot, original);
            }

            if (!movedOne) {
                break;
            }
        }

        return movedAny;
    }

    private static void applyDestinationHopperCooldown(CustomHopperBlockEntity source, CustomHopperBlockEntity destination) {
        if (destination.isDisabled()) {
            return;
        }

        int cooldownOffset = destination.lastTickTime >= source.lastTickTime ? 1 : 0;
        destination.setTransferCooldown(destination.maxCooldown - cooldownOffset);
    }

    private static IntStream getAvailableSlots(Inventory inventory, Direction side) {
        return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory) inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
    }

    private static boolean isInventoryFull(Inventory inventory, Direction direction) {
        return getAvailableSlots(inventory, direction).allMatch(slot -> {
            ItemStack stack = inventory.getStack(slot);
            return stack.getCount() >= Math.min(stack.getMaxCount(), inventory.getMaxCountPerStack());
        });
    }

    private static boolean isInventoryEmpty(Inventory inventory, Direction direction) {
        return getAvailableSlots(inventory, direction).allMatch(slot -> inventory.getStack(slot).isEmpty());
    }

    public static boolean extract(World world, CustomHopperBlockEntity hopper, int itemRate) {
        BlockPos sourcePos = hopper.getPos().up();

        Storage<ItemVariant> sourceStorage = findBlockItemStorage(world, sourcePos, Direction.DOWN);

        if (sourceStorage != null) {
            Storage<ItemVariant> targetStorage = InventoryStorage.of(hopper, null);

            long moved = moveOneResourceBatch(sourceStorage, targetStorage, itemRate);

            return moved > 0;
        }

        Inventory inventory = getInputInventory(world, hopper);
        if (inventory != null) {
            Direction direction = Direction.DOWN;

            if (isInventoryEmpty(inventory, direction)) {
                return false;
            }

            if (itemRate <= 0) {
                return false;
            }

            int remaining = itemRate;
            boolean movedAny = false;

            while (remaining > 0) {
                boolean movedOne = false;

                for (int slot : getAvailableSlots(inventory, direction).toArray()) {
                    if (extractOne(hopper, inventory, slot, direction)) {
                        movedOne = true;
                        movedAny = true;
                        --remaining;
                        break;
                    }
                }

                if (!movedOne) {
                    break;
                }
            }

            return movedAny;
        }

        for (ItemEntity itemEntity : getInputItemEntities(world, hopper)) {
            if (extract(hopper, itemEntity, hopper.itemRate)) {
                return true;
            }
        }

        return false;
    }

    private static boolean extractOne(Hopper hopper, Inventory inventory, int slot, Direction side) {
        ItemStack stack = inventory.getStack(slot);

        if (stack.isEmpty() || !canExtract(inventory, stack, slot, side)) {
            return false;
        }

        ItemStack original = stack.copy();
        ItemStack leftover = transfer(inventory, hopper, inventory.removeStack(slot, 1), null);

        if (leftover.isEmpty()) {
            inventory.markDirty();
            return true;
        }

        inventory.setStack(slot, original);
        return false;
    }

    public static boolean extract(Inventory inventory, ItemEntity itemEntity, int itemRate) {
        ItemStack entityStack = itemEntity.getStack();
        if (entityStack.isEmpty()) {
            return false;
        }

        if (itemRate <= 0) {
            return false;
        }

        int requested = Math.min(itemRate, entityStack.getCount());
        ItemStack moving = entityStack.copy();
        moving.setCount(requested);

        ItemStack leftover = transfer(null, inventory, moving, null);
        int inserted = requested - leftover.getCount();

        if (inserted <= 0) {
            return false;
        }

        entityStack.decrement(inserted);

        if (entityStack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setStack(entityStack);
        }

        inventory.markDirty();
        return true;
    }

    @Nullable
    private static Storage<ItemVariant> findBlockItemStorage(World world, BlockPos pos, @Nullable Direction side) {
        return ItemStorage.SIDED.find(world, pos, side);
    }

    public static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, @Nullable Direction side) {
        if (to instanceof SidedInventory && side != null) {
            int[] slots = ((SidedInventory) to).getAvailableSlots(side);

            for (int slot : slots) {
                if (stack.isEmpty()) {
                    break;
                }
                stack = transfer(from, to, stack, slot, side);
            }
        } else {
            for (int slot = 0; slot < to.size() && !stack.isEmpty(); ++slot) {
                stack = transfer(from, to, stack, slot, side);
            }
        }

        return stack;
    }

    private static boolean canInsert(Inventory inventory, ItemStack stack, int slot, @Nullable Direction side) {
        if (!inventory.isValid(slot, stack)) {
            return false;
        }

        return !(inventory instanceof SidedInventory) || ((SidedInventory) inventory).canInsert(slot, stack, side);
    }

    private static boolean canExtract(Inventory inventory, ItemStack stack, int slot, Direction side) {
        return !(inventory instanceof SidedInventory) || ((SidedInventory) inventory).canExtract(slot, stack, side);
    }

    private static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, int slot, @Nullable Direction side) {
        ItemStack targetStack = to.getStack(slot);

        if (!canInsert(to, stack, slot, side)) {
            return stack;
        }

        boolean changed = false;
        boolean targetWasEmpty = to.isEmpty();

        if (targetStack.isEmpty()) {
            to.setStack(slot, stack);
            stack = ItemStack.EMPTY;
            changed = true;
        } else if (canMergeItems(targetStack, stack)) {
            int maxForSlot = Math.min(targetStack.getMaxCount(), to.getMaxCountPerStack());
            int space = maxForSlot - targetStack.getCount();
            int amount = Math.min(stack.getCount(), space);

            if (amount > 0) {
                stack.decrement(amount);
                targetStack.increment(amount);
                changed = true;
            }
        }

        if (changed) {
            if (targetWasEmpty && to instanceof CustomHopperBlockEntity) {
                CustomHopperBlockEntity destination = (CustomHopperBlockEntity) to;

                if (!destination.isDisabled()) {
                    int cooldownOffset = 0;

                    if (from instanceof CustomHopperBlockEntity) {
                        CustomHopperBlockEntity source = (CustomHopperBlockEntity) from;
                        if (destination.lastTickTime >= source.lastTickTime) {
                            cooldownOffset = 1;
                        }
                    }

                    destination.setTransferCooldown(destination.maxCooldown - cooldownOffset);
                }
            }

            to.markDirty();
        }

        return stack;
    }

    @Nullable
    private static Inventory getOutputInventory(World world, BlockPos pos, BlockState state) {
        Direction direction = state.get(HopperBlock.FACING);
        return getInventoryAt(world, pos.offset(direction));
    }

    @Nullable
    private static Inventory getInputInventory(World world, Hopper hopper) {
        return getInventoryAt(world, hopper.getHopperX(), hopper.getHopperY() + 1.0D, hopper.getHopperZ());
    }

    public static List<ItemEntity> getInputItemEntities(World world, CustomHopperBlockEntity hopper) {
        if (!hopper.getAcceptsItemEntities()) {
            return List.of();
        }

        return hopper.getInputAreaShape()
                .getBoundingBoxes()
                .stream()
                .flatMap(box -> world.getEntitiesByClass(ItemEntity.class,
                        box.offset(hopper.getHopperX() - 0.5D, hopper.getHopperY() - 0.5D, hopper.getHopperZ() - 0.5D),
                        EntityPredicates.VALID_ENTITY
                ).stream())
                .collect(Collectors.toList());
    }

    @Nullable
    public static Inventory getInventoryAt(World world, BlockPos pos) {
        return getInventoryAt(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
    }

    @Nullable
    private static Inventory getInventoryAt(World world, double x, double y, double z) {
        Inventory inventory = null;
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider) block).getInventory(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);

            if (blockEntity instanceof Inventory) {
                inventory = (Inventory) blockEntity;

                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getInventory((ChestBlock) block, blockState, world, blockPos, true);
                }
            }
        }

        if (inventory == null) {
            List<Entity> inventories = world.getOtherEntities(null, new Box(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.VALID_INVENTORIES);

            if (!inventories.isEmpty()) {
                inventory = (Inventory) inventories.get(world.random.nextInt(inventories.size()));
            }
        }

        return inventory;
    }

    private static boolean canMergeItems(ItemStack first, ItemStack second) {
        if (!first.isOf(second.getItem())) {
            return false;
        }

        if (first.getDamage() != second.getDamage()) {
            return false;
        }

        if (first.getCount() > first.getMaxCount()) {
            return false;
        }

        return ItemStack.areNbtEqual(first, second);
    }

    @Override
    public double getHopperX() {
        return this.pos.getX() + 0.5D;
    }

    @Override
    public double getHopperY() {
        return this.pos.getY() + 0.5D;
    }

    @Override
    public double getHopperZ() {
        return this.pos.getZ() + 0.5D;
    }

    private void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }

    private boolean needsCooldown() {
        return this.transferCooldown > 0;
    }

    private boolean isDisabled() {
        return this.transferCooldown > this.maxCooldown;
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    public static void onEntityCollided(World world, BlockPos pos, BlockState state, Entity entity, CustomHopperBlockEntity blockEntity) {
        if (entity instanceof ItemEntity && VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ())), blockEntity.getInputAreaShape(), BooleanBiFunction.AND)) {
            insertAndExtract(world, pos, state, blockEntity, () -> extract(blockEntity, (ItemEntity) entity, blockEntity.itemRate));
        }
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new HopperScreenHandler(syncId, playerInventory, this);
    }
}