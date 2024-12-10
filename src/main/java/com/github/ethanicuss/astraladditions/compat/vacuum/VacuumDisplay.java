package com.github.ethanicuss.astraladditions.compat.vacuum;

import com.github.ethanicuss.astraladditions.compat.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.registry.ChromaticVacuumRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class VacuumDisplay extends BasicDisplay {

    private final Identifier id;
    private final List<EntryIngredient> remainder;

    public VacuumDisplay(Identifier id, List<EntryIngredient> inputs, List<EntryIngredient> outputs, List<EntryIngredient> remainder) {
        super(inputs, outputs);
        this.id = id;
        this.remainder = remainder;

    }

    public Identifier getRecipeId() {
        return id;
    }

    public List<EntryIngredient> getRemainderEntries() {
        return remainder;
    }
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return AstralAdditionsREIClientPlugin.VACUUM;
    }

    public static VacuumDisplay of(Recipe<?> recipe) {

        ChromaticVacuumRecipe chromaticRecipe = (ChromaticVacuumRecipe) recipe;

        List<EntryIngredient> ingredient = Collections.singletonList(
                EntryIngredients.of(chromaticRecipe.getIngredient())
        );

        List<EntryIngredient> output = Collections.singletonList(
                EntryIngredients.of(chromaticRecipe.getOutput())
        );

        List<EntryIngredient> remainder = chromaticRecipe.hasRemainder()
                ? Collections.singletonList(EntryIngredients.of(chromaticRecipe.getRemainder()))
                : Collections.emptyList();

        return new VacuumDisplay(recipe.getId(), ingredient, output, remainder);
    }
}
