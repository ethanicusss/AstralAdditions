package com.github.ethanicuss.astraladditions.musicplayer;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.config.AstralAdditionsConfig;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.registry.ModMusic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;

import java.util.List;

public class MusicPlayer {

    private static Identifier lastShownTrack = null;

    public static MusicSound findMusic(MusicTracker tracker) {

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null) return ModMusic.MENU;

        Identifier dim = player.world.getRegistryKey().getValue();
        String dimStr = dim.toString();
        long time = player.world.getTimeOfDay() % 24000;

        boolean overworld = dimStr.equals("minecraft:overworld");
        boolean nether = dimStr.equals("minecraft:the_nether");
        boolean end = dimStr.equals("minecraft:the_end");
        boolean moonDebris = dimStr.equals("createastral:moon_debris");

        MusicSound val = ModMusic.MENU;

        MusicSound bossMusic = bossMusic(player, tracker, client, dimStr);
        if (bossMusic != null) return bossMusic;

        MusicSound blazeMusic = blazeMusic(player, tracker);
        if (blazeMusic != null) return blazeMusic;

        MusicSound combatMusic = combatMusic(player, tracker, nether, end, moonDebris);
        if (combatMusic != null) return combatMusic;

        if (overworld) {
            if (time < 12000) val = ModMusic.OW_DAY;
            else val = ModMusic.OW_NIGHT;

            if (player.getY() < 50) val = ModMusic.OW_CAVE;

            if (player.getY() < 30 && player.world.getLightLevel(player.getBlockPos()) == 0)
                val = ModMusic.OW_SCARY;
        }

        if (!overworld && !end && !moonDebris) {
            if (time < 12000) val = ModMusic.DAY;
            else val = ModMusic.NIGHT;
        }

        if (dimStr.equals("ad_astra:moon") && player.getY() < 95)
            val = ModMusic.MOON;

        if (dimStr.equals("ad_astra:mercury"))
            val = ModMusic.MERCURY;

        if (nether || moonDebris || end)
            val = ModMusic.END;

        if (dimStr.equals("ad_astra:mars") && player.getY() < 80)
            val = ModMusic.MARS;

        if (dimStr.equals("ad_astra:moon_orbit")
                || dimStr.equals("ad_astra:mars_orbit")
                || dimStr.equals("ad_astra:mercury_orbit")
                || dimStr.equals("ad_astra:earth_orbit"))
            val = ModMusic.ORBIT;

        val = moonProgression(player, val, overworld);

