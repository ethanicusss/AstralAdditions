package com.github.ethanicuss.astraladditions.compat.rei.yttr.voidfiltering;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.unascribed.yttr.crafting.VoidFilteringRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class VoidFilteringDisplay extends BasicDisplay {
	private final Identifier id;
	private final float chance;

	public VoidFilteringDisplay(Identifier id, List<EntryIngredient> outputs, float chance) {
		super(Collections.emptyList(), outputs);
		this.id = id;
		this.chance = chance;
	}

	public float getChance() {
		return chance;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AstralAdditionsREIClientPlugin.VOID_FILTERING;
	}

	public static VoidFilteringDisplay of(VoidFilteringRecipe recipe) {
		EntryIngredient output = EntryIngredients.of(recipe.getOutput());
		return new VoidFilteringDisplay(recipe.getId(), Collections.singletonList(output), recipe.getChance());
	}
}