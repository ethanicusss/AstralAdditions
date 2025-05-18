package com.github.ethanicuss.astraladditions.compat.rei.yttr.voidfiltering;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.compat.rei.transmute.TransmuteDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.vacuum.VacuumDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.yttr.soaking.SoakingDisplay;
import com.unascribed.yttr.init.YBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class VoidFilteringCategory implements DisplayCategory<VoidFilteringDisplay> {

	@Override
	public Text getTitle() {
		return new TranslatableText("category.astraladditions.voidfiltering");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(YBlocks.VOID_FILTER);
	}

	@Override
	public CategoryIdentifier<? extends VoidFilteringDisplay> getCategoryIdentifier() {
		return AstralAdditionsREIClientPlugin.VOID_FILTERING;
	}

	@Override
	public List<Widget> setupDisplay(VoidFilteringDisplay display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));

		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 18);

		List<EntryIngredient> outputs = display.getOutputEntries();

		widgets.add(Widgets.createSlot(new Point(bounds.x + 6, startPoint.y + 10))
				.entries(outputs.get(0))
				.markOutput());

		widgets.add(Widgets.createLabel(new Point(bounds.x + 26, bounds.getMaxY() - 17),
				new TranslatableText("category.astraladditions.void_filtering.chance", display.getChance()))
				.leftAligned());

		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 26;

	}

	@Override
	public int getDisplayWidth(VoidFilteringDisplay display) {
		return 113;
	}

}