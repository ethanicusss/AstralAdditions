package com.github.ethanicuss.astraladditions.mixin.betterdragon;

import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderDragon.class)
public interface DragonAccessor {
    @Accessor
    EnderDragonPart getBody();

    @Accessor
    EnderDragonPhaseManager getPhaseManager();
}
