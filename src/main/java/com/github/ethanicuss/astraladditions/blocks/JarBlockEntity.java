package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class JarBlockEntity extends BlockEntity implements ImplementedInventory{

    private final DefaultedList<ItemStack> item = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.JAR_BLOCKENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems(){
        return this.item;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        //this.item.set(0, ItemStack.EMPTY);
        //this.item.set(1, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.item);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {//waaa it not worky. don't use Inventories.writeNbt. copy from another mod like tech reborn
        Inventories.writeNbt(nbt, this.item);
        DefaultedList<ItemStack> stacks = this.item;
        NbtList nbtList = new NbtList();
        {
            ItemStack itemStack = stacks.get(0);
            //if (itemStack.isEmpty()) {
            //    System.out.println("it's empty");
            //    continue;
            //}
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)0);
            itemStack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        if (!nbtList.isEmpty()) {
            nbt.put("Items", nbtList);
        }

        //super.writeNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
