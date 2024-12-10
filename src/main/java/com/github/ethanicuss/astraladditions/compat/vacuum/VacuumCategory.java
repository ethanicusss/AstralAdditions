package com.github.ethanicuss.astraladditions.compat.vacuum;

import com.github.ethanicuss.astraladditions.compat.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.compat.widgets.RotatedArrowWidget;
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
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

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


        widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.width/ 2 - 8, bounds.y+5))
                .entries(Collections.singletonList(EntryStacks.of(ModItems.CHROMATIC_VACUUM))));

        widgets.add(new RotatedArrowWidget(new Point(bounds.x + bounds.width/ 2 -11, bounds.y+30), 90));

        Rectangle inputRenderBounds = new Rectangle(bounds.x + bounds.width/ 2 - 7, bounds.y + 55, 16, 16);

        //?Input Block
        widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> {
            EntryStack<?> entryStack = inputs.get(0).get(0);
            if (entryStack.getValue() instanceof ItemStack stack && stack.getItem() instanceof BlockItem blockItem) {
                EntryStacks.of(blockItem.getBlock()).render(matrices, inputRenderBounds, mouseX, mouseY, delta);
            } else {
                entryStack.render(matrices, inputRenderBounds, mouseX, mouseY, delta);
            }

        })));

        //? Remainder Arrow and Block
        if (!display.getRemainderEntries().isEmpty()) {
            widgets.add(new RotatedArrowWidget(new Point(bounds.x + bounds.width/ 2 - 32, bounds.y+55), 180));
            Rectangle renderBounds = new Rectangle(bounds.x + bounds.width/ 2 - 50, bounds.y + 55, 16, 16);

            widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> {
                EntryStack<?> entryStack = display.getRemainderEntries().get(0).get(0);

                if (entryStack.getValue() instanceof ItemStack stack && stack.getItem() instanceof BlockItem blockItem) {
                    EntryStacks.of(blockItem.getBlock()).render(matrices, renderBounds, mouseX, mouseY, delta);
                } else {
                    entryStack.render(matrices, renderBounds, mouseX, mouseY, delta);
                }

            })));

        }
        //? Output Arrow and Box
        widgets.add(Widgets.createArrow(new Point(bounds.x + bounds.width/ 2 + 10, bounds.y + 54)));
        widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.width/ 2 + 38, bounds.y + 54)).entries(outputs.get(0)));



        return widgets;
    }

    @Override
    public int getDisplayHeight() {
            return 80;


    }

}
