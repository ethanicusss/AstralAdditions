package com.github.ethanicuss.astraladditions.entities.moonman;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

@Environment(value=EnvType.CLIENT)
public class MoonmanEyesFeatureRenderer<T extends LivingEntity>
        extends EyesLayer<T, MoonmanEntityModel<T>> {
    private static final RenderType SKIN = RenderType.eyes(new ResourceLocation(AstralAdditions.MOD_ID, "textures/entity/moonman/moonman_eyes.png"));

    public MoonmanEyesFeatureRenderer(RenderLayerParent<T, MoonmanEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderType renderType() {
        return SKIN;
    }
}
