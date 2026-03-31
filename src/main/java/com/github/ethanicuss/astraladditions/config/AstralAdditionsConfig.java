package com.github.ethanicuss.astraladditions.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.musicplayer.NowPlayingHud;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class AstralAdditionsConfig {

    private static final Path configDir = FabricLoader.getInstance().getConfigDir().resolve("astraladditions");
    private static final Path configFile = configDir.resolve("config.toml");

    private static CommentedFileConfig config;


    public static boolean combatMusicEnabled = true;

    public static int musicBoxX = 0;
    public static int musicBoxY = 0;
    public static double musicBoxOpacity = 0.85;
    public static double musicBoxFadeIn = 0.2;
    public static double musicBoxHangTime = 5.0;
    public static double musicBoxFadeOut = 0.2;
    public static boolean musicBoxShimmerBorder = true;
    public static int musicBoxSizeX = 80;
    public static int musicBoxSizeY = 32;
    public static boolean musicBoxWordWrap = true;

    public static boolean hasBeenToMoon = false;
    public static boolean hasHeardPostMoonSong = false;


    public static void load() {
        try {
            if (config != null) {
                config.close();
                config = null;
            }

            Files.createDirectories(configDir);

            config = CommentedFileConfig.builder(configFile)
                    .sync()
                    .preserveInsertionOrder()
                    .writingMode(WritingMode.REPLACE)
                    .build();

            config.load();
            setDefaults();

            combatMusicEnabled = config.getOrElse("combatMusic.enabled", true);

            musicBoxX = config.getOrElse("musicBox.x", 0);
            musicBoxY = config.getOrElse("musicBox.y", 0);
            musicBoxOpacity = config.getOrElse("musicBox.opacity", 0.85);
            musicBoxFadeIn = config.getOrElse("musicBox.fadeIn", 0.2);
            musicBoxHangTime = config.getOrElse("musicBox.hangTime", 5.0);
            musicBoxFadeOut = config.getOrElse("musicBox.fadeOut", 0.2);
            musicBoxShimmerBorder = config.getOrElse("musicBox.shimmerborder", true);
            musicBoxSizeX = config.getOrElse("musicBox.sizeX", 80);
            musicBoxSizeY = config.getOrElse("musicBox.sizeY", 32);
            musicBoxWordWrap = config.getOrElse("musicBox.wordWrap", true);

            hasBeenToMoon = config.getOrElse("playerData.hasBeenToMoon", false);
            hasHeardPostMoonSong = config.getOrElse("playerData.hasHeardPostMoonSong", false);

            save();
            AstralAdditions.LOGGER.info("AstralAdditions config loaded successfully");
        } catch (Exception error) {
            AstralAdditions.LOGGER.error("Failed to load AstralAdditions config", error);
        }
    }

    private static void setDefaults() {
        addDefault("combatMusic.enabled", true, "Enable combat music");

        addDefault("musicBox.x", 0, "Music box X position");
        addDefault("musicBox.y", 0, "Music box Y position");
        addDefault("musicBox.opacity", 0.85, "Music box opacity");
        addDefault("musicBox.fadeIn", 0.2, "Fade in time");
        addDefault("musicBox.hangTime", 5.0, "Time to remain fully visible");
        addDefault("musicBox.fadeOut", 0.2, "Fade out time");
        addDefault("musicBox.shimmerborder", true, "Enable shimmer border");
        addDefault("musicBox.sizeX", 80, "Music box width");
        addDefault("musicBox.sizeY", 32, "Music box height");
        addDefault("musicBox.wordWrap", true, "Wrap long text");

        addDefault("playerData.hasBeenToMoon", false, "Has the player has visited the moon");
        addDefault("playerData.hasHeardPostMoonSong", false, "Has the player has heard the post-moon song");
    }

    private static void addDefault(String path, Object value, String comment) {
        if (!config.contains(path)) {
            config.set(path, value);
        }
        config.setComment(path, comment);
    }

    public static void save() {
        try {
            if (config == null) {
                return;
            }

            config.set("combatMusic.enabled", combatMusicEnabled);

            config.set("musicBox.x", musicBoxX);
            config.set("musicBox.y", musicBoxY);
            config.set("musicBox.opacity", musicBoxOpacity);
            config.set("musicBox.fadeIn", musicBoxFadeIn);
            config.set("musicBox.hangTime", musicBoxHangTime);
            config.set("musicBox.fadeOut", musicBoxFadeOut);
            config.set("musicBox.shimmerborder", musicBoxShimmerBorder);
            config.set("musicBox.sizeX", musicBoxSizeX);
            config.set("musicBox.sizeY", musicBoxSizeY);
            config.set("musicBox.wordWrap", musicBoxWordWrap);

            config.set("playerData.hasBeenToMoon", hasBeenToMoon);
            config.set("playerData.hasHeardPostMoonSong", hasHeardPostMoonSong);

            config.save();
            AstralAdditions.LOGGER.info("AstralAdditions config saved successfully");
        } catch (Exception error) {
            AstralAdditions.LOGGER.error("Failed to save AstralAdditions config", error);
        }
    }

    public static void onReload() {
        NowPlayingHud.onConfigReload();
    }
}