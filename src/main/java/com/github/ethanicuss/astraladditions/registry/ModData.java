package com.github.ethanicuss.astraladditions.registry;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModData {
    public static final TagKey<Block> BULBA_GROWABLE = register("bulba_growable");
    public static final TagKey<Block> LUNE_SHROOM_GROWABLE = register("lune_shroom_growable");
    public static final TagKey<Block> DESIZER_IGNORE_BLOCKS = register("desizer_ignore_blocks");
    public static final TagKey<Block> DESIZER_CASING_BLOCKS = register("desizer_casing_blocks");
    public static final TagKey<Item> INGORE_TRANSMUTATION = registerItemKey("ignore_shimmer_transmutation");

    public static final Identifier FRAGILE_ITEM_PARTS = new Identifier(AstralAdditions.MOD_ID, "gameplay/fragile_items/fragile_item");
    public static final Identifier FRAGILE_ITEM_PARTS_2 = new Identifier(AstralAdditions.MOD_ID, "gameplay/fragile_items/fragile_item_2");
    public static final Identifier FRAGILE_ITEM_PARTS_3 = new Identifier(AstralAdditions.MOD_ID, "gameplay/fragile_items/fragile_item_3");

    private static TagKey<Block> register(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(AstralAdditions.MOD_ID, id));
    }
    private static TagKey<Item> registerItemKey(String id) {
        return TagKey.of(Registry.ITEM_KEY, new Identifier(AstralAdditions.MOD_ID, id));
    }
}
