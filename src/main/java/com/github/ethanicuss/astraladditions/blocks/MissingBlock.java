package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class MissingBlock extends Block {

    public MissingBlock(Settings settings) {
        super(settings);
    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int count = 1;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++) {
                    BlockPos checkPos = new BlockPos(pos.getX() - 1 + i, pos.getY() - 1 + k, pos.getZ() - 1 + j);
                    if (world.getBlockState(checkPos).isOf(ModBlocks.MISSING_BLOCK)) {
                        count++;
                    }
                }
            }
        }
        if (count <= 5) {
            BlockPos blockPos = new BlockPos(pos.getX() - 1 + (int)(random.nextFloat()*3), pos.getY() - 1 + (int)(random.nextFloat()*3), pos.getZ() - 1 + (int)(random.nextFloat()*3));
            if (canPlaceAt(this.getDefaultState(), world, blockPos)) {
                world.setBlockState(blockPos, this.getDefaultState());
            }
        }
        if (world.random.nextInt(5) == 0){
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
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
        if (world.getBlockState(pos).isOf(ModBlocks.MISSING_BLOCK)){
            return false;
        }
        return true;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.slowMovement(state, new Vec3d(0.7f, 0.75, 0.7f));
        entity.damage(DamageSource.CACTUS, 8.0f);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}