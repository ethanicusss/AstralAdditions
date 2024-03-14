package com.github.ethanicuss.astraladditions.entities.meteor_mitts;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

@Environment(value= EnvType.CLIENT)
public class MeteorPunchEntityRenderer
        extends EntityRenderer<MeteorPunchEntity> {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/meteor_mitts/meteor_punch.png");
    private static RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private static final RenderLayer FIST_RENDER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private final ModelPart core;

    public MeteorPunchEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        ModelPart modelPart = context.getPart(EntityModelLayers.END_CRYSTAL);
        this.core = modelPart.getChild(EntityModelPartNames.CUBE);
    }

    @Override
    protected int getBlockLight(MeteorPunchEntity meteorPunchEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(MeteorPunchEntity meteorPunchEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        //float j = ((float)meteorPunchEntity.punchAge + g) * 10.0f;
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(FIST_RENDER);
        matrixStack.push();
        matrixStack.scale(1.0f, 1.0f, 1.0f);
        matrixStack.translate(0.0, 0.0, 0.0);
        int k = OverlayTexture.DEFAULT_UV;
        //matrixStack.scale(0.875f, 0.875f, 0.875f);
        matrixStack.multiply(new Quaternion(new Vec3f(0, 0, 0), 0.0f, true));
        //matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((meteorPunchEntity.getPitch() + 90) * Math.abs(meteorPunchEntity.getYaw()-90)/90));
        //matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(meteorPunchEntity.getPitch() * (meteorPunchEntity.getYaw() + 90)/180 + 90));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(meteorPunchEntity.getYaw() - 180));
        this.core.render(matrixStack, vertexConsumer, i, k);
        matrixStack.pop();
        super.render(meteorPunchEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    @Override
    public Identifier getTexture(MeteorPunchEntity cometballEntity) {
        LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
        return TEXTURE;
    }

}