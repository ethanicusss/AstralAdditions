package com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class VoidtouchedSkeletonEntityRenderer
        extends SkeletonEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/voidtouched_skeleton/skeleton.png");

    public VoidtouchedSkeletonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, AstralAdditionsClient.MODEL_VOIDTOUCHED_SKELETON_LAYER, EntityModelLayers.SKELETON_INNER_ARMOR, EntityModelLayers.SKELETON_OUTER_ARMOR);
    }

    @Override
    public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }
}