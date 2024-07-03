package com.github.ethanicuss.astraladditions.entities;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntity;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEntity;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEntityModel;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.meteor_mitts.MeteorPunchEntity;
import com.github.ethanicuss.astraladditions.entities.meteor_mitts.MeteorPunchEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.moondragon.EnderBallEntity;
import com.github.ethanicuss.astraladditions.entities.moondragon.EnderBallEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.moondragon.GluttonyBallEntity;
import com.github.ethanicuss.astraladditions.entities.moondragon.GluttonyBallEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.moonman.MoonmanEntity;
import com.github.ethanicuss.astraladditions.entities.moonman.MoonmanEntityModel;
import com.github.ethanicuss.astraladditions.entities.moonman.MoonmanEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.phast.PhastEntity;
import com.github.ethanicuss.astraladditions.entities.phast.PhastEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.*;
import com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton.VoidtouchedSkeletonEntity;
import com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton.VoidtouchedSkeletonEntityRenderer;
import com.github.ethanicuss.astraladditions.entities.voidtouchedzombie.VoidtouchedZombieEntity;
import com.github.ethanicuss.astraladditions.entities.voidtouchedzombie.VoidtouchedZombieEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.model.BlazeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.GhastEntityModel;
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
    public static final EntityType<HemogiantEntity> HEMOGIANT = Registry.register(
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
    public static final EntityType<CometballEntity> COMETBALL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "cometball"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CometballEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build()
    );
    public static final EntityType<PylonEntity> PYLON = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "pylon"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PylonEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build()
    );
    public static final EntityType<MeteorPunchEntity> METEOR_FIST = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "meteor_fist"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MeteorPunchEntity::new).dimensions(EntityDimensions.fixed(1.5f, 1.5f)).build()
    );
    public static final EntityType<ShimmerBlazeEntity> SHIMMER_BLAZE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "shimmer_blaze"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ShimmerBlazeEntity::new).dimensions(EntityDimensions.fixed(1.0f, 2.2f)).build()
    );
    public static final EntityType<SmallShimmerballEntity> SMALL_SHIMMERBALL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "small_shimmerball"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SmallShimmerballEntity::new).dimensions(EntityDimensions.fixed(0.1f, 0.1f)).build()
    );
    public static final EntityType<ShimmerBlazeRainEntity> SHIMMER_RAIN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "shimmer_rain"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ShimmerBlazeRainEntity::new).dimensions(EntityDimensions.fixed(1.0f, 5.0f)).build()
    );
    public static final EntityType<PhastEntity> PHAST = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(AstralAdditions.MOD_ID, "phast"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PhastEntity::new).dimensions(EntityDimensions.fixed(5.0f, 5.0f)).fireImmune().build()
    );

    public static void init() {
        FabricDefaultAttributeRegistry.register(MOONMAN, MoonmanEntity.createMoonmanAttributes());
        FabricDefaultAttributeRegistry.register(HEMOGIANT, HemogiantEntity.createGluttonAttributes());
        FabricDefaultAttributeRegistry.register(VOIDTOUCHED_SKELETON, VoidtouchedSkeletonEntity.createVoidtouchedSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(VOIDTOUCHED_ZOMBIE, VoidtouchedZombieEntity.createVoidtouchedZombieAttributes());
        FabricDefaultAttributeRegistry.register(SHIMMER_BLAZE, ShimmerBlazeEntity.createShimmerBlazeAttributes());
        FabricDefaultAttributeRegistry.register(PHAST, PhastEntity.createPhastAttributes());
    }

    public static void initClient() {
        EntityRendererRegistry.register(MOONMAN, MoonmanEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(AstralAdditionsClient.MODEL_MOONMAN_LAYER, MoonmanEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(HEMOGIANT, HemogiantEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(AstralAdditionsClient.MODEL_HEMOGIANT_LAYER, HemogiantEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(VOIDTOUCHED_SKELETON, VoidtouchedSkeletonEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(AstralAdditionsClient.MODEL_VOIDTOUCHED_SKELETON_LAYER, SkeletonEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(VOIDTOUCHED_ZOMBIE, VoidtouchedZombieEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(AstralAdditionsClient.MODEL_VOIDTOUCHED_ZOMBIE_LAYER, VoidtouchedZombieEntityRenderer::getTexturedModelData);

        EntityRendererRegistry.register(SHIMMER_BLAZE, ShimmerBlazeEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(AstralAdditionsClient.MODEL_SHIMMER_BLAZE_LAYER, ShimmerBlazeEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(PHAST, PhastEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(AstralAdditionsClient.MODEL_PHAST_LAYER, GhastEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(ENDER_BALL, EnderBallEntityRenderer::new);

        EntityRendererRegistry.register(GLUTTONY_BALL, GluttonyBallEntityRenderer::new);

        EntityRendererRegistry.register(COMETBALL, CometballEntityRenderer::new);

        EntityRendererRegistry.register(PYLON, PylonEntityRenderer::new);

        EntityRendererRegistry.register(METEOR_FIST, MeteorPunchEntityRenderer::new);

        EntityRendererRegistry.register(SMALL_SHIMMERBALL, SmallShimmerballEntityRenderer::new);

        EntityRendererRegistry.register(SHIMMER_RAIN, ShimmerBlazeRainEntityRenderer::new);
    }
}
