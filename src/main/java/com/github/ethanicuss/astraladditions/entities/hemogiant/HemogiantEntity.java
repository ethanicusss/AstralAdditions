package com.github.ethanicuss.astraladditions.entities.hemogiant;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;

public class HemogiantEntity extends EndermanEntity {
    private static final Double maxHP = 150.0;

    private static final TrackedData<Integer> EAT_TIME = DataTracker.registerData(HemogiantEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public HemogiantEntity(EntityType<? extends EndermanEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 2.0f;
        this.experiencePoints = 20;
        this.setPathfindingPenalty(PathNodeType.WATER, -0.2f);
    }

    public static DefaultAttributeContainer.Builder createGluttonAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, maxHP).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.12f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 96.0);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 5.5f;
    }

    @Override
    public void tickMovement() {
        if (this.world.isClient) {
            this.world.addParticle(ParticleTypes.SQUID_INK, this.getParticleX(0.5), this.getRandomBodyY() - 0.25, this.getParticleZ(0.5), (this.random.nextDouble() - 0.5) * 0.5, (this.random.nextDouble() - 0.5) * 0.5, (this.random.nextDouble() - 0.5) * 0.5);
            if (this.dataTracker.get(EAT_TIME) <= 0) {
                for (int i = 0; i < 4; ++i) {
                    this.world.addParticle(ParticleTypes.SQUID_INK, this.getParticleX(0.5), this.getRandomBodyY() - 0.25, this.getParticleZ(0.5), (this.random.nextDouble() - 0.5) * 1.0, (this.random.nextDouble() - 0.8) * 0.5, (this.random.nextDouble() - 0.5) * 1.0);
                    this.world.addParticle(ParticleTypes.DRAGON_BREATH, this.getParticleX(0.5), this.getRandomBodyY() - 0.25, this.getParticleZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, (this.random.nextDouble() - 0.5) * 1.0, (this.random.nextDouble() - 0.5) * 2.0);
                }
            }
            if (this.dataTracker.get(EAT_TIME) <= -20 - this.getHealth()/maxHP*40 + 1) {
                for (int i = 0; i < 40; ++i) {
                    this.world.addParticle(ParticleTypes.SQUID_INK, this.getParticleX(0.5), this.getRandomBodyY() - 0.25, this.getParticleZ(0.5), (this.random.nextDouble() - 0.5) * 4.0, (this.random.nextDouble() - 0.5) * 1.0, (this.random.nextDouble() - 0.5) * 4.0);
                    this.world.addParticle(ParticleTypes.DRAGON_BREATH, this.getParticleX(0.5), this.getRandomBodyY() - 0.25, this.getParticleZ(0.5), (this.random.nextDouble() - 0.5) * 4.0, (this.random.nextDouble() - 0.5) * 2.0, (this.random.nextDouble() - 0.5) * 4.0);
                }
            }
        }
        super.tickMovement();
    }

    @Override
    protected void mobTick() {

        if (isAngry()) {
            int eattime = this.dataTracker.get(EAT_TIME);
            this.dataTracker.set(EAT_TIME, eattime - 1);
            if (eattime <= 0) {
                if (eattime == 0){
                    this.getWorld().playSoundFromEntity(null, this, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.NEUTRAL, 1.1F, 0.6F + this.random.nextFloat() * 0.1F);
                }
                this.stopAnger();
                if (eattime <= -20 - this.getHealth()/maxHP*40) {
                    this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 4, Explosion.DestructionType.BREAK);
                    for (int i = 0; i < 50; i++){
                        int _x = random.nextInt(12) - 6;
                        int _y = random.nextInt(8) - 4;
                        int _z = random.nextInt(12) - 6;
                        BlockPos pos = new BlockPos(this.getBlockX() + _x, this.getBlockY() + _y, this.getBlockZ() + _z);
                        if (this.world.getBlockState(pos) != Blocks.AIR.getDefaultState() && this.world.getBlockState(pos) != Blocks.END_STONE.getDefaultState()){
                            this.world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
                        }
                    }
                    AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(), this.getZ());
                    cloud.setOwner((LivingEntity)this);
                    cloud.setParticleType(ParticleTypes.DRAGON_BREATH);
                    cloud.setRadius(3.0f);
                    cloud.setDuration(600);
                    cloud.setRadiusGrowth((7.0f - cloud.getRadius()) / (float)cloud.getDuration());
                    cloud.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
                    cloud.setPosition(this.getX(), this.getY(), this.getZ());
                    this.world.syncWorldEvent(WorldEvents.DRAGON_BREATH_CLOUD_SPAWNS, this.getBlockPos(), this.isSilent() ? -1 : 1);
                    this.world.spawnEntity(cloud);
                    this.setTarget(this.world.getClosestPlayer(this, 52));
                    this.dataTracker.set(EAT_TIME, 100 + (int) (this.random.nextFloat() * 30) + (int) (this.getHealth()/maxHP*100));
                }
            }
            else{
                if (this.random.nextFloat() * 100 > (98 + this.getHealth()/maxHP * 2)) {
                    this.teleportRandomly(16, 8);
                }
            }
        }
        super.mobTick();
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(EAT_TIME, 180);
    }

    @Override
    protected void playHurtSound(DamageSource source){
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_ENDERMAN_HURT, SoundCategory.NEUTRAL, 0.8F, 0.4F + this.random.nextFloat() * 0.1F);
        super.playHurtSound(source);
    }
    @Override
    protected void updatePostDeath() {
        if (this.deathTime == 1){
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_ENDERMAN_DEATH, SoundCategory.NEUTRAL, 1.0F, 0.3F + this.random.nextFloat() * 0.1F);
        }
        super.updatePostDeath();
    }

    protected boolean teleportRandomly(int range, int yRange) {
        if (this.world.isClient() || !this.isAlive()) {
            return false;
        }
        else{
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.8F, 0.7F + this.random.nextFloat() * 0.1F);
        }
        double d = this.getX() + (this.random.nextDouble() - 0.5) * range;
        double e = this.getY() + (double)(this.random.nextInt(yRange) - yRange/2);
        double f = this.getZ() + (this.random.nextDouble() - 0.5) * range;
        return this.teleportTo(d, e, f);
    }
    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.world.getBottomY() && !this.world.getBlockState(mutable).getMaterial().blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.world.getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }
        boolean bl3 = this.teleport(x, y, z, true);
        if (bl3 && !this.isSilent()) {
            this.world.playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0f, 1.0f);
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
        return bl3;
    }
}