package com.github.ethanicuss.astraladditions.entities.meteor_mitts;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MeteorPunchEntity extends ThrownItemEntity {

    public int punchAge;
    private double userSpeed;
    private Vec3d userPosOld;
    private double width = 1;

    private final double playerBoost = 7;

    public MeteorPunchEntity(EntityType<? extends Entity> entityType, World world) {
        super((EntityType<? extends ThrownItemEntity>)entityType, world);
        this.setNoGravity(true);
        //this.noClip = true;
        //this.setVelocity(0.0f, 1.0f, 0.0f);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.IRON_HOE;
    }

    private ParticleEffect getParticleParameters() {
        return ParticleTypes.WITCH;
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

    @Override
    public void tick() {
        this.punchAge++;
        this.world.addParticle(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        if (this.getOwner() != null) {
            if (this.punchAge == 1) {
                this.userPosOld = this.getOwner().getPos();
            }
            if (this.punchAge == 2) {
                this.addVelocity((this.getOwner().getX() - this.userPosOld.x) * playerBoost, (this.getOwner().getY() - this.userPosOld.y) * playerBoost/3, (this.getOwner().getZ() - this.userPosOld.z) * playerBoost);
            }
        }
        float dampen = 0.7f;
        this.setVelocity(this.getVelocity().x*dampen, this.getVelocity().y*dampen, this.getVelocity().z*dampen);
        if (punchAge > 7) {
            this.discard();
        }
        double i = this.getX();
        double j = this.getY();
        double k = this.getZ();
        float f = (float)this.width;
        Box box = new Box((float) i - f, (float) j - f, (float) k - f, (float) (i + 1) + f, (float) (j + 1) + f, (float) (k + 1) + f);
        List<Entity> list = world.getOtherEntities(this.getOwner(), box);
        for (Entity p : list) {
            if (p instanceof LivingEntity) {
                if (((LivingEntity) p).hurtTime == 0){
                    this.entityHit((LivingEntity) p);
                }
            }
        }
        super.tick();
    }

    protected void entityHit(LivingEntity entity){
        double damage = Math.abs(this.getVelocity().x) + Math.abs(this.getVelocity().y) + Math.abs(this.getVelocity().z);
        AstralAdditions.LOGGER.info("------");
        AstralAdditions.LOGGER.info(Double.toString((float)damage*3 + 2));
        damage = Math.sqrt((float)damage + 0.1)*8 + 2;
        AstralAdditions.LOGGER.info(Double.toString(damage));
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), (float)damage);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL, 0.1f + (float)Math.sqrt(damage/3)/4, 0.1f + (float)damage/10);
        float multiply = 1.6f;
        entity.setVelocity(this.getVelocity().x*multiply, Math.max(this.getVelocity().y*multiply*0.5, 0.0f) + 0.5f, this.getVelocity().z*multiply);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        //super.onEntityHit(entityHitResult);

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            //this.world.sendEntityStatus(this, (byte)3);
        }
    }
}
