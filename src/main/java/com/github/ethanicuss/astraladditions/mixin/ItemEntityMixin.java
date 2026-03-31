package com.github.ethanicuss.astraladditions.mixin;

import com.github.ethanicuss.astraladditions.registry.ModEntities;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.registry.ModFluids;
import com.github.ethanicuss.astraladditions.recipes.TransmuteRecipe;
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
    private static final int SHIMMER_COOLDOWN_TIME = 40;
    private static final int MINIMUM_TIME_IN_SHIMMER = 40;
    private static final double SHIMMER_MAX_RAYCAST_DISTANCE = 1.25;
    private static final Set<ItemStack> SHIMMER_PROCESSED_ITEMS = new HashSet<>();
    private int shimmerLastProcessedTick = -SHIMMER_COOLDOWN_TIME;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Shadow
    public abstract ItemStack getStack();


    private boolean isInShimmerFluid() {
        FluidState fluidState = world.getFluidState(getBlockPos());
        Fluid fluid = fluidState.getFluid();
        return fluid == ModFluids.STILL_SHIMMER || fluid == ModFluids.FLOWING_SHIMMER;
    }

    private boolean hasShimmerAbove() {
        BlockPos currentPos = getBlockPos();
        for (int i = 1; i <= SHIMMER_MAX_RAYCAST_DISTANCE; i++) {
            BlockPos checkPos = currentPos.up(i);
            Fluid fluid = world.getFluidState(checkPos).getFluid();
            if (fluid == ModFluids.STILL_SHIMMER || fluid == ModFluids.FLOWING_SHIMMER) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidForTransmutation(ItemStack stack) {
        return !stack.hasEnchantments() && !stack.hasCustomName() && !stack.isIn(ModData.INGORE_TRANSMUTATION);
    }

    private boolean hasBeenShimmerProcessed(ItemStack stack) {
        return SHIMMER_PROCESSED_ITEMS.contains(stack);
    }

    private void markAsShimmerProcessed(ItemStack stack) {
        SHIMMER_PROCESSED_ITEMS.add(stack.copy());
    }

    private void spawnShimmerParticlesAndSound(double yOffset) {
        ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD, getX(), getY() + yOffset, getZ(), 15, 0.1, 0.1, 0.1, 0.15);
        world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_FOX_SNIFF, SoundCategory.NEUTRAL, 0.5f, 0.8f, true);
    }

    private void HandleShimmerBlazeSpawn() {
        if (ModItems.isSacrificeItem(getStack().getItem())) {
            spawnShimmerParticlesAndSound(1);
            if (getStack().getItem() == ModItems.AWAKENED_SHIMMER_HEART && age > 60) {
                spawnShimmerParticlesAndSound(1);
                ShimmerBlazeEntity shimmerBlaze = new ShimmerBlazeEntity(ModEntities.SHIMMER_BLAZE, world);
                shimmerBlaze.refreshPositionAndAngles(getX(), getY(), getZ(), 0.0f, 0.0f);
                shimmerBlaze.setVelocity(0, 1, 0);
                world.spawnEntity(shimmerBlaze);
                discard();
            }
        }
    }

    private void HandleItemShimmerTransmutation() {
        if (isInShimmerFluid() && !hasShimmerAbove() && age >= MINIMUM_TIME_IN_SHIMMER && age - shimmerLastProcessedTick >= SHIMMER_COOLDOWN_TIME) {
            ItemStack itemStack = getStack();

            if (!isValidForTransmutation(itemStack) || hasBeenShimmerProcessed(itemStack)) return;

            Optional<TransmuteRecipe> recipeOptional = world.getRecipeManager()
                    .listAllOfType(TransmuteRecipe.Type.INSTANCE)
                    .stream()
                    .filter(recipe -> recipe.matches(itemStack))
                    .findFirst();

            if (recipeOptional.isPresent()) {
                TransmuteRecipe recipe = recipeOptional.get();
                ProcessShimmerTransmutation(recipe, itemStack);
            }
        }
    }

    private void ProcessShimmerTransmutation(TransmuteRecipe recipe, ItemStack itemStack) {
        ItemStack recipeInputItem = recipe.getInputItem();
        int recipeInputCount = recipeInputItem.getCount();

        int multiplier = calculateShimmerTransmutationMultiplier(recipe, itemStack, recipeInputCount);
        if (multiplier == 0) return;

        itemStack.decrement(multiplier * recipeInputCount);
        spawnShimmerParticlesAndSound(0.5);

        for (ItemStack outputItem : recipe.getOutputItems()) {
            ItemStack outputStack = outputItem.copy();
            outputStack.setCount(outputStack.getCount() * multiplier);

            ItemEntity outputEntity = new ItemEntity(world, getX(), getY() + 0.25, getZ(), outputStack);
            outputEntity.setNoGravity(true);
            outputEntity.setVelocity(0, 0.05, 0);
            world.spawnEntity(outputEntity);
        }

        markAsShimmerProcessed(itemStack);
        if (itemStack.isEmpty()) discard();
        shimmerLastProcessedTick = age;
    }

    private int calculateShimmerTransmutationMultiplier(TransmuteRecipe recipe, ItemStack itemStack, int recipeInputCount) {
        if (recipe.isIgnoreCount()) {
            return itemStack.getCount();
        } else if (recipe.isSoftIgnoreCount()) {
            return itemStack.getCount() / recipeInputCount;
        } else if (itemStack.getCount() == recipeInputCount) {
            return 1;
        }
        return 0;
    }


    @Inject(at = @At("RETURN"), method = "applyWaterBuoyancy", cancellable = true)
    private void applyWaterBuoyancy(CallbackInfo ci) {
        if (!world.isClient()) {
            HandleShimmerBlazeSpawn();
            HandleItemShimmerTransmutation();
        }
    }
}