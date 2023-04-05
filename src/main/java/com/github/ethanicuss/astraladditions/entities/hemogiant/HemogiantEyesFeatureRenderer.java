package com.github.ethanicuss.astraladditions.entities.hemogiant;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class HemogiantEyesFeatureRenderer<T extends LivingEntity>
        extends EyesFeatureRenderer<T, HemogiantEntityModel<T>> {
    private static final RenderLayer SKIN = RenderLayer.getEyes(new Identifier(AstralAdditions.MOD_ID, "textures/entity/glutton/glutton_eyes.png"));

    public HemogiantEyesFeatureRenderer(FeatureRendererContext<T, HemogiantEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}
