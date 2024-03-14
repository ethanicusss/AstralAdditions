package com.github.ethanicuss.astraladditions.entities.pylon;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.meteor_mitts.MeteorPunchEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

@Environment(value= EnvType.CLIENT)
public class PylonEntityRenderer
        extends EntityRenderer<PylonEntity> {
    private static final Identifier TEXTURE = new Identifier(AstralAdditions.MOD_ID,"textures/entity/pylon/pylon.png");
    private static final Identifier GLOW_TEXTURE = new Identifier(AstralAdditions.MOD_ID,"textures/entity/pylon/pylon_glow.png");
    private static final RenderLayer END_CRYSTAL = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private static final float SINE_45_DEGREES = (float)Math.sin(0.7853981633974483);
    private static final String GLASS = "glass";
    private static final String BASE = "base";
    private final ModelPart core;

    public PylonEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        ModelPart modelPart = context.getPart(EntityModelLayers.END_CRYSTAL);
        this.core = modelPart.getChild(EntityModelPartNames.CUBE);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void render(PylonEntity endCrystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        matrixStack.translate(0.0f, -0.32f, -0.05f);
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer_ = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(GLOW_TEXTURE));
        produceVertex(vertexConsumer_, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        produceVertex(vertexConsumer_, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        produceVertex(vertexConsumer_, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        produceVertex(vertexConsumer_, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.pop();
        float h = getYOffset(endCrystalEntity, g);
        float j = ((float)endCrystalEntity.pylonAge + g) * 3.0f;
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
        matrixStack.push();
        matrixStack.scale(0.5f, 1.0f, 0.5f);
        matrixStack.translate(0.0, 0.0, 0.0);
        int k = OverlayTexture.DEFAULT_UV;
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j));
        matrixStack.scale(0.875f, 0.875f, 0.875f);
        matrixStack.multiply(new Quaternion(new Vec3f(SINE_45_DEGREES, 0.0f, SINE_45_DEGREES), 45.0f, true));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j));
        this.core.render(matrixStack, vertexConsumer, i, k);
        matrixStack.pop();

        super.render(endCrystalEntity, f, g, matrixStack, vertexConsumerProvider, 5);
    }
    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }
    public static float getYOffset(PylonEntity crystal, float tickDelta) {
        float f = (float)crystal.pylonAge + tickDelta;
        float g = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        g = (g * g + g) * 0.4f;
        return g - 1.4f;
    }

    @Override
    public Identifier getTexture(PylonEntity endCrystalEntity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLight(PylonEntity pylonEntity, BlockPos blockPos) {
        return 15;
    }
}
