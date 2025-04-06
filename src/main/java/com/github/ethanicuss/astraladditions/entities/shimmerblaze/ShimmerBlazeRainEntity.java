package com.github.ethanicuss.astraladditions.entities.shimmerblaze;

import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class ShimmerBlazeRainEntity extends Entity {
    private ShimmerBlazeEntity owner;
    private static final TrackedData<Integer> AGE = DataTracker.registerData(ShimmerBlazeRainEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public ShimmerBlazeRainEntity(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    public void setAge(int _age){
        this.getDataTracker().set(AGE, _age);
    }

    @Override
    public void tick() {
        this.getDataTracker().set(AGE, this.getDataTracker().get(AGE)+1);
        this.world.addParticle(ParticleTypes.WITCH, true, this.getX()-0.5+this.world.random.nextFloat(), this.getY() + 0.1, this.getZ()-0.5+this.world.random.nextFloat(), 0, 5.0, 0);
        if (this.getDataTracker().get(AGE) >= 60){

            Box box = new Box(this.getX()-0.5, this.getY()-1, this.getZ()-0.5, this.getX()+0.5, this.getY()+6, this.getZ()+0.5);
            List<Entity> ls = this.world.getOtherEntities(this, box);
            for (Entity p : ls) {
                if (p instanceof LivingEntity) {
                    if (!(p instanceof ShimmerBlazeEntity)){
                        p.damage(DamageSource.MAGIC, 4.0f);
                    }
                    p.setVelocity(0, 0.5, 0);
                }
            }
        }
        if (this.getDataTracker().get(AGE) >= 90){
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
    public void setOwner(ShimmerBlazeEntity _owner){
        this.owner = _owner;
    }
}
