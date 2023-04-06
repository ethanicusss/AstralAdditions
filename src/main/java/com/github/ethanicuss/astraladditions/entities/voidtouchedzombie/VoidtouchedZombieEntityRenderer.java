package com.github.ethanicuss.astraladditions.entities.voidtouchedzombie;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.AstralAdditionsClient;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;


public class VoidtouchedZombieEntityRenderer
        extends ZombieRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AstralAdditions.MOD_ID, "textures/entity/voidtouched_zombie/zombie.png");

    public VoidtouchedZombieEntityRenderer(EntityRendererProvider.Context context) {
        super(context, ModEntities.MODEL_VOIDTOUCHED_ZOMBIE_LAYER, ModelLayers.HUSK_INNER_ARMOR, ModelLayers.HUSK_OUTER_ARMOR);
    }

    public static LayerDefinition getTexturedModelData(){
        return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f), 64, 64);
    }

    @Override
    protected void scale(Zombie zombieEntity, PoseStack matrixStack, float f) {
        float g = 1.25f;
        matrixStack.scale(g, g, g);
        super.scale(zombieEntity, matrixStack, f);
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie zombieEntity) {
        return TEXTURE;
    }
}
