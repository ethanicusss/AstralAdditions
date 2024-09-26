package com.github.ethanicuss.astraladditions.entities.ender_watcher;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.moondragon.GluttonyBallEntity;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeRainEntity;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.SmallShimmerballEntity;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.EnumSet;
import java.util.List;

public class EnderWatcherEntity extends BlazeEntity {

    public EnderWatcherEntity(EntityType<? extends BlazeEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createWatcherAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 60).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.12f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 96.0);
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

    @Override
    public void tickMovement() {
        if (!this.onGround && this.getVelocity().y < 0.0) {
            this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
        }
        if (this.world.isClient) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.world.playSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, SoundEvents.BLOCK_SHROOMLIGHT_STEP, this.getSoundCategory(), 1.0f + this.random.nextFloat(), this.random.nextFloat() * 0.7f + 0.3f, false);
            }
            this.world.addParticle(ParticleTypes.DRAGON_BREATH, this.getParticleX(3), this.getRandomBodyY(), this.getParticleZ(3), 0.0, 0.0, 0.0);

        }
        super.tickMovement();
    }

    static class ShootFireballGoal
            extends Goal {
        private final EnderWatcherEntity blaze;
        private int fireballCooldown;
        private int targetNotVisibleTicks;
        private String attack = "";
        private double tpX;
        private double tpY;
        private double tpZ;

        public ShootFireballGoal(EnderWatcherEntity blaze) {
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
                    switch (this.blaze.world.random.nextInt(4)) {
                        case 0,1 -> this.attack = "burst";
                        case 2 -> this.attack = "rain";
                        case 3 -> this.attack = "teleport";
                    }
                    this.fireballCooldown = 20;
                }
                double e = livingEntity.getX() - this.blaze.getX();
                double f = livingEntity.getBodyY(0.5) - this.blaze.getBodyY(0.5);
                double g = livingEntity.getZ() - this.blaze.getZ();
                switch (this.attack) {
                    case "burst":
                        if (this.fireballCooldown == 10) {
                            int rand = this.blaze.world.random.nextInt(2);
                            this.blaze.addVelocity(e / (14 - rand * 5), 0.7, g / (14 - rand * 5));
                        }
                        if (this.fireballCooldown <= 0) {
                            ModUtils.spawnForcedParticles((ServerWorld)this.blaze.world, ParticleTypes.WITCH, this.blaze.getX(), this.blaze.getY(), this.blaze.getZ(), 10, 1, 2, 1, 0.01);

                            double h = Math.sqrt(Math.sqrt(d)) * 0.015;
                            if (!this.blaze.isSilent()) {
                                this.blaze.world.syncWorldEvent(null, WorldEvents.BLAZE_SHOOTS, this.blaze.getBlockPos(), 0);
                            }
                            for (int i = 0; i < 1; ++i) {
                                ModUtils.playSound((ServerWorld)this.blaze.world, this.blaze.getX(), this.blaze.getY(), this.blaze.getZ(), SoundEvents.ENTITY_SHULKER_OPEN, SoundCategory.HOSTILE, 1.0f, 1.1f + this.blaze.world.random.nextFloat() * 0.2f, true);
                                this.blaze.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 0.5f, 1.1f + this.blaze.world.random.nextFloat() * 0.2f);
                                /*SmallShimmerballEntity smallShimmerballEntity = new SmallShimmerballEntity(ModEntities.SMALL_SHIMMERBALL, this.blaze.world);
                                smallShimmerballEntity.setPosition(this.blaze.getX(), this.blaze.getBodyY(0.5) + 0.5, this.blaze.getZ());
                                smallShimmerballEntity.refreshPositionAndAngles(this.blaze.getX(), this.blaze.getBodyY(0.5) + 0.5, this.blaze.getZ(), 0.0f, 0.0f);
                                double speed = 0.05;
                                smallShimmerballEntity.setVelocity(e * speed + this.blaze.getRandom().nextGaussian() * h, f * speed + this.blaze.getRandom().nextGaussian() * h, g * speed + this.blaze.getRandom().nextGaussian() * h);
                                this.blaze.world.spawnEntity(smallShimmerballEntity);*/
                                ItemStack arrow = new ItemStack(Items.SPECTRAL_ARROW);
                                PersistentProjectileEntity persistentProjectileEntity = ProjectileUtil.createArrowProjectile(this.blaze, arrow, 3.0f);
                                double dArrow = livingEntity.getX() - this.blaze.getX();
                                double eArrow = livingEntity.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
                                double fArrow = livingEntity.getZ() - this.blaze.getZ();
                                double gArrow = Math.sqrt(dArrow * dArrow + fArrow * fArrow);
                                persistentProjectileEntity.setVelocity(dArrow, eArrow + gArrow * 0.20000000298023224, fArrow, (float) (1.4 + this.blaze.world.getDifficulty().getId() * 0.2), (float) (14 - this.blaze.world.getDifficulty().getId() * 4));
                                persistentProjectileEntity.setPunch(2);
                                this.blaze.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.blaze.getRandom().nextFloat() * 0.4F + 0.8F));
                                this.blaze.world.spawnEntity(persistentProjectileEntity);
                            }

                            this.fireballCooldown = 40 + (int) (this.blaze.world.random.nextFloat() * 10 + (this.blaze.getHealth()/this.blaze.getMaxHealth())*30);
                            this.attack = "";
                        }
                        this.blaze.getLookControl().lookAt(livingEntity, 10.0f, 10.0f);
                        break;
                    case "rain":
                        if (this.fireballCooldown <= 20 && this.fireballCooldown >= 8 + (this.blaze.getHealth()/this.blaze.getMaxHealth())*10){
                            BlockPos pPos = new BlockPos(this.blaze.getX()-5+this.blaze.world.random.nextInt(11), this.blaze.getY() + 4, this.blaze.getZ()-5+this.blaze.world.random.nextInt(11));
                            int stopper = 0;
                            while (!this.blaze.world.getBlockState(pPos).isAir() && stopper < 32){
                                pPos = pPos.down();
                                stopper++;
                            }
                            /*ShimmerBlazeRainEntity shimmerRain = new ShimmerBlazeRainEntity(ModEntities.SHIMMER_RAIN, this.blaze.world);
                            shimmerRain.setPosition(pPos.getX(), pPos.getY() - 0.5, pPos.getZ());
                            shimmerRain.refreshPositionAndAngles(pPos.getX(), pPos.getY() - 0.5, pPos.getZ(), 0.0f, 0.0f);
                            this.blaze.world.spawnEntity(shimmerRain);*/
                            GluttonyBallEntity gluttonyBallEntity = new GluttonyBallEntity(ModEntities.GLUTTONY_BALL, this.blaze.world);
                            gluttonyBallEntity.setPos(pPos.getX(), pPos.getY() - 0.5, pPos.getZ());
                            gluttonyBallEntity.refreshPositionAndAngles(pPos.getX(), pPos.getY() - 0.5, pPos.getZ(), 0.0f, 0.0f);
                            //double speed = 0.1;
                            //e.setVelocity((p.getX() - e.getX()) * speed - 0.1 + i * 0.1, (p.getY() - e.getY()) * speed - 0.2 + i * 0.2, (p.getZ() - e.getZ()) * speed - 0.1 + i * 0.1);
                            this.blaze.world.spawnEntity(gluttonyBallEntity);
                        }
                        if (this.fireballCooldown == 0){
                            this.fireballCooldown = 30;
                            this.attack = "";
                        }
                        break;
                    case "teleport":
                        if (this.fireballCooldown == 20){
                            PlayerEntity p = this.blaze.world.getClosestPlayer(this.blaze, 48);
                            this.tpX = p.getX();
                            this.tpY = p.getY() + 1.5;
                            this.tpZ = p.getZ();

                        }
                        if (this.fireballCooldown < 20){
                            ModUtils.spawnForcedParticles((ServerWorld)this.blaze.world, ParticleTypes.SQUID_INK, this.tpX, this.tpY, this.tpZ, 3, 0, 2, 0, 0.01);
                        }
                        if (this.fireballCooldown == 0){
                            this.blaze.setPos(this.tpX, this.tpY, this.tpZ);
                            ModUtils.spawnForcedParticles((ServerWorld)this.blaze.world, ParticleTypes.END_ROD, this.tpX, this.tpY, this.tpZ, 20, 1, 2, 1, 0.05);

                            this.fireballCooldown = 0;
                            this.attack = "";
                        }
                        break;
                }
            } else if (this.targetNotVisibleTicks < 5) {
                this.blaze.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
            }
            super.tick();
        }

        private double getFollowRange() {
            return this.blaze.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }

        public String getAttack(){
            return this.attack;
        }
    }
}
