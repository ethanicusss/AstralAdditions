package com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class VoidtouchedSkeletonEntity
        extends StrayEntity {

    public VoidtouchedSkeletonEntity(EntityType<? extends VoidtouchedSkeletonEntity> entityType, World world) {
        super((EntityType<? extends StrayEntity>) entityType, world);
        int catchCount = 0;
        while ((this.world.getBlockState(this.getBlockPos().down()) == Blocks.AIR.getDefaultState() || this.world.getBlockState(this.getBlockPos()) != Blocks.AIR.getDefaultState()) && catchCount < 80){
            this.setPos(this.getX(), this.getY()+1, this.getZ());
            catchCount++;
            System.out.println("up");
        }
    }

    public static DefaultAttributeContainer.Builder createVoidtouchedSkeletonAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 22.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0);
    }


    @Override
    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        PersistentProjectileEntity persistentProjectileEntity = super.createArrowProjectile(arrow, damageModifier);
        if (persistentProjectileEntity instanceof ArrowEntity) {
            float f = this.world.getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
            ((ArrowEntity)persistentProjectileEntity).addEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100 * (int)f));
        }
        return persistentProjectileEntity;
    }

    @Override
    protected void playHurtSound(DamageSource source){
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_STRAY_HURT, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
        super.playHurtSound(source);//do all mobs sounds then ur done done
    }
    @Override
    protected void updatePostDeath() {
        if (this.deathTime == 1){
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_STRAY_DEATH, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
        }
        super.updatePostDeath();
    }
}
