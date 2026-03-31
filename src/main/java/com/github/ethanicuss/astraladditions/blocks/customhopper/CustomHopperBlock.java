package com.github.ethanicuss.astraladditions.blocks.customhopper;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CustomHopperBlock extends HopperBlock {

    private final int cooldown;
    private final int itemRate;
    private final String blockEntityTypeId;

    public CustomHopperBlock(Settings settings, String _blockEntityTypeId, int _cooldown, int _itemRate) {
        super(settings);
        this.cooldown = _cooldown;
        this.itemRate = _itemRate;
        this.blockEntityTypeId = _blockEntityTypeId;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomHopperBlockEntity(pos, state, this.blockEntityTypeId, this.cooldown, this.itemRate);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        BlockEntityType<CustomHopperBlockEntity> blockEntityType = (BlockEntityType<CustomHopperBlockEntity>) Registry.BLOCK_ENTITY_TYPE.get(new Identifier(AstralAdditions.MOD_ID, blockEntityTypeId));
        return world.isClient ? null : checkType(type, blockEntityType, CustomHopperBlockEntity::serverTick);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CustomHopperBlockEntity) {
                ((CustomHopperBlockEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }

    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CustomHopperBlockEntity) {
                player.openHandledScreen((CustomHopperBlockEntity)blockEntity);
                player.incrementStat(Stats.INSPECT_HOPPER);
            }

            return ActionResult.CONSUME;
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CustomHopperBlockEntity) {
                ItemScatterer.spawn(world, pos, (CustomHopperBlockEntity)blockEntity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
