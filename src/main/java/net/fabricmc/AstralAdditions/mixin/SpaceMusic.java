package net.fabricmc.AstralAdditions.mixin;

import net.fabricmc.AstralAdditions.AstralAdditions;
import net.fabricmc.AstralAdditions.AstralAdditionsClient;
import net.fabricmc.AstralAdditions.playertracker.PlayerTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
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


@Mixin(MinecraftClient.class)
public class SpaceMusic {
    @Shadow public boolean wireFrame;
    @Shadow @Final private MusicTracker musicTracker;
    private static final int GAME_MIN_DELAY = 12000;
    private static final int GAME_MAX_DELAY = 24000;//12000, 24000
    private static SoundEvent register(String id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new Identifier(AstralAdditions.MOD_ID, id)));
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
    private static final MusicSound MOON = new MusicSound(MUSIC_MOON, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound POST_MOON = new MusicSound(MUSIC_POST_MOON, 0, 0, true);
    private static final MusicSound DAY = new MusicSound(MUSIC_DAY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound NIGHT = new MusicSound(MUSIC_NIGHT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound OW_DAY = new MusicSound(MUSIC_OW_DAY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound OW_NIGHT = new MusicSound(MUSIC_OW_NIGHT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound OW_CAVE = new MusicSound(MUSIC_OW_CAVE, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound OW_SCARY = new MusicSound(MUSIC_OW_SCARY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound ORBIT = new MusicSound(MUSIC_ORBIT, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound MERCURY = new MusicSound(MUSIC_MERCURY, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound MARS = new MusicSound(MUSIC_MARS, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound END = new MusicSound(MUSIC_END, GAME_MIN_DELAY, GAME_MAX_DELAY, false);
    private static final MusicSound END_BOSS = new MusicSound(MUSIC_END_BOSS, 0, 0, true);
    private static final MusicSound WITHER = new MusicSound(MUSIC_WITHER, 0, 0, true);
    private static final MusicSound WITHER_PHASE2 = new MusicSound(MUSIC_WITHER_PHASE2, 0, 0, true);
    private static final MusicSound WITHER_SPAWN = new MusicSound(MUSIC_WITHER_SPAWN, 0, 0, true);
    private static final MusicSound WITHER_DEATH = new MusicSound(MUSIC_WITHER_DEATH, 0, 0, true);
    private static final MusicSound COMBAT = new MusicSound(MUSIC_COMBAT, 0, 0, true);
    private static final MusicSound COMBAT_END = new MusicSound(MUSIC_COMBAT_END, 0, 0, true);

    private static final Predicate<WitherEntity> WITHER_PREDICATE = (entity) -> {
        return true;
    };
    private static final Predicate<HostileEntity> ENEMY_PREDICATE = (entity) -> {
        return true;
    };

    @Inject(method = "getMusicType", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void getMusicType(CallbackInfoReturnable<MusicSound> cir){
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:overworld")){
                if (player.world.getTimeOfDay()%24000 < 12000) {
                    cir.setReturnValue(OW_DAY);
                }
                else{
                    cir.setReturnValue(OW_NIGHT);
                }
                if (player.getY() < 50){
                    cir.setReturnValue(OW_CAVE);
                }
                if (player.getY() < 30 && player.world.getLightLevel(player.getBlockPos()) == 0){
                    cir.setReturnValue(OW_SCARY);
                }
            }
            if (!Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:overworld") && !Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_end")) {
                if (player.world.getTimeOfDay()%24000 < 12000) {
                    cir.setReturnValue(DAY);
                }
                else{
                    cir.setReturnValue(NIGHT);
                }
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:moon") && player.getY() < 95) {
                cir.setReturnValue(MOON);
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:mercury")) {
                cir.setReturnValue(MERCURY);
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:the_end")) {
                cir.setReturnValue(END);
                if (MinecraftClient.getInstance().inGameHud.getBossBarHud().shouldPlayDragonMusic()) {
                    cir.setReturnValue(END_BOSS);
                }
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:mars") && player.getY() < 80) {
                cir.setReturnValue(MARS);
            }
            if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:moon_orbit") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:mars_orbit") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:mercury_orbit") || Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:earth_orbit")) {
                cir.setReturnValue(ORBIT);
            }
            if ((player.getHealth() < 12.0f || musicTracker.isPlayingType(COMBAT)) && !musicTracker.isPlayingType(WITHER_DEATH)) {
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
                    cir.setReturnValue(COMBAT);
                }
                if (enemyDied && musicTracker.isPlayingType(COMBAT)){
                    if (enemySeeCount == 0){
                        cir.setReturnValue(COMBAT_END);
                    }
                }
                if (enemyCount == 0 && musicTracker.isPlayingType(COMBAT)) {
                    cir.setReturnValue(COMBAT_END);
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
                            if (musicTracker.isPlayingType(WITHER) || musicTracker.isPlayingType(WITHER_PHASE2)){
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
                if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "ad_astra:moon")) {
                    AstralAdditionsClient.playerTracker.hasBeenToMoon = true;
                }
            }
            else{
                if (!AstralAdditionsClient.playerTracker.hasHeardPostMoonSong){
                    if (Objects.equals(player.world.getRegistryKey().getValue().toString(), "minecraft:overworld")) {
                        AstralAdditionsClient.playerTracker.hasHeardPostMoonSong = true;
                        cir.setReturnValue(POST_MOON);
                        AstralAdditionsClient.playerTracker.SaveTrackingData();
                    }
                }
            }
        }
    }
}
