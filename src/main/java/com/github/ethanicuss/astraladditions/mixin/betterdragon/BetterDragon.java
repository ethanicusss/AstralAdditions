package com.github.ethanicuss.astraladditions.mixin.betterdragon;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.moondragon.EnderBallEntity;
import com.github.ethanicuss.astraladditions.entities.voidtouchedzombie.VoidtouchedZombieEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.phase.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(EnderDragonEntity.class)
public class BetterDragon {

    @Shadow @Final private PhaseManager phaseManager;

    @Inject(method = "createEnderDragonAttributes", at = @At("HEAD"), cancellable = true)
    private static void createEnderDragonAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 300.0));
    }

    @Inject(method = "addStatusEffect", at = @At("HEAD"), cancellable = true)
    public void addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void tickMovement(CallbackInfo ci){
        //System.out.println(((EnderDragonEntity)(Object) this).getPhaseManager().getCurrent().getType().toString());
    }

    @Inject(method = "launchLivingEntities", at = @At("HEAD"), cancellable = true)
    private void launchLivingEntities(List<Entity> entities, CallbackInfo ci) {
        EnderDragonPart body = ((DragonAccessor) (EnderDragonEntity) (Object) this).getBody();
        PhaseManager phaseManager = ((DragonAccessor) (EnderDragonEntity) (Object) this).getPhaseManager();

        double d = (body.getBoundingBox().minX + body.getBoundingBox().maxX) / 2.0;
        double e = (body.getBoundingBox().minZ + body.getBoundingBox().maxZ) / 2.0;
        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity)) continue;
            if ((entity instanceof VoidtouchedZombieEntity)) continue;
            double f = entity.getX() - d;
            double g = entity.getZ() - e;
            double h = Math.max(f * f + g * g, 0.1);
            entity.setVelocity(f / h * 2.0, 0.2f, g / h * 2.0);
            if (phaseManager.getCurrent().isSittingOrHovering() || phaseManager.getCurrent().getType() == PhaseType.SITTING_ATTACKING || phaseManager.getCurrent().getType() == PhaseType.SITTING_FLAMING || ((LivingEntity) entity).getLastAttackedTime() >= entity.age - 2)
                continue;


            entity.setVelocity(f / h * 4.0, 0.5f, g / h * 4.0);
            entity.damage(DamageSource.mob((EnderDragonEntity) (Object) this), 5.0f);
            System.out.println(phaseManager.getCurrent().getType().toString());
        }
        ci.cancel();
    }
}

@Mixin(LandingPhase.class)
class BetterLandingPhase {

    @Inject(method = "serverTick", at = @At("TAIL"))
    public void serverTick(CallbackInfo ci) {
        Vec3d target = ((LandingPhaseAccessor) (LandingPhase) (Object) this).getTarget();
        EnderDragonEntity dragon = ((AbstractPhaseAccessor) (AbstractPhase) (Object) this).getDragon();
        boolean fountainTooFarAway = false;

        if (target != null){
            if (target.squaredDistanceTo(dragon.getX(), target.y, dragon.getZ()) > 256.0) {
                fountainTooFarAway = true;
            }
        }
        if (target == null || fountainTooFarAway){
            PlayerEntity p = dragon.world.getClosestPlayer(dragon, 64);
            if (p != null){
                Vec3d vec3d3 = dragon.getRotationVec(1.0f);
                double l = dragon.head.getX() - vec3d3.x;
                double m = dragon.head.getBodyY(0.5) + 0.5;
                double n = dragon.head.getZ() - vec3d3.z;
                EnderBallEntity e = new EnderBallEntity(AstralAdditions.ENDER_BALL, dragon.world);
                e.setPos(l, m, n);
                e.refreshPositionAndAngles(l, m, n, 0.0f, 0.0f);
                double speed = 0.1;
                e.setVelocity((p.getX() - e.getX()) * speed, (p.getY() - e.getY()) * speed, (p.getZ() - e.getZ()) * speed);
                dragon.world.spawnEntity(e);
                if (!dragon.isSilent()) {
                    dragon.world.syncWorldEvent(null, WorldEvents.ENDER_DRAGON_SHOOTS, dragon.getBlockPos(), 0);
                }
            }

            BlockPos pos = dragon.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, dragon.getBlockPos());
            if (pos.getY() == 0 && p != null){
                pos = p.getBlockPos();
            }
            BlockPos newpos = new BlockPos(pos.getX(), pos.getY() + 4, pos.getZ());
            target = Vec3d.ofBottomCenter(newpos);
            ((LandingPhaseAccessor) this).setTarget(target);
        }
        if (target.squaredDistanceTo(dragon.getX(), dragon.getY(), dragon.getZ()) < 1.0) {
            dragon.getPhaseManager().create(PhaseType.SITTING_FLAMING).reset();
            dragon.getPhaseManager().setPhase(PhaseType.SITTING_SCANNING);
        }
    }
}

@Mixin(HoverPhase.class)
class BetterHoverPhase {

