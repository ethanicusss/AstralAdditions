package com.github.ethanicuss.astraladditions.effects;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.registry.ModEffects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.logging.Log;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SinkEffect extends StatusEffect {

    public static final Logger LOGGER = LoggerFactory.getLogger(AstralAdditions.MOD_ID);
    private static Boolean StartedFalling = false;
    private static Boolean InBlock = false;

    public SinkEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        MakeFall(entity, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
            entity.noClip = false;
            entity.fallDistance = 0;
            entity.setInvulnerable(false);

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    private void MakeFall(LivingEntity entity, int amplifier) {
        double speed = (double) amplifier / 10000;
        entity.noClip = true;
        entity.fallDistance = 0;
        entity.setInvulnerable(true);

        Vec3d velocity = entity.getVelocity();
        if (velocity.y < 0) {
            entity.setVelocity(velocity.x, velocity.y - speed, velocity.z);

        }
    }

}