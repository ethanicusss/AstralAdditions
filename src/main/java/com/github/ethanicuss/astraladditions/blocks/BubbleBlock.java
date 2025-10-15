package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BubbleBlock extends Block {

    public BubbleBlock(Settings settings) {
        super(settings);
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.BUBBLE, pos.getX(), pos.getY(), pos.getZ(), 10, 0.8, 0.8, 0.8, 0.1);
        ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.SPLASH, pos.getX(), pos.getY(), pos.getZ(), 10, 0.8, 0.8, 0.8, 0.1);

        super.onSteppedOn(world, pos, state, entity);
    }
}
