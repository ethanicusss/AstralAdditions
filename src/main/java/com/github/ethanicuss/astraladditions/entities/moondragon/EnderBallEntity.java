package com.github.ethanicuss.astraladditions.entities.moondragon;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

public class EnderBallEntity extends AbstractHurtingProjectile {

    private int timer = 20;
    private int despawntimer = 60;
    public EnderBallEntity(EntityType<? extends EnderBallEntity> entityType, Level world) {
        super((EntityType<? extends AbstractHurtingProjectile>)entityType, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (timer == 20 && hitResult.getType() == HitResult.Type.BLOCK){
            setDeltaMovement(0, 0, 0);
            timer--;
        }
    }

    private void pullPlayer(double strength, double vStrength){
        //PlayerEntity p = this.world.getClosestPlayer(this, 32);
        List<Entity> pl = level.getEntities(this, new AABB(this.getX()-16, this.getY()-32, this.getZ()-16, this.getX()+16, this.getY()+32, this.getZ()+16));
        for (Entity p : pl) {
            if (p instanceof LivingEntity){
                int strMult = 1;
                if (!(p instanceof Player)) {
                    strMult *= 2;
                }
                double xdiff = this.getX() - p.getX();
                double zdiff = this.getZ() - p.getZ();
                double dist = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(zdiff, 2));
                if (dist < 10) {
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
                    dist = -dist + 10;
                    p.push(dist * cosX * strength * strMult * (Math.abs(angleX) / angleX), dist * vStrength * strMult, dist * cosZ * strength * strMult * (Math.abs(angleZ) / angleZ));
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (despawntimer == 0 && timer == 20){
            timer--;
        }
        else {
            despawntimer--;
        }
        if (timer < 20) {
            timer--;
            pullPlayer(0.01, 0);
            if (timer == 0){
                pullPlayer(-0.1, -0.01);
                int rad = 3;
                for (var _y = 0; _y < this.level.getHeight(); _y++){
                    for (var _x = 0; _x < rad; _x++){
                        for (var _z = 0; _z < rad; _z++){
                            BlockPos pos = new BlockPos(this.getBlockX() - 1 + _x, _y, this.getBlockZ() - 1 + _z);
                            this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                            this.level.addParticle(this.getTrailParticle(), this.getBlockX() - 1 + _x, _y + 0.5, this.getBlockZ() - 1 + _z, (-1 + _x)/4.0, 0.0, (-1 + _z)/4.0);
                        }
                    }
                }
            }
            for (var _y = 0; _y < this.level.getHeight(); _y++){
                BlockPos pos = new BlockPos(this.getBlockX(), _y, this.getBlockZ());
                this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                if (_y / 2.0 == Math.round(_y / 2.0)) {
                    this.level.addParticle(this.getTrailParticle(), this.getBlockX(), _y, this.getBlockZ(), 0.0, 0.0, 0.0);
                }
            }
        }
        if (timer < 0){
            this.discard();
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.END_ROD;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}
