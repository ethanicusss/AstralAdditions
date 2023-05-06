package net.fabricmc.AstralAdditions.mixin.betterdragon;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderDragonEntity.class)
public interface DragonAccessor {
    @Accessor
    EnderDragonPart getBody();

    @Accessor
    PhaseManager getPhaseManager();
}
