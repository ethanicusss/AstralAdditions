package net.fabricmc.AstralAdditions.entities.moonman;

import net.fabricmc.AstralAdditions.AstralAdditions;
import net.fabricmc.AstralAdditions.AstralAdditionsClient;
import net.minecraft.client.render.entity.EndermanEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EndermanBlockFeatureRenderer;
import net.minecraft.client.render.entity.feature.EndermanEyesFeatureRenderer;
import net.minecraft.client.render.entity.model.EndermanEntityModel;
import net.minecraft.entity.mob.EndermanEntity;
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
