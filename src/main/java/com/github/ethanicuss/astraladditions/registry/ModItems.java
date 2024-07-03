package com.github.ethanicuss.astraladditions.registry;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.items.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModItems {
    public static final Item COMETBALL = new CometballItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(16).rarity(Rarity.UNCOMMON));
    public static final Item PYLON = new PylonItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1).rarity(Rarity.UNCOMMON));
    public static final Item METEOR_MITTS = new MeteorMittsItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON).maxDamage(1536));
    public static final Item COSMIC_HOURGLASS = new CosmicHourglassItem(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON));
    public static final Item E_GUITAR = new EGuitarItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON).maxDamage(2048));
    public static final Item MOONSET_CRYSTAL = new Item(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON));
    public static final Item OHNO = new OHNOItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1).rarity(Rarity.RARE));
    public static final Item OHNO_BROKEN = new OHNOBrokenItem(new FabricItemSettings().group(ItemGroup.TOOLS).rarity(Rarity.RARE));
    public static final Item SHIMMER_BLOWER = new ShimmerBlowerItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON).maxDamage(20));
    public static final Item LUNAR_WACKER = new LunarWackerItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.UNCOMMON).maxDamage(512));
    public static final Item SHIMMER_HEART = new Item(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON));
    public static final Item AWAKENED_SHIMMER_HEART = new Item(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON));
    public static final Item SHIMMER_BLAZE_ROD = new Item(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON));
    public static final Item SHIMMER_BLAZE_POWDER = new Item(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON));
    public static final Item RING_GOLD_CAST = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item STEEL_RING = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item BLAZED_STEEL_RING = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item ORBITAL_NAVIGATION_RING = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item MOONBLAZED_ORB = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item FRAGILE_ITEM = new FragileItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1), ModData.FRAGILE_ITEM_PARTS);
    public static final Item FRAGILE_ITEM_2 = new FragileItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1), ModData.FRAGILE_ITEM_PARTS_2);
    public static final Item FRAGILE_ITEM_3 = new FragileItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1), ModData.FRAGILE_ITEM_PARTS_3);
    public static final Item DISC_PURPLE_PRISON = new ModMusicDiscItem(7, ModSounds.MUSIC_SHIMMER_BLAZE, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final Item DISC_ASTRAL_LAKES_REMIX = new ModMusicDiscItem(7, ModSounds.MUSIC_ASTRAL_LAKES_REMIX, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final Item SHIMMER_BOTTLE = new ShimmerBottleItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(1));
    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "cometball"), COMETBALL);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "pylon"), PYLON);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "meteor_mitts"), METEOR_MITTS);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "cosmic_hourglass"), COSMIC_HOURGLASS);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "e_guitar"), E_GUITAR);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "moonset_crystal"), MOONSET_CRYSTAL);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "oh-no"), OHNO);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "oh-no_broken"), OHNO_BROKEN);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "shimmer_blower"), SHIMMER_BLOWER);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "lunar_wacker"), LUNAR_WACKER);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "shimmer_heart"), SHIMMER_HEART);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "awakened_shimmer_heart"), AWAKENED_SHIMMER_HEART);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "shimmer_blaze_rod"), SHIMMER_BLAZE_ROD);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "shimmer_blaze_powder"), SHIMMER_BLAZE_POWDER);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "ring_gold_cast"), RING_GOLD_CAST);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "steel_ring"), STEEL_RING);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "blazed_steel_ring"), BLAZED_STEEL_RING);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "orbital_navigation_ring"), ORBITAL_NAVIGATION_RING);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "moonblazed_orb"), MOONBLAZED_ORB);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "fragile_item"), FRAGILE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "fragile_item_2"), FRAGILE_ITEM_2);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "fragile_item_3"), FRAGILE_ITEM_3);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "disc_purple_prison"), DISC_PURPLE_PRISON);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "disc_astral_lakes_remix"), DISC_ASTRAL_LAKES_REMIX);
        Registry.register(Registry.ITEM, new Identifier(AstralAdditions.MOD_ID, "shimmer_bottle"), SHIMMER_BOTTLE);
        addSacrificeItem(AWAKENED_SHIMMER_HEART, ModEntities.SHIMMER_BLAZE);
    }
    private static List<Item> sacrificableItems = new ArrayList<Item>();
    public static void addSacrificeItem(Item item, EntityType spawns){
        sacrificableItems.add(item);
    }
    public static boolean isSacrificeItem(Item item){
        return sacrificableItems.contains(item);
    }
}
