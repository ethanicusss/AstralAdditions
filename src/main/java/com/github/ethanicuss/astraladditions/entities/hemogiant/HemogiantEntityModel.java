package com.github.ethanicuss.astraladditions.entities.hemogiant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

@Environment(value= EnvType.CLIENT)
public class HemogiantEntityModel<T extends LivingEntity> extends HumanoidModel<T> {
    public HemogiantEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
    public boolean carryingBlock;
    public boolean angry;

    public static LayerDefinition getTexturedModelData() {
        float f = -14.0f;
        MeshDefinition modelData = HumanoidModel.createMesh(CubeDeformation.NONE, -14.0f);
        PartDefinition modelPartData = modelData.getRoot();
        PartPose modelTransform = PartPose.offset(0.0f, -13.0f, 0.0f);
        modelPartData.addOrReplaceChild(PartNames.HAT, CubeListBuilder.create().texOffs(0, 32).addBox(-8.0f, -16.0f, -8.0f, 16.0f, 16.0f, 16.0f, new CubeDeformation(-0.5f)), modelTransform);
        modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -16.0f, -8.0f, 16.0f, 16.0f, 16.0f), modelTransform);
        modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(64, 32).addBox(-8.0f, 0.0f, -4.0f, 16.0f, 24.0f, 8.0f), PartPose.offset(0.0f, -28.0f, 0.0f));
        modelPartData.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create().texOffs(112, 0).addBox(-7.0f, -10.0f, -2.0f, 4.0f, 60.0f, 4.0f), PartPose.offset(-15.0f, -30.0f, 0.0f));
        modelPartData.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create().texOffs(112, 0).mirror().addBox(3.0f, -10.0f, -2.0f, 4.0f, 60.0f, 4.0f), PartPose.offset(15.0f, -30.0f, 0.0f));
        modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create().texOffs(112, 0).addBox(-2.0f, -4.0f, -2.0f, 4.0f, 60.0f, 4.0f), PartPose.offset(-4.0f, -14.0f, 0.0f));
        modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create().texOffs(112, 0).mirror().addBox(-2.0f, -4.0f, -2.0f, 4.0f, 60.0f, 4.0f), PartPose.offset(4.0f, -14.0f, 0.0f));
        return LayerDefinition.create(modelData, 128, 64);
    }

    @Override
    public void setupAnim(T livingEntity, float f, float g, float h, float i, float j) {
        super.setupAnim(livingEntity, f, g, h, i, j);
        this.head.visible = true;
        int k = -14;
        this.body.xRot = 0.0f;
        this.body.y = -52.0f;
        this.body.z = -0.0f;
        this.rightLeg.xRot -= 0.0f;
        this.leftLeg.xRot -= 0.0f;
        this.rightArm.xRot *= 0.5f;
        this.leftArm.xRot *= 0.5f;
        this.rightLeg.xRot *= 0.5f;
        this.leftLeg.xRot *= 0.5f;
        float l = 0.4f;
        if (this.rightArm.xRot > 0.4f) {
            this.rightArm.xRot = 0.4f;
        }
        if (this.leftArm.xRot > 0.4f) {
            this.leftArm.xRot = 0.4f;
        }
        if (this.rightArm.xRot < -0.4f) {
            this.rightArm.xRot = -0.4f;
        }
        if (this.leftArm.xRot < -0.4f) {
            this.leftArm.xRot = -0.4f;
        }
        if (this.rightLeg.xRot > 0.4f) {
            this.rightLeg.xRot = 0.4f;
        }
        if (this.leftLeg.xRot > 0.4f) {
            this.leftLeg.xRot = 0.4f;
        }
        if (this.rightLeg.xRot < -0.4f) {
            this.rightLeg.xRot = -0.4f;
        }
        if (this.leftLeg.xRot < -0.4f) {
            this.leftLeg.xRot = -0.4f;
        }
        if (this.carryingBlock) {
            this.rightArm.xRot = -0.5f;
            this.leftArm.xRot = -0.5f;
            this.rightArm.zRot = 0.05f;
            this.leftArm.zRot = -0.05f;
        }
        this.rightLeg.z = 0.0f;
        this.leftLeg.z = 0.0f;
        this.rightLeg.y = -30.0f;
        this.leftLeg.y = -30.0f;
        this.head.z = -0.0f;
        this.head.y = -51.0f;
        this.hat.x = this.head.x;
        this.hat.y = this.head.y;
        this.hat.z = this.head.z;
        this.hat.xRot = this.head.xRot;
        this.hat.yRot = this.head.yRot;
        this.hat.zRot = this.head.zRot;
        if (this.angry) {
            float m = 1.0f;
            this.head.y -= 5.0f;
        }
        int n = -14;
        this.rightArm.setPos(-5.0f, -42.0f, 0.0f);
        this.leftArm.setPos(5.0f, -42.0f, 0.0f);
    }
}
