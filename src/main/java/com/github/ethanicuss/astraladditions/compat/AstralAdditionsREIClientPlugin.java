package com.github.ethanicuss.astraladditions.compat;


import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.recipes.DesizerRecipe;
import com.github.ethanicuss.astraladditions.recipes.DesizerRecipes;
import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import com.github.ethanicuss.astraladditions.registry.ModRecipes;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class AstralAdditionsREIClientPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<DesizerDisplay> DESIZER =
            CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "desizer"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new DesizerCategory());
        registry.addWorkstations(DESIZER, EntryStacks.of(ModBlocks.DESIZER_CONTROLLER));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        RecipeManager recipeManager = MinecraftClient.getInstance().getServer().getRecipeManager();
        List<DesizerRecipe> desizerRecipes = recipeManager.listAllOfType(DesizerRecipe.Type.INSTANCE);

        for (DesizerRecipe recipe : desizerRecipes) {
            DesizerDisplay display = DesizerDisplay.of(recipe);
            registry.add(display);
        }
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        REIClientPlugin.super.registerScreens(registry);
    }
}
