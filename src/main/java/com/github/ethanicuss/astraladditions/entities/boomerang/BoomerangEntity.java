package com.github.ethanicuss.astraladditions.entities.boomerang;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.UUID;

public class BoomerangEntity extends ThrownItemEntity {

    private static final TrackedData<Boolean> HIT = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> AGE = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DAMAGE = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> MAX_AGE = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> SPEED = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> CURVE = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<String> OWNER = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> ITEM = DataTracker.registerData(BoomerangEntity.class, TrackedDataHandlerRegistry.STRING);

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
    public void setRangItem(String rangItem){
        this.getDataTracker().set(ITEM, rangItem);
    }
    public ItemStack getRangItem(){
        return Registry.ITEM.get(new Identifier(AstralAdditions.MOD_ID, this.dataTracker.get(ITEM))).getDefaultStack();
    }
    public void setMaxAge(int maxAge){
        this.getDataTracker().set(MAX_AGE, maxAge);
    }
    public void setRangDamage(float damage){
        this.getDataTracker().set(DAMAGE, damage);
    }
    public void setRangSpeed(float speed){
        this.getDataTracker().set(SPEED, speed);
    }
    public void setCurve(float curve){
        this.getDataTracker().set(CURVE, curve);
    }

    @Override
    public void tick() {
        if (this.getDataTracker().get(HIT)) {
            if (!Objects.equals(this.getDataTracker().get(OWNER), "")) {
                PlayerEntity p = world.getPlayerByUuid(UUID.fromString(this.getDataTracker().get(OWNER)));
                if (p != null){
                    double strength = this.getDataTracker().get(SPEED)+0.5f;
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
            float xspeed = (float)this.getVelocity().getX();
            float yspeed = (float)this.getVelocity().getZ();
            float angle = (float)(Math.toDegrees(Math.atan(xspeed/yspeed))) - this.getDataTracker().get(CURVE);
            if (yspeed < 0){angle += 180; System.out.println("brah");}
            this.setVelocity(Math.sqrt(Math.pow(xspeed, 2)+Math.pow(yspeed, 2))*Math.sin(Math.toRadians(angle)), (float)this.getVelocity().getY(), Math.sqrt(Math.pow(xspeed, 2)+Math.pow(yspeed, 2))*Math.cos(Math.toRadians(angle)));
            if (this.getDataTracker().get(AGE) > this.getDataTracker().get(MAX_AGE)){
                this.getDataTracker().set(HIT, true);
            }
        }
        if (this.getDataTracker().get(AGE) > this.getDataTracker().get(MAX_AGE)*3){
            PlayerEntity p = world.getPlayerByUuid(UUID.fromString(this.getDataTracker().get(OWNER)));
            if (p != null){
                world.spawnEntity(new ItemEntity(world, p.getX(), p.getY() + 1, p.getZ(), this.getRangItem()));
                this.discard();
            }
        }
        this.getDataTracker().set(AGE, this.getDataTracker().get(AGE)+1);
        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (!Objects.equals(entity.getUuidAsString(), this.getDataTracker().get(OWNER))) {
            entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), this.getDataTracker().get(DAMAGE));
            entity.addVelocity(0.0f, 0.01f*this.getDataTracker().get(SPEED), 0.0f);
        }
        else{
            PlayerEntity p = world.getPlayerByUuid(UUID.fromString(this.getDataTracker().get(OWNER)));
            if (p != null){
                ItemStack i = this.getRangItem();
                if (i != null) {
                    if (p.getInventory().getStack(p.getInventory().selectedSlot).isEmpty()) {
                        p.getInventory().setStack(p.getInventory().selectedSlot, i);
                    } else {
                        world.spawnEntity(new ItemEntity(world, p.getX(), p.getY() + 1, p.getZ(), i));
                    }
                }
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
        this.dataTracker.startTracking(DAMAGE, 1.0f);
        this.dataTracker.startTracking(MAX_AGE, 8);
        this.dataTracker.startTracking(SPEED, 1.0f);
        this.dataTracker.startTracking(CURVE, 0.0f);
        this.dataTracker.startTracking(ITEM, "minecraft:dirt");
    }
}
