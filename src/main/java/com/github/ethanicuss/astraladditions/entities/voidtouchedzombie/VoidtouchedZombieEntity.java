package com.github.ethanicuss.astraladditions.entities.voidtouchedzombie;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.Random;

public class VoidtouchedZombieEntity
        extends ZombieEntity {
    public VoidtouchedZombieEntity(EntityType<? extends VoidtouchedZombieEntity> entityType, World world) {
        super((EntityType<? extends ZombieEntity>)entityType, world);
    }

    public static boolean canSpawn(EntityType<VoidtouchedZombieEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return VoidtouchedZombieEntity.canSpawnInDark(type, world, spawnReason, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.isSkyVisible(pos));
    }

    public static DefaultAttributeContainer.Builder createVoidtouchedZombieAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0).add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0.0f);
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);
        if (bl && this.getMainHandStack().isEmpty() && target instanceof LivingEntity) {
            float f = this.world.getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
            ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 140 * (int)f), this);
        }
        return bl;
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void playHurtSound(DamageSource source){
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_HUSK_HURT, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
        super.playHurtSound(source);
    }

    @Override
    protected void updatePostDeath() {
        if (this.deathTime == 1){
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_HUSK_DEATH, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
        }
        super.updatePostDeath();
    }
}
