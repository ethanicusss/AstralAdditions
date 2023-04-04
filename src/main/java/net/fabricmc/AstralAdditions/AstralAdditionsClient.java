package net.fabricmc.AstralAdditions;

import net.fabricmc.AstralAdditions.entities.glutton.GluttonEntityModel;
import net.fabricmc.AstralAdditions.entities.glutton.GluttonEntityRenderer;
import net.fabricmc.AstralAdditions.entities.moondragon.EnderBallEntityRenderer;
import net.fabricmc.AstralAdditions.entities.moonman.MoonmanEntityModel;
import net.fabricmc.AstralAdditions.entities.moonman.MoonmanEntityRenderer;
import net.fabricmc.AstralAdditions.entities.voidtouchedskeleton.VoidtouchedSkeletonEntityRenderer;
import net.fabricmc.AstralAdditions.entities.voidtouchedzombie.VoidtouchedZombieEntityRenderer;
import net.fabricmc.AstralAdditions.fluids.ModFluids;
import net.fabricmc.AstralAdditions.playertracker.PlayerTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.util.Identifier;

public class AstralAdditionsClient implements ClientModInitializer {

    public static PlayerTracker playerTracker = new PlayerTracker();
    public static final EntityModelLayer MODEL_MOONMAN_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "moonman"), "main");
    public static final EntityModelLayer MODEL_GLUTTON_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "hemogiant"), "main");
    public static final EntityModelLayer MODEL_VOIDTOUCHED_SKELETON_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "voidtouched_skeleton"), "main");
    public static final EntityModelLayer MODEL_VOIDTOUCHED_ZOMBIE_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "voidtouched_zombie"), "main");
    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_SHIMMER, ModFluids.FLOWING_SHIMMER, new SimpleFluidRenderHandler(
                new Identifier("astraladditions:block/shimmer"),
                new Identifier("astraladditions:block/shimmer"),
                0xffd6fa
        ));

        EntityRendererRegistry.register(AstralAdditions.MOONMAN, MoonmanEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_MOONMAN_LAYER, MoonmanEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(AstralAdditions.GLUTTON, GluttonEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_GLUTTON_LAYER, GluttonEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(AstralAdditions.VOIDTOUCHED_SKELETON, VoidtouchedSkeletonEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_VOIDTOUCHED_SKELETON_LAYER, SkeletonEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(AstralAdditions.VOIDTOUCHED_ZOMBIE, VoidtouchedZombieEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_VOIDTOUCHED_ZOMBIE_LAYER, VoidtouchedZombieEntityRenderer::getTexturedModelData);

        EntityRendererRegistry.register(AstralAdditions.ENDER_BALL, EnderBallEntityRenderer::new);

    }
}

