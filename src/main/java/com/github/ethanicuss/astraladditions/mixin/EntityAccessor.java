package com.github.ethanicuss.astraladditions.mixin;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor{
    @Accessor
    boolean getFirstTick();
    @Accessor
    Object2DoubleMap<TagKey<Fluid>> getFluidHeight();
}