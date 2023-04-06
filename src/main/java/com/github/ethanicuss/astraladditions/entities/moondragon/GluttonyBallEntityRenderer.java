package com.github.ethanicuss.astraladditions.entities.moondragon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

@Environment(value= EnvType.CLIENT)
public class GluttonyBallEntityRenderer
        extends EntityRenderer<GluttonyBallEntity> {
    private static final ResourceLocation[] TEXTURE = {new ResourceLocation("textures/entity/enderdragon/gluttony_ball1.png"), new ResourceLocation("textures/entity/enderdragon/gluttony_ball2.png"), new ResourceLocation("textures/entity/enderdragon/gluttony_ball3.png"), new ResourceLocation("textures/entity/enderdragon/gluttony_ball4.png"), new ResourceLocation("textures/entity/enderdragon/gluttony_ball5.png"), new ResourceLocation("textures/entity/enderdragon/gluttony_ball6.png"), new ResourceLocation("textures/entity/enderdragon/gluttony_ball7.png"), new ResourceLocation("textures/entity/enderdragon/gluttony_ball8.png"), new ResourceLocation("textures/entity/enderdragon/gluttony_ball9.png")};
    private static RenderType LAYER = RenderType.entityCutoutNoCull(TEXTURE[1]);

    public GluttonyBallEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GluttonyBallEntity dragonFireballEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.pushPose();
        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        PoseStack.Pose entry = matrixStack.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();
        int frame = dragonFireballEntity.getFrame();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(TEXTURE[frame]));
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.popPose();
        super.render(dragonFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).uv(textureU, textureV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(GluttonyBallEntity gluttonyBallEntity) {
        int i = gluttonyBallEntity.getFrame();
        LAYER = RenderType.entityCutoutNoCull(TEXTURE[i]);
        return TEXTURE[i];
    }

}