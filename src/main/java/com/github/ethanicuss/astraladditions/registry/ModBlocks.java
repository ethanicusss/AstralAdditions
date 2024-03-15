package com.github.ethanicuss.astraladditions.registry;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.blocks.DroptusBlock;
import com.github.ethanicuss.astraladditions.blocks.LuneShroomBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.ToIntFunction;

public class ModBlocks {
    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return (state) -> {
            return (Boolean)state.get(Properties.LIT) ? litLevel : 0;
        };
    }
    public static final Block MOONSET_CRYSTAL_BLOCK = new Block(FabricBlockSettings.of(Material.AMETHYST).nonOpaque().ticksRandomly().sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(2.0f).luminance((state) -> {
        return 10;
    }));
    public static final BlockItem MOONSET_CRYSTAL_BLOCK_ITEM = new BlockItem(MOONSET_CRYSTAL_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block BULBA_BLOCK = new DroptusBlock(FabricBlockSettings.of(Material.CACTUS).ticksRandomly().sounds(BlockSoundGroup.SHROOMLIGHT).strength(0.2f).luminance(createLightLevelFromLitBlockState(7)));
    public static final BlockItem BULBA_BLOCK_ITEM = new BlockItem(BULBA_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block LUNE_SHROOM_BLOCK = new LuneShroomBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().sounds(BlockSoundGroup.SHROOMLIGHT).luminance((state) -> {
        return 2;
    }));
    public static final BlockItem LUNE_SHROOM_ITEM = new BlockItem(LUNE_SHROOM_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "moonset_crystal_block"), MOONSET_CRYSTAL_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "moonset_crystal_block"), MOONSET_CRYSTAL_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "bulba_root"), BULBA_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "bulba_root"), BULBA_BLOCK_ITEM);
        Registry.register(Registry.BLOCK, new Identifier(AstralAdditions.MOD_ID, "lune_shroom"), LUNE_SHROOM_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "lune_shroom"), LUNE_SHROOM_ITEM);
    }
}
