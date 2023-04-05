package com.github.ethanicuss.astraladditions.entities.glutton;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class GluttonEyesFeatureRenderer<T extends LivingEntity>
        extends EyesFeatureRenderer<T, GluttonEntityModel<T>> {
    private static final RenderLayer SKIN = RenderLayer.getEyes(new Identifier(AstralAdditions.MOD_ID, "textures/entity/glutton/glutton_eyes.png"));

    public GluttonEyesFeatureRenderer(FeatureRendererContext<T, GluttonEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}
