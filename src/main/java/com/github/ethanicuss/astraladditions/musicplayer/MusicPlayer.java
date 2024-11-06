package com.github.ethanicuss.astraladditions.musicplayer;

import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.registry.ModMusic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MusicPlayer {
    private static final Predicate<WitherEntity> WITHER_PREDICATE = (entity) -> {
        return true;
    };
    private static final Predicate<EnderDragonEntity> ENDER_DRAGON_PREDICATE = (entity) -> {
        return true;
    };
    private static final Predicate<ShimmerBlazeEntity> SHIMMER_BLAZE_PREDICATE = (entity) -> {
        return true;
    };
    private static final Predicate<HostileEntity> ENEMY_PREDICATE = (entity) -> {
        return true;
    };
    public static MusicSound findMusic(MusicTracker musicTracker){
        MusicSound val = ModMusic.MENU;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:overworld")){
                if (player.world.getTimeOfDay()%24000 < 12000) {
                    val = ModMusic.OW_DAY;
                }
                else{
                    val = ModMusic.OW_NIGHT;
                }
                if (player.getY() < 50){
                    val = ModMusic.OW_CAVE;
                }
                if (player.getY() < 30 && player.world.getLightLevel(player.getBlockPos()) == 0){
                    val = ModMusic.OW_SCARY;
                }
            }
            if (!Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:overworld") && !Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_end") && !Objects.equals(player.world.getRegistryKey().getValue().toString(), "createastral:moon_debris")) {
                if (player.world.getTimeOfDay()%24000 < 12000) {
                    val = ModMusic.DAY;
                }
                else{
                    val = ModMusic.NIGHT;
                }
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:moon") && player.getY() < 95) {
                val = ModMusic.MOON;
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:mercury")) {
                val = ModMusic.MERCURY;
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_nether") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "createastral:moon_debris") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_end")) {
                val = ModMusic.END;
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:mars") && player.getY() < 80) {
                val = ModMusic.MARS;
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:moon_orbit") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:mars_orbit") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:mercury_orbit") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:earth_orbit")) {
                val = ModMusic.ORBIT;
            }
            if (AstralAdditionsClient.playerTracker.doCombatMusic && ((player.getHealth() < 12.0f || musicTracker.isPlayingType(ModMusic.COMBAT)) && !musicTracker.isPlayingType(ModMusic.WITHER_DEATH) && !Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_nether") && !Objects.equals(player.world.getRegistryKey().getValue().toString(), "createastral:moon_debris") && !Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_end"))) {
                double i = player.getX();
                double j = player.getY();
                double k = player.getZ();
                float f = 20.0f;
                Box box = new Box((float) i - f, (float) j - f, (float) k - f, (float) (i + 1) + f, (float) (j + 1) + f, (float) (k + 1) + f);
                List<HostileEntity> list = player.world.getEntitiesByType(TypeFilter.instanceOf(HostileEntity.class), box, ENEMY_PREDICATE);
                int enemySeeCount = 0;
                int enemyCount = 0;
                boolean enemyDied = false;
                for (HostileEntity e : list) {
                    enemyCount++;
                    if (e.canSee(player)) {
                        enemySeeCount++;
                    }
                    if (e.isDead()) {
                        enemyDied = true;
                        enemySeeCount--;
                        enemyCount--;
                    }
                }
                if (enemySeeCount >= 3) {
                    val = ModMusic.COMBAT;
                }
                if (enemyDied && musicTracker.isPlayingType(ModMusic.COMBAT)){
                    if (enemySeeCount == 0){
                        val = ModMusic.COMBAT_END;
                    }
                }
                if (enemyCount == 0 && musicTracker.isPlayingType(ModMusic.COMBAT)) {
                    val = ModMusic.COMBAT_END;
                }
            }
            if (MinecraftClient.getInstance().inGameHud.getBossBarHud().shouldDarkenSky()) {
                double i = player.getX();
                double j = player.getY();
                double k = player.getZ();
                float f = 40.0f;
                Box box = new Box((float)i - f, (float)j - f, (float)k - f, (float)(i + 1) + f, (float)(j + 1) + f, (float)(k + 1) + f);
                List<WitherEntity> list = player.world.getEntitiesByType(TypeFilter.instanceOf(WitherEntity.class), box, WITHER_PREDICATE);
                boolean witherExists = false;
                boolean spawnExists = false;
                boolean phase2Exists = false;
                boolean witherDead = false;
                for (WitherEntity w : list) {
                    witherExists = true;
                    if (w.getInvulnerableTimer() > 0){
                        spawnExists = true;
                    }
                    else {
                        if (w.shouldRenderOverlay()) {
                            phase2Exists = true;
                        }
                    }
                    if (w.getHealth() <= 0){
                        witherDead = true;
                    }
                }
                if (spawnExists){
                    val = ModMusic.WITHER_SPAWN;
                }
                else {
                    if (phase2Exists) {
                        val = ModMusic.WITHER_PHASE2;
                    }
                    else{
                        if (witherExists) {
                            val = ModMusic.WITHER;
                        }
                        else{
                            if (musicTracker.isPlayingType(ModMusic.WITHER) || musicTracker.isPlayingType(ModMusic.WITHER_PHASE2)){
                                val = ModMusic.COMBAT_END;
                            }
                        }
                    }
                }
                if (witherDead){
                    val = ModMusic.WITHER_DEATH;
                }
            }
            {
                double i = player.getX();
                double j = player.getY();
                double k = player.getZ();
                float f = 30.0f;
                Box box = new Box((float)i - f, (float)j - f, (float)k - f, (float)(i + 1) + f, (float)(j + 1) + f, (float)(k + 1) + f);
                List<ShimmerBlazeEntity> list = player.world.getEntitiesByType(TypeFilter.instanceOf(ShimmerBlazeEntity.class), box, SHIMMER_BLAZE_PREDICATE);
                if (!list.isEmpty()) {
                    boolean blazeExists = false;
                    boolean blazeSeesU = false;
                    for (ShimmerBlazeEntity b : list) {
                        blazeExists = true;
                        if (b.canSee(player)) {
                            blazeSeesU = true;
                        }
                    }
                    if (blazeSeesU) {
                        val = ModMusic.SHIMMER_BLAZE;
                    }
                } else {
                    if (musicTracker.isPlayingType(ModMusic.SHIMMER_BLAZE)) {
                        val = ModMusic.WITHER_DEATH;
                    }
                }
            }
            if (!AstralAdditionsClient.playerTracker.hasBeenToMoon){
                if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:moon")) {
                    AstralAdditionsClient.playerTracker.hasBeenToMoon = true;
                }
            }
            else{
                if (!AstralAdditionsClient.playerTracker.hasHeardPostMoonSong){
                    if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:overworld")) {
                        AstralAdditionsClient.playerTracker.hasHeardPostMoonSong = true;
                        val = ModMusic.POST_MOON;
                        AstralAdditionsClient.playerTracker.SaveTrackingData();
                    }
                }
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_nether") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "createastral:moon_debris") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_end")) {
                double i = player.getX();
                double j = player.getY();
                double k = player.getZ();
                float f = 80.0f;
                Box box = new Box((float)i - f, (float)j - f, (float)k - f, (float)(i + 1) + f, (float)(j + 1) + f, (float)(k + 1) + f);
                List<EnderDragonEntity> list = player.world.getEntitiesByType(TypeFilter.instanceOf(EnderDragonEntity.class), box, ENDER_DRAGON_PREDICATE);
                boolean dragonExists = false;
                boolean dragonDead = false;
                for (EnderDragonEntity w : list) {
                    dragonExists = true;
                    if (w.getHealth() <= 0){
                        dragonDead = true;
                    }
                }
                if (dragonExists) {
                    val = ModMusic.END_BOSS;
                }
                else{
                    if (musicTracker.isPlayingType(ModMusic.END_BOSS)){
                        val = ModMusic.COMBAT_END;
                    }
                }
                if (dragonDead){
                    val = ModMusic.WITHER_DEATH;
                }
            }
        }
        return val;
    }
}
