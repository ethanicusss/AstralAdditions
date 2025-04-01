package com.github.ethanicuss.astraladditions.effects.sinkeffect;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.registry.ModEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.util.Collections;
import java.util.Map;


public class SinkEffect extends StatusEffect {

    public static final Logger LOGGER = LoggerFactory.getLogger(AstralAdditions.MOD_ID);

    public SinkEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        if (CheckForSpace(entity, world)) {
            MakeFall(entity, amplifier);
        }

    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.noClip = false;
        entity.fallDistance = 0;
        entity.setInvulnerable(false);
        if (entity.isInsideWall()) {
            if (!entity.world.isClient) {
                entity.world.getServer().execute(() -> {
                    if (entity.isAlive() && entity.isInsideWall() && !entity.hasStatusEffect(this)) {
                        entity.addStatusEffect(new StatusEffectInstance(this, 20, amplifier, false, true, true), entity);
                    }
                });
            }
        }
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstant() {
        return false;
    }


    private boolean CheckForSpace(LivingEntity entity, World world) {
        BlockPos playerPos = entity.getBlockPos();
        int airCount = 0;

        for (int y = playerPos.getY() - 1; y >= world.getBottomY(); y--) {
            BlockPos checkPos = new BlockPos(playerPos.getX(), y, playerPos.getZ());
            if (world.getBlockState(checkPos).getBlock() == Blocks.AIR) {
                airCount++;
                if (airCount >= 3) {
                    return true;
                }
            } else {
                airCount = 0;
            }
        }
        return false;
    }


    private void MakeFall(LivingEntity entity, int amplifier) {
        double speed = amplifier * 0.001;
        entity.noClip = true;
        entity.fallDistance = 0;
        entity.setInvulnerable(true);

        Vec3d velocity = entity.getVelocity();
        entity.setVelocity(velocity.x, velocity.y - speed, velocity.z);
    }
}