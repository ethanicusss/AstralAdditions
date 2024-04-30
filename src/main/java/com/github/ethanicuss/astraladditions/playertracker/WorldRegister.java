package com.github.ethanicuss.astraladditions.playertracker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public class WorldRegister {

    private static Map<String, ServerWorld> worldRegisterMap;
    private HashMap<String, ServerWorld> worldRegister = new HashMap<>();
    public WorldRegister(){

    }

    public void LoadData(){
        MinecraftClient client = MinecraftClient.getInstance();
        for (ServerWorld serverWorld : Objects.requireNonNull(client.getServer()).getWorlds()) {
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