    @Inject(method = "serverTick", at = @At("HEAD"))
    public void serverTick(CallbackInfo ci){
        Vec3d target = ((HoverPhaseAccessor) (HoverPhase) (Object) this).getTarget();
        EnderDragonEntity dragon = ((AbstractPhaseAccessor) (AbstractPhase) (Object) this).getDragon();
        if (target == null){
            BlockPos pos = new BlockPos(dragon.getX(), dragon.getY() + 10, dragon.getZ());
            target = Vec3d.ofBottomCenter(pos);
        }
        ((HoverPhaseAccessor) this).setTarget(target);
        if (dragon.getY() > target.y - 1){
            dragon.getPhaseManager().create(PhaseType.LANDING);
            dragon.getPhaseManager().setPhase(PhaseType.LANDING);
        }
    }
}

@Mixin(AbstractSittingPhase.class)
class BetterAbstractSittingPhase {

    @Inject(method = "modifyDamageTaken", at = @At("RETURN"))
    public void modifyDamageTaken(DamageSource damageSource, float damage, CallbackInfoReturnable<Float> cir){
        if (damageSource.getSource() instanceof PersistentProjectileEntity proj) {
            proj.setPunch(3);
            double vel = 2;
            PlayerEntity p = proj.world.getClosestPlayer(proj, 64);
            if (p != null) {
                proj.setVelocity((proj.getX() - p.getX()) * vel, (proj.getY() - p.getY()) * vel - 4.5, (proj.getZ() - p.getZ()) * vel);
            }
        }
    }
}

@Mixin(SittingAttackingPhase.class)
class BetterSittingAttackPhase {

    @Inject(method = "beginPhase", at = @At("HEAD"))
    public void beginPhase(CallbackInfo ci) {
        EnderDragonEntity dragon = ((AbstractPhaseAccessor) (AbstractPhase) (Object) this).getDragon();
        Random random = new Random();
        for (int i = 0; i < 2; i++){
            VoidtouchedZombieEntity z = new VoidtouchedZombieEntity(AstralAdditions.VOIDTOUCHED_ZOMBIE, dragon.world);
            BlockPos pos = dragon.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, dragon.getBlockPos());
            if (pos.getY() == 0) {
                pos = new BlockPos(pos.getX(), dragon.getY(), pos.getZ());
            }
            BlockPos newpos = new BlockPos(pos.getX() + random.nextInt(2) - 1, pos.getY(), pos.getZ() + random.nextInt(2) - 1);
            z.setPos(newpos.getX(), newpos.getY(), newpos.getZ());
            z.refreshPositionAndAngles(newpos, random.nextInt(360), 0);
            dragon.world.spawnEntity(z);
        }
    }
}

@Mixin(SittingFlamingPhase.class)
class BetterSittingFlamingPhase {

    @Inject(method = "serverTick", at = @At("HEAD"))
    public void serverTick(CallbackInfo ci) {
        int ticks = ((SittingFlamingPhaseAccessor) (Object) this).getTicks();
        int timesRun = ((SittingFlamingPhaseAccessor) (Object) this).getTimesRun();
        EnderDragonEntity dragon = ((AbstractPhaseAccessor) (AbstractPhase) (Object) this).getDragon();
        ++ticks;
        if (ticks >= 30) {
            if (timesRun >= 3) {
                dragon.getPhaseManager().setPhase(PhaseType.TAKEOFF);
            } else {
                dragon.getPhaseManager().setPhase(PhaseType.SITTING_SCANNING);
            }
        } else if (ticks == 10) {
            double g;
            Vec3d vec3d = new Vec3d(dragon.head.getX() - dragon.getX(), 0.0, dragon.head.getZ() - dragon.getZ()).normalize();
            float f = 5.0f;
            double d = dragon.head.getX() + vec3d.x * 5.0 / 2.0;
            double e = dragon.head.getZ() + vec3d.z * 5.0 / 2.0;
            double h = g = dragon.head.getBodyY(0.5);
            BlockPos.Mutable mutable = new BlockPos.Mutable(d, h, e);
            while (dragon.world.isAir(mutable)) {
                if ((h -= 1.0) < 0.0) {
                    h = g;
                    break;
                }
                mutable.set(d, h, e);
            }
            h = MathHelper.floor(h) + 1;
            for (var i = 0; i < 5; i++) {
                double xdiff = dragon.getX() - d;
                double zdiff = dragon.getZ() - e;
                double dist = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(zdiff, 2)) * -i * 0.8;
                if (xdiff == 0){
                    xdiff = 0.01;
                }
                if (zdiff == 0){
                    zdiff = 0.01;
                }
                double angleX = Math.atan(Math.abs(zdiff) / xdiff);
                double angleZ = Math.atan(Math.abs(xdiff) / zdiff);
                double cosX = Math.cos(angleX);
                double cosZ = Math.cos(angleZ);
                if (cosX == 0){
                    cosX = 0.01;
                }
                if (cosZ == 0){
                    cosZ = 0.01;
                }

                BlockPos hpos = new BlockPos(d + dist*cosX*(Math.abs(angleX) / angleX), h, e + dist*cosZ*(Math.abs(angleZ) / angleZ));
                BlockPos pos = dragon.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, hpos);
                AreaEffectCloudEntity dragonBreathEntity = new AreaEffectCloudEntity(dragon.world, pos.getX(), pos.getY(), pos.getZ());
                dragonBreathEntity.setOwner(dragon);
                dragonBreathEntity.setRadius(4.0f+i/2.0f);
                dragonBreathEntity.setDuration(180);
                dragonBreathEntity.setParticleType(ParticleTypes.DRAGON_BREATH);
                dragonBreathEntity.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE));
                dragon.world.spawnEntity(dragonBreathEntity);
            }
        }
    }
}