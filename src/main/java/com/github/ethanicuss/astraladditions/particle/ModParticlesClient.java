package com.github.ethanicuss.astraladditions.particle;

import com.github.ethanicuss.astraladditions.particle.shimmer.ShimmerBubbleParticle;
import com.github.ethanicuss.astraladditions.particle.shimmer.ShimmerSplashParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticlesClient {

	public static void registerFactories() {
		ParticleFactoryRegistry.getInstance().register(ModParticles.SHIMMER_BUBBLE, ShimmerBubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.SHIMMER_SPLASH, ShimmerSplashParticle.Factory::new);
	}
}