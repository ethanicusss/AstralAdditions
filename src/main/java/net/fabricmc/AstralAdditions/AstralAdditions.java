package net.fabricmc.AstralAdditions;

import net.fabricmc.AstralAdditions.entities.glutton.GluttonEntity;
import net.fabricmc.AstralAdditions.entities.moondragon.EnderBallEntity;
import net.fabricmc.AstralAdditions.entities.moondragon.GluttonyBallEntity;
import net.fabricmc.AstralAdditions.entities.moonman.MoonmanEntity;
import net.fabricmc.AstralAdditions.entities.voidtouchedskeleton.VoidtouchedSkeletonEntity;
import net.fabricmc.AstralAdditions.entities.voidtouchedzombie.VoidtouchedZombieEntity;
import net.fabricmc.AstralAdditions.fluids.ModFluids;
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
	public static final EntityType<MoonmanEntity> MOONMAN = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(AstralAdditions.MOD_ID, "moonman"),
			FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, MoonmanEntity::new).dimensions(EntityDimensions.fixed(0.75f, 2.8f)).build()
	);
	public static final EntityType<GluttonEntity> GLUTTON = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(AstralAdditions.MOD_ID, "hemogiant"),
			FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GluttonEntity::new).dimensions(EntityDimensions.fixed(1.05f, 5.8f)).build()
	);
	public static final EntityType<VoidtouchedSkeletonEntity> VOIDTOUCHED_SKELETON = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(AstralAdditions.MOD_ID, "voidtouched_skeleton"),
			FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, VoidtouchedSkeletonEntity::new).dimensions(EntityDimensions.fixed(0.75f, 1.8f)).build()
	);
	public static final EntityType<VoidtouchedZombieEntity> VOIDTOUCHED_ZOMBIE = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(AstralAdditions.MOD_ID, "voidtouched_zombie"),
			FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, VoidtouchedZombieEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.5f)).build()
	);
	public static final EntityType<EnderBallEntity> ENDER_BALL = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(AstralAdditions.MOD_ID, "ender_ball"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, EnderBallEntity::new).dimensions(EntityDimensions.fixed(1.0f, 1.0f)).build()
	);
	public static final EntityType<GluttonyBallEntity> GLUTTONY_BALL = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(AstralAdditions.MOD_ID, "gluttony_ball"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, GluttonyBallEntity::new).dimensions(EntityDimensions.fixed(1.0f, 1.0f)).build()
	);

	@Override
	public void onInitialize() {
		ModFluids.registerFluids();
		FabricDefaultAttributeRegistry.register(MOONMAN, MoonmanEntity.createMoonmanAttributes());
		FabricDefaultAttributeRegistry.register(GLUTTON, GluttonEntity.createGluttonAttributes());
		FabricDefaultAttributeRegistry.register(VOIDTOUCHED_SKELETON, VoidtouchedSkeletonEntity.createVoidtouchedSkeletonAttributes());
		FabricDefaultAttributeRegistry.register(VOIDTOUCHED_ZOMBIE, VoidtouchedZombieEntity.createVoidtouchedZombieAttributes());
		LOGGER.info("Astral Additions is active!");
	}
}