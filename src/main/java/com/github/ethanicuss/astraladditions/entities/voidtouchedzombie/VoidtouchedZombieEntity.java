package com.github.ethanicuss.astraladditions.entities.voidtouchedzombie;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class VoidtouchedZombieEntity
        extends Zombie {
    public VoidtouchedZombieEntity(EntityType<? extends VoidtouchedZombieEntity> entityType, Level world) {
        super((EntityType<? extends Zombie>)entityType, world);
    }

    public static boolean canSpawn(EntityType<VoidtouchedZombieEntity> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        return VoidtouchedZombieEntity.checkMonsterSpawnRules(type, world, spawnReason, pos, random) && (spawnReason == MobSpawnType.SPAWNER || world.canSeeSky(pos));
    }

    public static AttributeSupplier.Builder createVoidtouchedZombieAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.28f).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0f);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean bl = super.doHurtTarget(target);
        if (bl && this.getMainHandItem().isEmpty() && target instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.HUNGER, 140 * (int)f), this);
        }
        return bl;
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}
