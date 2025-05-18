package com.github.ethanicuss.astraladditions.compat.rei.yttr.centrifuging;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.init.YItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;

import java.util.List;


public class CentrifugeCategory implements DisplayCategory<CentrifugeDisplay> {

	@Override
	public Text getTitle() {
		return new TranslatableText("category.astraladditions.centrifuge");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(YItems.CENTRIFUGE);
	}

	@Override
	public CategoryIdentifier<? extends CentrifugeDisplay> getCategoryIdentifier() {
		return AstralAdditionsREIClientPlugin.CENTRIFUGE;
	}

	@Override
	public List<Widget> setupDisplay(CentrifugeDisplay display, Rectangle bounds) {
		List<Widget> widgets = Lists.newArrayList();


		int offsetX = -1;
		int offsetY = -4;
		int layoutWidth = 94;
		int layoutHeight = 100;

		int originX = bounds.getCenterX() - (layoutWidth / 2);
		int originY = bounds.getCenterY() - (layoutHeight / 2);

		List<EntryIngredient> inputs = display.getInputEntries();
		List<EntryIngredient> outputs = display.getOutputEntries();

		widgets.add(Widgets.createRecipeBase(bounds));


		widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
			Identifier texture = ConfigObject.getInstance().isUsingDarkTheme()
					? new Identifier(AstralAdditions.MOD_ID, "textures/gui/reicompat/centrifuge_dark.png")
					: Yttr.id("textures/gui/centrifuge.png");

			RenderSystem.setShaderTexture(0, texture);
			DrawableHelper.drawTexture(
					matrices,
					originX, originY,
					41, 5,
					layoutWidth, layoutHeight,
					256, 256
			);
		}));


		Point inputPos = new Point(originX + (38 - offsetX), originY + (39 - offsetY));

		Point[] outputPositions = {
				new Point(originX + (-offsetX) + 4, originY + (31 - offsetY) + 4),
				new Point(originX + (38 - offsetX) + 4, originY + (-offsetY) + 4),
				new Point(originX + (68 - offsetX) + 4, originY + (39 - offsetY) + 4),
				new Point(originX + (30 - offsetX) + 4, originY + (69 - offsetY) + 4)
		};

		widgets.add(Widgets.createSlot(inputPos)
				.entries(inputs.get(0))
				.markInput()
				.disableBackground());

		for (int i = 0; i < 4; i++) {
			EntryIngredient outputEntry = i < outputs.size()
					? outputs.get(i)
					: EntryIngredient.of(EntryStacks.of(ItemStack.EMPTY));

			widgets.add(Widgets.createSlot(outputPositions[i])
					.entries(outputEntry)
					.markOutput()
					.disableBackground());
		}

		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 110;
	}

	@Override
	public int getDisplayWidth(CentrifugeDisplay display) {
		return 110;
	}
}