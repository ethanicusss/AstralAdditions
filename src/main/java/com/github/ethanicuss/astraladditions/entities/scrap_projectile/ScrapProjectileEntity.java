package com.github.ethanicuss.astraladditions.entities.scrap_projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ScrapProjectileEntity extends ThrownItemEntity {

    public ScrapProjectileEntity(EntityType<? extends Entity> entityType, World world) {
        super((EntityType<? extends ThrownItemEntity>)entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.IRON_HOE;
    }

    private ParticleEffect getParticleParameters() {
        return ParticleTypes.ASH;
    }

    @Override
    public void tick() {
        super.tick();
        ParticleEffect particleEffect = this.getParticleParameters();
        this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        if (this.age > 20){
            this.world.sendEntityStatus(this, (byte)3);
            this.discard();
        }
        float dampen = 0.95f;
        Vec3d v = this.getVelocity();
        this.setVelocity(v.getX()*dampen, v.getY()*dampen, v.getZ()*dampen);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();

        float damage = 25 - this.age*2;

        System.out.println(damage);

        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), damage);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            Vec3d v = this.getVelocity();
            this.setVelocity(v.getX(), -v.getY(), v.getZ());
            if (!this.world.getBlockState(this.getBlockPos().up()).isAir()){
                this.world.sendEntityStatus(this, (byte)3);
                this.discard();
            }
        }
    }
}
