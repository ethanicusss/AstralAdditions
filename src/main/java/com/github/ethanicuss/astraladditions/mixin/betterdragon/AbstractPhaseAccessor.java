package com.github.ethanicuss.astraladditions.mixin.betterdragon;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractDragonPhaseInstance.class)
public interface AbstractPhaseAccessor {
    @Accessor
    EnderDragon getDragon();
}
