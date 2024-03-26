package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import com.github.ethanicuss.astraladditions.registry.ModData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class BrambleboneBlock extends Block {

    public BrambleboneBlock(Settings settings) {
        super(settings);
    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        for (var i = 1; i < 3; i++) {
            if (!world.getBlockState(pos.down(i)).isAir() && !world.getBlockState(pos.down(i)).isOf(ModBlocks.BRAMBLEBONE_BLOCK)){
                break;
            }
            if (i == 2){
                return;
            }
        }
        int count = 1;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++) {
                    BlockPos checkPos = new BlockPos(pos.getX() - 1 + i, pos.getY() - 1 + k, pos.getZ() - 1 + j);
                    if (world.getBlockState(checkPos).isOf(ModBlocks.BRAMBLEBONE_BLOCK)) {
                        count++;
                    }
                }
            }
        }
        if (count <= 5) {
            BlockPos blockPos = new BlockPos(pos.getX() - 1 + (int)(random.nextFloat()*3), pos.getY() - 1 + (int)(random.nextFloat()*3), pos.getZ() - 1 + (int)(random.nextFloat()*3));
            if (canPlaceAt(this.getDefaultState(), world, blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
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
        if (!world.getBlockState(pos).isAir() && !world.getBlockState(pos).isOf(ModBlocks.BRAMBLEBONE_BLOCK)){
            return false;
        }
        return true;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.slowMovement(state, new Vec3d(0.8f, 0.75, 0.8f));
        entity.damage(DamageSource.CACTUS, 4.0f);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
