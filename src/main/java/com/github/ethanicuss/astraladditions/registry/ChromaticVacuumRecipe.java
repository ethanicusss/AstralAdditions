package com.github.ethanicuss.astraladditions.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ChromaticVacuumRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final ItemStack remainder;
    private final DefaultedList<Ingredient> recipeItems;

    public ChromaticVacuumRecipe(Identifier id, ItemStack output, ItemStack remainder, DefaultedList<Ingredient> recipeItems){
        this.id = id;
        this.output = output;
        this.remainder = remainder;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        return recipeItems.get(0).test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    public ItemStack getRemainder() {
        return remainder.copy();
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

    public static class Type implements RecipeType<ChromaticVacuumRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "chromatic_vacuum";
    }

    public static class Serializer implements RecipeSerializer<ChromaticVacuumRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "chromatic_vacuum";
        // this is the name given in the json file

        @Override
        public ChromaticVacuumRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
            ItemStack remainder = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "remainder"));

            JsonObject ingredient = JsonHelper.getObject(json, "ingredient");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);

            inputs.set(0, Ingredient.fromJson(ingredient));

            return new ChromaticVacuumRecipe(id, output, remainder, inputs);
        }

        @Override
        public ChromaticVacuumRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);
            ItemStack remainder = new ItemStack(Items.DIRT);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new ChromaticVacuumRecipe(id, output, remainder, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, ChromaticVacuumRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
