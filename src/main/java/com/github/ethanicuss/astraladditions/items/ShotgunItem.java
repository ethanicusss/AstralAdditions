package com.github.ethanicuss.astraladditions.items;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ShotgunItem extends BowItem {

    public ShotgunItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }
        boolean bl2;
        boolean bl = playerEntity.getAbilities().creativeMode;
        ItemStack itemStack = playerEntity.getArrowType(stack);
        boolean bl3 = bl2 = bl && itemStack.isOf(Items.ARROW);
        float f = getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks);

        if (!world.isClient && !itemStack.isEmpty()) {
            int k;
            int j;
            for (var i = 0; i < 15; i++) {
                ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 1.0f, 2.0f + f-1, 13f);
                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * 1);
                stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
                if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                    persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
                world.spawnEntity(persistentProjectileEntity);
            }
        }
        if (!itemStack.isEmpty() || bl) {
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.8f, 0.5f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 0.3f);
        }
        if (!bl2 && !bl) {
            itemStack.decrement(1);
            if (itemStack.isEmpty()) {
                playerEntity.getInventory().removeOne(itemStack);
            }
        }

        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        float f = getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks);

        if (world.isClient) {
            if ((f * 20) % 5 == 0 && f < 1.0f) {
                MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_STONE_HIT, 0.3f, 0.5f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 1.0f + f / 2);
            }
            if (f == 1 || f == 1.9) {
                MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8f, 0.5f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 1.0f + f / 2);
            }
        }
    }

    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0f;
        if (f > 2.0f){
            f = 2.0f;
        }
        return f;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        boolean bl;
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl2 = bl = user.getArrowType(itemStack).getCount() > 0;
        if (user.getAbilities().creativeMode || bl) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }
}
