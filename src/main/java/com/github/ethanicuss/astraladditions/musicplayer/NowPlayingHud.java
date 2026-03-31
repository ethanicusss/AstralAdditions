package com.github.ethanicuss.astraladditions.musicplayer;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.config.AstralAdditionsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class NowPlayingHud {

	private static Text title = Text.of("");
	private static Text artist = Text.of("");
	private static int ticksLeft = 0;

	private NowPlayingHud() {}

	public static void show(Text titleText, Text artistText) {
		title = titleText;
		artist = artistText;
		ticksLeft = getTotalTicks();
	}

	public static void tick() {
		if (ticksLeft > 0) ticksLeft--;
	}

	public static void render(MatrixStack matrices) {
		if (ticksLeft <= 0) return;

		MinecraftClient client = MinecraftClient.getInstance();
		if (client.options.hudHidden) return;
		if (client.player == null) return;

		TextRenderer textRenderer = client.textRenderer;

		int fadeInTicks = secondsToTicks(AstralAdditionsConfig.musicBoxFadeIn);
		int hangTicks = secondsToTicks(AstralAdditionsConfig.musicBoxHangTime);
		int fadeOutTicks = secondsToTicks(AstralAdditionsConfig.musicBoxFadeOut);
		int totalTicks = Math.max(1, fadeInTicks + hangTicks + fadeOutTicks);

		if (ticksLeft > totalTicks) {
			ticksLeft = totalTicks;
		}

		float alpha = 1.0f;
		int elapsed = totalTicks - ticksLeft;

		if (fadeInTicks > 0 && elapsed < fadeInTicks) {
			alpha = elapsed / (float) fadeInTicks;
		} else if (fadeOutTicks > 0 && ticksLeft <= fadeOutTicks) {
			alpha = ticksLeft / (float) fadeOutTicks;
		}

		alpha *= (float) clamp(AstralAdditionsConfig.musicBoxOpacity, 0.05, 1.0);

		int screenWidth = client.getWindow().getScaledWidth();
		int screenHeight = client.getWindow().getScaledHeight();

		int maxWidth = Math.max(80, AstralAdditionsConfig.musicBoxSizeX);
		int minHeight = Math.max(32, AstralAdditionsConfig.musicBoxSizeY);

		List<OrderedText> lines = new ArrayList<>();
		lines.add(new TranslatableText("gui." + AstralAdditions.MOD_ID + ".music_hud_now_playing").asOrderedText());

		if (AstralAdditionsConfig.musicBoxWordWrap) {
			lines.addAll(textRenderer.wrapLines(title, maxWidth));
		} else {
			lines.add(title.asOrderedText());
		}

		int artistStartIndex = lines.size();

		Text byLine = Text.of(new TranslatableText("gui." + AstralAdditions.MOD_ID + ".music_hud_by").getString() + artist.getString());
		if (AstralAdditionsConfig.musicBoxWordWrap) {
			lines.addAll(textRenderer.wrapLines(byLine, maxWidth));
		} else {
			lines.add(byLine.asOrderedText());
		}

		int lineHeight = textRenderer.fontHeight + 2;
		int contentWidth = 0;
		for (OrderedText line : lines) {
			contentWidth = Math.max(contentWidth, textRenderer.getWidth(line));
		}

		int boxWidth = Math.min(Math.max(contentWidth, 40), maxWidth);
		int contentHeight = lines.size() * lineHeight;
		int boxHeight = Math.max(contentHeight + 4, minHeight);

		int x = AstralAdditionsConfig.musicBoxX;
		int y = AstralAdditionsConfig.musicBoxY;

		x = Math.max(4, Math.min(x, screenWidth - boxWidth - 8));
		y = Math.max(4, Math.min(y, screenHeight - boxHeight - 8));

		int backgroundColor = colorWithAlpha(0x101010, alpha * 0.85f);
		DrawableHelper.fill(matrices, x - 4, y - 4, x + boxWidth + 4, y + boxHeight, backgroundColor);

		if (AstralAdditionsConfig.musicBoxShimmerBorder) {
            if (client.world != null) {
                drawShimmerBorder(matrices, x - 4, y - 4, x + boxWidth + 4, y + boxHeight, alpha, client.world.getTime());
            }
        }

		int textY = y;
		for (int i = 0; i < lines.size(); i++) {
			if (textY + textRenderer.fontHeight > y + boxHeight - 2) break;

			int color = i >= artistStartIndex ? 0xB0B0B0 : 0xFFFFFF;
			textRenderer.drawWithShadow(matrices, lines.get(i), x, textY, colorWithAlpha(color, alpha));
			textY += lineHeight;
		}
	}

	private static void drawShimmerBorder(MatrixStack matrices, int x1, int y1, int x2, int y2, float alpha, long worldTime) {
		int border = 2;
		float speed = 0.75f;
		float offset = worldTime * speed;

		for (int x = x1; x < x2; x++) {
			float tTop = wrap((x - x1) + offset, x2 - x1);
			float tBottom = wrap((x2 - x) + offset, x2 - x1);

			int topColor = shimmerColor(tTop, alpha);
			int bottomColor = shimmerColor(tBottom, alpha);

			DrawableHelper.fill(matrices, x, y1, x + 1, y1 + border, topColor);
			DrawableHelper.fill(matrices, x, y2 - border, x + 1, y2, bottomColor);
		}

		for (int y = y1; y < y2; y++) {
			float tLeft = wrap((y2 - y) + offset, y2 - y1);
			float tRight = wrap((y - y1) + offset, y2 - y1);

			int leftColor = shimmerColor(tLeft, alpha);
			int rightColor = shimmerColor(tRight, alpha);

			DrawableHelper.fill(matrices, x1, y, x1 + border, y + 1, leftColor);
			DrawableHelper.fill(matrices, x2 - border, y, x2, y + 1, rightColor);
		}
	}

	private static int shimmerColor(float time, float alpha) {
		float[][] shimmerColors = new float[][] {
				new float[] { 0.83f, 0.6f, 1.0f },
				new float[] { 0.9f, 0.6f, 1.0f },
				new float[] { 0.58f, 0.6f, 1.0f }
		};

		float scaled = time * shimmerColors.length;
		int indexA = (int) Math.floor(scaled) % shimmerColors.length;
		int indexB = (indexA + 1) % shimmerColors.length;
		float blend = scaled - (float) Math.floor(scaled);

		float r = lerp(shimmerColors[indexA][0], shimmerColors[indexB][0], blend);
		float g = lerp(shimmerColors[indexA][1], shimmerColors[indexB][1], blend);
		float b = lerp(shimmerColors[indexA][2], shimmerColors[indexB][2], blend);

		return floatRgbToArgb(new float[] { r, g, b }, alpha);
	}

	private static float wrap(float value, float length) {
		if (length <= 0.0f) return 0.0f;
		float wrapped = value % length;
		if (wrapped < 0.0f) wrapped += length;
		return wrapped / length;
	}

	private static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}

	private static int getTotalTicks() {
		return Math.max(1, secondsToTicks(AstralAdditionsConfig.musicBoxFadeIn)
				+ secondsToTicks(AstralAdditionsConfig.musicBoxHangTime)
				+ secondsToTicks(AstralAdditionsConfig.musicBoxFadeOut));
	}

	private static int secondsToTicks(double seconds) {
		return Math.max(0, (int) Math.round(seconds * 20.0));
	}

	private static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	private static int colorWithAlpha(int rgb, float alpha) {
		int a = ((int) (clamp(alpha, 0.0, 1.0) * 255.0f) & 0xFF) << 24;
		return a | (rgb & 0xFFFFFF);
	}

	private static int floatRgbToArgb(float[] rgb, float alpha) {
		int r = (int) (clamp(rgb[0], 0.0, 1.0) * 255.0);
		int g = (int) (clamp(rgb[1], 0.0, 1.0) * 255.0);
		int b = (int) (clamp(rgb[2], 0.0, 1.0) * 255.0);
		int a = ((int) (clamp(alpha, 0.0, 1.0) * 255.0) & 0xFF) << 24;
		return a | (r << 16) | (g << 8) | b;
	}

	public static void onConfigReload() {
		if (ticksLeft > 0) {
			ticksLeft = Math.min(ticksLeft, getTotalTicks());
		}
	}
}