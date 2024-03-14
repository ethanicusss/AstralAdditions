package com.github.ethanicuss.astraladditions.mixin.betterdragon;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.entity.boss.dragon.phase.HoverPhase;
import net.minecraft.entity.boss.dragon.phase.LandingPhase;
import net.minecraft.entity.boss.dragon.phase.SittingFlamingPhase;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LandingPhase.class)
interface LandingPhaseAccessor {
    @Accessor
    Vec3d getTarget();

    @Accessor("target")
    public void setTarget(Vec3d target);
}
@Mixin(HoverPhase.class)
interface HoverPhaseAccessor {
    @Accessor
    Vec3d getTarget();

    @Accessor("target")
    public void setTarget(Vec3d target);
}

@Mixin(SittingFlamingPhase.class)
interface SittingFlamingPhaseAccessor {

    @Accessor
    int getTicks();

    @Accessor
    int getTimesRun();
}
