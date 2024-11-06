package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.util.List;

public class ShimmerBottleItem extends Item {

    StatusEffectInstance glowing = new StatusEffectInstance(StatusEffects.GLOWING, 600);
    StatusEffectInstance vision = new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600);

    private static final int MAX_USE_TIME = 40;

    public ShimmerBottleItem(StatusEffect effect, Integer time, Integer amp, MutableText descrip, Item.Settings settings){
        super(settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user){
        ItemStack itemStack = super.finishUsing(stack, world, user);
        if (!world.isClient) {
            PlayerEntity player = (PlayerEntity)user;
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 600, 0, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0, false, false));
            player.playSound(SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, SoundCategory.AMBIENT, 0.8f, 0.8f);
            return user instanceof PlayerEntity && ((PlayerEntity) user).getAbilities().creativeMode ? itemStack : new ItemStack(Items.GLASS_BOTTLE);
        }

        return itemStack;
    }
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText(AstralAdditions.MOD_ID + ".text.shimmer_bottle"));
    }

    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public SoundEvent getDrinkSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
