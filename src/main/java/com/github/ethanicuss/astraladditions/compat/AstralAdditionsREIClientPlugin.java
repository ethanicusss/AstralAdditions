package com.github.ethanicuss.astraladditions.compat;


import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.compat.desizer.DesizerCategory;
import com.github.ethanicuss.astraladditions.compat.desizer.DesizerDisplay;
import com.github.ethanicuss.astraladditions.compat.transmute.TransmuteCategory;
import com.github.ethanicuss.astraladditions.compat.transmute.TransmuteDisplay;
import com.github.ethanicuss.astraladditions.compat.vacuum.VacuumCategory;
import com.github.ethanicuss.astraladditions.compat.vacuum.VacuumDisplay;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.recipes.DesizerRecipe;
import com.github.ethanicuss.astraladditions.recipes.TransmuteRecipe;
import com.github.ethanicuss.astraladditions.registry.ChromaticVacuumRecipe;
import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import java.util.List;

public class AstralAdditionsREIClientPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<DesizerDisplay> DESIZER = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "desizer"));

    public static final CategoryIdentifier<TransmuteDisplay> TRANSMUTE = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "transmute"));

    public static final CategoryIdentifier<VacuumDisplay> VACUUM = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "vacuum"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new DesizerCategory());
        registry.addWorkstations(DESIZER, EntryStacks.of(ModBlocks.DESIZER_CONTROLLER));

        registry.add(new TransmuteCategory());
        registry.addWorkstations(TRANSMUTE, EntryStacks.of(ModFluids.SHIMMER_BUCKET));

        registry.add(new VacuumCategory());
        registry.addWorkstations(VACUUM, EntryStacks.of(ModItems.CHROMATIC_VACUUM));

    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        RecipeManager recipeManager = MinecraftClient.getInstance().getServer().getRecipeManager();

        List<DesizerDisplay> desizerRecipes = recipeManager.listAllOfType(DesizerRecipe.Type.INSTANCE).stream().map(DesizerDisplay::of).toList();
        desizerRecipes.forEach(registry::add);

        List<TransmuteDisplay> transmuteRecipes = recipeManager.listAllOfType(TransmuteRecipe.Type.INSTANCE).stream().map(TransmuteDisplay::of).toList();
        transmuteRecipes.forEach(registry::add);

        List<VacuumDisplay> vacuumRecipes = recipeManager.listAllOfType(ChromaticVacuumRecipe.Type.INSTANCE).stream().map(VacuumDisplay::of).toList();
        vacuumRecipes.forEach(registry::add);

    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        REIClientPlugin.super.registerScreens(registry);
    }
}
