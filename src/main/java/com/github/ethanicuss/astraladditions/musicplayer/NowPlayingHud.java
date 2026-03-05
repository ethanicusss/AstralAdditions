package com.github.ethanicuss.astraladditions.musicplayer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class NowPlayingHud {

	private static final int showTick = 20*4;
	private static final int fadeTick = 20;

	private static Text title = Text.of("");
	private static Text artist = Text.of("");
	private static int ticksLeft = 0;

	private NowPlayingHud() {}

	public static void show(Text titleText, Text artistText) {
		title = titleText;
		artist = artistText;
		ticksLeft = showTick + fadeTick * 2;
	}

	public static void tick() {
		if (ticksLeft > 0) ticksLeft--;
	}

	public static void render(MatrixStack matrices) {
		if (ticksLeft <= 0) return;

		MinecraftClient client = MinecraftClient.getInstance();
		if (client.options.hudHidden) return;

		float alpha = 1.0f;
		if (ticksLeft > showTick + fadeTick) {
			//fading out
			int tick = (showTick + fadeTick * 2) - ticksLeft;
			alpha = Math.min(1.0f, tick / (float) fadeTick);
		} else if (ticksLeft < fadeTick) {
			//fading out
			alpha = ticksLeft / (float) fadeTick;
		}

		int a = ((int)(alpha * 255.0f) & 0xFF) << 24;

		Text line1 = title;
		Text line2 = artist;

		TextRenderer textRenderer = client.textRenderer;
		int x = 10;
		int y = 10;

		int w = Math.max(textRenderer.getWidth(line1), textRenderer.getWidth(line2));
		int h = textRenderer.fontHeight * 2 + 6;

		DrawableHelper.fill(matrices, x - 4, y - 4, x + w + 4, y + h, a | 0x101010);

		textRenderer.drawWithShadow(matrices, line1, x, y, a | 0xFFFFFF);
		textRenderer.drawWithShadow(matrices, line2, x, y + textRenderer.fontHeight + 2, a | 0xB0B0B0);
	}
}