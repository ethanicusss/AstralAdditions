package com.github.ethanicuss.astraladditions.compat.rei.vacuum;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.compat.rei.widgets.CurvedArrowWidget;
import com.github.ethanicuss.astraladditions.compat.rei.widgets.RotatedArrowWidget;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Collections;
import java.util.List;

public class VacuumCategory implements DisplayCategory<VacuumDisplay> {

	@Override
	public Text getTitle() {
		return new TranslatableText("category.astraladditions.vacuum");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(ModItems.CHROMATIC_VACUUM);
	}

	@Override
	public CategoryIdentifier<? extends VacuumDisplay> getCategoryIdentifier() {
		return AstralAdditionsREIClientPlugin.VACUUM;
	}

	@Override
	public List<Widget> setupDisplay(VacuumDisplay display, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));

		List<EntryIngredient> inputs = display.getInputEntries();
		List<EntryIngredient> outputs = display.getOutputEntries();

		int centerX = bounds.getCenterX();
		int centerY = bounds.getCenterY();


		widgets.add(Widgets.createSlot(new Point(centerX - 8, centerY - 43))
				.entries(Collections.singletonList(EntryStacks.of(ModItems.CHROMATIC_VACUUM))));

		widgets.add(new RotatedArrowWidget(new Point(centerX - 11, centerY - 20), 90));

		widgets.add(Widgets.createSlot(new Point(centerX - 8, centerY + 3))
				.entries(inputs.get(0))
				.markInput());


		widgets.add(CurvedArrowWidget.of(new Point(centerX + 11, centerY + 9), 90, false));

		if (!display.getRemainderEntries().isEmpty()) {
			widgets.add(CurvedArrowWidget.of(new Point(centerX - 27, centerY + 9), -90, true));

			ItemStack outputRemainder = (ItemStack) display.getRemainderEntries().get(0).get(0).getValue();

			if (!outputRemainder.hasNbt()) {
				outputRemainder.setNbt(new NbtCompound());
			}
			NbtCompound displayTag = outputRemainder.getOrCreateSubNbt("display");

			NbtList lore = new NbtList();
			lore.add(NbtString.of(Text.Serializer.toJson(
					new TranslatableText("category.astraladditions.vacuum.remainder_hint")
							.setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.YELLOW))
			)));
			displayTag.put("Lore", lore);

			widgets.add(Widgets.createSlot(new Point(centerX - 28, centerY + 28))
					.entries(Collections.singletonList(EntryStacks.of(outputRemainder)))
					.markOutput());


		}
		widgets.add(Widgets.createSlot(new Point(centerX + 13, centerY + 28))
				.entries(outputs.get(0))
				.markOutput());


		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 100;
	}

	@Override
	public int getDisplayWidth(VacuumDisplay display) {
		return 90;
	}
}