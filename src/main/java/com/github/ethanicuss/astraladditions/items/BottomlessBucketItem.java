package com.github.ethanicuss.astraladditions.items;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;


public class BottomlessBucketItem extends BucketItem {
	public BottomlessBucketItem(Fluid fluid, Settings settings) {
		super(fluid, settings);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}