package com.github.ethanicuss.astraladditions.items.weapons;

import com.github.ethanicuss.astraladditions.registry.ModEntities;
import com.github.ethanicuss.astraladditions.entities.scrap_projectile.ScrapProjectileEntity;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class ShotgunItem extends BowItem {

    public ShotgunItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }
        boolean bl2 = false;
        boolean isCreative = playerEntity.getAbilities().creativeMode;

        Predicate<ItemStack> predicate = (ammoStack) -> ammoStack.isOf(ModItems.SPUD_MAG);

        PlayerEntity p = (PlayerEntity) user;

        ItemStack itemStack = stack;//set to stack just to init

        for(int i = 0; i < p.getInventory().size(); ++i) {
            ItemStack itemStack2 = p.getInventory().getStack(i);
            if (predicate.test(itemStack2)) {
                bl2 = true;
                itemStack = itemStack2;
            }
        }

        float f = getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks);

        if (!world.isClient && (isCreative || bl2)) {
            int k;
            int j;
            for (var i = 0; i < 15; i++) {
                ScrapProjectileEntity proj = new ScrapProjectileEntity(ModEntities.SCRAP_PROJECTILE, world);
                proj.setPos(playerEntity.getX(), playerEntity.getEyeY(), playerEntity.getZ());
                proj.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 1.0f, 1.5f + f-1, 12f);
                world.spawnEntity(proj);
            }
            stack.damage(1, playerEntity, p2 -> p2.sendToolBreakStatus(playerEntity.getActiveHand()));
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.8f, 0.5f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 0.3f);
        }
        if (bl2 && !isCreative) {
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