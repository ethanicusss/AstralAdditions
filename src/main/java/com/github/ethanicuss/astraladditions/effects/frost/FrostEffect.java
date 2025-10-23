package com.github.ethanicuss.astraladditions.effects.frost;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class FrostEffect extends StatusEffect {

    public static final Logger LOGGER = LoggerFactory.getLogger(AstralAdditions.MOD_ID);

    public FrostEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        BlockPos pos = entity.getBlockPos();

        if (world.isClient) {
            Random random = world.getRandom();
            boolean bl = entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ();
            if (bl && random.nextBoolean()) {
                world.addParticle(ParticleTypes.SNOWFLAKE, entity.getX(), (double)(pos.getY() + 1), entity.getZ(), (double)(MathHelper.nextBetween(random, -1.0F, 1.0F) * 0.083333336F), 0.05000000074505806D, (double)(MathHelper.nextBetween(random, -1.0F, 1.0F) * 0.083333336F));
            }
        }

        entity.setInPowderSnow(true);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {

    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

}