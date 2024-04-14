package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

@Environment(value= EnvType.CLIENT)
public class JarBlockEntityRenderer
        implements BlockEntityRenderer<JarBlockEntity> {

    public JarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {    }

    @Override
    public void render(JarBlockEntity jarBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        DefaultedList<ItemStack> defaultedList = jarBlockEntity.getItems();
        int k = (int)jarBlockEntity.getPos().asLong();
        ItemStack itemStack = defaultedList.get(0);
        System.out.println(itemStack);
        if (itemStack != ItemStack.EMPTY || itemStack.isOf(Items.AIR)) {
            matrixStack.push();
            matrixStack.translate(0.5, 0.44921875, 0.5);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(jarBlockEntity.getWorld().getTime() + f));
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, i, j, matrixStack, vertexConsumerProvider, k);
            matrixStack.pop();
        }
    }
}