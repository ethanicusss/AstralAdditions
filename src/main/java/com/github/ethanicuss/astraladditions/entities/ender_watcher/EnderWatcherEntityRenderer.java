package com.github.ethanicuss.astraladditions.entities.ender_watcher;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEntity;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEntityModel;
import com.github.ethanicuss.astraladditions.entities.hemogiant.HemogiantEyesFeatureRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class EnderWatcherEntityRenderer extends MobEntityRenderer<EnderWatcherEntity, EnderWatcherEntityModel<EnderWatcherEntity>> {

    public EnderWatcherEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new EnderWatcherEntityModel(context.getPart(AstralAdditionsClient.MODEL_ENDER_WATCHER_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(EnderWatcherEntity entity){
        return new Identifier(AstralAdditions.MOD_ID, "textures/entity/ender_watcher/texture_jelly.png");
    }
}
