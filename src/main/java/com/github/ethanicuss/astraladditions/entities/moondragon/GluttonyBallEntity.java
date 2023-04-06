package com.github.ethanicuss.astraladditions.entities.moondragon;

import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;

public class GluttonyBallEntity extends AbstractHurtingProjectile {

    private int timer = 240;
    private static final EntityDataAccessor<Integer> FRAME = SynchedEntityData.defineId(GluttonyBallEntity.class, EntityDataSerializers.INT);
    public GluttonyBallEntity(EntityType<? extends GluttonyBallEntity> entityType, Level world) {
        super((EntityType<? extends AbstractHurtingProjectile>)entityType, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (hitResult.getType() == HitResult.Type.ENTITY){
            Player p = this.level.getNearestPlayer(this, 3);
            if (p != null){
                p.setDeltaMovement(0, -2.0, 0);
                p.hurt(DamageSource.STARVE, 6);
                this.discard();
            }
        }
        if (hitResult.getType() == HitResult.Type.BLOCK){
            if (this.getFeetBlockState() != Blocks.AIR.defaultBlockState()) {
                boolean bl = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                this.level.explode(null, this.getX(), this.getY(), this.getZ(), 1, false, bl ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
                this.discard();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.entityData.set(FRAME, this.entityData.get(FRAME)+1);
        if (this.entityData.get(FRAME) == 9){
            this.entityData.set(FRAME, 0);
        }

        Player p = this.level.getNearestPlayer(this, 64);
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

                this.push(dist * cosX * strength * (Math.abs(angleX) / angleX), yVel * strength, dist * cosZ * strength * (Math.abs(angleZ) / angleZ));
            //}
        }

        timer--;
        if (timer < 0){
            this.discard();
        }
    }

    public int getFrame(){
        return this.entityData.get(FRAME);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FRAME, 0);
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.ENCHANTED_HIT;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}
