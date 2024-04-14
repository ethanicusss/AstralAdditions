package com.github.ethanicuss.astraladditions.mixin;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    @Inject(at = @At("RETURN"), method = "applyWaterBuoyancy", cancellable = true)
    private void applyWaterBuoyancy(CallbackInfo ci) {
        if (!this.world.isClient()) {
            if (!ModItems.isSacrificeItem(this.getStack().getItem())){
                return;
            }
            ModUtils.spawnForcedParticles((ServerWorld) this.world, ParticleTypes.END_ROD, this.getX(), this.getY() + 1, this.getZ(), 3, 0.2, 0.2, 0.2, 0.05);
            this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_FOX_SNIFF, SoundCategory.HOSTILE, 0.5f, 0.8f, true);
            if (this.getStack().getItem() == ModItems.AWAKENED_SHIMMER_HEART){
                if (this.age > 60) {
                    ModUtils.spawnForcedParticles((ServerWorld) this.world, ParticleTypes.END_ROD, this.getX(), this.getY() + 1, this.getZ(), 15, 0.1, 1, 0.1, 0.15);
                    ShimmerBlazeEntity shimmerBlaze = new ShimmerBlazeEntity(ModEntities.SHIMMER_BLAZE, this.world);
                    shimmerBlaze.setPosition(this.getX(), this.getY(), this.getZ());
                    shimmerBlaze.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f);
                    shimmerBlaze.setVelocity(0, 1, 0);
                    this.world.spawnEntity(shimmerBlaze);
                    this.discard();
                }
            }
        }
    }
}
