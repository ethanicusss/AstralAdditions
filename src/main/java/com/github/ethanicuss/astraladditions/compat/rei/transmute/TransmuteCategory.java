package com.github.ethanicuss.astraladditions.compat.rei.transmute;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.compat.rei.widgets.CurvedArrowWidget;
import com.github.ethanicuss.astraladditions.compat.rei.widgets.RotatedArrowWidget;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class TransmuteCategory implements DisplayCategory<TransmuteDisplay> {


    @Override
    public Text getTitle() {
        return new TranslatableText("category.astraladditions.transmute");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModFluids.SHIMMER_BUCKET);
    }

    @Override
    public CategoryIdentifier<? extends TransmuteDisplay> getCategoryIdentifier() {
        return AstralAdditionsREIClientPlugin.TRANSMUTE;
    }

    @Override
    public List<Widget> setupDisplay(TransmuteDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> outputs = display.getOutputEntries();


        int centerX = bounds.getCenterX();
        int centerY = bounds.getCenterY();

        widgets.add(Widgets.createSlot(new Point(centerX - 75, centerY-45))
                .entries(inputs.get(0))
                .markInput());

        widgets.add(CurvedArrowWidget.of(new Point(centerX - 54, centerY-38),90, false));

        Rectangle renderBounds = new Rectangle(centerX - 61, centerY-18, 32, 32);

        widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> {
            EntryStacks.of(ModFluids.FLOWING_SHIMMER).render(matrices, renderBounds, mouseX, mouseY, delta);
                })));

        widgets.add(Widgets.createTooltip(renderBounds, new TranslatableText("tooltip.astraladditions.transmute_hint")));

        widgets.add(CurvedArrowWidget.of(new Point(centerX - 46, centerY+19),180, true));

        widgets.add(Widgets.createLabel(new Point(centerX - 75,centerY+42), display.getHintText())
                .leftAligned());

        int[] xOffsets = {-25, -5, 15, 35};
        int[] yOffsets = {19, -1, -21, -41};

        int index = 0;
        for (int xOffset : xOffsets) {
            for (int yOffset : yOffsets) {
                if (index < outputs.size()) {
                    widgets.add(Widgets.createSlot(new Point(centerX + xOffset, centerY + yOffset))
                            .entries(outputs.get(index))
                            .markOutput());
                    index++;
                }
            }
        }
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 110;

    }

    @Override
    public int getDisplayWidth(TransmuteDisplay TransmuteDisplay) {
        return 160;
    }
}