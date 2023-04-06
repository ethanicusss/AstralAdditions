package com.github.ethanicuss.astraladditions.entities.hemogiant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class HemogiantEntity extends EnderMan {
    private static final Double maxHP = 150.0;

    private static final EntityDataAccessor<Integer> EAT_TIME = SynchedEntityData.defineId(HemogiantEntity.class, EntityDataSerializers.INT);
    public HemogiantEntity(EntityType<? extends EnderMan> entityType, Level world) {
        super(entityType, world);
        this.maxUpStep = 2.0f;
        this.setPathfindingMalus(BlockPathTypes.WATER, -0.2f);
    }

    public static AttributeSupplier.Builder createGluttonAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, maxHP).add(Attributes.MOVEMENT_SPEED, 0.12f).add(Attributes.ATTACK_DAMAGE, 16.0).add(Attributes.FOLLOW_RANGE, 96.0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 5.5f;
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.SQUID_INK, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 0.5, (this.random.nextDouble() - 0.5) * 0.5, (this.random.nextDouble() - 0.5) * 0.5);
            if (this.entityData.get(EAT_TIME) <= 0) {
                for (int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.SQUID_INK, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 1.0, (this.random.nextDouble() - 0.8) * 0.5, (this.random.nextDouble() - 0.5) * 1.0);
                    this.level.addParticle(ParticleTypes.DRAGON_BREATH, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, (this.random.nextDouble() - 0.5) * 1.0, (this.random.nextDouble() - 0.5) * 2.0);
                }
            }
            if (this.entityData.get(EAT_TIME) <= -20 - this.getHealth()/maxHP*40 + 1) {
                for (int i = 0; i < 40; ++i) {
                    this.level.addParticle(ParticleTypes.SQUID_INK, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 4.0, (this.random.nextDouble() - 0.5) * 1.0, (this.random.nextDouble() - 0.5) * 4.0);
                    this.level.addParticle(ParticleTypes.DRAGON_BREATH, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 4.0, (this.random.nextDouble() - 0.5) * 2.0, (this.random.nextDouble() - 0.5) * 4.0);
                }
            }
        }
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {

        if (isCreepy()) {
            int eattime = this.entityData.get(EAT_TIME);
            this.entityData.set(EAT_TIME, eattime - 1);
            if (eattime <= 0) {
                this.stopBeingAngry();
                if (eattime <= -20 - this.getHealth()/maxHP*40) {
                    this.level.explode(this, this.getX(), this.getY(), this.getZ(), 4, Explosion.BlockInteraction.BREAK);
                    for (int i = 0; i < 50; i++){
                        int _x = random.nextInt(12) - 6;
                        int _y = random.nextInt(8) - 4;
                        int _z = random.nextInt(12) - 6;
                        BlockPos pos = new BlockPos(this.getBlockX() + _x, this.getBlockY() + _y, this.getBlockZ() + _z);
                        if (this.level.getBlockState(pos) != Blocks.AIR.defaultBlockState() && this.level.getBlockState(pos) != Blocks.END_STONE.defaultBlockState()){
                            this.level.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
                        }
                    }
                    AreaEffectCloud cloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
                    cloud.setOwner((LivingEntity)this);
                    cloud.setParticle(ParticleTypes.DRAGON_BREATH);
                    cloud.setRadius(3.0f);
                    cloud.setDuration(600);
                    cloud.setRadiusPerTick((7.0f - cloud.getRadius()) / (float)cloud.getDuration());
                    cloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
                    cloud.setPos(this.getX(), this.getY(), this.getZ());
                    this.level.levelEvent(LevelEvent.PARTICLES_DRAGON_FIREBALL_SPLASH, this.blockPosition(), this.isSilent() ? -1 : 1);
                    this.level.addFreshEntity(cloud);
                    this.setTarget(this.level.getNearestPlayer(this, 52));
                    this.entityData.set(EAT_TIME, 100 + (int) (this.random.nextFloat() * 30) + (int) (this.getHealth()/maxHP*100));
                }
            }
            else{
                if (this.random.nextFloat() * 100 > (98 + this.getHealth()/maxHP * 2)) {
                    this.teleportRandomly(16, 8);
                }
            }
        }
        super.customServerAiStep();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EAT_TIME, 180);
    }

    protected boolean teleportRandomly(int range, int yRange) {
        if (this.level.isClientSide() || !this.isAlive()) {
            return false;
        }
        double d = this.getX() + (this.random.nextDouble() - 0.5) * range;
        double e = this.getY() + (double)(this.random.nextInt(yRange) - yRange/2);
        double f = this.getZ() + (this.random.nextDouble() - 0.5) * range;
        return this.teleport(d, e, f);
    }
    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, y, z);
        while (mutable.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(mutable).getMaterial().blocksMotion()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.level.getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMotion();
        boolean bl2 = blockState.getFluidState().is(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }
        boolean bl3 = this.randomTeleport(x, y, z, true);
        if (bl3 && !this.isSilent()) {
            this.level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0f, 1.0f);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
        return bl3;
    }
}