package com.github.ethanicuss.astraladditions.compat;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.recipes.DesizerRecipe;
import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import com.github.ethanicuss.astraladditions.registry.ModRecipes;
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

import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DesizerCategory implements DisplayCategory<DesizerDisplay> {

    private static final int GRID_ROWS = 3;
    private static final int GRID_COLUMNS = 3;
    private static final int CELL_SIZE = 20;

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.DESIZER_CONTROLLER);
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("category.astraladditions.desizer");
    }

    @Override
    public CategoryIdentifier<? extends DesizerDisplay> getCategoryIdentifier() {
        return AstralAdditionsREIClientPlugin.DESIZER;
    }

    private void drawGrid(List<Widget> gridWidgets, List<EntryIngredient> items, int layer, int startX, int startY) {
        gridWidgets.clear();
        int startIndex = (layer - 1) * 9;
        int endIndex = Math.min(startIndex + 9, items.size());
        List<EntryIngredient> currentItems = items.subList(startIndex, endIndex);

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                int itemIndex = row * GRID_COLUMNS + col;
                if (itemIndex >= currentItems.size()) return;

                EntryIngredient entry = currentItems.get(itemIndex);
                int x = startX + col * CELL_SIZE - 40;
                int y = startY + row * CELL_SIZE - 10;

                boolean isBedrock = entry.stream()
                        .anyMatch(stack -> EntryStacks.equalsExact(stack, EntryStacks.of(Items.BEDROCK)));

                if (isBedrock) {
                    gridWidgets.add(Widgets.createSlot(new Point(x, y)));
                } else {
                    gridWidgets.add(Widgets.createSlot(new Point(x, y)).entries(entry));
                }
            }
        }
    }

    @Override
    public List<Widget> setupDisplay(DesizerDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));

        AtomicInteger currentLayer = new AtomicInteger(1);
        int gridWidth = GRID_COLUMNS * CELL_SIZE;
        int gridHeight = GRID_ROWS * CELL_SIZE;
        int startX = bounds.x + (bounds.width - gridWidth) / 2;
        int startY = bounds.y + (bounds.height - gridHeight) / 2;
        int buttonY = bounds.y + bounds.height - 25;

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> outputs = display.getOutputEntries();
        List<Widget> gridWidgets = Lists.newArrayList();

        drawGrid(gridWidgets, inputs, currentLayer.get(), startX, startY);
        widgets.addAll(gridWidgets);

        Widget[] layerLabel = {createLayerLabel(startX, buttonY, currentLayer.get())};
        widgets.add(layerLabel[0]);

        widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.width / 2 + 20, startY + 10)).entries(outputs.get(0)));
        widgets.add(Widgets.createArrow(new Point(bounds.x + bounds.width / 2 - 10, startY + 10)));

        widgets.add(Widgets.createButton(new Rectangle(startX - 40, buttonY, 16, 16), Text.of("<"))
                .onClick(button -> {
                    if (currentLayer.get() > 1) {
                        currentLayer.decrementAndGet();
                        updateLayerLabelAndGrid(currentLayer.get(), inputs, layerLabel, gridWidgets, widgets, startX, startY, buttonY);
                    }
                }));


        widgets.add(Widgets.createButton(new Rectangle(startX, buttonY, 16, 16), Text.of(">"))
                .onClick(button -> {
                    if (currentLayer.get() < 3) {
                        currentLayer.incrementAndGet();
                        updateLayerLabelAndGrid(currentLayer.get(), inputs, layerLabel, gridWidgets, widgets, startX, startY, buttonY);
                    }
                }));

        return widgets;
    }

    private void updateLayerLabelAndGrid(int newLayer, List<EntryIngredient> inputs, Widget[] layerLabel,
                                         List<Widget> gridWidgets, List<Widget> widgets, int startX, int startY, int buttonY) {
        
        widgets.remove(layerLabel[0]);
        layerLabel[0] = createLayerLabel(startX, buttonY, newLayer);
        widgets.add(layerLabel[0]);

        widgets.removeAll(gridWidgets);
        gridWidgets.clear();
        drawGrid(gridWidgets, inputs, newLayer, startX, startY);
        widgets.addAll(gridWidgets);
    }

    private Widget createLayerLabel(int startX, int buttonY, int layer) {
        return Widgets.createLabel(new Point(startX - 11.1, buttonY + 3.5), Text.of(String.valueOf(layer)))
                .noShadow()
                .color(0xFFFFFF, 0xAAAAAA);
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }
}
