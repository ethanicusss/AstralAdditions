package com.github.ethanicuss.astraladditions.compat.transmute;

import com.github.ethanicuss.astraladditions.compat.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.recipes.TransmuteRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class TransmuteDisplay extends BasicDisplay {

    private final Identifier id;

    public TransmuteDisplay(Identifier id, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
        this.id = id;

    }

    public Identifier getRecipeId() {
        return id;
    }


    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return AstralAdditionsREIClientPlugin.TRANSMUTE;
    }

    public static TransmuteDisplay of(Recipe<?> recipe) {
        TransmuteRecipe transmuteRecipe = (TransmuteRecipe) recipe;

        List<EntryIngredient> inputs = Collections.singletonList(EntryIngredients.of(transmuteRecipe.getInputItem()));
        List<EntryIngredient> outputs = transmuteRecipe.getOutputItems().stream()
                .map(EntryIngredients::of)
                .toList();

        return new TransmuteDisplay(recipe.getId(), inputs, outputs);
    }
}
