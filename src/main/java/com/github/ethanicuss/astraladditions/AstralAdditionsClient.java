package com.github.ethanicuss.astraladditions;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.particle.ModParticlesClient;
import com.github.ethanicuss.astraladditions.playertracker.PlayerTracker;
import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

public class AstralAdditionsClient implements ClientModInitializer {

    public static PlayerTracker playerTracker = new PlayerTracker();
    public static final EntityModelLayer MODEL_MOONMAN_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "moonman"), "main");
    public static final EntityModelLayer MODEL_HEMOGIANT_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "hemogiant"), "main");
    public static final EntityModelLayer MODEL_VOIDTOUCHED_SKELETON_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "voidtouched_skeleton"), "main");
    public static final EntityModelLayer MODEL_VOIDTOUCHED_ZOMBIE_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "voidtouched_zombie"), "main");
    public static final EntityModelLayer MODEL_SHIMMER_BLAZE_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "shimmer_blaze"), "main");
    public static final EntityModelLayer MODEL_PHAST_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "phast"), "main");
    public static final EntityModelLayer MODEL_WHAST_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "whast"), "main");
    public static final EntityModelLayer MODEL_GLAZER_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "glazer"), "main");
    public static final EntityModelLayer MODEL_ENDER_WATCHER_LAYER = new EntityModelLayer(new Identifier(AstralAdditions.MOD_ID, "ender_watcher"), "main");
    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_SHIMMER, ModFluids.FLOWING_SHIMMER, new SimpleFluidRenderHandler(
                new Identifier("astraladditions:block/shimmer"),
                new Identifier("astraladditions:block/shimmer"),
                0xffd6fa
        ));
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_SPUTUM, ModFluids.FLOWING_SPUTUM, new SimpleFluidRenderHandler(
                new Identifier("astraladditions:block/sputum/sputum"),
                new Identifier("astraladditions:block/sputum/sputum"),
                0xffffff
        ));
        FluidRenderHandlerRegistry.INSTANCE.setBlockTransparency(ModFluids.SPUTUM, true);
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ModFluids.STILL_SPUTUM, ModFluids.FLOWING_SPUTUM, ModFluids.STILL_SHIMMER, ModFluids.FLOWING_SHIMMER);

        ModEntities.initClient();
        ModBlocks.registerClient();
        ModParticlesClient.registerFactories();

    }
}