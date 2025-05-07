package com.github.ethanicuss.astraladditions.compat.rei.yttr.soaking;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.compat.rei.widgets.CurvedArrowWidget;
import com.unascribed.yttr.init.YItems;
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

public class SoakingCategory implements DisplayCategory<SoakingDisplay> {


    @Override
    public Text getTitle() {
        return new TranslatableText("category.astraladditions.soaking");

    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(YItems.VOID_BUCKET);
    }

    @Override
    public CategoryIdentifier<? extends SoakingDisplay> getCategoryIdentifier() {
        return AstralAdditionsREIClientPlugin.SOAKING;
    }

    @Override
    public List<Widget> setupDisplay(SoakingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        List<EntryIngredient> outputs = display.getOutputEntries();
        List<EntryIngredient> catalyst = display.getCatalystEntries();


        int centerX = bounds.getCenterX()+16;
        int centerY = bounds.getCenterY()-4;

        int offsetX = -display.getInputEntries().size()*18;

        for (EntryIngredient in : display.getInputEntries()) {
            widgets.add(Widgets.createSlot(new Point(centerX-34+ offsetX, centerY-20)).entries(in).markInput());
            offsetX += 18;
        }
        widgets.add(Widgets.createSlot(new Point(centerX+5, centerY-20)).entries(outputs.get(0)).markOutput());

        widgets.add(CurvedArrowWidget.of(new Point( centerX-15, centerY-20), 0, false));
        widgets.add(CurvedArrowWidget.of(new Point( centerX-32, centerY-20), 90, false));

        widgets.add(Widgets.createSlotBase(new Rectangle(centerX-33, centerY-1, 34, 18)));
        widgets.add(Widgets.createSlot(new Point(centerX-16, centerY)).entries(catalyst.get(0)).markInput().disableBackground());
        widgets.add(Widgets.createSlot(new Point(centerX-32, centerY)).entries(catalyst.get(0)).markInput().disableBackground());



        return widgets;

    }

    @Override
    public int getDisplayHeight() {
        return 62;
    }

    @Override
    public int getDisplayWidth(SoakingDisplay display) {
        return 89;
    }
}