package com.github.ethanicuss.astraladditions.mixin.fishing;

import com.github.ethanicuss.astraladditions.items.BottomlessBucketItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BottomlessBucketItemMixin {
	@Inject(method = "getEmptiedStack", at = @At("HEAD"), cancellable = true)
	private static void injected_getEmptiedStack(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
		if (stack.getItem() instanceof BottomlessBucketItem) {
			cir.setReturnValue(stack);
		}
	}
}