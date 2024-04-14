package com.github.ethanicuss.astraladditions.entities.shimmerblaze;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ShimmerBlazeEntity extends BlazeEntity {
    public ShimmerBlazeEntity(EntityType<? extends BlazeEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 1;
    }

    public static DefaultAttributeContainer.Builder createShimmerBlazeAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0).add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(4, new ShootFireballGoal(this));
        this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0));
        this.goalSelector.add(7, new WanderAroundFarGoal((PathAwareEntity)this, 1.0, 0.0f));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
    }

    private static final TrackedData<String> ATTACK = DataTracker.registerData(ShimmerBlazeEntity.class, TrackedDataHandlerRegistry.STRING);

    static class ShootFireballGoal
            extends Goal {
        private final ShimmerBlazeEntity blaze;
        private int fireballsFired;
        private int fireballCooldown;
        private int targetNotVisibleTicks;
        private String attack = "";
        private String prevAttack = "";
        private boolean doneMega = false;

        public ShootFireballGoal(ShimmerBlazeEntity blaze) {
            this.blaze = blaze;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.blaze.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.blaze.canTarget(livingEntity);
        }

        @Override
        public void start() {
            this.fireballsFired = 0;
        }

        @Override
        public void stop() {
            this.targetNotVisibleTicks = 0;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            --this.fireballCooldown;
            LivingEntity livingEntity = this.blaze.getTarget();
            if (livingEntity == null) {
                return;
            }
            boolean bl = this.blaze.getVisibilityCache().canSee(livingEntity);
            this.targetNotVisibleTicks = bl ? 0 : ++this.targetNotVisibleTicks;
            double d = this.blaze.squaredDistanceTo(livingEntity);
            if (d < 4.0) {
                if (!bl) {
                    return;
                }
                if (this.fireballCooldown <= 0) {
                    this.fireballCooldown = 20;
                    this.blaze.tryAttack(livingEntity);
                }
                this.blaze.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
            } else if (d < this.getFollowRange() * this.getFollowRange() && bl) {
                if (this.attack == "") {
                    switch (this.blaze.world.random.nextInt(5)) {
                        case 0,1 -> this.attack = "burst";
                        case 2 -> this.attack = "stomp";
                        case 3 -> this.attack = "chase";
                        case 4 -> this.attack = "rain";
                    }
                    if (this.prevAttack == this.attack) {
                        if (this.attack == "stomp" || this.attack == "chase"){
                            this.attack = "burst";
                        }
                    }
                    if (this.attack == "chase"){
                        this.fireballCooldown = 120;
                    }
                    this.prevAttack = this.attack;
                }
                double e = livingEntity.getX() - this.blaze.getX();
                double f = livingEntity.getBodyY(0.5) - this.blaze.getBodyY(0.5);
                double g = livingEntity.getZ() - this.blaze.getZ();
                switch (this.attack) {
                    case "burst":
                        if (this.fireballCooldown == 15) {
                            int rand = this.blaze.world.random.nextInt(2);
                            this.blaze.addVelocity(e / (14 - rand * 4), 0.5, g / (14 - rand * 4));
                        }
                        if (this.fireballCooldown <= 0) {
                            ++this.fireballsFired;
                            if (this.fireballsFired == 1) {
                                ModUtils.spawnForcedParticles((ServerWorld)this.blaze.world, ParticleTypes.WITCH, this.blaze.getX(), this.blaze.getY(), this.blaze.getZ(), 10, 1, 2, 1, 0.01);
                                this.fireballCooldown = 10;
                            } else if (this.fireballsFired <= 10) {
                                this.fireballCooldown = 1;
                            } else {
                                this.fireballCooldown = 40 + (int) (this.blaze.world.random.nextFloat() * 10 + (this.blaze.getHealth()/this.blaze.getMaxHealth())*30);
                                this.fireballsFired = 0;
                                this.attack = "";
                            }
                            if (this.fireballsFired > 1) {
                                double h = Math.sqrt(Math.sqrt(d)) * 0.015;
                                if (!this.blaze.isSilent()) {
                                    this.blaze.world.syncWorldEvent(null, WorldEvents.BLAZE_SHOOTS, this.blaze.getBlockPos(), 0);
                                }
                                for (int i = 0; i < 1; ++i) {
                                    ModUtils.playSound((ServerWorld)this.blaze.world, this.blaze.getX(), this.blaze.getY(), this.blaze.getZ(), SoundEvents.ENTITY_SHULKER_OPEN, SoundCategory.HOSTILE, 1.0f, 1.1f + this.blaze.world.random.nextFloat() * 0.2f, true);
                                    this.blaze.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 0.5f, 1.1f + this.blaze.world.random.nextFloat() * 0.2f);
                                    SmallShimmerballEntity smallShimmerballEntity = new SmallShimmerballEntity(ModEntities.SMALL_SHIMMERBALL, this.blaze.world);
                                    smallShimmerballEntity.setPosition(this.blaze.getX(), this.blaze.getBodyY(0.5) + 0.5, this.blaze.getZ());
                                    smallShimmerballEntity.refreshPositionAndAngles(this.blaze.getX(), this.blaze.getBodyY(0.5) + 0.5, this.blaze.getZ(), 0.0f, 0.0f);
                                    double speed = 0.05;
                                    smallShimmerballEntity.setVelocity(e * speed + this.blaze.getRandom().nextGaussian() * h, f * speed + this.blaze.getRandom().nextGaussian() * h, g * speed + this.blaze.getRandom().nextGaussian() * h);
                                    this.blaze.world.spawnEntity(smallShimmerballEntity);
                                }
                            }
                        }
                        this.blaze.getLookControl().lookAt(livingEntity, 10.0f, 10.0f);
                        break;
                    case "stomp":
                        if (this.fireballCooldown == 30){
                            ModUtils.playSound((ServerWorld)this.blaze.world, this.blaze.getX(), this.blaze.getY(), this.blaze.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 0.9f, 0.5f + this.blaze.world.random.nextFloat() * 0.2f, true);
                        }
                        if (this.fireballCooldown <= 30 && this.fireballCooldown > 15){
                            this.blaze.setVelocity(0, 0.2, 0);
                            ModUtils.spawnForcedParticles((ServerWorld)this.blaze.world, ParticleTypes.SOUL_FIRE_FLAME, this.blaze.getX(), this.blaze.getY(), this.blaze.getZ(), 2, 1, 2, 1, 0.01);
                        }
                        if (this.fireballCooldown <= 13){
                            ModUtils.spawnForcedParticles((ServerWorld)this.blaze.world, ParticleTypes.SOUL_FIRE_FLAME, this.blaze.getX(), this.blaze.getY(), this.blaze.getZ(), 5, 0.1, 2, 0.1, 0.05);
                            this.blaze.setVelocity(0, -1.0, 0);
                            if (this.blaze.isOnGround()){
                                ModUtils.playSound((ServerWorld)this.blaze.world, this.blaze.getX(), this.blaze.getY(), this.blaze.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.HOSTILE, 0.8f, 1.1f + this.blaze.world.random.nextFloat() * 0.2f, true);
                                Box box = new Box(this.blaze.getX()-10, this.blaze.getY()-5, this.blaze.getZ()-10, this.blaze.getX()+10, this.blaze.getY()+5, this.blaze.getZ()+10);
                                List<Entity> pl = this.blaze.world.getOtherEntities(this.blaze, box);
                                for (var i = 0; i < 20; i++) {
                                    for (var j = 0; j < 20; j++) {
                                        BlockPos pPos = new BlockPos(this.blaze.getX()-10+i, this.blaze.getY(), this.blaze.getZ()-10+j);
                                        while (this.blaze.world.getBlockState(pPos.down()).isAir()){
                                            pPos = pPos.down();
                                        }
                                        ModUtils.spawnForcedParticles((ServerWorld)this.blaze.world, ParticleTypes.DRAGON_BREATH, pPos.getX(), pPos.getY(), pPos.getZ(), 3, 1, 0, 1, 0.01);
                                    }
                                }
                                for (Entity p : pl) {
                                    if (p.isOnGround()) {
                                        if (p instanceof LivingEntity) {
                                            p.damage(DamageSource.mob(this.blaze), 8.0f);
                                            p.setVelocity(0, 0.5, 0);
                                        }
                                    }
                                }
                                this.fireballCooldown = 50;
                                this.attack = "";
                            }
                        }
                        if (this.fireballCooldown == 0){
                            this.fireballCooldown = 35;
                            this.attack = "";
                        }
                        break;
                    case "chase":
                        this.blaze.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0.1);
                        this.blaze.setVelocity(e/Math.sqrt(d)/4, f/5 + Math.sin((float)this.fireballCooldown/100), g/Math.sqrt(d)/4);
                        if (this.fireballCooldown == 0){
                            this.fireballCooldown = 40;
                            this.attack = "";
                        }
                        break;
                    case "rain":
                        if (this.fireballCooldown <= 20 && this.fireballCooldown >= 8 + (this.blaze.getHealth()/this.blaze.getMaxHealth())*10){
                            BlockPos pPos = new BlockPos(this.blaze.getX()-10+this.blaze.world.random.nextInt(21), this.blaze.getY() + 2, this.blaze.getZ()-10+this.blaze.world.random.nextInt(21));
                            while (this.blaze.world.getBlockState(pPos.down()).isAir()){
                                pPos = pPos.down();
                            }
                            ShimmerBlazeRainEntity shimmerRain = new ShimmerBlazeRainEntity(ModEntities.SHIMMER_RAIN, this.blaze.world);
                            shimmerRain.setPosition(pPos.getX(), pPos.getY() - 0.5, pPos.getZ());
                            shimmerRain.refreshPositionAndAngles(pPos.getX(), pPos.getY() - 0.5, pPos.getZ(), 0.0f, 0.0f);
                            this.blaze.world.spawnEntity(shimmerRain);
                        }
                        if (this.fireballCooldown == 0){
                            this.fireballCooldown = 30;
                            this.attack = "";
                        }
                        break;
                }
            } else if (this.targetNotVisibleTicks < 5) {
                this.blaze.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
            }
            this.blaze.setAttack(this.attack);
            super.tick();
        }

        private double getFollowRange() {
            return this.blaze.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }

        public String getAttack(){
            return this.attack;
        }
    }


    public String getAttack() {
        return this.dataTracker.get(ATTACK);
    }

    public void setAttack(String attack) {
        this.dataTracker.set(ATTACK, attack);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACK, "");
    }
    @Override
    public void tickMovement() {
        if (!this.onGround && this.getVelocity().y < 0.0) {
            this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
        }
        if (this.world.isClient) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.world.playSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, SoundEvents.ENTITY_BLAZE_BURN, this.getSoundCategory(), 1.0f + this.random.nextFloat(), this.random.nextFloat() * 0.7f + 0.3f, false);
            }
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.WITCH, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0);
            }
        }
        super.tickMovement();
    }
    @Override
    protected void updatePostDeath() {
        ++this.deathTime;
        this.setNoGravity(true);
        this.setVelocity(0, 0.1, 0);
        this.dropXp();
        if (!this.world.isClient()) {
            ModUtils.spawnForcedParticles((ServerWorld) this.world, ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 3, 0.2, 0.2, 0.2, 0.05);
        }
        if (this.deathTime == 40 && !this.world.isClient()) {
            if (!this.world.isClient()) {
                ModUtils.spawnForcedParticles((ServerWorld) this.world, ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 30, 0.5, 0.5, 0.5, 0.2);
            }
            this.world.sendEntityStatus(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }
    @Override
    public boolean isOnFire() {
        return false;
    }
    @Override
    public boolean hurtByWater() {
        return false;
    }
}
