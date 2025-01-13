package com.github.ethanicuss.astraladditions.mixin;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
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
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private static final int COOLDOWN_TIME = 40;
    private static final int MINIMUM_TIME_IN_SHIMMER = 40;
    private static final double MAX_RAYCAST_DISTANCE = 1.25;
    private static final Set<ItemStack> processedItems = new HashSet<>();

    private int lastProcessedTick = -COOLDOWN_TIME;


    @Shadow
    public abstract ItemStack getStack();


    private boolean isInShimmerFluid() {
        BlockPos pos = this.getBlockPos();
        FluidState fluidState = this.world.getFluidState(pos);
        Fluid fluid = fluidState.getFluid();
        return fluid == ModFluids.STILL_SHIMMER || fluid == ModFluids.FLOWING_SHIMMER;
    }

    private boolean hasShimmerAbove() {
        BlockPos currentPos = this.getBlockPos();
        for (int i = 1; i <= MAX_RAYCAST_DISTANCE; i++) {
            BlockPos checkPos = currentPos.up(i);
            FluidState fluidState = this.world.getFluidState(checkPos);
            Fluid fluid = fluidState.getFluid();
            if (fluid == ModFluids.STILL_SHIMMER || fluid == ModFluids.FLOWING_SHIMMER) {
                return true;
            }
        }
        return false;
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
            //* ShimmerBlaze
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
            //* Item Transmute
            if (isInShimmerFluid() && !hasShimmerAbove()) {
                if (this.age >= MINIMUM_TIME_IN_SHIMMER && this.age - lastProcessedTick >= COOLDOWN_TIME) {
                    ItemStack itemStack = this.getStack();

                    if (!isValidItemForTransformation(itemStack) || isIgnoredByShimmerTransmutation(itemStack)) {
                        return;
                    }
                    if (hasBeenProcessed(itemStack)) {
                        return;
                    }
                    Optional<TransmuteRecipe> recipeOptional = this.world.getRecipeManager()
                            .listAllOfType(TransmuteRecipe.Type.INSTANCE)
                            .stream()
                            .filter(recipe -> recipe.matches(itemStack))
                            .findFirst();

                    if (recipeOptional.isPresent()) {
                        TransmuteRecipe recipe = recipeOptional.get();
                        ItemStack recipeInputItem = recipe.getInputItem();
                        int recipeInputCount = recipeInputItem.getCount();

                        if (recipe.isIgnoreCount() ||
                                (recipe.isSoftIgnoreCount() && itemStack.getCount() >= recipeInputCount) ||
                                (itemStack.getItem() == recipeInputItem.getItem() && itemStack.getCount() == recipeInputCount)) {

                            ModUtils.spawnForcedParticles((ServerWorld) this.world, ParticleTypes.END_ROD, this.getX(), this.getY() + 0.5, this.getZ(), 15, 0.1, 0.1, 0.1, 0.15);
                            this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_FOX_SNIFF, SoundCategory.NEUTRAL, 0.5f, 0.8f, true);

                            int multiplier = 1;

                            if (recipe.isSoftIgnoreCount()) {
                                multiplier = itemStack.getCount() / recipeInputCount;
                                itemStack.decrement(multiplier * recipeInputCount);
                            }
                            else if (recipe.isIgnoreCount()) {
                                multiplier = itemStack.getCount();
                                itemStack.decrement(itemStack.getCount());
                            }
                            else {
                                itemStack.decrement(recipeInputCount);
                            }

                            for (ItemStack outputItem : recipe.getOutputItems()) {
                                ItemStack outputStack = outputItem.copy();
                                outputStack.setCount(outputStack.getCount() * multiplier);

                                ItemEntity outputEntity = new ItemEntity(this.world, this.getX(), this.getY() + 0.25, this.getZ(), outputStack);
                                outputEntity.setNoGravity(true);
                                outputEntity.setVelocity(0, 0.05, 0);
                                this.world.spawnEntity(outputEntity);
                            }

                            markAsProcessed(itemStack);

                            if (itemStack.isEmpty()) {
                                this.discard();
                            }

                            lastProcessedTick = this.age;
                        }
                    }
                }
            }
        }
    }
}