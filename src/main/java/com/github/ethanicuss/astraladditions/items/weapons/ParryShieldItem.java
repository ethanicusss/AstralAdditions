package com.github.ethanicuss.astraladditions.items.weapons;

import com.github.ethanicuss.astraladditions.registry.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ParryShieldItem extends Item {
    public ParryShieldItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.NEUTRAL, 0.6f, 0.6f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            user.addStatusEffect(new StatusEffectInstance(ModEffects.PARRY, 4, 6));
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.getItemCooldownManager().set(this, 40);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

}
