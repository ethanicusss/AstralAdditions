package com.github.ethanicuss.astraladditions;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.glutton.GluttonEntity;
import com.github.ethanicuss.astraladditions.entities.moondragon.EnderBallEntity;
import com.github.ethanicuss.astraladditions.entities.moondragon.GluttonyBallEntity;
import com.github.ethanicuss.astraladditions.entities.moonman.MoonmanEntity;
import com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton.VoidtouchedSkeletonEntity;
import com.github.ethanicuss.astraladditions.entities.voidtouchedzombie.VoidtouchedZombieEntity;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstralAdditions implements ModInitializer {
	public static final String MOD_ID = "astraladditions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModFluids.registerFluids();
		ModEntities.init();
		LOGGER.info("Astral Additions is active!");
	}
}