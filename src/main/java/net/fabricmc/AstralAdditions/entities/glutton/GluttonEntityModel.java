package net.fabricmc.AstralAdditions.entities.glutton;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

@Environment(value= EnvType.CLIENT)
public class GluttonEntityModel<T extends LivingEntity> extends BipedEntityModel<T> {
    public GluttonEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
    public boolean carryingBlock;
    public boolean angry;

    public static TexturedModelData getTexturedModelData() {
        float f = -14.0f;
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, -14.0f);
        ModelPartData modelPartData = modelData.getRoot();
        ModelTransform modelTransform = ModelTransform.pivot(0.0f, -13.0f, 0.0f);
        modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(0, 32).cuboid(-8.0f, -16.0f, -8.0f, 16.0f, 16.0f, 16.0f, new Dilation(-0.5f)), modelTransform);
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0f, -16.0f, -8.0f, 16.0f, 16.0f, 16.0f), modelTransform);
        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(64, 32).cuboid(-8.0f, 0.0f, -4.0f, 16.0f, 24.0f, 8.0f), ModelTransform.pivot(0.0f, -28.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(112, 0).cuboid(-7.0f, -10.0f, -2.0f, 4.0f, 60.0f, 4.0f), ModelTransform.pivot(-15.0f, -30.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(112, 0).mirrored().cuboid(3.0f, -10.0f, -2.0f, 4.0f, 60.0f, 4.0f), ModelTransform.pivot(15.0f, -30.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(112, 0).cuboid(-2.0f, -4.0f, -2.0f, 4.0f, 60.0f, 4.0f), ModelTransform.pivot(-4.0f, -14.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(112, 0).mirrored().cuboid(-2.0f, -4.0f, -2.0f, 4.0f, 60.0f, 4.0f), ModelTransform.pivot(4.0f, -14.0f, 0.0f));
        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        super.setAngles(livingEntity, f, g, h, i, j);
        this.head.visible = true;
        int k = -14;
        this.body.pitch = 0.0f;
        this.body.pivotY = -52.0f;
        this.body.pivotZ = -0.0f;
        this.rightLeg.pitch -= 0.0f;
        this.leftLeg.pitch -= 0.0f;
        this.rightArm.pitch *= 0.5f;
        this.leftArm.pitch *= 0.5f;
        this.rightLeg.pitch *= 0.5f;
        this.leftLeg.pitch *= 0.5f;
        float l = 0.4f;
        if (this.rightArm.pitch > 0.4f) {
            this.rightArm.pitch = 0.4f;
        }
        if (this.leftArm.pitch > 0.4f) {
            this.leftArm.pitch = 0.4f;
        }
        if (this.rightArm.pitch < -0.4f) {
            this.rightArm.pitch = -0.4f;
        }
        if (this.leftArm.pitch < -0.4f) {
            this.leftArm.pitch = -0.4f;
        }
        if (this.rightLeg.pitch > 0.4f) {
            this.rightLeg.pitch = 0.4f;
        }
        if (this.leftLeg.pitch > 0.4f) {
            this.leftLeg.pitch = 0.4f;
        }
        if (this.rightLeg.pitch < -0.4f) {
            this.rightLeg.pitch = -0.4f;
        }
        if (this.leftLeg.pitch < -0.4f) {
            this.leftLeg.pitch = -0.4f;
        }
        if (this.carryingBlock) {
            this.rightArm.pitch = -0.5f;
            this.leftArm.pitch = -0.5f;
            this.rightArm.roll = 0.05f;
            this.leftArm.roll = -0.05f;
        }
        this.rightLeg.pivotZ = 0.0f;
        this.leftLeg.pivotZ = 0.0f;
        this.rightLeg.pivotY = -30.0f;
        this.leftLeg.pivotY = -30.0f;
        this.head.pivotZ = -0.0f;
        this.head.pivotY = -51.0f;
        this.hat.pivotX = this.head.pivotX;
        this.hat.pivotY = this.head.pivotY;
        this.hat.pivotZ = this.head.pivotZ;
        this.hat.pitch = this.head.pitch;
        this.hat.yaw = this.head.yaw;
        this.hat.roll = this.head.roll;
        if (this.angry) {
            float m = 1.0f;
            this.head.pivotY -= 5.0f;
        }
        int n = -14;
        this.rightArm.setPivot(-5.0f, -42.0f, 0.0f);
        this.leftArm.setPivot(5.0f, -42.0f, 0.0f);
    }
}
