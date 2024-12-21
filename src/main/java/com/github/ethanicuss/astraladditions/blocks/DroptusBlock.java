package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import com.github.ethanicuss.astraladditions.registry.ModData;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class DroptusBlock extends Block {

    public static final IntProperty AGE = Properties.AGE_2;
    public static final BooleanProperty LIT = Properties.LIT;
    protected static final VoxelShape OUTLINE_SHAPE_0 = Block.createCuboidShape(5.0, 8.0, 5.0, 11.0, 16.0, 11.0);
    protected static final VoxelShape OUTLINE_SHAPE_1 = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    protected static final VoxelShape OUTLINE_SHAPE_2 = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public DroptusBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AGE, 0));
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(LIT, false));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) < 3;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int j = state.get(AGE);
        if (j < 2) {
            world.setBlockState(pos, (BlockState)state.with(AGE, j + 1), Block.NOTIFY_LISTENERS);
            if (j == 1){
                world.setBlockState(pos, (BlockState)state.with(LIT, true).with(AGE, 2), Block.NOTIFY_LISTENERS);
                return;
            }
        }
        int i = 1;
        while (world.getBlockState(pos.up(i)).isOf(this)) {
            ++i;
        }
        BlockPos blockPos = pos.down();
        if (i < 4) {
            if (world.getBlockState(blockPos).isAir()) {
                world.setBlockState(blockPos, this.getDefaultState());
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.createAndScheduleBlockTick(pos, this, 1);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (!world.getBlockState(pos).isAir() && !world.getBlockState(pos).isOf(ModBlocks.BULBA_BLOCK)){
            return false;
        }
        BlockState blockState2 = world.getBlockState(pos.up());
        return ((blockState2.isOf(ModBlocks.BULBA_BLOCK) && blockState2.get(AGE) != 0) || blockState2.isOf(ModBlocks.MOONSET_CRYSTAL_BLOCK) || blockState2.isIn(ModData.BULBA_GROWABLE));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AGE)) {
            case 0 -> OUTLINE_SHAPE_0;
            case 1 -> OUTLINE_SHAPE_1;
            case 2 -> OUTLINE_SHAPE_2;
            default -> OUTLINE_SHAPE_2;
        };
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AGE)) {
            case 0 -> OUTLINE_SHAPE_0;
            case 1 -> OUTLINE_SHAPE_1;
            case 2 -> OUTLINE_SHAPE_2;
            default -> OUTLINE_SHAPE_2;
        };
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        builder.add(LIT);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
