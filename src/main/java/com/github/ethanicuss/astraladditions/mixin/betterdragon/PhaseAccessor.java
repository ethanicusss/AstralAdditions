package com.github.ethanicuss.astraladditions.mixin.betterdragon;

import net.minecraft.world.entity.boss.enderdragon.phases.DragonHoverPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonLandingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonSittingFlamingPhase;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DragonLandingPhase.class)
interface LandingPhaseAccessor {
    @Accessor
    Vec3 getTargetLocation();

    @Accessor("targetLocation")
    public void setTargetLocation(Vec3 target);
}
@Mixin(DragonHoverPhase.class)
interface HoverPhaseAccessor {
    @Accessor
    Vec3 getTargetLocation();

    @Accessor("targetLocation")
    public void setTarget(Vec3 target);
}

@Mixin(DragonSittingFlamingPhase.class)
interface SittingFlamingPhaseAccessor {

    @Accessor
    int getFlameTicks();

    @Accessor
    int getFlameCount();
}
