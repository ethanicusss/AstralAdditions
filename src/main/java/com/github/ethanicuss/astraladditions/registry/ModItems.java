package com.github.ethanicuss.astraladditions.registry;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.items.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item COMETBALL = new CometballItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(16).rarity(Rarity.UNCOMMON));
    public static final Item PYLON = new PylonItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON));
    public static final Item METEOR_MITTS = new MeteorMittsItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON));
    public static final Item COSMIC_HOURGLASS = new CosmicHourglassItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON));
    public static final Item E_GUITAR = new EGuitarItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON));
    public static final Item MOONSET_CRYSTAL = new Item(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON));
    public static final Item OHNO = new OHNOItem(new FabricItemSettings().group(ItemGroup.TOOLS).rarity(Rarity.RARE));
    public static final Item OHNO_BROKEN = new Item(new FabricItemSettings().group(ItemGroup.TOOLS).rarity(Rarity.RARE));
    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "cometball"), COMETBALL);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "pylon"), PYLON);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "meteor_mitts"), METEOR_MITTS);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "cosmic_hourglass"), COSMIC_HOURGLASS);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "e_guitar"), E_GUITAR);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "moonset_crystal"), MOONSET_CRYSTAL);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "oh-no"), OHNO);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "oh-no_broken"), OHNO_BROKEN);
    }
}
