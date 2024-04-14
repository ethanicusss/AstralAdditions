package com.github.ethanicuss.astraladditions.entities.shimmerblaze;

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
public class ShimmerBlazeRainEntityRenderer
        extends EntityRenderer<ShimmerBlazeRainEntity> {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/entity/shimmer_blaze/shimmer_rain.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);

    public ShimmerBlazeRainEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(ShimmerBlazeRainEntity dragonFireballEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(ShimmerBlazeRainEntity dragonFireballEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 2, 0);
        if (dragonFireballEntity.getAge() < 60){
            matrixStack.scale(0, 0, 0);
        }
        else {
            if (dragonFireballEntity.getAge() < 75) {
                matrixStack.scale(0.2f + ((float) dragonFireballEntity.getAge() - 60) / 10 + (float)Math.sin(dragonFireballEntity.getAge()*2)/10, 10.0f, 0.2f + ((float) dragonFireballEntity.getAge() - 60) / 10 + (float)Math.sin(dragonFireballEntity.getAge()*2)/10);
            }
            else{
                matrixStack.scale(1.0f - ((float) dragonFireballEntity.getAge() - 75) / 20 + (float)Math.sin(dragonFireballEntity.getAge()*2)/10, 10.0f, 1.0f - ((float) dragonFireballEntity.getAge() - 75) / 20 + (float)Math.sin(dragonFireballEntity.getAge()*2)/10);
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
        super.render(dragonFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    @Override
    public Identifier getTexture(ShimmerBlazeRainEntity dragonFireballEntity) {
        return TEXTURE;
    }
}
