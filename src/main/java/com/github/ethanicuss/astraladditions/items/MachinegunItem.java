package com.github.ethanicuss.astraladditions.items;

import net.minecraft.client.MinecraftClient;
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

public class MachinegunItem extends BowItem {

    public MachinegunItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }

        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        float f = getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks);

        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }
        boolean bl2;
        boolean bl = playerEntity.getAbilities().creativeMode;
        ItemStack itemStack = playerEntity.getArrowType(stack);
        boolean bl3 = bl2 = bl && itemStack.isOf(Items.ARROW);
        if ((f*20)%11 == 0 && f > 0) {
            if (world.isClient){
                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.3f, 0.5f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 1.0f);
            }

            if (!world.isClient && !itemStack.isEmpty()) {
                int k;
                int j;
                ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 1.0f, 4.0f, 0.6f);
                persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * 1);
                stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
                if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                    persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
                world.spawnEntity(persistentProjectileEntity);
            }
            if (!itemStack.isEmpty() || bl) {
                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 0.5f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 0.3f);
            }
            if (!bl2 && !bl) {
                itemStack.decrement(1);
                if (itemStack.isEmpty()) {
                    playerEntity.getInventory().removeOne(itemStack);
                }
            }
        }
    }

    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0f;

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
