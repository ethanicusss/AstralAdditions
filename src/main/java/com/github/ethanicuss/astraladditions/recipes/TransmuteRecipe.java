package com.github.ethanicuss.astraladditions.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TransmuteRecipe implements Recipe<Inventory> {

    private final Identifier id;
    private final ItemStack inputItem;
    private final List<ItemStack> outputItems;
    private final boolean ignoreCount;
    private final boolean softIgnoreCount;

    public TransmuteRecipe(Identifier id, ItemStack inputItem, List<ItemStack> outputItems, boolean ignoreCount, boolean softIgnoreCount) {
        this.id = id;
        this.inputItem = inputItem;
        this.outputItems = outputItems;
        this.ignoreCount = ignoreCount;
        this.softIgnoreCount = softIgnoreCount;
    }

    public boolean matches(ItemStack stack) {
        if (ignoreCount) {
            return stack.getItem() == inputItem.getItem();
        } else if (softIgnoreCount) {
            return stack.getItem() == inputItem.getItem() && stack.getCount() >= inputItem.getCount();
        }
        return stack.getItem() == inputItem.getItem() && stack.getCount() >= inputItem.getCount();
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }
    public ItemStack getInputItem() {
        return inputItem;
    }
    public boolean isIgnoreCount() {
        return ignoreCount;
    }
    public boolean isSoftIgnoreCount(){
        return softIgnoreCount;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        return false;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return null;
    }

    public ItemStack getResult() {
        return outputItems.isEmpty() ? ItemStack.EMPTY : outputItems.get(0);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<TransmuteRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "shimmer_transmute";
    }

    public static class Serializer implements RecipeSerializer<TransmuteRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "shimmer_transmute";

        @Override
        public TransmuteRecipe read(Identifier id, JsonObject json) {
            JsonObject inputJson = JsonHelper.getObject(json, "input");
            Item inputItem = JsonHelper.getItem(inputJson, "item");
            int inputCount = JsonHelper.getInt(inputJson, "count", 1);
            boolean ignoreCount = JsonHelper.getBoolean(inputJson, "ignore_count", false);
            boolean softIgnoreCount = JsonHelper.getBoolean(inputJson, "soft_ignore_count", false);
            ItemStack inputStack = new ItemStack(inputItem, inputCount);

            List<ItemStack> outputItems = new ArrayList<>();
            JsonArray outputArray = JsonHelper.getArray(json, "output");
            for (int i = 0; i < outputArray.size(); i++) {
                JsonObject outputJson = outputArray.get(i).getAsJsonObject();
                Item outputItem = JsonHelper.getItem(outputJson, "item");
                int outputCount = JsonHelper.getInt(outputJson, "count", 1);
                outputItems.add(new ItemStack(outputItem, outputCount));
            }

            return new TransmuteRecipe(id, inputStack, outputItems, ignoreCount, softIgnoreCount);
        }

        @Override
        public TransmuteRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack inputStack = buf.readItemStack();
            boolean ignoreCount = buf.readBoolean();
            boolean softIgnoreCount = buf.readBoolean();

            int outputSize = buf.readInt();
            List<ItemStack> outputItems = new ArrayList<>(outputSize);
            for (int i = 0; i < outputSize; i++) {
                outputItems.add(buf.readItemStack());
            }

            return new TransmuteRecipe(id, inputStack, outputItems, ignoreCount, softIgnoreCount);
        }

        @Override
        public void write(PacketByteBuf buf, TransmuteRecipe recipe) {
            buf.writeItemStack(recipe.inputItem);
            buf.writeBoolean(recipe.ignoreCount);
            buf.writeBoolean(recipe.softIgnoreCount);

            buf.writeInt(recipe.outputItems.size());
            for (ItemStack output : recipe.outputItems) {
                buf.writeItemStack(output);
            }
        }
    }
}