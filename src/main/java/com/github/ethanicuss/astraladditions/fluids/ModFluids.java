package com.github.ethanicuss.astraladditions.fluids;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;

public class ModFluids {
    public static final String ASTRAL_ID = "kubejs";
    public static FlowingFluid STILL_SHIMMER;
    public static FlowingFluid FLOWING_SHIMMER;
    public static Item SHIMMER_BUCKET;
    public static Block SHIMMER;

    public static final TagKey<Fluid> SHIMMER_TAG = register("shimmer");

    public static void registerFluids(){
        STILL_SHIMMER = Registry.register(Registry.FLUID, new ResourceLocation(ASTRAL_ID, "shimmer"), new ShimmerFluid.Still());
        FLOWING_SHIMMER = Registry.register(Registry.FLUID, new ResourceLocation(ASTRAL_ID, "flowing_shimmer"), new ShimmerFluid.Flowing());
        SHIMMER_BUCKET = Registry.register(Registry.ITEM, new ResourceLocation(ASTRAL_ID, "shimmer_bucket"),
                new BucketItem(STILL_SHIMMER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
        SHIMMER = Registry.register(Registry.BLOCK, new ResourceLocation(ASTRAL_ID, "shimmer"), new LiquidBlock(STILL_SHIMMER, FabricBlockSettings.of(Material.WATER).noCollission().randomTicks().strength(1.0F).lightLevel((state) -> 10).noDrops()));

    }

    private static TagKey<Fluid> register(String id) {
        return TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(id));
    }
}
