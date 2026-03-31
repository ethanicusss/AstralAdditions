package com.github.ethanicuss.astraladditions.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new LiteralText("Astral Additions Config"))
                .setSavingRunnable(() -> {
                    AstralAdditionsConfig.save();
                    AstralAdditionsConfig.load();
                    AstralAdditionsConfig.onReload();
                });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory combatCategory = builder.getOrCreateCategory(new LiteralText("Combat Music"));
        ConfigCategory musicBoxCategory = builder.getOrCreateCategory(new LiteralText("Music Box"));

        combatCategory.addEntry(entryBuilder
                .startBooleanToggle(new LiteralText("Enabled"), AstralAdditionsConfig.combatMusicEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(value -> AstralAdditionsConfig.combatMusicEnabled = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startIntField(new LiteralText("X"), AstralAdditionsConfig.musicBoxX)
                .setDefaultValue(0)
                .setMin(0)
                .setMax(10000)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxX = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startIntField(new LiteralText("Y"), AstralAdditionsConfig.musicBoxY)
                .setDefaultValue(0)
                .setMin(0)
                .setMax(10000)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxY = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startDoubleField(new LiteralText("Opacity"), AstralAdditionsConfig.musicBoxOpacity)
                .setDefaultValue(0.85)
                .setMin(0.0)
                .setMax(1.0)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxOpacity = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startDoubleField(new LiteralText("Fade In"), AstralAdditionsConfig.musicBoxFadeIn)
                .setDefaultValue(0.2)
                .setMin(0.0)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxFadeIn = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startDoubleField(new LiteralText("Hang Time"), AstralAdditionsConfig.musicBoxHangTime)
                .setDefaultValue(5.0)
                .setMin(0.0)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxHangTime = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startDoubleField(new LiteralText("Fade Out"), AstralAdditionsConfig.musicBoxFadeOut)
                .setDefaultValue(0.2)
                .setMin(0.0)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxFadeOut = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startBooleanToggle(new LiteralText("Shimmer Border"), AstralAdditionsConfig.musicBoxShimmerBorder)
                .setDefaultValue(true)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxShimmerBorder = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startIntField(new LiteralText("Size X"), AstralAdditionsConfig.musicBoxSizeX)
                .setDefaultValue(80)
                .setMin(80)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxSizeX = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startIntField(new LiteralText("Size Y"), AstralAdditionsConfig.musicBoxSizeY)
                .setDefaultValue(32)
                .setMin(32)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxSizeY = value)
                .build());

        musicBoxCategory.addEntry(entryBuilder
                .startBooleanToggle(new LiteralText("Word Wrap"), AstralAdditionsConfig.musicBoxWordWrap)
                .setDefaultValue(true)
                .setSaveConsumer(value -> AstralAdditionsConfig.musicBoxWordWrap = value)
                .build());

        return builder.build();
    }
}