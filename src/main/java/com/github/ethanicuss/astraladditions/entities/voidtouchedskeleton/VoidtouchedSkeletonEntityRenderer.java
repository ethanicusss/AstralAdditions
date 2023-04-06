package com.github.ethanicuss.astraladditions.entities.voidtouchedskeleton;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

@Environment(value= EnvType.CLIENT)
public class VoidtouchedSkeletonEntityRenderer
        extends SkeletonRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AstralAdditions.MOD_ID, "textures/entity/voidtouched_skeleton/skeleton.png");

    public VoidtouchedSkeletonEntityRenderer(EntityRendererProvider.Context context) {
        super(context, ModEntities.MODEL_VOIDTOUCHED_SKELETON_LAYER, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton abstractSkeletonEntity) {
        return TEXTURE;
    }
}