package com.github.ethanicuss.astraladditions.mixin;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;


@Mixin(Minecraft.class)
public class SpaceMusic {
    @Shadow public boolean wireframe;
    @Shadow @Final private MusicManager musicManager;
    private static final int GAME_MIN_DELAY = 12000;
    private static final int GAME_MAX_DELAY = 24000;//12000, 24000
    private static SoundEvent register(String id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new ResourceLocation(AstralAdditions.MOD_ID, id)));
    }
    private static final SoundEvent MUSIC_MOON = register("music_moon");
    private static final SoundEvent MUSIC_POST_MOON = register("music_post_moon");
    private static final SoundEvent MUSIC_DAY = register("music_day");
    private static final SoundEvent MUSIC_OW_CAVE = register("music_ow_cave");
    private static final SoundEvent MUSIC_OW_SCARY = register("music_ow_scary");
    private static final SoundEvent MUSIC_OW_DAY = register("music_ow_day");
    private static final SoundEvent MUSIC_OW_NIGHT = register("music_ow_night");
    private static final SoundEvent MUSIC_NIGHT = register("music_night");
    private static final SoundEvent MUSIC_ORBIT = register("music_orbit");
    private static final SoundEvent MUSIC_MERCURY = register("music_mercury");
    private static final SoundEvent MUSIC_MARS = register("music_mars");
    private static final SoundEvent MUSIC_END = register("music_end");
    private static final SoundEvent MUSIC_END_BOSS = register("music_end_boss");
    private static final SoundEvent MUSIC_WITHER = register("music_wither");
    private static final SoundEvent MUSIC_WITHER_PHASE2 = register("music_wither_phase2");
    private static final SoundEvent MUSIC_WITHER_SPAWN = register("music_wither_spawn");
    private static final SoundEvent MUSIC_WITHER_DEATH = register("music_wither_death");
    private static final SoundEvent MUSIC_COMBAT = register("music_combat");
    private static final SoundEvent MUSIC_COMBAT_END = register("music_combat_end");
    private static final Music MOON = new Music(MUSIC_MOON, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music POST_MOON = new Music(MUSIC_POST_MOON, 0, 0, true);
    private static final Music DAY = new Music(MUSIC_DAY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music NIGHT = new Music(MUSIC_NIGHT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music OW_DAY = new Music(MUSIC_OW_DAY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music OW_NIGHT = new Music(MUSIC_OW_NIGHT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music OW_CAVE = new Music(MUSIC_OW_CAVE, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music OW_SCARY = new Music(MUSIC_OW_SCARY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music ORBIT = new Music(MUSIC_ORBIT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music MERCURY = new Music(MUSIC_MERCURY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music MARS = new Music(MUSIC_MARS, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music END = new Music(MUSIC_END, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final Music END_BOSS = new Music(MUSIC_END_BOSS, 0, 0, true);
    private static final Music WITHER = new Music(MUSIC_WITHER, 0, 0, true);
    private static final Music WITHER_PHASE2 = new Music(MUSIC_WITHER_PHASE2, 0, 0, true);
    private static final Music WITHER_SPAWN = new Music(MUSIC_WITHER_SPAWN, 0, 0, true);
    private static final Music WITHER_DEATH = new Music(MUSIC_WITHER_DEATH, 0, 0, true);
    private static final Music COMBAT = new Music(MUSIC_COMBAT, 0, 0, true);
    private static final Music COMBAT_END = new Music(MUSIC_COMBAT_END, 0, 0, true);

    private static final Predicate<WitherBoss> WITHER_PREDICATE = (entity) -> {
        return true;
    };
    private static final Predicate<Monster> ENEMY_PREDICATE = (entity) -> {
        return true;
    };

    @Inject(method = "getSituationalMusic", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void getMusicType(CallbackInfoReturnable<Music> cir){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (Objects.equals(player.level.dimension().location().toString(), "minecraft:overworld")){
                if (player.level.getDayTime()%24000 < 12000) {
                    cir.setReturnValue(OW_DAY);
                }
                else{
                    cir.setReturnValue(OW_NIGHT);
                }
                if (player.getY() < 50){
                    cir.setReturnValue(OW_CAVE);
                }
                if (player.getY() < 30 && player.level.getMaxLocalRawBrightness(player.blockPosition()) == 0){
                    cir.setReturnValue(OW_SCARY);
                }
            }
            if (!Objects.equals(player.level.dimension().location().toString(), "minecraft:overworld") && !Objects.equals(player.level.dimension().location().toString(), "minecraft:the_end")) {
                if (player.level.getDayTime()%24000 < 12000) {
                    cir.setReturnValue(DAY);
                }
                else{
                    cir.setReturnValue(NIGHT);
                }
            }
            if (Objects.equals(player.level.dimension().location().toString(), "ad_astra:moon") && player.getY() < 95) {
                cir.setReturnValue(MOON);
            }
            if (Objects.equals(player.level.dimension().location().toString(), "ad_astra:mercury")) {
                cir.setReturnValue(MERCURY);
            }
            if (Objects.equals(player.level.dimension().location().toString(), "minecraft:the_end")) {
                cir.setReturnValue(END);
                if (Minecraft.getInstance().gui.getBossOverlay().shouldPlayMusic()) {
                    cir.setReturnValue(END_BOSS);
                }
            }
            if (Objects.equals(player.level.dimension().location().toString(), "ad_astra:mars") && player.getY() < 80) {
                cir.setReturnValue(MARS);
            }
            if (Objects.equals(player.level.dimension().location().toString(), "ad_astra:moon_orbit") || Objects.equals(player.level.dimension().location().toString(), "ad_astra:mars_orbit") || Objects.equals(player.level.dimension().location().toString(), "ad_astra:mercury_orbit") || Objects.equals(player.level.dimension().location().toString(), "ad_astra:earth_orbit")) {
                cir.setReturnValue(ORBIT);
            }
            if ((player.getHealth() < 12.0f || musicManager.isPlayingMusic(COMBAT)) && !musicManager.isPlayingMusic(WITHER_DEATH)) {
                double i = player.getX();
                double j = player.getY();
                double k = player.getZ();
                float f = 20.0f;
                AABB box = new AABB((float) i - f, (float) j - f, (float) k - f, (float) (i + 1) + f, (float) (j + 1) + f, (float) (k + 1) + f);
                List<Monster> list = player.level.getEntities(EntityTypeTest.forClass(Monster.class), box, ENEMY_PREDICATE);
                int enemySeeCount = 0;
                int enemyCount = 0;
                boolean enemyDied = false;
                for (Monster e : list) {
                    enemyCount++;
                    if (e.hasLineOfSight(player)) {
                        enemySeeCount++;
                    }
                    if (e.isDeadOrDying()) {
                        enemyDied = true;
                        enemySeeCount--;
                        enemyCount--;
                    }
                }
                if (enemySeeCount >= 3) {
                    cir.setReturnValue(COMBAT);
                }
                if (enemyDied && musicManager.isPlayingMusic(COMBAT)){
                    if (enemySeeCount == 0){
                        cir.setReturnValue(COMBAT_END);
                    }
                }
                if (enemyCount == 0 && musicManager.isPlayingMusic(COMBAT)) {
                    cir.setReturnValue(COMBAT_END);
                }
            }
            if (Minecraft.getInstance().gui.getBossOverlay().shouldDarkenScreen()) {
                double i = player.getX();
                double j = player.getY();
                double k = player.getZ();
                float f = 40.0f;
                AABB box = new AABB((float)i - f, (float)j - f, (float)k - f, (float)(i + 1) + f, (float)(j + 1) + f, (float)(k + 1) + f);
                List<WitherBoss> list = player.level.getEntities(EntityTypeTest.forClass(WitherBoss.class), box, WITHER_PREDICATE);
                boolean witherExists = false;
                boolean spawnExists = false;
                boolean phase2Exists = false;
                boolean witherDead = false;
                for (WitherBoss w : list) {
                    witherExists = true;
                    if (w.getInvulnerableTicks() > 0){
                        spawnExists = true;
                    }
                    else {
                        if (w.isPowered()) {
                            phase2Exists = true;
                        }
                    }
                    if (w.getHealth() <= 0){
                        witherDead = true;
                    }
                }
                if (spawnExists){
                    cir.setReturnValue(WITHER_SPAWN);
                }
                else {
                    if (phase2Exists) {
                        cir.setReturnValue(WITHER_PHASE2);
                    }
                    else{
                        if (witherExists) {
                            cir.setReturnValue(WITHER);
                        }
                        else{
                            if (musicManager.isPlayingMusic(WITHER) || musicManager.isPlayingMusic(WITHER_PHASE2)){
                                cir.setReturnValue(COMBAT_END);
                            }
                        }
                    }
                }
                if (witherDead){
                    cir.setReturnValue(WITHER_DEATH);
                }
            }
            if (!AstralAdditionsClient.playerTracker.hasBeenToMoon){
                if (Objects.equals(player.level.dimension().location().toString(), "ad_astra:moon")) {
                    AstralAdditionsClient.playerTracker.hasBeenToMoon = true;
                }
            }
            else{
                if (!AstralAdditionsClient.playerTracker.hasHeardPostMoonSong){
                    if (Objects.equals(player.level.dimension().location().toString(), "minecraft:overworld")) {
                        AstralAdditionsClient.playerTracker.hasHeardPostMoonSong = true;
                        cir.setReturnValue(POST_MOON);
                        AstralAdditionsClient.playerTracker.SaveTrackingData();
                    }
                }
            }
        }
    }
}
