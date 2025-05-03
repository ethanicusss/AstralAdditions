package com.github.ethanicuss.astraladditions;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.particle.ModParticles;
import com.github.ethanicuss.astraladditions.playertracker.WorldRegister;
import com.github.ethanicuss.astraladditions.registry.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstralAdditions implements ModInitializer {
	public static final String MOD_ID = "astraladditions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static WorldRegister worldRegister = new WorldRegister();

	@Override
	public void onInitialize() {

		ModFluids.registerFluids();
		ModEntities.init();
		ModBlocks.registerBlocks();
		ModItems.registerItems();
		ModSounds.registerSounds();
		//DesizerRecipes.init();
		ModRecipes.registerRecipes();
		ModEntitySpawn.addEntitySpawn();

		ModEffects.registerEffects();
		ModParticles.registerParticles();

		LOGGER.info("Astral Additions is active!");
	}
}