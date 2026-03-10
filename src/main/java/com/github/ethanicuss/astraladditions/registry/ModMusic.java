package com.github.ethanicuss.astraladditions.registry;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.minecraft.sound.MusicSound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ModMusic {
    private static final int GAME_MIN_DELAY = 12000;
    private static final int GAME_MAX_DELAY = 24000;//12000, 24000

    public static final MusicSound MOON               = new MusicSound(ModSounds.MUSIC_MOON, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound MENU               = new MusicSound(ModSounds.MUSIC_ASTRAL_LAKES, 0, 0, true);
    public static final MusicSound POST_MOON          = new MusicSound(ModSounds.MUSIC_POST_MOON, 0, 0, true);
    public static final MusicSound DAY                = new MusicSound(ModSounds.MUSIC_DAY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound NIGHT              = new MusicSound(ModSounds.MUSIC_NIGHT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound OW_DAY             = new MusicSound(ModSounds.MUSIC_OW_DAY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound OW_NIGHT           = new MusicSound(ModSounds.MUSIC_OW_NIGHT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound OW_CAVE            = new MusicSound(ModSounds.MUSIC_OW_CAVE, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound OW_SCARY           = new MusicSound(ModSounds.MUSIC_OW_SCARY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound ORBIT              = new MusicSound(ModSounds.MUSIC_ORBIT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound MERCURY            = new MusicSound(ModSounds.MUSIC_MERCURY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound MARS               = new MusicSound(ModSounds.MUSIC_MARS, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound GLACIO               = new MusicSound(ModSounds.MUSIC_GLACIO, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound END                = new MusicSound(ModSounds.MUSIC_END, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    public static final MusicSound END_BOSS           = new MusicSound(ModSounds.MUSIC_END_BOSS, 0, 0, true);
    public static final MusicSound WITHER             = new MusicSound(ModSounds.MUSIC_WITHER, 0, 0, true);
    public static final MusicSound WITHER_PHASE2      = new MusicSound(ModSounds.MUSIC_WITHER_PHASE2, 0, 0, true);
    public static final MusicSound WITHER_SPAWN       = new MusicSound(ModSounds.MUSIC_WITHER_SPAWN, 0, 0, true);
    public static final MusicSound WITHER_DEATH       = new MusicSound(ModSounds.MUSIC_WITHER_DEATH, 0, 0, true);
    public static final MusicSound COMBAT             = new MusicSound(ModSounds.MUSIC_COMBAT, 0, 0, true);
    public static final MusicSound COMBAT_END         = new MusicSound(ModSounds.MUSIC_COMBAT_END, 0, 0, true);
    public static final MusicSound SHIMMER_BLAZE      = new MusicSound(ModSounds.MUSIC_SHIMMER_BLAZE, 0, 0, true);
    public static final MusicSound ASTRAL_LAKES_REMIX = new MusicSound(ModSounds.MUSIC_ASTRAL_LAKES_REMIX, 0, 0, true);


    private static final Map<Identifier, TrackInfo> NOW_PLAYING = new HashMap<>();

    public static void registerNowPlaying(Identifier soundId, String key) {
        NOW_PLAYING.put(soundId, new TrackInfo(
                new TranslatableText("music." + AstralAdditions.MOD_ID + ".track." + key + ".title"),
                new TranslatableText("music." + AstralAdditions.MOD_ID + ".track." + key + ".artist")
        ));
    }

    public static TrackInfo getNowPlaying(Identifier soundId) {
        return NOW_PLAYING.get(soundId);
    }

    public record TrackInfo(Text title, Text artist) {}

    static {
        //?These MUST match the name entries in sounds.json
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "nostalgia"), "nostalgia");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "astroid_cluster"), "astroid_cluster");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "astral_lakes"), "astral_lakes");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "despiteeverythingitsstillyou"), "despiteeverythingitsstillyou");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "uneasy"), "uneasy");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "bodiesfloatinginspace"), "bodiesfloatinginspace");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "wow"), "wow");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "newday"), "newday");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "breathein"), "breathein");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "moonlight"), "moonlight");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "day"), "day");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "night"), "night");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "cave"), "cave");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "starcave"), "starcave");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "gazinglakes"), "gazinglakes");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "astralsend"), "astralsend");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "wither"), "wither");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "witherphase2"), "witherphase2");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "witherspawn"), "witherspawn");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "stance"), "stance");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "tensionrising"), "tensionrising");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "andbreathe"), "andbreathe");
        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "purple_prison"), "purple_prison");

        registerNowPlaying(new Identifier(AstralAdditions.MOD_ID, "disc/astral_lakes_remix"), "astral_lakes_remix");
    }
}