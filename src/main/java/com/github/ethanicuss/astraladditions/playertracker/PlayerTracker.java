package com.github.ethanicuss.astraladditions.playertracker;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.io.*;
import java.nio.file.Path;

public class PlayerTracker{
    private ClientPlayerEntity player;
    public boolean hasBeenToMoon = false;
    public boolean hasHeardPostMoonSong = false;
    public PlayerTracker(){
        player = MinecraftClient.getInstance().player;
        LoadTrackingData();
    }

    public void SaveTrackingData(){
        try {
            Path path = FabricLoader.getInstance().getConfigDir();
            if (new File(String.valueOf(path).concat("/astraladditions")).mkdirs()){
                AstralAdditions.LOGGER.info("Astraladditions config folder did not exist. It has been created");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(path).concat("/astraladditions/musicdata.txt")));
            writer.write(String.valueOf(hasBeenToMoon) + "\n");
            writer.write(String.valueOf(hasHeardPostMoonSong) + "\n");
            writer.close();
            AstralAdditions.LOGGER.info("Astraladditions music data successfully saved");
        }
        catch (Exception e) {
            AstralAdditions.LOGGER.warn("Could not save music memory data: " + e);
        }
    }

    public void LoadTrackingData(){
        try {
            Path path = FabricLoader.getInstance().getConfigDir();
            File file = new File(String.valueOf(path).concat("/astraladditions/musicdata.txt"));
            InputStream inputStream = new FileInputStream(file);

            try (BufferedReader br
                         = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                int counter = 0;
                while ((line = br.readLine()) != null) {
                    switch (counter) {
                        case 0 -> hasBeenToMoon = line.matches("true");
                        case 1 -> hasHeardPostMoonSong = line.matches("true");
                    }
                    counter++;
                }
                AstralAdditions.LOGGER.info("Astraladditions music data successfully loaded");
            }
        }
        catch (Exception e) {
            AstralAdditions.LOGGER.warn("Could not load music memory data (this is fine if it's your first time loading this instance):  " + e);
            try {
                Path path = FabricLoader.getInstance().getConfigDir();
                if (new File(String.valueOf(path).concat("/astraladditions")).mkdirs()) {
                    AstralAdditions.LOGGER.info("Astraladditions config folder did not exist. It has been created");
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(path).concat("/astraladditions/musicdata.txt")));
                writer.write(String.valueOf(hasBeenToMoon) + "\n");
                writer.write(String.valueOf(hasHeardPostMoonSong) + "\n");
                writer.close();
            }
            catch (Exception e2) {
                AstralAdditions.LOGGER.warn("Could not create config path or data file: " + e2);
            }
        }
    }
}
