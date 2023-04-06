package com.github.ethanicuss.astraladditions.mixin.betterdragon;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.moondragon.EnderBallEntity;
import com.github.ethanicuss.astraladditions.entities.moondragon.GluttonyBallEntity;
import com.github.ethanicuss.astraladditions.entities.voidtouchedzombie.VoidtouchedZombieEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonSittingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonChargePlayerPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonHoverPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonLandingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonSittingAttackingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonSittingFlamingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(EnderDragon.class)
public class BetterDragon {

    @Shadow @Final private EnderDragonPhaseManager phaseManager;

    @Inject(method = "createAttributes", at = @At("HEAD"), cancellable = true)
    private static void createEnderDragonAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 300.0));
    }

    @Inject(method = "addEffect", at = @At("HEAD"), cancellable = true)
    public void addStatusEffect(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void tickMovement(CallbackInfo ci){
        ((EnderDragon)(Object) this).level.addParticle(ParticleTypes.SQUID_INK, ((EnderDragon)(Object) this).getRandomX(0.5), ((EnderDragon)(Object) this).getRandomY() - 0.25, ((EnderDragon)(Object) this).getRandomZ(0.5), 0, 0, 0);

        if (((EnderDragon)(Object) this).inWall) {
            ((EnderDragon)(Object) this).push(0, 0.01, 0);
        }
        AstralAdditions.LOGGER.debug(phaseManager.getCurrentPhase().toString());
    }

    @Inject(method = "knockBack", at = @At("HEAD"), cancellable = true)
    private void launchLivingEntities(List<Entity> entities, CallbackInfo ci) {
        EnderDragonPart body = ((DragonAccessor) (EnderDragon) (Object) this).getBody();
        EnderDragonPhaseManager phaseManager = ((DragonAccessor) (EnderDragon) (Object) this).getPhaseManager();

        double d = (body.getBoundingBox().minX + body.getBoundingBox().maxX) / 2.0;
        double e = (body.getBoundingBox().minZ + body.getBoundingBox().maxZ) / 2.0;
        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity)) continue;
            if ((entity instanceof VoidtouchedZombieEntity)) continue;
            double f = entity.getX() - d;
            double g = entity.getZ() - e;
            double h = Math.max(f * f + g * g, 0.1);
            entity.setDeltaMovement(f / h * 2.0, 0.2f, g / h * 2.0);
            if (phaseManager.getCurrentPhase().isSitting() || phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.SITTING_ATTACKING || phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.SITTING_FLAMING || ((LivingEntity) entity).getLastHurtByMobTimestamp() >= entity.tickCount - 2)
                continue;


            entity.setDeltaMovement(f / h * 4.0, 0.5f, g / h * 4.0);
            entity.hurt(DamageSource.mobAttack((EnderDragon) (Object) this), 5.0f);
            System.out.println(phaseManager.getCurrentPhase().getPhase().toString());
        }
        ci.cancel();
    }
}

@Mixin(DragonLandingPhase.class)
class BetterLandingPhase {

