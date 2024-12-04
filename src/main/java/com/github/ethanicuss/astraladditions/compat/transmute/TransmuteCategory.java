package com.github.ethanicuss.astraladditions.compat.transmute;

import com.github.ethanicuss.astraladditions.compat.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.compat.widgets.RotatedArrowWidget;
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
    boolean makeBigger = false;
    @Override
    public List<Widget> setupDisplay(TransmuteDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> outputs = display.getOutputEntries();


        widgets.add(new RotatedArrowWidget(new Point(bounds.x + 1, bounds.y+30), 90));

        widgets.add(Widgets.createSlot(new Point(bounds.x + 5, bounds.y+5)).entries(inputs.get(0)));

        Rectangle renderBounds = new Rectangle(bounds.x + 5, bounds.y + 55, 16, 16);

        widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> {
            EntryStacks.of(ModFluids.FLOWING_SHIMMER).render(matrices, renderBounds, mouseX, mouseY, delta);
                })));

        widgets.add(Widgets.createTooltip(renderBounds, new TranslatableText("tooltip.astraladditions.transmute_text")));

        widgets.add(Widgets.createArrow(new Point(bounds.x + 5 + 20, bounds.y+55)));




        int baseX = bounds.x + 55;
        int baseY = bounds.y + 55;
        int outputSize = outputs.size();
        //? math, me head hurts
        for (int i = 0; i < outputSize; i++) {
            if (outputSize < 4) {
                makeBigger = false;
                widgets.add(Widgets.createSlot(new Point(baseX, baseY - (i * 20))).entries(outputs.get(i)));
            } else if (outputSize == 4) {
                makeBigger = true;
                widgets.add(Widgets.createSlot(new Point(baseX, baseY - (i * 20) + 20)).entries(outputs.get(i)));
            } else {
                int column = i / 4;
                int row = i % 4;
                makeBigger = true;
                widgets.add(Widgets.createSlot(new Point(baseX + (column * 20), baseY - (row * 20) + 20)).entries(outputs.get(i)));
            }
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        if (makeBigger) {
            return 100;
        }
        else return 80;

    }

    @Override
    public int getDisplayWidth(TransmuteDisplay TransmuteDisplay) {
        return 146;
    }
}
