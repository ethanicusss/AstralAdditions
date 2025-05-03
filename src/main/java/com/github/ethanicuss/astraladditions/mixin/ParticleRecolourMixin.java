package com.github.ethanicuss.astraladditions.mixin;

import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.particle.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public class ParticleRecolourMixin {


	private long lastShimmerCheckTime = -1L;
	private BlockPos lastCheckedPos = BlockPos.ORIGIN;
	private boolean lastShimmerNearbyResult = false;

	@Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)V", at = @At("HEAD"), cancellable = true)
	private void onAddParticle(ParticleEffect particle, boolean alwaysSpawn, boolean important, double x, double y, double z, double velX, double velY, double velZ, CallbackInfo ci) {
		if (!isWaterParticle(particle)) return;

		ClientWorld world = MinecraftClient.getInstance().world;
		if (world == null) return;

		BlockPos blockPos = new BlockPos(x, y, z);

		if (shouldCheckNew(blockPos)) {
			lastShimmerNearbyResult = isShimmerNearby(world, blockPos, x, y, z);
			lastCheckedPos = blockPos;
			lastShimmerCheckTime = world.getTime();
		}

		if (lastShimmerNearbyResult) {
			ParticleEffect shimmerParticle = getShimmerReplacement(particle);
			if (shimmerParticle != null) {
				MinecraftClient.getInstance().particleManager.addParticle(shimmerParticle, x, y, z, velX, velY, velZ);
				ci.cancel();
			}
		}
	}

	private boolean isWaterParticle(ParticleEffect particle) {
		return particle == ParticleTypes.SPLASH
				|| particle == ParticleTypes.RAIN
				|| particle == ParticleTypes.BUBBLE;
	}

	private boolean isShimmerFluid(ClientWorld world, BlockPos pos) {
		Fluid fluid = world.getFluidState(pos).getFluid();
		return fluid == ModFluids.STILL_SHIMMER || fluid == ModFluids.FLOWING_SHIMMER;
	}

	private boolean isShimmerNearby(ClientWorld world, BlockPos center, double particleX, double particleY, double particleZ) {
		double maxDistanceSquared = 1.125 * 1.125;

		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dz = -1; dz <= 1; dz++) {
					BlockPos checkPos = center.add(dx, dy, dz);
					if (isShimmerFluid(world, checkPos)) {
						double cx = checkPos.getX() + 0.25;
						double cy = checkPos.getY() + 0.25;
						double cz = checkPos.getZ() + 0.25;

						double distSq = (cx - particleX) * (cx - particleX)
								+ (cy - particleY) * (cy - particleY)
								+ (cz - particleZ) * (cz - particleZ);

						if (distSq <= maxDistanceSquared) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private ParticleEffect getShimmerReplacement(ParticleEffect original) {
		ParticleType<?> type = original.getType();
		Identifier id = Registry.PARTICLE_TYPE.getId(type);

		if (id == null) return null;

		return switch (id.getPath()) {
			case "splash", "rain" -> ModParticles.SHIMMER_SPLASH;
			case "bubble" -> ModParticles.SHIMMER_BUBBLE;
			default -> null;
		};
	}

	private boolean shouldCheckNew(BlockPos currentPos) {
		return !currentPos.equals(lastCheckedPos) || MinecraftClient.getInstance().world.getTime() != lastShimmerCheckTime;
	}
}