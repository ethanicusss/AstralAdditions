package com.github.ethanicuss.astraladditions.entities.hemogiant;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class HemogiantEntityRenderer extends MobEntityRenderer<HemogiantEntity, HemogiantEntityModel<HemogiantEntity>> {

    public HemogiantEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new HemogiantEntityModel(context.getPart(AstralAdditionsClient.MODEL_GLUTTON_LAYER)), 0.5f);
        this.addFeature(new HemogiantEyesFeatureRenderer<HemogiantEntity>(this));
    }

    @Override
    public Identifier getTexture(HemogiantEntity entity){
        return new Identifier(AstralAdditions.MOD_ID, "textures/entity/glutton/glutton.png");
    }
}
