package com.github.ethanicuss.astraladditions.items;


import com.github.ethanicuss.astraladditions.entities.shimmerfishingrod.ShimmerFishingBobberEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class ShimmerFishingRodItem extends FishingRodItem {

	public ShimmerFishingRodItem(Item.Settings settings) {
		super(settings);
	}

	//* Just mostly yonked from minecraft itself
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);

		if (user.fishHook != null) {
			int i = user.fishHook.use(itemStack);
			itemStack.damage(i, user, (p) -> p.sendToolBreakStatus(hand));
			user.swingHand(hand);
			world.playSound(null, user.getX(), user.getY(), user.getZ(),
					SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, user.getSoundCategory(), 1.0F, 1.0F);
			user.incrementStat(Stats.USED.getOrCreateStat(this));
		} else {
			world.playSound(null, user.getX(), user.getY(), user.getZ(),
					SoundEvents.ENTITY_FISHING_BOBBER_THROW, user.getSoundCategory(), 1.0F, 1.0F);

			if (!world.isClient) {
				int luckOfTheSeaLevel = EnchantmentHelper.getLuckOfTheSea(itemStack);
				int lureLevel = EnchantmentHelper.getLure(itemStack);
				ShimmerFishingBobberEntity bobber = new ShimmerFishingBobberEntity(user, world, luckOfTheSeaLevel, lureLevel);

				world.spawnEntity(bobber);
			}

			user.swingHand(hand);
			user.incrementStat(Stats.USED.getOrCreateStat(this));
		}

		return TypedActionResult.success(itemStack, world.isClient());
	}

}