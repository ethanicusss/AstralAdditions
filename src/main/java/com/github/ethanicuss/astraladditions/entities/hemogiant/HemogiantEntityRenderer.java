package com.github.ethanicuss.astraladditions.entities.hemogiant;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HemogiantEntityRenderer extends MobRenderer<HemogiantEntity, HemogiantEntityModel<HemogiantEntity>> {

    public HemogiantEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new HemogiantEntityModel(context.bakeLayer(ModEntities.MODEL_GLUTTON_LAYER)), 0.5f);
        this.addLayer(new HemogiantEyesFeatureRenderer<HemogiantEntity>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(HemogiantEntity entity){
        return new ResourceLocation(AstralAdditions.MOD_ID, "textures/entity/glutton/glutton.png");
    }
}
