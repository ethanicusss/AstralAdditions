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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class LuneShroomBlock extends Block {
    protected static final VoxelShape OUTLINE_SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 4.0, 12.0);
    protected static final VoxelShape NO_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    public LuneShroomBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int count = 1;
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k<7; k++) {
                    BlockPos checkPos = new BlockPos(pos.getX() - 3 + i, pos.getY() - 1 + j, pos.getZ() - 3 + k);
                    if (world.getBlockState(checkPos).isOf(ModBlocks.LUNE_SHROOM_BLOCK)) {
                        count++;
                    }
                }
            }
        }
        if (count <= 4) {
            BlockPos blockPos = new BlockPos(pos.getX() - 3 + (int)(random.nextFloat()*7), pos.getY(), pos.getZ() - 3 + (int)(random.nextFloat()*7));
            if (canPlaceAt(this.getDefaultState(), world, blockPos)) {
                world.setBlockState(blockPos, this.getDefaultState());
            }
            else if(canPlaceAt(this.getDefaultState(), world, blockPos.up())) {
                world.setBlockState(blockPos.up(), this.getDefaultState());
            }
            else if(canPlaceAt(this.getDefaultState(), world, blockPos.down())) {
                world.setBlockState(blockPos.down(), this.getDefaultState());
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
        if (!world.getBlockState(pos).isAir() && !world.getBlockState(pos).isOf(ModBlocks.LUNE_SHROOM_BLOCK)){
            return false;
        }
        BlockState blockState2 = world.getBlockState(pos.down());
        return (blockState2.isIn(ModData.LUNE_SHROOM_GROWABLE));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        //entity.damage(DamageSource.CACTUS, 2.0f);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return NO_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public AbstractBlock.OffsetType getOffsetType() {
        return AbstractBlock.OffsetType.XYZ;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
