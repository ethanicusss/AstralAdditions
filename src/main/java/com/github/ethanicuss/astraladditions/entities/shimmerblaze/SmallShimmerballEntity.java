package com.github.ethanicuss.astraladditions.entities.shimmerblaze;

import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class SmallShimmerballEntity extends SmallFireballEntity {

    public SmallShimmerballEntity(EntityType<? extends SmallShimmerballEntity> entityType, World world) {
        super((EntityType<? extends SmallFireballEntity>)entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > 13){
            this.setVelocity(0, 0, 0);
            if (this.age == 14) {
                for (var i = 0; i < 4; i++) {
                    this.world.addParticle(this.getParticleType(), this.getX() + this.world.random.nextFloat() * 3 - 1.5, this.getY() + this.world.random.nextFloat() * 3 - 1.5, this.getZ() + this.world.random.nextFloat() * 3 - 1.5, 0, -0.2, 0);
                }
                this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 0.5f, 1.1f + this.world.random.nextFloat() * 0.2f);
            }
        }
        if (this.age > 23){
            if (!this.world.isClient) {
                Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
                this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.5f, destructionType);
                for (var i = 0; i < 6; i++){
                    this.world.addParticle(this.getParticleType(), this.getX() + this.world.random.nextFloat() * 4 - 2, this.getY() + this.world.random.nextFloat() * 4 - 2, this.getZ() + this.world.random.nextFloat() * 4 - 2, this.world.random.nextFloat()*3-1.5, this.world.random.nextFloat()*3-2, this.world.random.nextFloat()*3-1.5);
                }
                this.discard();
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.world.isClient) {
            return;
        }
        Entity entity = entityHitResult.getEntity();
        Entity entity2 = this.getOwner();
        boolean bl = entity.damage(DamageSource.fireball(this, entity2), 5.0f);
        if (!bl) {
        } else if (entity2 instanceof LivingEntity) {
            this.applyDamageEffects((LivingEntity)entity2, entity);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos blockPos;
        if (this.world.isClient) {
            return;
        }
        Entity entity = this.getOwner();
        if ((!(entity instanceof MobEntity) || this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) && this.world.isAir(blockPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide()))) {
            if (this.world.getBlockState(blockPos.down()).isOf(Blocks.WATER)) {
                this.world.setBlockState(blockPos, ModFluids.SHIMMER.getDefaultState());
            }
        }
        this.setVelocity(0, 0, 0);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }
    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.WITCH;
    }

    @Override
    protected float getDrag() {
        return 1.05f;
    }
}
