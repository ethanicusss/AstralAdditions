package com.github.ethanicuss.astraladditions.compat.rei.widgets;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

import java.util.Collections;
import java.util.List;

public class RotatedArrowWidget extends Widget {
    private final Widget baseArrow;
    private final float rotationDegrees;
    private final Rectangle bounds;
    public RotatedArrowWidget(Point point, float rotationDegrees) {
        this.baseArrow = Widgets.createArrow(point);
        this.rotationDegrees = rotationDegrees;

        this.bounds = new Rectangle(point.x, point.y, 24, 17);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        matrices.push();
        matrices.translate(bounds.getCenterX(), bounds.getCenterY(), 0);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotationDegrees));

        matrices.translate(-bounds.getCenterX(), -bounds.getCenterY(), 0);
        baseArrow.render(matrices, mouseX, mouseY, delta);
        matrices.pop();
    }


    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }
}