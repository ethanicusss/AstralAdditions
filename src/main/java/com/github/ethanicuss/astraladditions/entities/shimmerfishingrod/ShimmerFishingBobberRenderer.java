package com.github.ethanicuss.astraladditions.entities.shimmerfishingrod;

import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.Color;
import java.util.List;

@Environment(EnvType.CLIENT)

public class ShimmerFishingBobberRenderer extends EntityRenderer<ShimmerFishingBobberEntity> {
	private static final Identifier TEXTURE = new Identifier("minecraft", "textures/entity/fishing_hook.png");
	private static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);

	public ShimmerFishingBobberRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	//* Just mostly yonked from minecraft itself
	@Override
	public void render(ShimmerFishingBobberEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		PlayerEntity player = entity.getPlayerOwner();
		if (player == null) return;

		matrices.push();

		matrices.push();
		matrices.scale(0.5F, 0.5F, 0.5F);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
		MatrixStack.Entry entry = matrices.peek();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(LAYER);
		drawBobber(vertexConsumer, entry.getPositionMatrix(), entry.getNormalMatrix(), light);
		matrices.pop();


		double dX = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
		double dY = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + 0.25;
		double dZ = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());

		double playerX = MathHelper.lerp(tickDelta, player.prevX, player.getX());
		double playerY = MathHelper.lerp(tickDelta, player.prevY, player.getY()) + player.getStandingEyeHeight();
		double playerZ = MathHelper.lerp(tickDelta, player.prevZ, player.getZ());

		double offsetX = playerX - dX;
		double offsetY = playerY - dY;
		double offsetZ = playerZ - dZ;

		int rodHand = player.getMainArm() == Arm.RIGHT ? 1 : -1;
		ItemStack itemStack = player.getMainHandStack();
		if (!isValidFishingRod(itemStack)) {
			rodHand = -rodHand;
		}

		if ((this.dispatcher.gameOptions == null || this.dispatcher.gameOptions.getPerspective().isFirstPerson()) && player == MinecraftClient.getInstance().player) {
			double fovMultiplier = 960.0D / this.dispatcher.gameOptions.fov;
			Vec3d vec = this.dispatcher.camera.getProjection().getPosition((float)rodHand * 0.525F, -0.1F).multiply(fovMultiplier);
			vec = vec.rotateY(MathHelper.sin(MathHelper.sqrt(player.getHandSwingProgress(tickDelta)) * (float)Math.PI) * 0.5F);
			vec = vec.rotateX(-MathHelper.sin(MathHelper.sqrt(player.getHandSwingProgress(tickDelta)) * (float)Math.PI) * 0.7F);

			offsetX = playerX + vec.x - dX;
			offsetY = playerY + vec.y - dY;
			offsetZ = playerZ + vec.z - dZ;
		}

		VertexConsumer lineConsumer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
		MatrixStack.Entry matrixEntry = matrices.peek();

		for (int i = 0; i <= 16; ++i) {
			renderShimmerFishingLine(offsetX, offsetY, offsetZ, lineConsumer, matrixEntry, percentage(i, 16), percentage(i + 1, 16));
		}

		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}

	private void drawBobber(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int light) {
		drawVertex(buffer, matrix, normalMatrix, light, 0.0F, 0, 0, 1);
		drawVertex(buffer, matrix, normalMatrix, light, 1.0F, 0, 1, 1);
		drawVertex(buffer, matrix, normalMatrix, light, 1.0F, 1, 1, 0);
		drawVertex(buffer, matrix, normalMatrix, light, 0.0F, 1, 0, 0);
	}

	private void drawVertex(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int light, float x, int y, int u, int v) {
		buffer.vertex(matrix, x - 0.5F, (float) y - 0.5F, 0.0F)
				.color(255, 255, 255, 255)
				.texture((float) u, (float) v)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(normalMatrix, 0.0F, 1.0F, 0.0F)
				.next();
	}

	private static float percentage(int value, int max) {
		return (float) value / (float) max;
	}

	private static final List<float[]> SHIMMER_COLORS = List.of(
			new float[] { 0.83f, 0.6f, 1.0f },
			new float[] { 0.9f, 0.6f, 1.0f },
			new float[] { 0.58f, 0.6f, 1.0f }
	);


	private static void renderShimmerFishingLine(double x, double y, double z, VertexConsumer buffer, MatrixStack.Entry matrices, float start, float end) {
		float f = (float)(x * start);
		float g = (float)(y * (start * start + start) * 0.5D) + 0.25F;
		float h = (float)(z * start);
		float i = (float)(x * end) - f;
		float j = (float)(y * (end * end + end) * 0.5D) + 0.25F - g;
		float k = (float)(z * end) - h;
		float l = MathHelper.sqrt(i * i + j * j + k * k);
		i /= l;
		j /= l;
		k /= l;

		long time = System.currentTimeMillis();
		float scroll = (time % 10000L) / 10000.0f;

		float totalProgress = (start + scroll) % 1.0f;
		float scaledProgress = totalProgress * SHIMMER_COLORS.size();
		int index1 = (int) Math.floor(scaledProgress) % SHIMMER_COLORS.size();
		int index2 = (index1 + 1) % SHIMMER_COLORS.size();
		float mix = scaledProgress - (float)index1;

		float[] color1 = SHIMMER_COLORS.get(index1);
		float[] color2 = SHIMMER_COLORS.get(index2);

		float hue = MathHelper.lerp(mix, color1[0], color2[0]);
		float saturation = MathHelper.lerp(mix, color1[1], color2[1]);
		float brightness = MathHelper.lerp(mix, color1[2], color2[2]);

		int rgb = Color.HSBtoRGB(hue, saturation, brightness);
		int r = (rgb >> 16) & 0xFF;
		int gCol = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;

		buffer.vertex(matrices.getPositionMatrix(), f, g, h)
				.color(r, gCol, b, 255)
				.normal(matrices.getNormalMatrix(), i, j, k)
				.next();
	}

	private boolean isValidFishingRod(ItemStack stack) {
		return stack.isOf(ModItems.SHIMMER_FISHING_ROD);
	}

	@Override
	public Identifier getTexture(ShimmerFishingBobberEntity entity) {
		return TEXTURE;
	}
}