package com.github.ethanicuss.astraladditions.entities.phast;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.GhastEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.GhastEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class PhastEntityRenderer
        extends MobEntityRenderer<PhastEntity, GhastEntityModel<PhastEntity>> {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/phast/phast.png");
    private static final Identifier ANGRY_TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/phast/phast_shooting.png");

    public PhastEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GhastEntityModel(context.getPart(EntityModelLayers.GHAST)), 1.5f);
    }

    @Override
    public Identifier getTexture(PhastEntity ghastEntity) {
        if (ghastEntity.isShooting()) {
            return ANGRY_TEXTURE;
        }
        return TEXTURE;
    }

    @Override
    protected void scale(PhastEntity ghastEntity, MatrixStack matrixStack, float f) {
        float g = 1.0f;
        float h = 4.5f;
        float i = 4.5f;
        matrixStack.scale(4.5f, 4.5f, 4.5f);
    }
}