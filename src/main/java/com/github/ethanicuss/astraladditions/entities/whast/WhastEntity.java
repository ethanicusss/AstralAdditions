package com.github.ethanicuss.astraladditions.entities.whast;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;

import java.util.EnumSet;
import java.util.Random;

public class WhastEntity extends GhastEntity {

    private static final TrackedData<Boolean> SHOOT_SOUND_PLAYED = DataTracker.registerData(GhastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private boolean hasPlayedShootSound = false;

    public WhastEntity(EntityType<? extends GhastEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(5, new FlyRandomlyGoal(this));
        this.goalSelector.add(7, new LookAtTargetGoal(this));
        this.goalSelector.add(7, new ShootFireballGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<PlayerEntity>(this, PlayerEntity.class, 10, true, false, entity -> Math.abs(entity.getY() - this.getY()) <= 4.0));
    }

    @Override
    public int getFireballStrength() {
        return 2;
    }

    public static DefaultAttributeContainer.Builder createWhastAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isShooting()){
            if (!this.getDataTracker().get(SHOOT_SOUND_PLAYED)) {
                this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F + this.random.nextFloat() * 0.1F);
                this.getDataTracker().set(SHOOT_SOUND_PLAYED, true);
            }
        }
        else{
            if (this.getDataTracker().get(SHOOT_SOUND_PLAYED)) {this.getDataTracker().set(SHOOT_SOUND_PLAYED, false);}
        }
    }

    @Override
    protected void playHurtSound(DamageSource source){
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_GHAST_HURT, SoundCategory.NEUTRAL, 0.8F, 0.7F + this.random.nextFloat() * 0.1F);
        super.playHurtSound(source);
    }
    @Override
    protected void updatePostDeath() {
        if (this.deathTime == 1){
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.NEUTRAL, 1.0F, 0.8F + this.random.nextFloat() * 0.1F);
        }
        super.updatePostDeath();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SHOOT_SOUND_PLAYED, false);
    }

    static class FlyRandomlyGoal
            extends Goal {
        private final WhastEntity ghast;

        public FlyRandomlyGoal(WhastEntity ghast) {
            this.ghast = ghast;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            double f;
            double e;
            MoveControl moveControl = this.ghast.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            }
            double d = moveControl.getTargetX() - this.ghast.getX();
            double g = d * d + (e = moveControl.getTargetY() - this.ghast.getY()) * e + (f = moveControl.getTargetZ() - this.ghast.getZ()) * f;
            return g < 1.0 || g > 3600.0;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            Random random = this.ghast.getRandom();
            double d = this.ghast.getX() + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double e = this.ghast.getY() + (double)((random.nextFloat() * 2.0f - 1.0f) * 8.0f);
            double f = this.ghast.getZ() + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            this.ghast.getMoveControl().moveTo(d, e, f, 0.8);
        }
    }

    static class LookAtTargetGoal
            extends Goal {
        private final WhastEntity ghast;

        public LookAtTargetGoal(WhastEntity ghast) {
            this.ghast = ghast;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return true;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3d vec3d = this.ghast.getVelocity();
                this.ghast.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776f);
                this.ghast.bodyYaw = this.ghast.getYaw();
            } else {
                LivingEntity livingEntity = this.ghast.getTarget();
                double d = 64.0;
                if (livingEntity.squaredDistanceTo(this.ghast) < 4096.0) {
                    double e = livingEntity.getX() - this.ghast.getX();
                    double f = livingEntity.getZ() - this.ghast.getZ();
                    this.ghast.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776f);
                    this.ghast.bodyYaw = this.ghast.getYaw();
                    if (livingEntity.squaredDistanceTo(this.ghast) > Math.pow(24, 2)){
                        this.ghast.setVelocity(e/100, this.ghast.getVelocity().getY(), f/100);
                    }
                }
            }
        }
    }

    static class ShootFireballGoal
            extends Goal {
        private final WhastEntity ghast;
        public int cooldown;

        public ShootFireballGoal(WhastEntity ghast) {
            this.ghast = ghast;
        }

        @Override
        public boolean canStart() {
            return this.ghast.getTarget() != null;
        }

        @Override
        public void start() {
            this.cooldown = 0;
        }

        @Override
        public void stop() {
            this.ghast.setShooting(false);
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.ghast.getTarget();
            if (livingEntity == null) {
                return;
            }
            double d = 64.0;
            if (livingEntity.squaredDistanceTo(this.ghast) < 4096.0 && this.ghast.canSee(livingEntity)) {
                World world = this.ghast.world;
                ++this.cooldown;

                double speed = 0.1;
                double dyaw = Math.atan((livingEntity.getX() - this.ghast.getX())/(livingEntity.getZ() - this.ghast.getZ()));
                double dpitch = Math.atan((livingEntity.getX() - this.ghast.getX())/(livingEntity.getY() - this.ghast.getY()));
                double dx = Math.sin(dyaw)*speed;
                double dz = Math.cos(dyaw)*speed;
                double dy = Math.cos(dpitch)*speed;
                if (livingEntity.getZ()<this.ghast.getZ()){dx *= -1;dz *= -1;}
                this.ghast.setVelocity(dx, -dy, dz);
                if (this.cooldown == 10 && !this.ghast.isSilent()) {
                    world.syncWorldEvent(null, WorldEvents.GHAST_WARNS, this.ghast.getBlockPos(), 0);
                }
                if (this.cooldown == 20) {
                    double e = 4.0;
                    Vec3d vec3d = this.ghast.getRotationVec(1.0f);
                    double f = livingEntity.getX() - (this.ghast.getX() + vec3d.x * 4.0);
                    double g = livingEntity.getBodyY(0.5) - (0.5 + this.ghast.getBodyY(0.5));
                    double h = livingEntity.getZ() - (this.ghast.getZ() + vec3d.z * 4.0);
                    if (!this.ghast.isSilent()) {
                        world.syncWorldEvent(null, WorldEvents.GHAST_SHOOTS, this.ghast.getBlockPos(), 0);
                    }
                    float spread = 15;
                    for (int i = 0; i < 7; i++) {
                        WitherSkullEntity fireballEntity = new WitherSkullEntity(world, (LivingEntity) this.ghast, f * 2 + this.ghast.random.nextFloat()*spread - spread/2, g * 2 + this.ghast.random.nextFloat()*spread - spread/2, h * 2 + this.ghast.random.nextFloat()*spread - spread/2);
                        fireballEntity.setPosition(this.ghast.getX() + vec3d.x * 4.0, this.ghast.getBodyY(0.5) + 0.5, fireballEntity.getZ() + vec3d.z * 4.0);
                        world.spawnEntity(fireballEntity);
                    }
                    this.cooldown = -80;
                }
            } else if (this.cooldown > 0) {
                --this.cooldown;
            }
            this.ghast.setShooting(this.cooldown > 10);
        }
    }

}
