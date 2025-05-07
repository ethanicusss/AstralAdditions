package com.github.ethanicuss.astraladditions.compat.rei;


import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.compat.rei.desizer.DesizerCategory;
import com.github.ethanicuss.astraladditions.compat.rei.desizer.DesizerDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.transmute.TransmuteCategory;
import com.github.ethanicuss.astraladditions.compat.rei.transmute.TransmuteDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.vacuum.VacuumCategory;
import com.github.ethanicuss.astraladditions.compat.rei.vacuum.VacuumDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.centrifuging.CentrifugeCategory;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.centrifuging.CentrifugeDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.pistonsmashing.PistonSmashingCategory;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.pistonsmashing.PistonSmashingDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.shattering.ShatteringCategory;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.shattering.ShatteringDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.soaking.SoakingCategory;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.soaking.SoakingDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.voidfiltering.VoidFilteringCategory;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.voidfiltering.VoidFilteringDisplay;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.recipes.DesizerRecipe;
import com.github.ethanicuss.astraladditions.recipes.TransmuteRecipe;
import com.github.ethanicuss.astraladditions.registry.ChromaticVacuumRecipe;
import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import com.unascribed.yttr.init.YEnchantments;
import com.unascribed.yttr.init.YItems;
import com.unascribed.yttr.init.YRecipeTypes;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class AstralAdditionsREIClientPlugin implements REIClientPlugin {
	public static final CategoryIdentifier<DesizerDisplay> DESIZER = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "desizer"));

	public static final CategoryIdentifier<TransmuteDisplay> TRANSMUTE = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "transmute"));

	public static final CategoryIdentifier<VacuumDisplay> VACUUM = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "vacuum"));

	//* YTTR
	public static final CategoryIdentifier<CentrifugeDisplay> CENTRIFUGE = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "centrifuge"));
	public static final CategoryIdentifier<PistonSmashingDisplay> PISTON_SMASHING = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "piston_smashing"));
	public static final CategoryIdentifier<SoakingDisplay> SOAKING = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "soaking"));
	public static final CategoryIdentifier<VoidFilteringDisplay> VOID_FILTERING = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "void_filtering"));
	public static final CategoryIdentifier<ShatteringDisplay> SHATTERING = CategoryIdentifier.of(new Identifier(AstralAdditions.MOD_ID, "shattering"));

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new DesizerCategory());
		registry.addWorkstations(DESIZER, EntryStacks.of(ModBlocks.DESIZER_CONTROLLER));

		registry.add(new TransmuteCategory());
		registry.addWorkstations(TRANSMUTE, EntryStacks.of(ModFluids.SHIMMER_BUCKET));

		registry.add(new VacuumCategory());
		registry.addWorkstations(VACUUM, EntryStacks.of(ModItems.CHROMATIC_VACUUM));

		//* YTTR
		registry.add(new CentrifugeCategory());
		registry.addWorkstations(CENTRIFUGE, EntryStacks.of(YItems.CENTRIFUGE));

		registry.add(new PistonSmashingCategory());
		registry.addWorkstations(PISTON_SMASHING, EntryStacks.of(Items.PISTON));
		registry.addWorkstations(PISTON_SMASHING, EntryStacks.of(Items.STICKY_PISTON));

		registry.add(new SoakingCategory());

		registry.add(new VoidFilteringCategory());
		registry.addWorkstations(VOID_FILTERING, EntryStacks.of(YItems.VOID_FILTER));


		registry.add(new ShatteringCategory());
		ItemStack shatteringBook = new ItemStack(Items.ENCHANTED_BOOK);
		EnchantmentHelper.set(Map.of(YEnchantments.SHATTERING_CURSE, 1), shatteringBook);

		NbtList lore = new NbtList();
		lore.add(NbtString.of(Text.Serializer.toJson(
				new TranslatableText("category.astraladditions.shattering.workstations").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.YELLOW))
		)));
		NbtCompound displayTag = shatteringBook.getOrCreateSubNbt("display");
		displayTag.put("Lore", lore);

		registry.addWorkstations(SHATTERING, EntryStacks.of(shatteringBook));

	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		RecipeManager recipeManager = MinecraftClient.getInstance().world.getRecipeManager();

		List<DesizerDisplay> desizerRecipes = recipeManager.listAllOfType(DesizerRecipe.Type.INSTANCE).stream().map(DesizerDisplay::of).toList();
		desizerRecipes.forEach(registry::add);

		List<TransmuteDisplay> transmuteRecipes = recipeManager.listAllOfType(TransmuteRecipe.Type.INSTANCE).stream().map(TransmuteDisplay::of).toList();
		transmuteRecipes.forEach(registry::add);

		List<VacuumDisplay> vacuumRecipes = recipeManager.listAllOfType(ChromaticVacuumRecipe.Type.INSTANCE).stream().map(VacuumDisplay::of).toList();
		vacuumRecipes.forEach(registry::add);

		//* YTTR
		List<CentrifugeDisplay> centrifugeRecipes = recipeManager.listAllOfType(YRecipeTypes.CENTRIFUGING).stream().map(CentrifugeDisplay::of).toList();
		centrifugeRecipes.forEach(registry::add);

		List<PistonSmashingDisplay> pistonSmashingRecipes = recipeManager.listAllOfType(YRecipeTypes.PISTON_SMASHING).stream().map(PistonSmashingDisplay::of).toList();
		pistonSmashingRecipes.forEach(registry::add);

		List<SoakingDisplay> soakingRecipes = recipeManager.listAllOfType(YRecipeTypes.SOAKING).stream().map(SoakingDisplay::of).toList();
		soakingRecipes.forEach(registry::add);

		List<VoidFilteringDisplay> voidFilteringRecipe = recipeManager.listAllOfType(YRecipeTypes.VOID_FILTERING).stream().map(VoidFilteringDisplay::of).toList();
		voidFilteringRecipe.forEach(registry::add);

		//Shattering stuff
		List<ShatteringDisplay> shatteringRecipes = recipeManager.listAllOfType(YRecipeTypes.SHATTERING).stream()
				.map(ShatteringDisplay::of)
				.toList();
		shatteringRecipes.forEach(registry::add);

		List<ShatteringDisplay> shatteringstonecuttingRecipes = recipeManager.listAllOfType(RecipeType.STONECUTTING).stream()
				.filter(r -> r.getOutput().getCount() == 1 && !r.getIngredients().isEmpty())
				.map(ShatteringDisplay::of)
				.toList();
		shatteringstonecuttingRecipes.forEach(registry::add);

		List<ShatteringDisplay> shatteringoneByOneCraftingRecipes = recipeManager.listAllOfType(RecipeType.CRAFTING).stream()
				.filter(r -> r.fits(1, 1) && !r.getIngredients().isEmpty())
				.map(ShatteringDisplay::of)
				.toList();
		shatteringoneByOneCraftingRecipes.forEach(registry::add);
	}


	@Override
	public void registerScreens(ScreenRegistry registry) {
		REIClientPlugin.super.registerScreens(registry);
	}
}