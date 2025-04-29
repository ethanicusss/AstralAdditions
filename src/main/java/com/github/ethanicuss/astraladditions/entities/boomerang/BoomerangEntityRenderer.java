package com.github.ethanicuss.astraladditions.entities.boomerang;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Environment(value= EnvType.CLIENT)
public class BoomerangEntityRenderer
        extends EntityRenderer<BoomerangEntity> {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID, "textures/item/fragile_item.png");
    private static RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);

    public BoomerangEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(BoomerangEntity dragonFireballEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(BoomerangEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        /*matrixStack.push();
        matrixStack.scale(1.0f, 1.0f, 1.0f);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.pop();
        super.render(dragonFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);*/
        ItemStack itemStack = entity.getRangItem();
        if (itemStack != ItemStack.EMPTY || itemStack.isOf(Items.AIR)) {
            matrixStack.push();
            matrixStack.translate(0.0, -0.1, 0.0);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.getWorld().getTime())*20));
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
            matrixStack.scale(1.5f, 1.0f, 1.5f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, i, 0, matrixStack, vertexConsumerProvider, 0);
            matrixStack.pop();
        }
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    @Override
    public Identifier getTexture(BoomerangEntity cometballEntity) {
        LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
        return TEXTURE;
    }

}