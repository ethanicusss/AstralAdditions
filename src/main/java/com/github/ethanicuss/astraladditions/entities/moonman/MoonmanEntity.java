package com.github.ethanicuss.astraladditions.entities.moonman;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class MoonmanEntity extends EnderMan {

    public MoonmanEntity(EntityType<? extends EnderMan> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createMoonmanAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.25f).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 64.0);
    }

}