    @Inject(method = "doServerTick", at = @At("TAIL"))
    public void serverTick(CallbackInfo ci) {
        Vec3 target = ((LandingPhaseAccessor) (DragonLandingPhase) (Object) this).getTargetLocation();
        EnderDragon dragon = ((AbstractPhaseAccessor) (AbstractDragonPhaseInstance) (Object) this).getDragon();
        boolean fountainTooFarAway = false;

        if (target != null){
            if (target.distanceToSqr(dragon.getX(), target.y, dragon.getZ()) > 256.0) {
                fountainTooFarAway = true;
            }
        }
        if (target == null || fountainTooFarAway){
            Player p = dragon.level.getNearestPlayer(dragon, 64);
            if (p != null){
                Vec3 vec3d3 = dragon.getViewVector(1.0f);
                double l = dragon.head.getX() - vec3d3.x;
                double m = dragon.head.getY(0.5) + 0.5;
                double n = dragon.head.getZ() - vec3d3.z;
                EnderBallEntity e = new EnderBallEntity(ModEntities.ENDER_BALL, dragon.level);
                e.setPosRaw(l, m, n);
                e.moveTo(l, m, n, 0.0f, 0.0f);
                double speed = 0.1;
                e.setDeltaMovement((p.getX() - e.getX()) * speed, (p.getY() - e.getY()) * speed, (p.getZ() - e.getZ()) * speed);
                dragon.level.addFreshEntity(e);
                if (!dragon.isSilent()) {
                    dragon.level.levelEvent(null, LevelEvent.SOUND_DRAGON_FIREBALL, dragon.blockPosition(), 0);
                }
            }

            BlockPos pos = dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dragon.blockPosition());
            BlockPos newpos = new BlockPos(pos.getX(), pos.getY() + 4, pos.getZ());
            target = Vec3.atBottomCenterOf(newpos);
            if (target == null && p != null){
                pos = p.blockPosition();
            }
            target = Vec3.atBottomCenterOf(pos);
            ((LandingPhaseAccessor) this).setTargetLocation(target);
        }
        if (dragon.getY() > target.y() + 5) {
            dragon.push(0, -0.05, 0);
        }
        if (target.distanceToSqr(dragon.getX(), dragon.getY(), dragon.getZ()) < 2.0) {
            dragon.getPhaseManager().getPhase(EnderDragonPhase.SITTING_FLAMING).resetFlameCount();
            dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
        }
    }
}

@Mixin(DragonHoverPhase.class)
class BetterHoverPhase {

    @Inject(method = "doServerTick", at = @At("HEAD"))
    public void serverTick(CallbackInfo ci){
        Vec3 target = ((HoverPhaseAccessor) (DragonHoverPhase) (Object) this).getTargetLocation();
        EnderDragon dragon = ((AbstractPhaseAccessor) (AbstractDragonPhaseInstance) (Object) this).getDragon();
        if (target == null){
            BlockPos pos = new BlockPos(dragon.getX(), dragon.getY() + 10, dragon.getZ());
            target = Vec3.atBottomCenterOf(pos);
        }
        ((HoverPhaseAccessor) this).setTarget(target);
        if (dragon.getY() > target.y - 1){
            dragon.getPhaseManager().getPhase(EnderDragonPhase.LANDING);
            dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING);
        }
    }
}

@Mixin(AbstractDragonSittingPhase.class)
class BetterAbstractSittingPhase {

    @Inject(method = "onHurt", at = @At("RETURN"))
    public void modifyDamageTaken(DamageSource damageSource, float damage, CallbackInfoReturnable<Float> cir){
        if (damageSource.getDirectEntity() instanceof AbstractArrow proj) {
            proj.setKnockback(3);
            double vel = 2;
            Player p = proj.level.getNearestPlayer(proj, 64);
            if (p != null) {
                proj.setDeltaMovement((proj.getX() - p.getX()) * vel, (proj.getY() - p.getY()) * vel - 4.5, (proj.getZ() - p.getZ()) * vel);
            }
        }
    }
}

@Mixin(DragonSittingAttackingPhase.class)
class BetterSittingAttackPhase {

    @Inject(method = "begin", at = @At("HEAD"))
    public void beginPhase(CallbackInfo ci) {
        EnderDragon dragon = ((AbstractPhaseAccessor) (AbstractDragonPhaseInstance) (Object) this).getDragon();
        Random random = new Random();
        for (int i = 0; i < 2; i++){
            VoidtouchedZombieEntity z = new VoidtouchedZombieEntity(ModEntities.VOIDTOUCHED_ZOMBIE, dragon.level);
            BlockPos pos = dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dragon.blockPosition());
            if (pos.getY() == 0) {
                pos = new BlockPos(pos.getX(), dragon.getY(), pos.getZ());
            }
            BlockPos newpos = new BlockPos(pos.getX() + random.nextInt(2) - 1, pos.getY(), pos.getZ() + random.nextInt(2) - 1);
            z.setPosRaw(newpos.getX(), newpos.getY(), newpos.getZ());
            z.moveTo(newpos, random.nextInt(360), 0);
            dragon.level.addFreshEntity(z);
        }
    }
}

