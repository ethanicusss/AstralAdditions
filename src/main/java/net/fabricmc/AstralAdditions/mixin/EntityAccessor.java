package net.fabricmc.AstralAdditions.mixin;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor{
    @Accessor
    boolean getFirstUpdate();
    @Accessor
    Object2DoubleMap<TagKey<Fluid>> getFluidHeight();
}