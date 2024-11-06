package com.github.ethanicuss.astraladditions.mixin;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Entity.class)
public class ShimmerEffect {

    @Inject(method = "baseTick", at = @At("HEAD"))
    public void baseTick(CallbackInfo ci) {
        boolean firstUpdate = ((EntityAccessor) (Entity) (Object) this).getFirstUpdate();
        Object2DoubleMap<TagKey<Fluid>> fluidHeight = ((EntityAccessor) (Entity) (Object) this).getFluidHeight();
        World world = ((Entity)(Object)this).getWorld();
        BlockState state = world.getBlockState(((Entity)(Object)this).getBlockPos());
        if (!firstUpdate && state.isOf(ModFluids.SHIMMER)){
            if (((Entity)(Object)this) instanceof LivingEntity le) {
                le.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0, false, false));
                le.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 330, 0, false, false));
            }
        }
        if (!firstUpdate && world.getBlockState(((Entity)(Object)this).getBlockPos()).isOf(ModFluids.SPUTUM)){
            if (((Entity)(Object)this) instanceof LivingEntity le) {
                le.damage(DamageSource.WITHER, 2);
                le.slowMovement(state, new Vec3d(0.5f, 0.1f, 0.5f));
            }
        }
    }
}
