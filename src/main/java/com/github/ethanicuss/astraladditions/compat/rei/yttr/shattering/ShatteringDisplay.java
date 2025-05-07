package com.github.ethanicuss.astraladditions.compat.rei.yttr.shattering;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.crafting.ShatteringRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class ShatteringDisplay extends BasicDisplay {
	private final Identifier id;
	private final boolean exclusive;

	public ShatteringDisplay(Identifier id, List<EntryIngredient> inputs, List<EntryIngredient> outputs, boolean exclusive) {
		super(inputs, outputs);
		this.id = id;
		this.exclusive = exclusive;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AstralAdditionsREIClientPlugin.SHATTERING;
	}

	public static ShatteringDisplay of(Recipe<?> recipe) {
		List<EntryIngredient> inputs = recipe.getIngredients().stream()
				.map(EntryIngredients::ofIngredient)
				.toList();

		List<EntryIngredient> outputs = Collections.singletonList(
				EntryIngredients.of(recipe.getOutput())
		);

		Identifier id;
		boolean exclusive = recipe instanceof ShatteringRecipe;

		if (exclusive) {
			id = recipe.getId();
		} else {
			id = Yttr.id("shattering/" + recipe.getId().getNamespace() + "/" + recipe.getId().getPath());
		}

		return new ShatteringDisplay(id, inputs, outputs, exclusive);

	}
}