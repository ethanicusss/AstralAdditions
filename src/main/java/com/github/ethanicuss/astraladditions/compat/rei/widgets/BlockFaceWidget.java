package com.github.ethanicuss.astraladditions.compat.rei.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.function.Supplier;

public class BlockFaceWidget extends WidgetWithBounds {
	private final Supplier<Block> blockSupplier;
	private final Point position;
	private final Direction side; //kinda forgot about this part
	private final int rotationDegrees;
	private final boolean showTooltip;

	public BlockFaceWidget(Supplier<Block> blockSupplier, Point position, Direction side, int rotationDegrees, boolean showTooltip) {
		this.blockSupplier = blockSupplier;
		this.position = position;
		this.side = side;
		this.rotationDegrees = rotationDegrees % 360;
		this.showTooltip = showTooltip;
	}

	public static BlockFaceWidget of(Supplier<Block> blockSupplier, Point position, Direction side, int rotation, boolean showTooltip) {
		return new BlockFaceWidget(blockSupplier, position, side, rotation, showTooltip);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		Block block = blockSupplier.get();
		MinecraftClient client = MinecraftClient.getInstance();
		BakedModel model = client.getBakedModelManager().getBlockModels().getModel(block.getDefaultState());
		Sprite sprite = model.getParticleSprite();

		RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());

		matrices.push();
		matrices.translate(position.x + 8, position.y + 8, 0);
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotationDegrees));
		matrices.translate(-8, -8, 0);

		DrawableHelper.drawSprite(matrices, 0, 0, 0, 16, 16, sprite);

		matrices.pop();


	}


	public int getX() {
		return position.x;
	}

	public int getY() {
		return position.y;
	}

	public int getWidth() {
		return 16;
	}

	public int getHeight() {
		return 16;
	}

	@Override
	public List<? extends Widget> children() {
		return List.of();
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, 16, 16);
	}
}