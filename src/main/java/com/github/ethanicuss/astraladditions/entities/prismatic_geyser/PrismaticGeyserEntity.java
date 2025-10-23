package com.github.ethanicuss.astraladditions.entities.prismatic_geyser;

import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.registry.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class PrismaticGeyserEntity extends Entity {
    private static final TrackedData<Integer> AGE = DataTracker.registerData(PrismaticGeyserEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public PrismaticGeyserEntity(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    public void setAge(int _age){
        this.getDataTracker().set(AGE, _age);
    }

    @Override
    public void tick() {
        this.getDataTracker().set(AGE, this.getDataTracker().get(AGE)+1);
        if (this.getDataTracker().get(AGE) == 10){
            for (var i = 0; i < 20; i++) {
                this.world.addParticle(ParticleTypes.EFFECT, true, this.getX() - 0.5 + random.nextFloat(), this.getY() + 0.1 + random.nextFloat() * 5, this.getZ() - 0.5 + random.nextFloat(), 0, 2.0, 0);
                this.world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, this.getX() - 0.5 + random.nextFloat(), this.getY() + 0.1 + random.nextFloat() * 5, this.getZ() - 0.5 + random.nextFloat(), 0, i/30f, 0);
            }
        }
        if (this.getDataTracker().get(AGE) >= 10){
            this.world.addParticle(ParticleTypes.EFFECT, true, this.getX()-0.5 + random.nextFloat(), this.getY() + 0.1 + random.nextFloat()*5, this.getZ()-0.5 + random.nextFloat(), 0, 1.0, 0);

            if (this.getDataTracker().get(AGE) < 90) {
                Box box = new Box(this.getX() - 0.5, this.getY() - 1, this.getZ() - 0.5, this.getX() + 0.5, this.getY() + 6, this.getZ() + 0.5);
                List<Entity> ls = this.world.getOtherEntities(this, box);
                for (Entity p : ls) {
                    if (p instanceof LivingEntity) {
                        p.damage(DamageSource.FREEZE, 3.0f);
                        ((LivingEntity) p).addStatusEffect(new StatusEffectInstance(ModEffects.FROST, 600, 0), this);
                        p.setVelocity(0, 1.5, 0);
                    }
                }
            }
        }
        else{
            this.world.addParticle(ParticleTypes.CLOUD, true, this.getX(), this.getY() + 0.1, this.getZ(), 0, 0.1, 0);
        }
        if (this.getDataTracker().get(AGE) >= 120){
            this.discard();
        }
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(AGE, 0);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public int getAge(){
        return this.getDataTracker().get(AGE);
    }
}
