package com.github.ethanicuss.astraladditions.compat.rei.yttr.pistonsmashing;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.unascribed.yttr.crafting.PistonSmashingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collections;
import java.util.List;

public class PistonSmashingDisplay extends BasicDisplay {

	private final PistonSmashingRecipe recipe;


	private final Identifier id;
	private final boolean hasCloud;
	private final int cloudColor;
	private final int cloudSize;
	private final EntryStack<?> cloudOutput;
	private final List<StatusEffectInstance> cloudEffects;

	private final Block inputBlock;
	private final Block catalystBlock;

	public PistonSmashingDisplay(Identifier id, List<EntryIngredient> inputs, List<EntryIngredient> outputs,
								 boolean hasCloud, int cloudColor, int cloudSize, EntryStack<?> cloudOutput, List<StatusEffectInstance> cloudEffects,
								 Block inputBlock, Block catalystBlock, PistonSmashingRecipe recipe) {
		super(inputs, outputs);
		this.id = id;
		this.hasCloud = hasCloud;
		this.cloudColor = cloudColor;
		this.cloudSize = cloudSize;
		this.cloudOutput = cloudOutput;
		this.cloudEffects = cloudEffects;
		this.inputBlock = inputBlock;
		this.catalystBlock = catalystBlock;
		this.recipe = recipe;
	}

	public Block getInputBlock() {
		List<Block> matching = Registry.BLOCK.stream()
				.filter(block -> recipe.getInput().test(block.getDefaultState().getBlock()))
				.toList();
		if (matching.isEmpty()) return Blocks.AIR;
		int index = (int) ((System.currentTimeMillis() / 1000) % matching.size());
		return matching.get(index);
	}

	public Block getCatalystBlock() {
		List<Block> matching = Registry.BLOCK.stream()
				.filter(block -> recipe.getCatalyst().test(block.getDefaultState().getBlock()))
				.toList();
		if (matching.isEmpty()) return Blocks.AIR;
		int index = (int) ((System.currentTimeMillis() / 1000) % matching.size());
		return matching.get(index);
	}

	public boolean hasCloud() {
		return hasCloud;
	}

	public int getCloudColor() {
		return cloudColor;
	}

	public int getCloudSize() {
		return cloudSize;
	}

	public EntryStack<?> getCloudOutput() {
		return cloudOutput;
	}

	public List<StatusEffectInstance> getCloudEffects() {
		return cloudEffects;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AstralAdditionsREIClientPlugin.PISTON_SMASHING;
	}

	public static PistonSmashingDisplay of(Recipe<?> recipe) {
		PistonSmashingRecipe pistonRecipe = (PistonSmashingRecipe) recipe;

		List<EntryIngredient> input = Collections.singletonList(
				EntryIngredient.of(
						Registry.BLOCK.stream()
								.filter(block -> pistonRecipe.getInput().test(block.getDefaultState().getBlock()))
								.map(block -> EntryStacks.of(new ItemStack(block)))
								.toList()
				)
		);

		List<EntryIngredient> output = Collections.singletonList(
				EntryIngredients.of(pistonRecipe.getOutput())
		);

		EntryStack<?> cloudOutput = EntryStacks.of(pistonRecipe.getCloudOutput());

		Block inputBlock = Registry.BLOCK.stream()
				.filter(block -> pistonRecipe.getInput().test(block.getDefaultState().getBlock()))
				.findFirst()
				.orElse(Blocks.AIR);

		Block catalystBlock = Registry.BLOCK.stream()
				.filter(block -> pistonRecipe.getCatalyst().test(block.getDefaultState().getBlock()))
				.findFirst()
				.orElse(Blocks.AIR);

		return new PistonSmashingDisplay(recipe.getId(), input,
				output,
				pistonRecipe.hasCloud(),
				pistonRecipe.getCloudColor(),
				pistonRecipe.getCloudSize(),
				cloudOutput,
				pistonRecipe.getCloudEffects(),
				inputBlock,
				catalystBlock,
				(PistonSmashingRecipe) recipe
		);
	}
}