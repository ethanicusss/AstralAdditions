package com.github.ethanicuss.astraladditions.entities.moonman;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class MoonmanEntityRenderer extends MobEntityRenderer<MoonmanEntity, MoonmanEntityModel<MoonmanEntity>> {

    public MoonmanEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MoonmanEntityModel(context.getPart(AstralAdditionsClient.MODEL_MOONMAN_LAYER)), 0.5f);
        this.addFeature(new MoonmanEyesFeatureRenderer<MoonmanEntity>(this));
    }

    @Override
    public Identifier getTexture(MoonmanEntity entity){
        return new Identifier(AstralAdditions.MOD_ID, "textures/entity/moonman/moonman.png");
    }
}