        return val;
    }

    private static MusicSound combatMusic(ClientPlayerEntity player, MusicTracker tracker,
                                          boolean nether, boolean end, boolean debris) {

        if (!AstralAdditionsConfig.combatMusicEnabled) return null;

        if (!(player.getHealth() < 12.0f || tracker.isPlayingType(ModMusic.COMBAT)))
            return null;

        if (tracker.isPlayingType(ModMusic.WITHER_DEATH))
            return null;

        if (nether || debris || end)
            return null;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        float r = 20f;

        Box box = new Box(x - r, y - r, z - r, x + 1 + r, y + 1 + r, z + 1 + r);

        List<HostileEntity> list =
                player.world.getEntitiesByType(TypeFilter.instanceOf(HostileEntity.class), box, e -> true);

        int enemySee = 0;
        int enemyCount = 0;
        boolean died = false;

        for (HostileEntity e : list) {

            enemyCount++;

            if (e.canSee(player))
                enemySee++;

            if (e.isDead()) {
                died = true;
                enemySee--;
                enemyCount--;
            }
        }

        if (enemySee >= 3)
            return ModMusic.COMBAT;

        if (died && tracker.isPlayingType(ModMusic.COMBAT) && enemySee == 0)
            return ModMusic.COMBAT_END;

        if (enemyCount == 0 && tracker.isPlayingType(ModMusic.COMBAT))
            return ModMusic.COMBAT_END;

        return null;
    }

    private static MusicSound bossMusic(ClientPlayerEntity player, MusicTracker tracker,
                                        MinecraftClient client, String dim) {

        if (client.inGameHud.getBossBarHud().shouldDarkenSky()) {

            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            float r = 40f;

            Box box = new Box(x - r, y - r, z - r, x + 1 + r, y + 1 + r, z + 1 + r);

            List<WitherEntity> list =
                    player.world.getEntitiesByType(TypeFilter.instanceOf(WitherEntity.class), box, e -> true);

            boolean exists = false;
            boolean spawn = false;
            boolean phase2 = false;
            boolean dead = false;

            for (WitherEntity w : list) {

                exists = true;

                if (w.getInvulnerableTimer() > 0)
                    spawn = true;
                else if (w.shouldRenderOverlay())
                    phase2 = true;

                if (w.getHealth() <= 0)
                    dead = true;
            }

            if (spawn) return ModMusic.WITHER_SPAWN;

            if (phase2) return ModMusic.WITHER_PHASE2;

            if (exists) return ModMusic.WITHER;

            if (tracker.isPlayingType(ModMusic.WITHER)
                    || tracker.isPlayingType(ModMusic.WITHER_PHASE2))
                return ModMusic.COMBAT_END;

            if (dead) return ModMusic.WITHER_DEATH;
        }

        if (dim.equals("minecraft:the_nether")
                || dim.equals("createastral:moon_debris")
                || dim.equals("minecraft:the_end")) {

            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            float r = 80f;

            Box box = new Box(x - r, y - r, z - r, x + 1 + r, y + 1 + r, z + 1 + r);

            List<EnderDragonEntity> list =
                    player.world.getEntitiesByType(TypeFilter.instanceOf(EnderDragonEntity.class), box, e -> true);

            boolean exists = false;
            boolean dead = false;

            for (EnderDragonEntity d : list) {

                exists = true;

                if (d.getHealth() <= 0)
                    dead = true;
            }

            if (exists)
                return ModMusic.END_BOSS;

            if (tracker.isPlayingType(ModMusic.END_BOSS))
                return ModMusic.COMBAT_END;

            if (dead)
                return ModMusic.WITHER_DEATH;
        }

        return null;
    }

    private static MusicSound blazeMusic(ClientPlayerEntity player, MusicTracker tracker) {

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        float r = 30f;

        Box box = new Box(x - r, y - r, z - r, x + 1 + r, y + 1 + r, z + 1 + r);

        List<ShimmerBlazeEntity> list =
                player.world.getEntitiesByType(TypeFilter.instanceOf(ShimmerBlazeEntity.class), box, e -> true);

        if (!list.isEmpty()) {

            for (ShimmerBlazeEntity b : list)
                if (b.canSee(player))
                    return ModMusic.SHIMMER_BLAZE;

        } else {

            if (tracker.isPlayingType(ModMusic.SHIMMER_BLAZE))
                return ModMusic.WITHER_DEATH;
        }

        return null;
    }

    private static MusicSound moonProgression(ClientPlayerEntity player, MusicSound current, boolean overworld) {

        String dim = player.world.getRegistryKey().getValue().toString();

        if (!AstralAdditionsConfig.hasBeenToMoon) {

            if (dim.equals("ad_astra:moon")) {
                AstralAdditionsConfig.hasBeenToMoon = true;
                AstralAdditionsConfig.save();
            }

            return current;
        }

        if (!AstralAdditionsConfig.hasHeardPostMoonSong && overworld) {

            AstralAdditionsConfig.hasHeardPostMoonSong = true;
            AstralAdditionsConfig.save();

            return ModMusic.POST_MOON;
        }

        return current;
    }

    public static void onTrackStarted(Identifier soundId) {

        if (soundId == null) return;
        if (!AstralAdditions.MOD_ID.equals(soundId.getNamespace())) return;
        if (soundId.equals(lastShownTrack)) return;

        lastShownTrack = soundId;

        ModMusic.TrackInfo info = ModMusic.getNowPlaying(soundId);

        if (info != null)
            NowPlayingHud.show(info.title(), info.artist());
    }
}