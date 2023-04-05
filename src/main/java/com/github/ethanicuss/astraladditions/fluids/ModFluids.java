package com.github.ethanicuss.astraladditions.fluids;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModFluids {
    public static final String ASTRAL_ID = "kubejs";
    public static FlowableFluid STILL_SHIMMER;
    public static FlowableFluid FLOWING_SHIMMER;
    public static Item SHIMMER_BUCKET;
    public static Block SHIMMER;

    public static final TagKey<Fluid> SHIMMER_TAG = register("shimmer");

    public static void registerFluids(){
        STILL_SHIMMER = Registry.register(Registry.FLUID, new Identifier(ASTRAL_ID, "shimmer"), new ShimmerFluid.Still());
        FLOWING_SHIMMER = Registry.register(Registry.FLUID, new Identifier(ASTRAL_ID, "flowing_shimmer"), new ShimmerFluid.Flowing());
        SHIMMER_BUCKET = Registry.register(Registry.ITEM, new Identifier(ASTRAL_ID, "shimmer_bucket"),
                new BucketItem(STILL_SHIMMER, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));
        SHIMMER = Registry.register(Registry.BLOCK, new Identifier(ASTRAL_ID, "shimmer"), new FluidBlock(STILL_SHIMMER, FabricBlockSettings.of(Material.WATER).noCollision().ticksRandomly().strength(1.0F).luminance((state) -> 10).dropsNothing()));

    }

    private static TagKey<Fluid> register(String id) {
        return TagKey.of(Registry.FLUID_KEY, new Identifier(id));
    }
}
