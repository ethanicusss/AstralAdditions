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
    public static final Block MOONSET_CRYSTAL_BLOCK = new Block(FabricBlockSettings.of(Material.AMETHYST).nonOpaque().ticksRandomly().sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(2.0f).luminance((state) -> {
        return 10;
    }));
    public static final BlockItem MOONSET_CRYSTAL_BLOCK_ITEM = new BlockItem(MOONSET_CRYSTAL_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block BULBA_BLOCK = new DroptusBlock(FabricBlockSettings.of(Material.CACTUS).ticksRandomly().sounds(BlockSoundGroup.SHROOMLIGHT).strength(0.2f).luminance(createLightLevelFromLitBlockState(7)));
    public static final BlockItem BULBA_BLOCK_ITEM = new BlockItem(BULBA_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block LUNE_SHROOM_BLOCK = new LuneShroomBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().sounds(BlockSoundGroup.SHROOMLIGHT).luminance((state) -> {
        return 2;
    }));

    public static final Block POTTED_LUNE_SHROOM_BLOCK = new FlowerPotBlock(ModBlocks.LUNE_SHROOM_BLOCK, FabricBlockSettings.copy(Blocks.POTTED_RED_MUSHROOM).nonOpaque());
    public static final BlockItem LUNE_SHROOM_ITEM = new BlockItem(LUNE_SHROOM_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block BRAMBLEBONE_BLOCK = new BrambleboneBlock(FabricBlockSettings.of(Material.GLASS).ticksRandomly().sounds(BlockSoundGroup.TUFF).collidable(false).nonOpaque().allowsSpawning(ModBlocks::never).suffocates(ModBlocks::never));
    public static final BlockItem BRAMBLEBONE_ITEM = new BlockItem(BRAMBLEBONE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block JAR_BLOCK = new JarBlock(FabricBlockSettings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(ModBlocks::never).suffocates(ModBlocks::never));
    public static final BlockItem JAR_ITEM = new BlockItem(JAR_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));
    public static final BlockEntityType<JarBlockEntity> JAR_BLOCKENTITY =
            Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(AstralAdditions.MOD_ID, "jar"), FabricBlockEntityTypeBuilder.create(JarBlockEntity::new, JAR_BLOCK).build());

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "moonset_crystal_block"), MOONSET_CRYSTAL_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "moonset_crystal_block"), MOONSET_CRYSTAL_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "bulba_root"), BULBA_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "bulba_root"), BULBA_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "lune_shroom"), LUNE_SHROOM_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "potted_lune_shroom"), POTTED_LUNE_SHROOM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "lune_shroom"), LUNE_SHROOM_ITEM);
        BlockRenderLayerMap.INSTANCE.putBlock(BRAMBLEBONE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BRAMBLEBONE_BLOCK, RenderLayer.getTranslucent());
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "bramblebone"), BRAMBLEBONE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "bramblebone"), BRAMBLEBONE_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "jar"), JAR_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "jar"), JAR_ITEM);
        BlockEntityRendererRegistry.register(JAR_BLOCKENTITY, JarBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(JAR_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JAR_BLOCK, RenderLayer.getTranslucent());
    }
}
