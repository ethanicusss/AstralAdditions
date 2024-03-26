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

    private static TagKey<Block> register(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(AstralAdditions.MOD_ID, id));
    }
}
