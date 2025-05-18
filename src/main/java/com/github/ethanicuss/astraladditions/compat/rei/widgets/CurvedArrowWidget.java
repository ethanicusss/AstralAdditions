package com.github.ethanicuss.astraladditions.compat.rei.widgets;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;


import java.util.List;

public class CurvedArrowWidget extends Widget {
    private static final Identifier arrowTexture = new Identifier(AstralAdditions.MOD_ID, "textures/gui/reicompat/curved_arrow.png");

    private final Point position;
    private final int rotationDegrees;
    private final boolean mirrored;

    public CurvedArrowWidget(Point position, int rotationDegrees, boolean mirrored) {
        this.position = position;
        this.rotationDegrees = rotationDegrees;
        this.mirrored = mirrored;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderTexture(0, arrowTexture);

        matrices.push();
        matrices.translate(position.x + 8, position.y + 8, 0);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotationDegrees));
        matrices.translate(-8, -8, 0);

        int textureU = mirrored ? 16 : 0;
        DrawableHelper.drawTexture(
                matrices,
                0, 0,
                textureU, 0,
                16, 16,
                32, 16
        );

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
    public List<? extends Element> children() {
        return List.of();
    }

    public static CurvedArrowWidget of(Point position, int rotationDegrees, boolean mirrored) {
        return new CurvedArrowWidget(position, rotationDegrees, mirrored);
    }
}
