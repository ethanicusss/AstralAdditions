package net.fabricmc.AstralAdditions.entities.moondragon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Environment(value= EnvType.CLIENT)
public class GluttonyBallEntityRenderer
        extends EntityRenderer<GluttonyBallEntity> {
    private static final Identifier[] TEXTURE = {new Identifier("textures/entity/enderdragon/gluttony_ball1.png"), new Identifier("textures/entity/enderdragon/gluttony_ball2.png"), new Identifier("textures/entity/enderdragon/gluttony_ball3.png"), new Identifier("textures/entity/enderdragon/gluttony_ball4.png"), new Identifier("textures/entity/enderdragon/gluttony_ball5.png"), new Identifier("textures/entity/enderdragon/gluttony_ball6.png"), new Identifier("textures/entity/enderdragon/gluttony_ball7.png"), new Identifier("textures/entity/enderdragon/gluttony_ball8.png"), new Identifier("textures/entity/enderdragon/gluttony_ball9.png")};
    private static RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE[1]);

    public GluttonyBallEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(GluttonyBallEntity dragonFireballEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(GluttonyBallEntity dragonFireballEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        int frame = dragonFireballEntity.getFrame();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE[frame]));
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.pop();
        super.render(dragonFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    @Override
    public Identifier getTexture(GluttonyBallEntity gluttonyBallEntity) {
        int i = gluttonyBallEntity.getFrame();
        LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE[i]);
        return TEXTURE[i];
    }

}