package com.github.ethanicuss.astraladditions.entities.moonman;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MoonmanEntityRenderer extends MobRenderer<MoonmanEntity, MoonmanEntityModel<MoonmanEntity>> {

    public MoonmanEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new MoonmanEntityModel(context.bakeLayer(ModEntities.MODEL_MOONMAN_LAYER)), 0.5f);
        this.addLayer(new MoonmanEyesFeatureRenderer<MoonmanEntity>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(MoonmanEntity entity){
        return new ResourceLocation(AstralAdditions.MOD_ID, "textures/entity/moonman/moonman.png");
    }
}
