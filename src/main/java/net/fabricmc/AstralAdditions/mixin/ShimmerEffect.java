package net.fabricmc.AstralAdditions.mixin;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.fabricmc.AstralAdditions.AstralAdditions;
import net.fabricmc.AstralAdditions.AstralAdditionsClient;
import net.fabricmc.AstralAdditions.fluids.ModFluids;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


@Mixin(Entity.class)
public class ShimmerEffect {

    @Inject(method = "baseTick", at = @At("HEAD"))
    public void baseTick(CallbackInfo ci) {
        boolean firstUpdate = ((EntityAccessor) (Entity) (Object) this).getFirstUpdate();
        Object2DoubleMap<TagKey<Fluid>> fluidHeight = ((EntityAccessor) (Entity) (Object) this).getFluidHeight();
        World world = ((Entity)(Object)this).getWorld();
        if (!firstUpdate && world.getBlockState(((Entity)(Object)this).getBlockPos()).isOf(ModFluids.SHIMMER)){
            if (((Entity)(Object)this) instanceof LivingEntity le) {
                le.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 1, false, false));
            }
        }
    }
}
