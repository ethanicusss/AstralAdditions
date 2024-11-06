package com.github.ethanicuss.astraladditions.entities.whast;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.phast.PhastEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(value= EnvType.CLIENT)
public class WhastEntityRenderer
        extends MobEntityRenderer<WhastEntity, GhastEntityModel<WhastEntity>> {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/whast/whast.png");
    private static final Identifier ANGRY_TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/whast/whast_shooting.png");

    public WhastEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GhastEntityModel(context.getPart(EntityModelLayers.GHAST)), 1.5f);
    }

    @Override
    public Identifier getTexture(WhastEntity ghastEntity) {
        if (ghastEntity.isShooting()) {
            return ANGRY_TEXTURE;
        }
        return TEXTURE;
    }

    @Override
    protected void scale(WhastEntity ghastEntity, MatrixStack matrixStack, float f) {
        float h = 4.5f;
        float i = h;
        float j = h;
        matrixStack.scale(i, j, i);
    }
}