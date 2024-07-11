package com.github.ethanicuss.astraladditions.entities.ender_watcher;

// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class EnderWatcherEntityModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier("modid", "ender_watcher"), "main");
    private final ModelPart watcher;
    //private final ModelPart body;
    /*private final ModelPart eye;
    private final ModelPart jelly;
    private final ModelPart rent;
    private final ModelPart tren;
    private final ModelPart tren2;
    private final ModelPart tren3;
    private final ModelPart tren4;
    private final ModelPart rent2;
    private final ModelPart tren5;
    private final ModelPart tren6;
    private final ModelPart tren7;
    private final ModelPart tren8;
    private final ModelPart rent3;
    private final ModelPart tren9;
    private final ModelPart tren10;
    private final ModelPart tren11;
    private final ModelPart tren12;
    private final ModelPart rent4;
    private final ModelPart tren13;
    private final ModelPart tren14;
    private final ModelPart tren15;
    private final ModelPart tren16;
    private final ModelPart bottom;
    private final ModelPart bone1;
    private final ModelPart big;
    private final ModelPart tent;
    private final ModelPart kent;
    private final ModelPart tent2;
    private final ModelPart tent3;
    private final ModelPart bone2;
    private final ModelPart big2;
    private final ModelPart tent4;
    private final ModelPart kent2;
    private final ModelPart tent5;
    private final ModelPart tent6;
    private final ModelPart bone3;
    private final ModelPart big3;
    private final ModelPart tent7;
    private final ModelPart kent3;
    private final ModelPart tent8;
    private final ModelPart tent9;
    private final ModelPart bone4;
    private final ModelPart big4;
    private final ModelPart tent10;
    private final ModelPart kent4;
    private final ModelPart tent11;
    private final ModelPart tent12;
    private final ModelPart bone5;
    private final ModelPart big5;
    private final ModelPart tent13;
    private final ModelPart kent5;
    private final ModelPart tent14;
    private final ModelPart tent15;
    private final ModelPart bone6;
    private final ModelPart big6;
    private final ModelPart tent18;
    private final ModelPart kent6;
    private final ModelPart tent16;
    private final ModelPart tent17;*/

    public EnderWatcherEntityModel(ModelPart root) {
        this.watcher = root.getChild(EntityModelPartNames.HEAD);
        //this.body = root.getChild("body");
        /*this.eye = root.getChild("eye");
        this.jelly = root.getChild("jelly");
        this.rent = root.getChild("rent");
        this.tren = root.getChild("tren");
        this.tren2 = root.getChild("tren2");
        this.tren3 = root.getChild("tren3");
        this.tren4 = root.getChild("tren4");
        this.rent2 = root.getChild("rent2");
        this.tren5 = root.getChild("tren5");
        this.tren6 = root.getChild("tren6");
        this.tren7 = root.getChild("tren7");
        this.tren8 = root.getChild("tren8");
        this.rent3 = root.getChild("rent3");
        this.tren9 = root.getChild("tren9");
        this.tren10 = root.getChild("tren10");
        this.tren11 = root.getChild("tren11");
        this.tren12 = root.getChild("tren12");
        this.rent4 = root.getChild("rent4");
        this.tren13 = root.getChild("tren13");
        this.tren14 = root.getChild("tren14");
        this.tren15 = root.getChild("tren15");
        this.tren16 = root.getChild("tren16");
        this.bottom = root.getChild("bottom");
        this.bone1 = root.getChild("bone1");
        this.big = root.getChild("big");
        this.tent = root.getChild("tent");
        this.kent = root.getChild("kent");
        this.tent2 = root.getChild("tent2");
        this.tent3 = root.getChild("tent3");
        this.bone2 = root.getChild("bone2");
        this.big2 = root.getChild("big2");
        this.tent4 = root.getChild("tent4");
        this.kent2 = root.getChild("kent2");
        this.tent5 = root.getChild("tent5");
        this.tent6 = root.getChild("tent6");
        this.bone3 = root.getChild("bone3");
        this.big3 = root.getChild("big3");
        this.tent7 = root.getChild("tent7");
        this.kent3 = root.getChild("kent3");
        this.tent8 = root.getChild("tent8");
        this.tent9 = root.getChild("tent9");
        this.bone4 = root.getChild("bone4");
        this.big4 = root.getChild("big4");
        this.tent10 = root.getChild("tent10");
        this.kent4 = root.getChild("kent4");
        this.tent11 = root.getChild("tent11");
        this.tent12 = root.getChild("tent12");
        this.bone5 = root.getChild("bone5");
        this.big5 = root.getChild("big5");
        this.tent13 = root.getChild("tent13");
        this.kent5 = root.getChild("kent5");
        this.tent14 = root.getChild("tent14");
        this.tent15 = root.getChild("tent15");
        this.bone6 = root.getChild("bone6");
        this.big6 = root.getChild("big6");
        this.tent18 = root.getChild("tent18");
        this.kent6 = root.getChild("kent6");
        this.tent16 = root.getChild("tent16");
        this.tent17 = root.getChild("tent17");*/
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData meshdefinition = new ModelData();
        ModelPartData partdefinition = meshdefinition.getRoot();

        ModelPartData watcher = partdefinition.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 19.0F, 0.0F));

        ModelPartData body = watcher.addChild("body", ModelPartBuilder.create().uv(0, 41).cuboid(-7.0F, -5.25F, -8.0F, 14.0F, 3.0F, 14.0F, new Dilation(0.0F))
                .uv(48, 22).cuboid(-6.0F, -8.25F, -7.0F, 12.0F, 3.0F, 12.0F, new Dilation(0.0F))
                .uv(36, 47).cuboid(-6.5F, -1.0F, -7.5F, 0.0F, 10.0F, 13.0F, new Dilation(0.0F))
                .uv(0, 58).cuboid(-7.0F, -2.0F, -8.0F, 0.0F, 4.0F, 14.0F, new Dilation(0.0F))
                .uv(77, 60).cuboid(-6.5F, -1.0F, -7.5F, 13.0F, 10.0F, 0.0F, new Dilation(0.0F))
                .uv(64, 37).cuboid(-7.0F, -2.0F, -8.0F, 14.0F, 4.0F, 0.0F, new Dilation(0.0F))
                .uv(36, 57).cuboid(6.5F, -1.0F, -7.5F, 0.0F, 10.0F, 13.0F, new Dilation(0.0F))
                .uv(0, 62).cuboid(7.0F, -2.0F, -8.0F, 0.0F, 4.0F, 14.0F, new Dilation(0.0F))
                .uv(81, 73).cuboid(-6.5F, -1.0F, 5.5F, 13.0F, 10.0F, 0.0F, new Dilation(0.0F))
                .uv(72, 15).cuboid(-7.0F, -2.0F, 6.0F, 14.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 1.0F));

        ModelPartData eye = body.addChild("eye", ModelPartBuilder.create().uv(54, 0).cuboid(-5.0F, -4.5F, -5.0F, 10.0F, 5.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.25F, -1.0F));

        ModelPartData jelly = body.addChild("jelly", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, 3.0F, -9.0F, 18.0F, 4.0F, 18.0F, new Dilation(0.0F))
                .uv(0, 22).cuboid(-8.0F, 0.0F, -8.0F, 16.0F, 3.0F, 16.0F, new Dilation(0.0F))
                .uv(42, 44).cuboid(-7.0F, -2.0F, -7.0F, 14.0F, 2.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -9.0F, -1.0F));

        ModelPartData rent = body.addChild("rent", ModelPartBuilder.create(), ModelTransform.pivot(-8.7071F, 0.0F, -10.1213F));

        ModelPartData tren = rent.addChild("tren", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = tren.addChild("cube_r1", ModelPartBuilder.create().uv(101, 68).cuboid(-8.1213F, -1.0F, -0.7071F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -2.1213F, 2.5F, 0.3999F, -0.6956F, -0.583F));

        ModelPartData tren2 = rent.addChild("tren2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r2 = tren2.addChild("cube_r2", ModelPartBuilder.create().uv(84, 4).cuboid(-10.6213F, -3.5F, -0.4571F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 3.5355F, -1.5F, 0.6155F, -0.5236F, -0.9553F));

        ModelPartData tren3 = rent.addChild("tren3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r3 = tren3.addChild("cube_r3", ModelPartBuilder.create().uv(84, 0).cuboid(-10.3713F, 0.0F, -0.4571F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, 16.9706F, -11.0F, 0.7835F, -0.0617F, -1.509F));

        ModelPartData tren4 = rent.addChild("tren4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r4 = tren4.addChild("cube_r4", ModelPartBuilder.create().uv(96, 23).cuboid(-6.6213F, -3.0F, -0.7071F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-7.0F, 11.3137F, -7.0F, 0.7363F, -0.3035F, -1.2523F));

        ModelPartData rent2 = body.addChild("rent2", ModelPartBuilder.create(), ModelTransform.pivot(9.3223F, 0.0F, -9.7071F));

        ModelPartData tren5 = rent2.addChild("tren5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r5 = tren5.addChild("cube_r5", ModelPartBuilder.create().uv(0, 90).cuboid(-1.0815F, -1.0481F, -7.4311F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, -1.6997F, 2.0F, 0.3927F, -0.7854F, 0.0F));

        ModelPartData tren6 = rent2.addChild("tren6", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r6 = tren6.addChild("cube_r6", ModelPartBuilder.create().uv(51, 73).cuboid(-1.3315F, -4.0481F, -10.1811F, 2.0F, 2.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 3.75F, -1.5F, 0.7854F, -0.7854F, 0.0F));

        ModelPartData tren7 = rent2.addChild("tren7", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r7 = tren7.addChild("cube_r7", ModelPartBuilder.create().uv(17, 72).cuboid(-1.3315F, 4.4519F, -20.4311F, 2.0F, 2.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(12.7577F, 4.7925F, -13.2577F, 1.309F, -0.7854F, 0.0F));

        ModelPartData tren8 = rent2.addChild("tren8", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r8 = tren8.addChild("cube_r8", ModelPartBuilder.create().uv(84, 19).cuboid(-0.8315F, 0.7019F, -9.4311F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(7.7873F, 6.7616F, -8.6408F, 0.9599F, -0.7854F, 0.0F));

        ModelPartData rent3 = body.addChild("rent3", ModelPartBuilder.create(), ModelTransform.pivot(8.9081F, 0.0F, 8.3223F));

        ModelPartData tren9 = rent3.addChild("tren9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r9 = tren9.addChild("cube_r9", ModelPartBuilder.create().uv(103, 90).cuboid(-0.163F, -1.0F, -1.2929F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, -2.1213F, -2.5F, -0.5208F, -0.6178F, 0.7805F));

        ModelPartData tren10 = rent3.addChild("tren10", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r10 = tren10.addChild("cube_r10", ModelPartBuilder.create().uv(84, 29).cuboid(-0.413F, -2.25F, -1.5429F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, 3.5355F, 1.5F, -0.6353F, -0.4981F, 0.9959F));

        ModelPartData tren11 = rent3.addChild("tren11", ModelPartBuilder.create(), ModelTransform.pivot(13.5F, 19.0919F, 13.5F));

        ModelPartData cube_r11 = tren11.addChild("cube_r11", ModelPartBuilder.create().uv(90, 86).cuboid(0.087F, 1.0F, -1.2929F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.3232F, -2.1213F, -2.6768F, -0.7811F, -0.0924F, 1.478F));

        ModelPartData tren12 = rent3.addChild("tren12", ModelPartBuilder.create(), ModelTransform.pivot(13.5F, 19.0919F, 13.5F));

        ModelPartData cube_r12 = tren12.addChild("cube_r12", ModelPartBuilder.create().uv(103, 61).cuboid(-0.663F, -1.5F, -1.2929F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-6.5F, -7.7782F, -6.5F, -0.7256F, -0.3326F, 1.2181F));

        ModelPartData rent4 = body.addChild("rent4", ModelPartBuilder.create(), ModelTransform.pivot(-9.1213F, 0.0F, 7.9081F));

        ModelPartData tren13 = rent4.addChild("tren13", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r13 = tren13.addChild("cube_r13", ModelPartBuilder.create().uv(35, 80).cuboid(-0.8492F, -1.0F, -0.0208F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -2.1213F, -2.5F, -0.48F, -0.7854F, 0.0F));

        ModelPartData tren14 = rent4.addChild("tren14", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r14 = tren14.addChild("cube_r14", ModelPartBuilder.create().uv(66, 75).cuboid(-0.5992F, -3.25F, -1.0208F, 2.0F, 2.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 3.5355F, 1.5F, -0.8727F, -0.7854F, 0.0F));

        ModelPartData tren15 = rent4.addChild("tren15", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r15 = tren15.addChild("cube_r15", ModelPartBuilder.create().uv(62, 60).cuboid(-0.5992F, 1.75F, -0.2708F, 2.0F, 2.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, 16.9706F, 11.0F, -1.3963F, -0.7854F, 0.0F));

        ModelPartData tren16 = rent4.addChild("tren16", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r16 = tren16.addChild("cube_r16", ModelPartBuilder.create().uv(0, 80).cuboid(-0.8492F, -1.75F, -1.5208F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-7.0F, 11.3137F, 7.0F, -1.2217F, -0.7854F, 0.0F));

        ModelPartData bottom = watcher.addChild("bottom", ModelPartBuilder.create().uv(0, 58).cuboid(-4.0F, -3.0F, -5.0F, 8.0F, 4.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, 0.0F));

        ModelPartData bone1 = bottom.addChild("bone1", ModelPartBuilder.create().uv(113, 34).cuboid(-1.5F, 0.25F, -1.5F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 0.75F, 0.0F));

        ModelPartData big = bone1.addChild("big", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.25F, 0.0F, 0.0F, 0.0F, 0.6545F));

        ModelPartData tent = big.addChild("tent", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData cube_r17 = tent.addChild("cube_r17", ModelPartBuilder.create().uv(33, 112).cuboid(-1.0F, -1.0F, -5.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData kent = big.addChild("kent", ModelPartBuilder.create(), ModelTransform.pivot(0.0741F, 7.2627F, 0.0F));

        ModelPartData tent2 = kent.addChild("tent2", ModelPartBuilder.create(), ModelTransform.pivot(0.1447F, -0.3343F, -0.25F));

        ModelPartData cube_r18 = tent2.addChild("cube_r18", ModelPartBuilder.create().uv(0, 41).cuboid(-1.2482F, -1.2908F, -4.75F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F))
                .uv(109, 94).cuboid(1.7518F, -1.2908F, -3.25F, 6.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.2397F, 1.2668F, 3.5F, 0.0F, 0.0F, -0.2618F));

        ModelPartData tent3 = kent.addChild("tent3", ModelPartBuilder.create(), ModelTransform.of(2.4067F, 9.9984F, -0.25F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r19 = tent3.addChild("cube_r19", ModelPartBuilder.create().uv(100, 8).cuboid(1.7518F, 8.7092F, -3.25F, 6.0F, 15.0F, 0.0F, new Dilation(0.0F))
                .uv(89, 38).cuboid(-1.2482F, 8.7092F, -4.75F, 3.0F, 15.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.3033F, -9.2181F, 3.5F, 0.0F, 0.0F, -0.2618F));

        ModelPartData bone2 = bottom.addChild("bone2", ModelPartBuilder.create().uv(89, 113).cuboid(-1.5F, 0.25F, -1.5F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 0.75F, 0.0F));

        ModelPartData big2 = bone2.addChild("big2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.25F, 0.0F, 0.0F, 0.0F, -0.6545F));

        ModelPartData tent4 = big2.addChild("tent4", ModelPartBuilder.create().uv(112, 0).cuboid(2.0F, -2.0F, -5.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData kent2 = big2.addChild("kent2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

        ModelPartData tent5 = kent2.addChild("tent5", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, 2.0F, 3.25F));

        ModelPartData cube_r20 = tent5.addChild("cube_r20", ModelPartBuilder.create().uv(80, 106).cuboid(-4.6369F, -3.0756F, -3.25F, 6.0F, 10.0F, 0.0F, new Dilation(0.0F))
                .uv(12, 103).cuboid(1.3631F, -3.0756F, -4.75F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData tent6 = kent2.addChild("tent6", ModelPartBuilder.create(), ModelTransform.of(-2.6831F, 9.5594F, 0.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r21 = tent6.addChild("cube_r21", ModelPartBuilder.create().uv(0, 100).cuboid(-4.6369F, 6.9244F, -3.25F, 6.0F, 15.0F, 0.0F, new Dilation(0.0F))
                .uv(55, 86).cuboid(1.3631F, 6.9244F, -4.75F, 3.0F, 15.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.6647F, -7.7578F, 3.25F, 0.0F, 0.0F, 0.2618F));

        ModelPartData bone3 = bottom.addChild("bone3", ModelPartBuilder.create().uv(0, 115).cuboid(-1.5F, 0.25F, -1.5F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.75F, -3.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData big3 = bone3.addChild("big3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.25F, 0.0F, 0.0F, 0.0F, 0.6545F));

        ModelPartData tent7 = big3.addChild("tent7", ModelPartBuilder.create().uv(112, 10).cuboid(2.0F, -2.0F, -5.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData kent3 = big3.addChild("kent3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

        ModelPartData tent8 = kent3.addChild("tent8", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData cube_r22 = tent8.addChild("cube_r22", ModelPartBuilder.create().uv(68, 106).cuboid(1.37F, -0.16F, 0.0F, 6.0F, 10.0F, 0.0F, new Dilation(0.0F))
                .uv(42, 41).cuboid(-1.63F, -0.16F, -1.5F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.63F, -1.84F, -3.5F, 0.0F, 0.0F, -0.2618F));

        ModelPartData tent9 = kent3.addChild("tent9", ModelPartBuilder.create(), ModelTransform.of(2.7398F, 10.1319F, -0.25F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r23 = tent9.addChild("cube_r23", ModelPartBuilder.create().uv(0, 0).cuboid(-1.63F, 9.84F, -1.5F, 3.0F, 15.0F, 3.0F, new Dilation(0.0F))
                .uv(101, 33).cuboid(1.37F, 9.84F, 0.0F, 6.0F, 15.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.6098F, -9.9719F, 0.25F, 0.0F, 0.0F, -0.2618F));

        ModelPartData bone4 = bottom.addChild("bone4", ModelPartBuilder.create().uv(45, 114).cuboid(-1.5F, 0.25F, -1.5F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.75F, 3.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData big4 = bone4.addChild("big4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.25F, 0.0F, 0.0F, 0.0F, 0.6545F));

        ModelPartData tent10 = big4.addChild("tent10", ModelPartBuilder.create().uv(112, 104).cuboid(2.0F, -2.0F, -5.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData kent4 = big4.addChild("kent4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

        ModelPartData tent11 = kent4.addChild("tent11", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData cube_r24 = tent11.addChild("cube_r24", ModelPartBuilder.create().uv(56, 106).cuboid(4.8017F, -1.8478F, -4.5F, 6.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.6922F, 0.7845F, 0.999F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r25 = tent11.addChild("cube_r25", ModelPartBuilder.create().uv(44, 101).cuboid(-1.7549F, 0.4846F, -1.5F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.5239F, -2.3889F, -3.5F, 0.0F, 0.0F, -0.2618F));

        ModelPartData tent12 = kent4.addChild("tent12", ModelPartBuilder.create(), ModelTransform.of(2.0849F, 9.689F, -0.25F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r26 = tent12.addChild("cube_r26", ModelPartBuilder.create().uv(91, 90).cuboid(1.2282F, 10.4215F, 0.0F, 6.0F, 15.0F, 0.0F, new Dilation(0.0F))
                .uv(79, 88).cuboid(-1.7718F, 10.4215F, -1.5F, 3.0F, 15.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.2593F, -9.9258F, 0.25F, 0.0F, 0.0F, -0.2618F));

        ModelPartData bone5 = bottom.addChild("bone5", ModelPartBuilder.create().uv(109, 114).cuboid(-1.5F, 0.25F, -1.5F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.75F, 3.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData big5 = bone5.addChild("big5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.25F, 0.5F, 0.0F, 0.0F, -0.6545F));

        ModelPartData tent13 = big5.addChild("tent13", ModelPartBuilder.create().uv(113, 24).cuboid(2.0F, -2.0F, -5.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 2.0F, 3.0F));

        ModelPartData kent5 = big5.addChild("kent5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, -0.5F));

        ModelPartData tent14 = kent5.addChild("tent14", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData cube_r27 = tent14.addChild("cube_r27", ModelPartBuilder.create().uv(24, 105).cuboid(-7.4729F, 0.0619F, 0.0F, 6.0F, 10.0F, 0.0F, new Dilation(0.0F))
                .uv(100, 102).cuboid(-1.4729F, 0.0619F, -1.5F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.4729F, -2.0619F, -3.5F, 0.0F, 0.0F, 0.2618F));

        ModelPartData tent15 = kent5.addChild("tent15", ModelPartBuilder.create(), ModelTransform.of(-2.6369F, 9.91F, 0.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r28 = tent15.addChild("cube_r28", ModelPartBuilder.create().uv(32, 90).cuboid(-7.4729F, 10.0619F, 0.0F, 6.0F, 15.0F, 0.0F, new Dilation(0.0F))
                .uv(67, 88).cuboid(-1.4729F, 10.0619F, -1.5F, 3.0F, 15.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(2.6098F, -9.9719F, 0.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData bone6 = bottom.addChild("bone6", ModelPartBuilder.create().uv(21, 115).cuboid(-1.5F, 0.25F, -1.5F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.75F, -3.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData big6 = bone6.addChild("big6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 7.25F, 0.0F, 0.0F, 0.0F, -0.6545F));

        ModelPartData tent18 = big6.addChild("tent18", ModelPartBuilder.create().uv(107, 72).cuboid(2.0F, -2.0F, -5.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData kent6 = big6.addChild("kent6", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

        ModelPartData tent16 = kent6.addChild("tent16", ModelPartBuilder.create(), ModelTransform.pivot(-3.5F, 2.0F, 3.5F));

        ModelPartData cube_r29 = tent16.addChild("cube_r29", ModelPartBuilder.create().uv(48, 22).cuboid(-7.3207F, -0.1364F, 0.0F, 6.0F, 10.0F, 0.0F, new Dilation(0.0F))
                .uv(103, 48).cuboid(-1.3207F, -0.1364F, -1.5F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.3207F, -1.8636F, -3.5F, 0.0F, 0.0F, 0.2618F));

        ModelPartData tent17 = kent6.addChild("tent17", ModelPartBuilder.create(), ModelTransform.of(-2.2864F, 9.8638F, 0.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r30 = tent17.addChild("cube_r30", ModelPartBuilder.create().uv(0, 22).cuboid(-7.3207F, 9.8636F, 0.0F, 6.0F, 15.0F, 0.0F, new Dilation(0.0F))
                .uv(20, 85).cuboid(-1.3207F, 9.8636F, -1.5F, 3.0F, 15.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(2.3054F, -9.5752F, 0.0F, 0.0F, 0.0F, 0.2618F));

        return TexturedModelData.of(meshdefinition, 128, 128);
    }

    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        watcher.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}