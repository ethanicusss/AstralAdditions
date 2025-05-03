package com.github.ethanicuss.astraladditions.particle;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {

	public static final DefaultParticleType SHIMMER_BUBBLE = FabricParticleTypes.simple();
	public static final DefaultParticleType SHIMMER_SPLASH = FabricParticleTypes.simple();

	public static void registerParticles() {
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(AstralAdditions.MOD_ID, "shimmer_bubble"), SHIMMER_BUBBLE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(AstralAdditions.MOD_ID, "shimmer_splash"), SHIMMER_SPLASH);
	}
}