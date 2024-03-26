package com.github.ethanicuss.astraladditions.registry;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static final Identifier MUSIC_MOON_ID = new Identifier(AstralAdditions.MOD_ID, "music_moon");
    public static final SoundEvent MUSIC_MOON = new SoundEvent(MUSIC_MOON_ID);
    public static final Identifier MUSIC_POST_MOON_ID = new Identifier(AstralAdditions.MOD_ID, "music_post_moon");
    public static final SoundEvent MUSIC_POST_MOON = new SoundEvent(MUSIC_POST_MOON_ID);
    public static final Identifier MUSIC_DAY_ID = new Identifier(AstralAdditions.MOD_ID, "music_day");
    public static final SoundEvent MUSIC_DAY = new SoundEvent(MUSIC_DAY_ID);
    public static final Identifier MUSIC_OW_CAVE_ID = new Identifier(AstralAdditions.MOD_ID, "music_ow_cave");
    public static final SoundEvent MUSIC_OW_CAVE = new SoundEvent(MUSIC_OW_CAVE_ID);
    public static final Identifier MUSIC_OW_SCARY_ID = new Identifier(AstralAdditions.MOD_ID, "music_ow_scary");
    public static final SoundEvent MUSIC_OW_SCARY = new SoundEvent(MUSIC_OW_SCARY_ID);
    public static final Identifier MUSIC_OW_DAY_ID = new Identifier(AstralAdditions.MOD_ID, "music_ow_day");
    public static final SoundEvent MUSIC_OW_DAY = new SoundEvent(MUSIC_OW_DAY_ID);
    public static final Identifier MUSIC_OW_NIGHT_ID = new Identifier(AstralAdditions.MOD_ID, "music_ow_night");
    public static final SoundEvent MUSIC_OW_NIGHT = new SoundEvent(MUSIC_OW_NIGHT_ID);
    public static final Identifier MUSIC_NIGHT_ID = new Identifier(AstralAdditions.MOD_ID, "music_night");
    public static final SoundEvent MUSIC_NIGHT = new SoundEvent(MUSIC_NIGHT_ID);
    public static final Identifier MUSIC_ORBIT_ID = new Identifier(AstralAdditions.MOD_ID, "music_orbit");
    public static final SoundEvent MUSIC_ORBIT = new SoundEvent(MUSIC_ORBIT_ID);
    public static final Identifier MUSIC_MERCURY_ID = new Identifier(AstralAdditions.MOD_ID, "music_mercury");
    public static final SoundEvent MUSIC_MERCURY = new SoundEvent(MUSIC_MERCURY_ID);
    public static final Identifier MUSIC_MARS_ID = new Identifier(AstralAdditions.MOD_ID, "music_mars");
    public static final SoundEvent MUSIC_MARS = new SoundEvent(MUSIC_MARS_ID);
    public static final Identifier MUSIC_END_ID = new Identifier(AstralAdditions.MOD_ID, "music_end");
    public static final SoundEvent MUSIC_END = new SoundEvent(MUSIC_END_ID);
    public static final Identifier MUSIC_END_BOSS_ID = new Identifier(AstralAdditions.MOD_ID, "music_end_boss");
    public static final SoundEvent MUSIC_END_BOSS = new SoundEvent(MUSIC_END_BOSS_ID);
    public static final Identifier MUSIC_WITHER_ID = new Identifier(AstralAdditions.MOD_ID, "music_wither");
    public static final SoundEvent MUSIC_WITHER = new SoundEvent(MUSIC_WITHER_ID);
    public static final Identifier MUSIC_WITHER_PHASE2_ID = new Identifier(AstralAdditions.MOD_ID, "music_wither_phase2");
    public static final SoundEvent MUSIC_WITHER_PHASE2 = new SoundEvent(MUSIC_WITHER_PHASE2_ID);
    public static final Identifier MUSIC_WITHER_SPAWN_ID = new Identifier(AstralAdditions.MOD_ID, "music_wither_spawn");
    public static final SoundEvent MUSIC_WITHER_SPAWN = new SoundEvent(MUSIC_WITHER_SPAWN_ID);
    public static final Identifier MUSIC_WITHER_DEATH_ID = new Identifier(AstralAdditions.MOD_ID, "music_wither_death");
    public static final SoundEvent MUSIC_WITHER_DEATH = new SoundEvent(MUSIC_WITHER_DEATH_ID);
    public static final Identifier MUSIC_COMBAT_ID = new Identifier(AstralAdditions.MOD_ID, "music_combat");
    public static final SoundEvent MUSIC_COMBAT = new SoundEvent(MUSIC_COMBAT_ID);
    public static final Identifier MUSIC_COMBAT_END_ID = new Identifier(AstralAdditions.MOD_ID, "music_combat_end");
    public static final SoundEvent MUSIC_COMBAT_END = new SoundEvent(MUSIC_COMBAT_END_ID);
    private static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, MUSIC_MOON_ID, MUSIC_MOON);
        Registry.register(Registry.SOUND_EVENT, MUSIC_POST_MOON_ID, MUSIC_POST_MOON);
        Registry.register(Registry.SOUND_EVENT, MUSIC_DAY_ID, MUSIC_DAY);
        Registry.register(Registry.SOUND_EVENT, MUSIC_OW_CAVE_ID, MUSIC_OW_CAVE);
        Registry.register(Registry.SOUND_EVENT, MUSIC_OW_SCARY_ID, MUSIC_OW_SCARY);
        Registry.register(Registry.SOUND_EVENT, MUSIC_OW_DAY_ID, MUSIC_OW_DAY);
        Registry.register(Registry.SOUND_EVENT, MUSIC_OW_NIGHT_ID, MUSIC_OW_NIGHT);
        Registry.register(Registry.SOUND_EVENT, MUSIC_NIGHT_ID, MUSIC_NIGHT);
        Registry.register(Registry.SOUND_EVENT, MUSIC_ORBIT_ID, MUSIC_ORBIT);
        Registry.register(Registry.SOUND_EVENT, MUSIC_MERCURY_ID, MUSIC_MERCURY);
        Registry.register(Registry.SOUND_EVENT, MUSIC_MARS_ID, MUSIC_MARS);
        Registry.register(Registry.SOUND_EVENT, MUSIC_END_ID, MUSIC_END);
        Registry.register(Registry.SOUND_EVENT, MUSIC_END_BOSS_ID, MUSIC_END_BOSS);
        Registry.register(Registry.SOUND_EVENT, MUSIC_WITHER_ID, MUSIC_WITHER);
        Registry.register(Registry.SOUND_EVENT, MUSIC_WITHER_PHASE2_ID, MUSIC_WITHER_PHASE2);
        Registry.register(Registry.SOUND_EVENT, MUSIC_WITHER_SPAWN_ID, MUSIC_WITHER_SPAWN);
        Registry.register(Registry.SOUND_EVENT, MUSIC_WITHER_DEATH_ID, MUSIC_WITHER_DEATH);
        Registry.register(Registry.SOUND_EVENT, MUSIC_COMBAT_ID, MUSIC_COMBAT);
        Registry.register(Registry.SOUND_EVENT, MUSIC_COMBAT_END_ID, MUSIC_COMBAT_END);
    }
}
