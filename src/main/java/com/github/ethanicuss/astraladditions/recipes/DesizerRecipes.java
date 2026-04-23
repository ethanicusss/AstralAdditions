package com.github.ethanicuss.astraladditions.recipes;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DesizerRecipes{

    public static List<TagKey<Block>> DESIZER_RECIPES = new ArrayList<>();

    private static TagKey<Block> register(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(AstralAdditions.MOD_ID, id));
    }

    public static List<RecipeStructure> recipes = new ArrayList<>();
    public static class RecipeStructure {
        public Block[][][] blocks = new Block[3][3][3];
        Item resultItem;
        int count;
        public RecipeStructure() {
            count = 1;
        }
    }

    public static Item checkRecipe(RecipeStructure input){

        for (int i = 0; i < recipes.size(); i++){
            /*for (int l = 0; l < 3; l++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        System.out.println(i + ", " + j + ", " + k + " - " + recipes.get(i).blocks[l][j][k]);
                    }
                }
            }*/
            if (Arrays.deepEquals(input.blocks, recipes.get(i).blocks)) { return recipes.get(0).resultItem; }
        }

        return Items.STRUCTURE_VOID;
    }

    public static void init(){
        /*Path path = FabricLoader.getInstance().getGameDir();
        File folder = path.toFile();
        System.out.println(path.toString());
        for (final File fileEntry : folder.listFiles()){
            System.out.println(fileEntry.getName());
        }*/

        DESIZER_RECIPES.add(register("desizer_recipes/testrecipe"));
        //TagPacketSerializer.

        recipes.add(new RecipeStructure());
        recipes.get(0).resultItem = Items.BEACON;
        recipes.get(0).blocks[0][0][0] = Blocks.GLASS;
        recipes.get(0).blocks[0][0][1] = Blocks.GLASS;
        recipes.get(0).blocks[0][0][2] = Blocks.GLASS;
        recipes.get(0).blocks[0][1][0] = Blocks.GLASS;
        recipes.get(0).blocks[0][1][1] = Blocks.GLASS;
        recipes.get(0).blocks[0][1][2] = Blocks.GLASS;
        recipes.get(0).blocks[0][2][0] = Blocks.GLASS;
        recipes.get(0).blocks[0][2][1] = Blocks.GLASS;
        recipes.get(0).blocks[0][2][2] = Blocks.GLASS;
        recipes.get(0).blocks[1][0][0] = Blocks.GLASS;
        recipes.get(0).blocks[1][0][1] = Blocks.GLASS;
        recipes.get(0).blocks[1][0][2] = Blocks.GLASS;
        recipes.get(0).blocks[1][1][0] = Blocks.GLASS;
        recipes.get(0).blocks[1][1][1] = Blocks.DIAMOND_BLOCK;
        recipes.get(0).blocks[1][1][2] = Blocks.GLASS;
        recipes.get(0).blocks[1][2][0] = Blocks.GLASS;
        recipes.get(0).blocks[1][2][1] = Blocks.GLASS;
        recipes.get(0).blocks[1][2][2] = Blocks.GLASS;
        recipes.get(0).blocks[2][0][0] = Blocks.OBSIDIAN;
        recipes.get(0).blocks[2][0][1] = Blocks.OBSIDIAN;
        recipes.get(0).blocks[2][0][2] = Blocks.OBSIDIAN;
        recipes.get(0).blocks[2][1][0] = Blocks.OBSIDIAN;
        recipes.get(0).blocks[2][1][1] = Blocks.OBSIDIAN;
        recipes.get(0).blocks[2][1][2] = Blocks.OBSIDIAN;
        recipes.get(0).blocks[2][2][0] = Blocks.OBSIDIAN;
        recipes.get(0).blocks[2][2][1] = Blocks.OBSIDIAN;
        recipes.get(0).blocks[2][2][2] = Blocks.OBSIDIAN;

        for (int i = 0; i < DESIZER_RECIPES.size(); i++){
            recipes.add(new RecipeStructure());
            recipes.get(0).resultItem = Items.GLOWSTONE;
            //RegistryEntryList<Block> blocks = DESIZER_RECIPES.get(i);
            //blocks.get();

            for (int l = 0; l < 3; l++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        //recipes.get(i).blocks[l][j][k] = ;
                    }
                }
            }
        }
    }
}