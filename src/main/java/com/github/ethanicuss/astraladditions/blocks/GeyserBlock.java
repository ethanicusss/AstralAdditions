package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GeyserBlock extends Block {

    public GeyserBlock(Settings settings) {
        super(settings);
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isFireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.damage(DamageSource.HOT_FLOOR, 3.0F);
        }
        entity.addVelocity(0, 1, 0);

        if (world instanceof ServerWorld) {
            ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.BUBBLE, pos.getX(), pos.getY() + 1.5, pos.getZ(), 20, 0.3, 3, 0.3, 0.1);
            ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.CLOUD, pos.getX(), pos.getY() + 2, pos.getZ(), 20, 0.5, 4, 0.5, 0.1);
        }

        super.onSteppedOn(world, pos, state, entity);
    }
}
