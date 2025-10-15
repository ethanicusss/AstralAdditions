package com.github.ethanicuss.astraladditions.entities.prismatic_geyser;

import com.github.ethanicuss.astraladditions.AstralAdditions;
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
public class PrismaticGeyserRenderer
        extends EntityRenderer<PrismaticGeyserEntity> {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/prismatic_geyser/prismatic_geyser.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);

    public PrismaticGeyserRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(PrismaticGeyserEntity geyser, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(PrismaticGeyserEntity geyser, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        if (geyser.getAge() < 10){
            matrixStack.translate(0, 2, 0);
            matrixStack.scale(0, 0, 0);
        }
        else {
            if (geyser.getAge() < 90) {
                matrixStack.translate(0, Math.min(((float) geyser.getAge()-10)/10*2, 2), 0);
                float width = 2.0f - ((float) geyser.getAge() - 10)/80 + (float)Math.sin(geyser.getAge())/20;
                matrixStack.scale(width, Math.min((float) geyser.getAge() - 10, 10.0f), width);
            }
            else{
                matrixStack.translate(0, 2 - Math.min(((float) geyser.getAge()-90)/30*2, 2), 0);
                float width = 1.0f - ((float) geyser.getAge() - 90)/30;
                matrixStack.scale(width, 10.0f - ((float) geyser.getAge() - 90)/30*10, width);
            }
        }
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.pop();
        super.render(geyser, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    @Override
    public Identifier getTexture(PrismaticGeyserEntity geyser) {
        return TEXTURE;
    }
}
