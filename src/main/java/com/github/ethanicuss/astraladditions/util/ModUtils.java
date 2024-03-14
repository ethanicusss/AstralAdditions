package com.github.ethanicuss.astraladditions.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ModUtils {
    public static <T extends ParticleEffect> void spawnForcedParticles(ServerWorld world, T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            world.spawnParticles(player, particle, true, x, y, z, count, deltaX, deltaY, deltaZ, speed);
        }
    }
}
