package com.github.ethanicuss.astraladditions.mixin.fishing;

import com.github.ethanicuss.astraladditions.entities.shimmerfishingrod.ShimmerFishingBobberEntity;
import com.github.ethanicuss.astraladditions.registry.ModData;
import com.github.ethanicuss.astraladditions.registry.ModFluids;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

	@Inject(method = "tickFishingLogic", at = @At("HEAD"), cancellable = true)
	private void controlFishingBasedOnFluid(BlockPos pos, CallbackInfo ci) {
		FishingBobberEntity bobber = (FishingBobberEntity)(Object)this;
		World world = bobber.world;
		FluidState fluidState = world.getFluidState(pos);

		boolean inShimmer = fluidState.isIn(ModData.SHIMMER_TAG);

		if (bobber instanceof ShimmerFishingBobberEntity) {
			if (!inShimmer) {
				ci.cancel();
			}
		} else {
			if (inShimmer) {
				ci.cancel();
			}
		}
	}
}