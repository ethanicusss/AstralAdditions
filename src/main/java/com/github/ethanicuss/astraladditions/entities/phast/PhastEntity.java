package com.github.ethanicuss.astraladditions.entities.phast;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.EnumSet;
import java.util.Random;

public class PhastEntity extends GhastEntity {

    public PhastEntity(EntityType<? extends GhastEntity> entityType, World world) {
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
        return 3;
    }

    public static DefaultAttributeContainer.Builder createPhastAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100.0);
    }

    @Override
    public void tick() {
        super.tick();

    }

    static class FlyRandomlyGoal
            extends Goal {
        private final PhastEntity ghast;

        public FlyRandomlyGoal(PhastEntity ghast) {
            this.ghast = ghast;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
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
        private final PhastEntity ghast;

        public LookAtTargetGoal(PhastEntity ghast) {
            this.ghast = ghast;
            this.setControls(EnumSet.of(Goal.Control.LOOK));
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
        private final PhastEntity ghast;
        public int cooldown;

        public ShootFireballGoal(PhastEntity ghast) {
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
                    FireballEntity fireballEntity = new FireballEntity(world, (LivingEntity)this.ghast, f*2, g*2, h*2, this.ghast.getFireballStrength());
                    fireballEntity.setPosition(this.ghast.getX() + vec3d.x * 4.0, this.ghast.getBodyY(0.5) + 0.5, fireballEntity.getZ() + vec3d.z * 4.0);
                    world.spawnEntity(fireballEntity);
                    this.cooldown = -20;
                }
            } else if (this.cooldown > 0) {
                --this.cooldown;
            }
            this.ghast.setShooting(this.cooldown > 10);
        }
    }

}