@Mixin(DragonSittingFlamingPhase.class)
class BetterSittingFlamingPhase {

    @Inject(method = "doServerTick", at = @At("HEAD"))
    public void serverTick(CallbackInfo ci) {
        int ticks = ((SittingFlamingPhaseAccessor) (Object) this).getFlameTicks();
        int timesRun = ((SittingFlamingPhaseAccessor) (Object) this).getFlameCount();
        EnderDragon dragon = ((AbstractPhaseAccessor) (AbstractDragonPhaseInstance) (Object) this).getDragon();
        ++ticks;
        if (ticks >= 30) {
            if (timesRun >= 3) {
                dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
            } else {
                dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
            }
        } else if (ticks == 10) {
            double g;
            Vec3 vec3d = new Vec3(dragon.head.getX() - dragon.getX(), 0.0, dragon.head.getZ() - dragon.getZ()).normalize();
            float f = 5.0f;
            double d = dragon.head.getX() + vec3d.x * 5.0 / 2.0;
            double e = dragon.head.getZ() + vec3d.z * 5.0 / 2.0;
            double h = g = dragon.head.getY(0.5);
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(d, h, e);
            while (dragon.level.isEmptyBlock(mutable)) {
                if ((h -= 1.0) < 0.0) {
                    h = g;
                    break;
                }
                mutable.set(d, h, e);
            }
            h = Mth.floor(h) + 1;
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
                BlockPos pos = dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, hpos);
                AreaEffectCloud dragonBreathEntity = new AreaEffectCloud(dragon.level, pos.getX(), pos.getY(), pos.getZ());
                dragonBreathEntity.setOwner(dragon);
                dragonBreathEntity.setRadius(4.0f+i/2.0f);
                dragonBreathEntity.setDuration(180);
                dragonBreathEntity.setParticle(ParticleTypes.DRAGON_BREATH);
                dragonBreathEntity.addEffect(new MobEffectInstance(MobEffects.HARM));
                dragon.level.addFreshEntity(dragonBreathEntity);
            }
        }
    }
}

@Mixin(DragonChargePlayerPhase.class)
class BetterChargingPlayerPhase {

    @Inject(method = "begin", at = @At("HEAD"))
    public void beginPhase(CallbackInfo ci) {
        EnderDragon dragon = ((AbstractPhaseAccessor) (AbstractDragonPhaseInstance) (Object) this).getDragon();
        Player p = dragon.level.getNearestPlayer(dragon, 96);
        if (p != null) {
            for (var i = 0; i < 3; i++) {
                Vec3 vec3d3 = dragon.getViewVector(1.0f);
                double l = dragon.head.getX() - vec3d3.x;
                double m = dragon.head.getY(0.5) + 0.5;
                double n = dragon.head.getZ() - vec3d3.z;
                GluttonyBallEntity e = new GluttonyBallEntity(ModEntities.GLUTTONY_BALL, dragon.level);
                e.setPosRaw(l, m, n);
                e.moveTo(l, m, n, 0.0f, 0.0f);
                double speed = 0.1;
                e.setDeltaMovement((p.getX() - e.getX()) * speed - 0.1 + i * 0.1, (p.getY() - e.getY()) * speed - 0.2 + i * 0.2, (p.getZ() - e.getZ()) * speed - 0.1 + i * 0.1);
                dragon.level.addFreshEntity(e);
            }
            if (!dragon.isSilent()) {
                dragon.level.levelEvent(null, LevelEvent.SOUND_DRAGON_FIREBALL, dragon.blockPosition(), 0);
            }
        }
    }
}