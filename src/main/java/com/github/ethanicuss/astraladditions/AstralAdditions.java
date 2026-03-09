package com.github.ethanicuss.astraladditions;

import com.github.ethanicuss.astraladditions.registry.ModFanProcessingType;
import com.github.ethanicuss.astraladditions.registry.ModEntities;
import com.github.ethanicuss.astraladditions.registry.ModFluids;
import com.github.ethanicuss.astraladditions.registry.ModParticles;
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
		ModEntities.registerEntities();
		ModBlocks.registerBlocks();
		ModBlocks.registerBlockItems();
		ModItems.registerItems();
		ModSounds.registerSounds();
		ModRecipes.registerRecipes();
		ModEntitySpawns.registerEntitySpawn();
		ModEffects.registerEffects();
		ModParticles.registerParticles();
		ModPotion.registerPotions();
		ModFanProcessingType.registerFanProcess();
		LOGGER.info("Astral Additions is active!");
	}
}