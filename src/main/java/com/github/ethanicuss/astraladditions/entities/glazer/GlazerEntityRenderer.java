package com.github.ethanicuss.astraladditions.entities.glazer;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.phast.PhastEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(value= EnvType.CLIENT)
public class GlazerEntityRenderer
        extends MobEntityRenderer<GlazerEntity, BeeEntityModel<GlazerEntity>> {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/glazer/glazer.png");
    private static final Identifier ANGRY_TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/glazer/glazer_angry.png");

    public GlazerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BeeEntityModel(context.getPart(EntityModelLayers.BEE)), 1.5f);
    }

    @Override
    public Identifier getTexture(GlazerEntity ghastEntity) {
        if (ghastEntity.hasAngerTime()) {
            return ANGRY_TEXTURE;
        }
        return TEXTURE;
    }

    @Override
    protected void scale(GlazerEntity ghastEntity, MatrixStack matrixStack, float f) {
        float h = 2.0f;
        float i = h;
        float j = h;
        matrixStack.scale(i, j, i);
    }
}