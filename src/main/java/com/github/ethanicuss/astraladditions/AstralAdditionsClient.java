package com.github.ethanicuss.astraladditions;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.playertracker.PlayerTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.util.Identifier;

public class AstralAdditionsClient implements ClientModInitializer {

    public static PlayerTracker playerTracker = new PlayerTracker();
    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_SHIMMER, ModFluids.FLOWING_SHIMMER, new SimpleFluidRenderHandler(
                new Identifier("astraladditions:block/shimmer"),
                new Identifier("astraladditions:block/shimmer"),
                0xffd6fa
        ));

        ModEntities.initClient();
    }
}

