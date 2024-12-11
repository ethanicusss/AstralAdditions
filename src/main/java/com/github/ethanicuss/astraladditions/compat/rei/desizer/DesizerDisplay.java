package com.github.ethanicuss.astraladditions.compat.rei.desizer;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class DesizerDisplay extends BasicDisplay {
    public static final Logger LOGGER = LoggerFactory.getLogger(AstralAdditions.MOD_ID);
    private final Identifier id;

    public DesizerDisplay(Identifier id, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
        this.id = id;

    }


    public Identifier getRecipeId() {
        return id;
    }


    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return AstralAdditionsREIClientPlugin.DESIZER;
    }

    public static DesizerDisplay of(Recipe<?> recipe) {
        List<EntryIngredient> inputs = EntryIngredients.ofIngredients(recipe.getIngredients());
        List<EntryIngredient> outputs = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));


        return new DesizerDisplay(recipe.getId(), inputs, outputs);
    }
}
