package com.github.ethanicuss.astraladditions.compat.rei.yttr.soaking;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.mojang.datafixers.util.Either;
import com.unascribed.yttr.crafting.SoakingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class SoakingDisplay extends BasicDisplay {
	private final Identifier id;
	private final List<EntryIngredient> catalystEntries;

	public SoakingDisplay(Identifier id, List<EntryIngredient> inputs, List<EntryIngredient> outputs, List<EntryIngredient> catalystEntries) {
		super(inputs, outputs);
		this.id = id;
		this.catalystEntries = catalystEntries;
	}


	public List<EntryIngredient> getCatalystEntries() {
		return catalystEntries;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AstralAdditionsREIClientPlugin.SOAKING;
	}

	public static SoakingDisplay of(Recipe<?> recipe) {
		SoakingRecipe soakingRecipe = (SoakingRecipe) recipe;

		List<EntryIngredient> inputs;
		Either<ItemStack, DefaultedList<Ingredient>> soakingIngredients = soakingRecipe.getSoakingIngredients();

		if (soakingIngredients.left().isPresent()) {
			ItemStack stack = soakingIngredients.left().get();
			inputs = List.of(EntryIngredients.of(stack));
		} else {
			DefaultedList<Ingredient> ingredientList = soakingIngredients.right().orElse(DefaultedList.of());
			if (!ingredientList.isEmpty()) {
				Ingredient ingredient = ingredientList.get(0);
				ItemStack[] matchingStacks = ingredient.getMatchingStacks();
				if (matchingStacks.length > 0) {
					ItemStack stack = matchingStacks[0].copy();
					inputs = List.of(EntryIngredients.of(stack));
				} else {
					inputs = List.of();
				}
			} else {
				inputs = List.of();
			}
		}

		ItemStack outputStack = soakingRecipe.getResult().map(
				itemStack -> itemStack,
				blockState -> new ItemStack(blockState.getBlock())
		);

		List<EntryIngredient> outputs = List.of(
				EntryIngredients.of(outputStack)
		);

		List<EntryIngredient> catalystEntries = List.of(
				EntryIngredient.of(EntryStacks.of(soakingRecipe.getCatalyst().getMatchingFluids().get(0)))
		);

		return new SoakingDisplay(recipe.getId(), inputs, outputs, catalystEntries);
	}
}