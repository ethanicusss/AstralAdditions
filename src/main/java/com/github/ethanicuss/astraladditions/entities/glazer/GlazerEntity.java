package com.github.ethanicuss.astraladditions.entities.glazer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GlazerEntity extends BeeEntity {
    public GlazerEntity(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 15;
    }

    @Override
    public boolean hasHive() {
        return false;
    }
    @Override
    public boolean hasNectar() {
        return false;
    }

    @Override
    public boolean hasStung() {
        return false;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            PlayerEntity p = this.world.getClosestPlayer(this, 48);
            if (p != null){
                if (this.getAngerTime() == 0) {
                    this.setVelocity(-(this.getX() - p.getX())/5, -(this.getY() - p.getY())/3, -(this.getZ() - p.getZ())/5);
                }
                this.setAngryAt(p.getUuid());
                this.isUniversallyAngry(this.world);
                this.setAngerTime(600);
            }
        }
    }

    public static DefaultAttributeContainer.Builder createGlazerAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 18.0).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.8f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
    }
}
