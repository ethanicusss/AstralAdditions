package com.github.ethanicuss.astraladditions.mixin;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.registry.ModData;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;


@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private static final int COOLDOWN_TIME = 40;
    private static final int ITEM_LIFETIME = 200;
    private static final Set<ItemStack> processedItems = new HashSet<>();

    private int lastProcessedTick = -COOLDOWN_TIME;
    private boolean spawnedIngredients = false;

    @Shadow
    public abstract ItemStack getStack();


    private boolean isInShimmerFluid() {
        BlockPos pos = this.getBlockPos();
        FluidState fluidState = this.world.getFluidState(pos);
        Fluid fluid = fluidState.getFluid();
        return fluid == ModFluids.STILL_SHIMMER || fluid == ModFluids.FLOWING_SHIMMER;
    }

    private boolean isValidItemForTransformation(ItemStack stack) {
        return !stack.hasEnchantments() && !stack.hasCustomName();
    }

    private boolean isIgnoredByShimmerTransmutation(ItemStack stack) {
        return stack.isIn(ModData.INGORE_TRANSMUTATION);
    }

    private boolean hasBeenProcessed(ItemStack stack) {
        return processedItems.contains(stack);
    }

    private void markAsProcessed(ItemStack stack) {
        processedItems.add(stack.copy());
    }

    @Inject(at = @At("RETURN"), method = "applyWaterBuoyancy", cancellable = true)
    private void applyWaterBuoyancy(CallbackInfo ci) {
        if (!this.world.isClient()) {
            if (ModItems.isSacrificeItem(this.getStack().getItem())) {

                ModUtils.spawnForcedParticles((ServerWorld) this.world, ParticleTypes.END_ROD, this.getX(), this.getY() + 1, this.getZ(), 3, 0.2, 0.2, 0.2, 0.05);
                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_FOX_SNIFF, SoundCategory.HOSTILE, 0.5f, 0.8f, true);
                if (this.getStack().getItem() == ModItems.AWAKENED_SHIMMER_HEART) {
                    if (this.age > 60) {
                        ModUtils.spawnForcedParticles((ServerWorld) this.world, ParticleTypes.END_ROD, this.getX(), this.getY() + 1, this.getZ(), 15, 0.1, 1, 0.1, 0.15);
                        ShimmerBlazeEntity shimmerBlaze = new ShimmerBlazeEntity(ModEntities.SHIMMER_BLAZE, this.world);
                        shimmerBlaze.setPosition(this.getX(), this.getY(), this.getZ());
                        shimmerBlaze.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f);
                        shimmerBlaze.setVelocity(0, 1, 0);
                        this.world.spawnEntity(shimmerBlaze);
                        this.discard();
                        return;
                    }
                }
            }

            if (isInShimmerFluid()) {
                if (this.age - lastProcessedTick >= COOLDOWN_TIME) {
                    if (!isValidItemForTransformation(this.getStack()) || isIgnoredByShimmerTransmutation(this.getStack())) {
                        return;
                    }
                    if (hasBeenProcessed(this.getStack())) {
                        return;
                    }
                    Optional<CraftingRecipe> recipeOptional = this.world.getRecipeManager()
                            .listAllOfType(RecipeType.CRAFTING)
                            .stream()
                            .filter(recipe -> recipe.getOutput().isItemEqual(this.getStack()))
                            .findFirst();

                    if (recipeOptional.isPresent()) {
                        CraftingRecipe recipe = recipeOptional.get();
                        if (!spawnedIngredients) {
                            ModUtils.spawnForcedParticles((ServerWorld) this.world, ParticleTypes.END_ROD, this.getX(), this.getY() + 0.5, this.getZ(), 15, 0.1, 0.1, 0.1, 0.15);
                            for (Ingredient ingredient : recipe.getIngredients()) {
                                for (ItemStack stack : ingredient.getMatchingStacks()) {
                                    ItemEntity ingredientEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), stack.copy());
                                    ingredientEntity.setNoGravity(true);
                                    ingredientEntity.setVelocity(0, 0.05, 0);
                                    this.world.spawnEntity(ingredientEntity);
                                }
                            }
                            spawnedIngredients = true;
                        }
                        markAsProcessed(this.getStack());
                        this.discard();
                        lastProcessedTick = this.age;
                    }
                }
            }
            if (spawnedIngredients && this.age - lastProcessedTick > ITEM_LIFETIME) {
                this.discard();
            }
        }
    }
}