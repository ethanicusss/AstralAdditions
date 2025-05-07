package com.github.ethanicuss.astraladditions.compat.rei.transmute;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.recipes.TransmuteRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class TransmuteDisplay extends BasicDisplay {

    private final Identifier id;
    private final TransmuteRecipe recipe;

    public TransmuteDisplay(Identifier id, List<EntryIngredient> inputs, List<EntryIngredient> outputs, TransmuteRecipe recipe) {
        super(inputs, outputs);
        this.id = id;
        this.recipe = recipe;
    }

    public Identifier getRecipeId() {
        return id;
    }

    public TransmuteRecipe getRecipe() {
        return recipe;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return AstralAdditionsREIClientPlugin.TRANSMUTE;
    }

    public int getCount() {
        return recipe.getInputItem().getCount();
    }

    public Text getHintText() {
        if (recipe.isIgnoreCount()) {
            return new TranslatableText("label.astraladditions.transmute_hint_any");
        } else if (recipe.isSoftIgnoreCount()) {
            return new TranslatableText("label.astraladditions.transmute_hint_least", getCount());
        } else {
            return new TranslatableText("label.astraladditions.transmute_hint_exactly", getCount());
        }
    }

    public static TransmuteDisplay of(Recipe<?> recipe) {
        TransmuteRecipe transmuteRecipe = (TransmuteRecipe) recipe;

        List<EntryIngredient> inputs = Collections.singletonList(EntryIngredients.of(transmuteRecipe.getInputItem()));
        List<EntryIngredient> outputs = transmuteRecipe.getOutputItems().stream()
                .map(EntryIngredients::of)
                .toList();

        return new TransmuteDisplay(recipe.getId(), inputs, outputs, transmuteRecipe);
    }
}