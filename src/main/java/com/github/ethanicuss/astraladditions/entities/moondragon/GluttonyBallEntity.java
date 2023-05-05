package com.github.ethanicuss.astraladditions.entities.moondragon;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;

public class GluttonyBallEntity extends ExplosiveProjectileEntity {

    private int timer = 240;
    private static final TrackedData<Integer> FRAME = DataTracker.registerData(GluttonyBallEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public GluttonyBallEntity(EntityType<? extends GluttonyBallEntity> entityType, World world) {
        super((EntityType<? extends ExplosiveProjectileEntity>)entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() == HitResult.Type.ENTITY){
            PlayerEntity p = this.world.getClosestPlayer(this, 3);
            if (p != null){
                p.setVelocity(0, -2.0, 0);
                p.damage(DamageSource.STARVE, 6);
                this.discard();
            }
        }
        if (hitResult.getType() == HitResult.Type.BLOCK){
            if (this.getBlockStateAtPos() != Blocks.AIR.getDefaultState()) {
                boolean bl = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
                this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 1, false, bl ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE);
                this.discard();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.dataTracker.set(FRAME, this.dataTracker.get(FRAME)+1);
        if (this.dataTracker.get(FRAME) == 9){
            this.dataTracker.set(FRAME, 0);
        }

        PlayerEntity p = this.world.getClosestPlayer(this, 64);
        if (p != null){
            double strength = 0.15;
            double xdiff = this.getX() - p.getX();
            double zdiff = this.getZ() - p.getZ();
            double dist = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(zdiff, 2));
            //if (dist < 20) {
                if (xdiff == 0) {
                    xdiff = 0.01;
                }
                if (zdiff == 0) {
                    zdiff = 0.01;
                }
                double angleX = Math.atan(Math.abs(zdiff) / xdiff);
                double angleZ = Math.atan(Math.abs(xdiff) / zdiff);
                double cosX = Math.cos(angleX);
                double cosZ = Math.cos(angleZ);
                if (cosX == 0) {
                    cosX = 0.01;
                }
                if (cosZ == 0) {
                    cosZ = 0.01;
                }
                //dist = -dist + 20;
                //dist = -dist;
                dist = -1;

                double yVel;
                yVel = dist * 0.5;
                if (p.getY() > this.getY()){
                    yVel = -dist;
                }

                this.addVelocity(dist * cosX * strength * (Math.abs(angleX) / angleX), yVel * strength, dist * cosZ * strength * (Math.abs(angleZ) / angleZ));
            //}
        }

        timer--;
        if (timer < 0){
            this.discard();
        }
    }

    public int getFrame(){
        return this.dataTracker.get(FRAME);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FRAME, 0);
    }

    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.ENCHANTED_HIT;
    }

    @Override
    protected boolean isBurning() {
        return false;
    }
}
