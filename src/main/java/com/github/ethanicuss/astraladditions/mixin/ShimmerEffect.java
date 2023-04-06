package com.github.ethanicuss.astraladditions.mixin;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Entity.class)
public class ShimmerEffect {

    @Inject(method = "baseTick", at = @At("HEAD"))
    public void baseTick(CallbackInfo ci) {
        boolean firstUpdate = ((EntityAccessor) (Entity) (Object) this).getFirstTick();
        Object2DoubleMap<TagKey<Fluid>> fluidHeight = ((EntityAccessor) (Entity) (Object) this).getFluidHeight();
        Level world = ((Entity)(Object)this).getLevel();
        if (!firstUpdate && world.getBlockState(((Entity)(Object)this).blockPosition()).is(ModFluids.SHIMMER)){
            if (((Entity)(Object)this) instanceof LivingEntity le) {
                le.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 1, false, false));
            }
        }
    }
}
