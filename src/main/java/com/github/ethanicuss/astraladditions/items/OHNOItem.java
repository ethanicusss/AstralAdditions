package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class OHNOItem extends Item {//Orbital Homeward Navigation Orb - O.H-N.O //doesn't work across dimensions as I want it to.

    public static final String X_KEY = "coord_x";
    public static final String Y_KEY = "coord_y";
    public static final String Z_KEY = "coord_z";
    public static final String WORLD_KEY = "world";

    public OHNOItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbtCompound = itemStack.getOrCreateNbt();

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.NEUTRAL, 0.5f, 0.5f);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.NEUTRAL, 0.5f, 0.5f);
        if (world.isClient()) {
            if (!nbtCompound.isEmpty()) {
                user.setVelocity(0, 0.2, 0);
                itemStack = new ItemStack(ModItems.OHNO_BROKEN);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack, world.isClient());
            }
        } else {//server
            if (nbtCompound.isEmpty()){
                double i = user.getX();
                double j = user.getY();
                double k = user.getZ();

                nbtCompound.putDouble(X_KEY, i);
                nbtCompound.putDouble(Y_KEY, j);
                nbtCompound.putDouble(Z_KEY, k);
                nbtCompound.putString(WORLD_KEY, user.world.getRegistryKey().getValue().toString());

                user.getItemCooldownManager().set(this, 60);
                return TypedActionResult.success(itemStack, world.isClient());
            } else{
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 5*20, 10));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5*20, 0));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*20, 10));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20*20, 0));
                ((ServerPlayerEntity) user).networkHandler.requestTeleport(nbtCompound.getDouble(X_KEY), nbtCompound.getDouble(Y_KEY), nbtCompound.getDouble(Z_KEY), user.getYaw(), user.getPitch(), EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
                AstralAdditions.worldRegister.LoadData(world);
                ServerWorld gotoWorld = AstralAdditions.worldRegister.getWorld(nbtCompound.getString(WORLD_KEY));
                if (gotoWorld != null) {
                    Vec3d tpPos = new Vec3d(nbtCompound.getDouble(X_KEY), nbtCompound.getDouble(Y_KEY), nbtCompound.getDouble(Z_KEY));
                    Vec3d tpVel = new Vec3d(0, 0.5, 0);
                    TeleportTarget target = new TeleportTarget(tpPos, tpVel, user.getYaw(), user.getPitch());
                    FabricDimensions.teleport(user, gotoWorld, target);// i love fabric
                    user.refreshPositionAndAngles(nbtCompound.getDouble(X_KEY), nbtCompound.getDouble(Y_KEY), nbtCompound.getDouble(Z_KEY), user.getYaw(), user.getPitch());
                    itemStack = new ItemStack(ModItems.OHNO_BROKEN);
                } else{
                    itemStack = new ItemStack(ModItems.OHNO);
                }
            }
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (itemStack.hasNbt()) {
            NbtCompound nbtCompound = itemStack.getNbt();
            String string = String.join(", ", String.valueOf(Math.round(nbtCompound.getDouble(X_KEY))), String.valueOf(Math.round(nbtCompound.getDouble(Y_KEY))), String.valueOf(Math.round(nbtCompound.getDouble(Z_KEY))), "in " + String.valueOf(nbtCompound.getString(WORLD_KEY)));
            if (!StringHelper.isEmpty(string)) {
                tooltip.add(new LiteralText(string).formatted(Formatting.DARK_AQUA));
            }
        }
    }

}
