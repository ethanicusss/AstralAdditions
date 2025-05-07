package com.github.ethanicuss.astraladditions.compat.rei.yttr.shattering;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.compat.rei.transmute.TransmuteDisplay;
import com.github.ethanicuss.astraladditions.compat.rei.vacuum.VacuumDisplay;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.init.YEnchantments;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

public class ShatteringCategory implements DisplayCategory<ShatteringDisplay> {


	@Override
	public Text getTitle() {
		return new TranslatableText("category.astraladditions.shattering");
	}

	@Override
	public Renderer getIcon() {
		ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
		EnchantmentHelper.set(ImmutableMap.of(YEnchantments.SHATTERING_CURSE, 1), itemStack);
		return EntryStacks.of(itemStack);

	}

	@Override
	public CategoryIdentifier<? extends ShatteringDisplay> getCategoryIdentifier() {
		return AstralAdditionsREIClientPlugin.SHATTERING;
	}

	@Override
	public List<Widget> setupDisplay(ShatteringDisplay display, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));

		Point startPoint = new Point(bounds.getCenterX() - 40, bounds.getCenterY() - 9);
		if (display.isExclusive()) {
			widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
				int v = ConfigObject.getInstance().isUsingDarkTheme() ? 17 : 0;

				Identifier texture = Yttr.id("textures/gui/shattering.png");

				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderTexture(0, texture);

				DrawableHelper.drawTexture(matrices, startPoint.x + 24, startPoint.y + 1, 0, v, 24, 17, 24, 36);
			}));

		} else {
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 1)));
		}


		widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 60, startPoint.y + 1)).entries(display.getOutputEntries().get(0)).markOutput());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 30;
	}

	@Override
	public int getDisplayWidth(ShatteringDisplay display) {
		return 90;
	}
}