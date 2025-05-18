package com.github.ethanicuss.astraladditions.compat.rei.yttr.pistonsmashing;

import com.github.ethanicuss.astraladditions.compat.rei.AstralAdditionsREIClientPlugin;
import com.github.ethanicuss.astraladditions.compat.rei.widgets.BlockFaceWidget;
import com.github.ethanicuss.astraladditions.compat.rei.widgets.CurvedArrowWidget;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;


public class PistonSmashingCategory implements DisplayCategory<PistonSmashingDisplay> {

    @Override
    public Text getTitle() {
        return new TranslatableText("category.astraladditions.piston_smashing");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.PISTON);
    }


    @Override
    public CategoryIdentifier<? extends PistonSmashingDisplay> getCategoryIdentifier() {
        return AstralAdditionsREIClientPlugin.PISTON_SMASHING;
    }

    @Override
    public List<Widget> setupDisplay(PistonSmashingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));

        int centerX = bounds.getCenterX();
        int centerY = bounds.getCenterY()+5;

        int leftStartX = centerX - 45;
        int outputX = centerX + 17;

        Function<Block, List<Text>> createTooltip = (block) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ItemStack stack = new ItemStack(block);

            TooltipContext tooltipContext = () -> client.options.advancedItemTooltips;
            List<Text> tooltip = stack.getTooltip(client.player, tooltipContext);

            String modId = Registry.BLOCK.getId(block).getNamespace();
            String modName = FabricLoader.getInstance()
                    .getModContainer(modId)
                    .map(container -> container.getMetadata().getName())
                    .orElse(modId);

            tooltip.add(new LiteralText(modName).formatted(Formatting.BLUE));

            return tooltip;
        };

        widgets.add(Widgets.withTooltip(
                BlockFaceWidget.of(() -> Blocks.PISTON, new Point(leftStartX, centerY), Direction.NORTH, 90, false),
                createTooltip.apply(Blocks.PISTON)
        ));

        widgets.add(Widgets.withTooltip(
                BlockFaceWidget.of(display::getCatalystBlock, new Point(leftStartX + 16, centerY), Direction.NORTH, 90, false),
                createTooltip.apply(display.getCatalystBlock())
        ));

        widgets.add(Widgets.withTooltip(
                BlockFaceWidget.of(display::getInputBlock, new Point(leftStartX + 32, centerY), Direction.NORTH, 90, false),
                createTooltip.apply(display.getInputBlock())
        ));

        widgets.add(Widgets.withTooltip(
                BlockFaceWidget.of(display::getCatalystBlock, new Point(leftStartX + 48, centerY), Direction.NORTH, 90, false),
                createTooltip.apply(display.getCatalystBlock())
        ));

        widgets.add(CurvedArrowWidget.of(new Point(centerX - 6, centerY - 20), 0, false));


        if (display.hasCloud()) {
            final int cloudX = outputX;
            widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/particle/effect_4.png"));

                int color = display.getCloudColor();
                float red = ((color >> 16) & 0xFF) / 255f;
                float green = ((color >> 8) & 0xFF) / 255f;
                float blue = (color & 0xFF) / 255f;

                RenderSystem.setShaderColor(red, green, blue, 1.0f);
                DrawableHelper.drawTexture(matrices, cloudX - 5, centerY - 16, 0f, 0f, 8, 8, 8, 8);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }));

            outputX += 8;

            ItemStack output = ((ItemStack) display.getCloudOutput().getValue()).copy();
            output.setCount(display.getCloudSize());
            if (!output.hasNbt()) {
                output.setNbt(new NbtCompound());
            }
            NbtCompound displayTag = output.getOrCreateSubNbt("display");

            NbtList lore = new NbtList();
            lore.add(NbtString.of(Text.Serializer.toJson(
                    new TranslatableText("category.astraladditions.piston_smashing.cloud_output_hint")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.YELLOW))
            )));
            displayTag.put("Lore", lore);

            widgets.add(Widgets.createSlot(new Point(outputX, centerY - 22))
                    .entries(Collections.singletonList(EntryStacks.of(output)))
                    .markOutput()
            );

            outputX += 20;
        } else {
            widgets.add(Widgets.createSlot(new Point(outputX, centerY - 22))
                    .entries(display.getOutputEntries().get(0))
                    .markOutput()
            );
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 55;
    }

    @Override
    public int getDisplayWidth(PistonSmashingDisplay display) {
        return 100;
    }


}