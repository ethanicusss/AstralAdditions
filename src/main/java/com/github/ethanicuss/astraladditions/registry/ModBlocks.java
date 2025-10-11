package com.github.ethanicuss.astraladditions.registry;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.blocks.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import java.util.function.ToIntFunction;

public class ModBlocks {
    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return (state) -> {
            return (Boolean)state.get(Properties.LIT) ? litLevel : 0;
        };
    }
    private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityType<?> entityType) {return false;}

    private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {return false;}
    public static final Block MOONSET_CRYSTAL_BLOCK = new Block(FabricBlockSettings.of(Material.AMETHYST).nonOpaque().sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(2.0f).luminance((state) -> {
        return 10;
    }));
    public static final BlockItem MOONSET_CRYSTAL_BLOCK_ITEM = new BlockItem(MOONSET_CRYSTAL_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block BULBA_BLOCK = new DroptusBlock(FabricBlockSettings.of(Material.CACTUS).ticksRandomly().sounds(BlockSoundGroup.SHROOMLIGHT).strength(0.2f).luminance(createLightLevelFromLitBlockState(7)));
    public static final BlockItem BULBA_BLOCK_ITEM = new BlockItem(BULBA_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block LUNE_SHROOM_BLOCK = new LuneShroomBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().sounds(BlockSoundGroup.SHROOMLIGHT).luminance((state) -> {
        return 2;
    }));
    public static final BlockItem LUNE_SHROOM_ITEM = new BlockItem(LUNE_SHROOM_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block POTTED_LUNE_SHROOM_BLOCK = new FlowerPotBlock(ModBlocks.LUNE_SHROOM_BLOCK, FabricBlockSettings.copy(Blocks.POTTED_RED_MUSHROOM).nonOpaque());

    public static final Block SHIMMER_LILY_BLOCK = new FlowerBlock(StatusEffects.GLOWING, 600, AbstractBlock.Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).luminance((state) -> {
        return 3;
    }));
    public static final BlockItem SHIMMER_LILY_ITEM = new BlockItem(SHIMMER_LILY_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block POTTED_SHIMMER_LILY_BLOCK = new FlowerPotBlock(ModBlocks.SHIMMER_LILY_BLOCK, FabricBlockSettings.copy(Blocks.POTTED_RED_MUSHROOM).nonOpaque());

    public static final Block BRAMBLEBONE_BLOCK = new BrambleboneBlock(FabricBlockSettings.of(Material.GLASS).ticksRandomly().sounds(BlockSoundGroup.TUFF).collidable(false).nonOpaque().allowsSpawning(ModBlocks::never).suffocates(ModBlocks::never));
    public static final BlockItem BRAMBLEBONE_ITEM = new BlockItem(BRAMBLEBONE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block MISSING_BLOCK = new MissingBlock(FabricBlockSettings.of(Material.LAVA).ticksRandomly().sounds(BlockSoundGroup.CROP).collidable(false).nonOpaque().allowsSpawning(ModBlocks::never).suffocates(ModBlocks::never));

    public static final Block PRISMATIC_GEYSER_BLOCK = new GeyserBlock(FabricBlockSettings.of(Material.STONE).ticksRandomly().sounds(BlockSoundGroup.STONE).allowsSpawning(ModBlocks::never));
    public static final Block CRACKED_ICE_BLOCK = new CrackedIceBlock(FabricBlockSettings.of(Material.ICE).ticksRandomly().sounds(BlockSoundGroup.GLASS).allowsSpawning(ModBlocks::never));

    public static final Block ENDERRACK_BLOCK = new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.NETHERRACK).strength(2.0f));
    public static final BlockItem ENDERRACK_ITEM = new BlockItem(ENDERRACK_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Block TWISTED_NYLIUM_BLOCK = new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.NYLIUM).strength(2.5f));
    public static final BlockItem TWISTED_NYLIUM_ITEM = new BlockItem(TWISTED_NYLIUM_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block ENDER_TIP_BLOCK = new EnderTipBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.POINTED_DRIPSTONE).strength(1.0f));
    public static final BlockItem ENDER_TIP_ITEM = new BlockItem(ENDER_TIP_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block ENDER_SPROUT_BLOCK = new EnderSproutsBlock(FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.NETHER_SPROUTS));
    public static final BlockItem ENDER_SPROUT_ITEM = new BlockItem(ENDER_SPROUT_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block PEARL_POD_BLOCK = new EnderSproutsBlock(FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.BONE));
    public static final BlockItem PEARL_POD_ITEM = new BlockItem(PEARL_POD_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final Block JAR_BLOCK = new JarBlock(FabricBlockSettings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(ModBlocks::never).suffocates(ModBlocks::never));
    public static final BlockItem JAR_ITEM = new BlockItem(JAR_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final BlockEntityType<JarBlockEntity> JAR_BLOCKENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(AstralAdditions.MOD_ID, "jar"), FabricBlockEntityTypeBuilder.create(JarBlockEntity::new, JAR_BLOCK).build());

    public static final Block DESIZER_1 = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_2 = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_3 = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_4 = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_6 = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_7 = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_8 = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_9 = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_CONTROLLER = new DesizerControllerBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));
    public static final Block DESIZER_BASE = new DesizerCasingBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(3.0f));

    public static final BlockItem DESIZER_1_ITEM = new BlockItem(DESIZER_1, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_2_ITEM = new BlockItem(DESIZER_2, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_3_ITEM = new BlockItem(DESIZER_3, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_4_ITEM = new BlockItem(DESIZER_4, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_6_ITEM = new BlockItem(DESIZER_6, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_7_ITEM = new BlockItem(DESIZER_7, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_8_ITEM = new BlockItem(DESIZER_8, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_9_ITEM = new BlockItem(DESIZER_9, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_CONTROLLER_ITEM = new BlockItem(DESIZER_CONTROLLER, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));
    public static final BlockItem DESIZER_BASE_ITEM = new BlockItem(DESIZER_BASE, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));


    /*public static final BlockEntityType<JarBlockEntity> DESIZER_CONTROLLER_ENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(AstralAdditions.MOD_ID, "desizer_8"), FabricBlockEntityTypeBuilder.create(JarBlockEntity::new, JAR_BLOCK).build());
*/
    public static final Block THE_END_BLOCK = new Block(FabricBlockSettings.of(Material.STONE, MapColor.WHITE).nonOpaque().sounds(BlockSoundGroup.AMETHYST_CLUSTER).requiresTool().strength(50.0f, 1200).luminance((state) -> {
        return 15;
    }));
    public static final BlockItem THE_END_BLOCK_ITEM = new BlockItem(THE_END_BLOCK, new FabricItemSettings().group(ItemGroup.MISC));

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "moonset_crystal_block"), MOONSET_CRYSTAL_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "moonset_crystal_block"), MOONSET_CRYSTAL_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "bulba_root"), BULBA_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "bulba_root"), BULBA_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "lune_shroom"), LUNE_SHROOM_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "potted_lune_shroom"), POTTED_LUNE_SHROOM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "lune_shroom"), LUNE_SHROOM_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "shimmer_lily"), SHIMMER_LILY_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "potted_shimmer_lily"), POTTED_SHIMMER_LILY_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "shimmer_lily"), SHIMMER_LILY_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "bramblebone"), BRAMBLEBONE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "bramblebone"), BRAMBLEBONE_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "missing_block"), MISSING_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "prismatic_geyser"), PRISMATIC_GEYSER_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "cracked_ice"), CRACKED_ICE_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "enderrack"), ENDERRACK_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "enderrack"), ENDERRACK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "twisted_nylium"), TWISTED_NYLIUM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "twisted_nylium"), TWISTED_NYLIUM_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "jar"), JAR_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "jar"), JAR_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "ender_tip"), ENDER_TIP_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "ender_tip"), ENDER_TIP_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "ender_sprouts"), ENDER_SPROUT_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "ender_sprouts"), ENDER_SPROUT_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "pearl_pod"), PEARL_POD_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "pearl_pod"), PEARL_POD_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_1"), DESIZER_1);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_1"), DESIZER_1_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_2"), DESIZER_2);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_2"), DESIZER_2_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_3"), DESIZER_3);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_3"), DESIZER_3_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_4"), DESIZER_4);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_4"), DESIZER_4_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_controller"), DESIZER_CONTROLLER);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_controller"), DESIZER_CONTROLLER_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_6"), DESIZER_6);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_6"), DESIZER_6_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_7"), DESIZER_7);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_7"), DESIZER_7_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_8"), DESIZER_8);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_8"), DESIZER_8_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_9"), DESIZER_9);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_9"), DESIZER_9_ITEM);

        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "desizer_base"), DESIZER_BASE);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "desizer_base"), DESIZER_BASE_ITEM);

        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "the_end"), THE_END_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "the_end"), THE_END_BLOCK_ITEM);
    }

    public static void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BRAMBLEBONE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BRAMBLEBONE_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SHIMMER_LILY_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SHIMMER_LILY_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(POTTED_SHIMMER_LILY_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(POTTED_SHIMMER_LILY_BLOCK, RenderLayer.getTranslucent());
        BlockEntityRendererRegistry.register(JAR_BLOCKENTITY, JarBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(JAR_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JAR_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ENDER_SPROUT_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ENDER_SPROUT_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ENDER_TIP_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ENDER_TIP_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PEARL_POD_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PEARL_POD_BLOCK, RenderLayer.getTranslucent());
    }
}
