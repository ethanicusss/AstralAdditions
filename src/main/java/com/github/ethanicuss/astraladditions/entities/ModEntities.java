package com.github.ethanicuss.astraladditions.entities;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEntity;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEntityModel;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.moondragon.EnderBallEntity;
import com.github.ethanicuss.astraladditions.entities.moondragon.EnderBallEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.moonman.MoonmanEntity;
import com.github.ethanicuss.astraladditions.entities.moonman.MoonmanEntityModel;
import com.github.ethanicuss.astraladditions.entities.moonman.MoonmanEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton.VoidtouchedSkeletonEntity;
import com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton.VoidtouchedSkeletonEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.voidtouchedzombie.VoidtouchedZombieEntity;
import com.github.ethanicuss.astraladditions.entities.voidtouchedzombie.VoidtouchedZombieEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<MoonmanEntity> MOONMAN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "moonman"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, MoonmanEntity::new).dimensions(EntityDimensions.fixed(0.75f, 2.8f)).build()
    );
    public static final EntityType<HemogiantEntity> GLUTTON = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "hemogiant"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HemogiantEntity::new).dimensions(EntityDimensions.fixed(1.05f, 5.8f)).build()
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

    public static final EntityModelLayer MODEL_MOONMAN_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "moonman"), "main");
    public static final EntityModelLayer MODEL_GLUTTON_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "hemogiant"), "main");
    public static final EntityModelLayer MODEL_VOIDTOUCHED_SKELETON_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "voidtouched_skeleton"), "main");
    public static final EntityModelLayer MODEL_VOIDTOUCHED_ZOMBIE_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "voidtouched_zombie"), "main");

    public static void init() {
        FabricDefaultAttributeRegistry.register(MOONMAN, MoonmanEntity.createMoonmanAttributes());
        FabricDefaultAttributeRegistry.register(GLUTTON, HemogiantEntity.createGluttonAttributes());
        FabricDefaultAttributeRegistry.register(VOIDTOUCHED_SKELETON, VoidtouchedSkeletonEntity.createVoidtouchedSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(VOIDTOUCHED_ZOMBIE, VoidtouchedZombieEntity.createVoidtouchedZombieAttributes());
    }

    public static void initClient() {
        EntityRendererRegistry.register(MOONMAN, MoonmanEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_MOONMAN_LAYER, MoonmanEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(GLUTTON, HemogiantEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_GLUTTON_LAYER, HemogiantEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(VOIDTOUCHED_SKELETON, VoidtouchedSkeletonEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_VOIDTOUCHED_SKELETON_LAYER, SkeletonEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(VOIDTOUCHED_ZOMBIE, VoidtouchedZombieEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_VOIDTOUCHED_ZOMBIE_LAYER, VoidtouchedZombieEntityRenderer::getTexturedModelData);

        EntityRendererRegistry.register(ENDER_BALL, EnderBallEntityRenderer::new);
        
        EntityRendererRegistry.register(AstralAdditions.GLUTTONY_BALL, GluttonyBallEntityRenderer::new);
    }
}
