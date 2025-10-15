package com.github.ethanicuss.astraladditions.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.include.com.google.gson.JsonObject;

import java.io.StringReader;

public class ModUtils {
    public static <T extends ParticleEffect> void spawnForcedParticles(ServerWorld world, T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            world.spawnParticles(player, particle, true, x, y, z, count, deltaX, deltaY, deltaZ, speed);
        }
    }

    public static void playSound(ServerWorld world, double x, double y, double z, SoundEvent sound, SoundCategory category, float vol, float pitch, boolean falloff) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            player.world.playSound(x, y, z, sound, category, vol, pitch, falloff);
        }
    }
}

