package com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VoidtouchedSkeletonEntity
        extends Stray {

    public VoidtouchedSkeletonEntity(EntityType<? extends VoidtouchedSkeletonEntity> entityType, Level world) {
        super((EntityType<? extends Stray>) entityType, world);
    }

    public static AttributeSupplier.Builder createVoidtouchedSkeletonAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 22.0).add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    protected AbstractArrow getArrow(ItemStack arrow, float damageModifier) {
        AbstractArrow persistentProjectileEntity = super.getArrow(arrow, damageModifier);
        if (persistentProjectileEntity instanceof Arrow) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((Arrow)persistentProjectileEntity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100 * (int)f));
        }
        return persistentProjectileEntity;
    }
}
