package com.github.ethanicuss.astraladditions.particle.shimmer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class ShimmerSplashParticle extends RainSplashParticle {

	public ShimmerSplashParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
		super(clientWorld, d, e, f);
		this.gravityStrength = 0.04F;
		if (h == (double)0.0F && (g != (double)0.0F || i != (double)0.0F)) {
			this.velocityX = g;
			this.velocityY = 0.1;
			this.velocityZ = i;
		}

	}

	@Environment(EnvType.CLIENT)
	public static class SplashFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public SplashFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			ShimmerSplashParticle shimmerSplashParticle = new ShimmerSplashParticle(clientWorld, d, e, f, g, h, i);
			shimmerSplashParticle.setSprite(this.spriteProvider);
			return shimmerSplashParticle;
		}
	}

	@Override
	public int getBrightness(float tint) {
		return 240;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
}