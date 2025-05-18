package com.github.ethanicuss.astraladditions.registry;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.util.Identifier;

public class ModItemProperties {

	public static void register() {
		ModelPredicateProviderRegistry.register(ModItems.SHIMMER_FISHING_ROD, new Identifier("cast"), (stack, world, entity, seed) -> {
			if (entity instanceof PlayerEntity player) {
				boolean usingMainHand = player.getMainHandStack() == stack;
				boolean usingOffHand = player.getOffHandStack() == stack;
				if (player.getMainHandStack().getItem() instanceof FishingRodItem) {
					usingOffHand = false;
				}
				return (usingMainHand || usingOffHand) && player.fishHook != null ? 1.0F : 0.0F;
			}
			return 0.0F;
		});
}}