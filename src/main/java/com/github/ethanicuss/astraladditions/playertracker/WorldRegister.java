package com.github.ethanicuss.astraladditions.playertracker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.*;

public class WorldRegister {

    private static Map<String, ServerWorld> worldRegisterMap;
    private HashMap<String, ServerWorld> worldRegister = new HashMap<>();
    public WorldRegister(){

    }

    public void LoadData(World world){
        MinecraftServer server = world.getServer();
        for (ServerWorld serverWorld : Objects.requireNonNull(server).getWorlds()) { //💀
            worldRegister.put(serverWorld.getRegistryKey().getValue().toString(), serverWorld);
        }
    }

    public ServerWorld getWorld(String worldName){
        for (Map.Entry<String, ServerWorld> entry : worldRegister.entrySet()) {
            if (Objects.equals(worldName, entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
