package com.github.ethanicuss.astraladditions.entities.boomerang;

import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.UUID;

public class BoomerangEntity extends ThrownItemEntity {

    private static final TrackedData<Boolean> HIT = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> AGE = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MAX_AGE = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<String> OWNER = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.STRING);

    public BoomerangEntity(EntityType<? extends Entity> entityType, World world) {
        super((EntityType<? extends ThrownItemEntity>)entityType, world);
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DIAMOND_BOOMER;
    }

    private ParticleEffect getParticleParameters() {
        return ParticleTypes.CLOUD;
    }
    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();
            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    public void setOwner(String UUID){
        this.getDataTracker().set(OWNER, UUID);
    }

    @Override
    public void tick() {
        if (this.getDataTracker().get(HIT)) {
            if (!Objects.equals(this.getDataTracker().get(OWNER), "")) {
                PlayerEntity p = world.getPlayerByUuid(UUID.fromString(this.getDataTracker().get(OWNER)));
                if (p != null){
                    double strength = 2.5;
                    double xdiff = this.getX() - p.getX();
                    double zdiff = this.getZ() - p.getZ();
                    double dist;
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
                    dist = -1;

                    double yVel = (p.getY() - (this.getY()-1))/5;

                    this.setVelocity(dist * cosX * strength * (Math.abs(angleX) / angleX), yVel * strength, dist * cosZ * strength * (Math.abs(angleZ) / angleZ));
                }
            }
            else{
                this.world.sendEntityStatus(this, (byte)3);
                this.discard();
            }
        }
        else{
            this.getDataTracker().set(AGE, this.getDataTracker().get(AGE)+1);
            if (this.getDataTracker().get(AGE) > this.getDataTracker().get(MAX_AGE)){
                this.getDataTracker().set(HIT, true);
            }
        }
        if (this.getDataTracker().get(AGE) > this.getDataTracker().get(MAX_AGE)*3){
            PlayerEntity p = world.getPlayerByUuid(UUID.fromString(this.getDataTracker().get(OWNER)));
            if (p != null){
                p.giveItemStack(ModItems.DIAMOND_BOOMER.getDefaultStack());
                this.discard();
            }
        }
        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (!Objects.equals(entity.getUuidAsString(), this.getDataTracker().get(OWNER))) {
            entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), 6);
            entity.addVelocity(0.0f, 0.3f, 0.0f);
        }
        else{
            PlayerEntity p = world.getPlayerByUuid(UUID.fromString(this.getDataTracker().get(OWNER)));
            if (p != null){
                p.giveItemStack(ModItems.DIAMOND_BOOMER.getDefaultStack());
                this.discard();
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            if (!this.world.isClient) {
                this.getDataTracker().set(HIT, true);
            }
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HIT, false);
        this.dataTracker.startTracking(OWNER, "");
        this.dataTracker.startTracking(AGE, 0);
        this.dataTracker.startTracking(MAX_AGE, 8);
    }
}
