package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class OHNOItem extends Item {//Orbital Homeward Navigation Orb - O.H-N.O

    public static final String X_KEY = "coord_x";
    public static final String Y_KEY = "coord_y";
    public static final String Z_KEY = "coord_z";

    public OHNOItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbtCompound = itemStack.getOrCreateNbt();
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_DEATH, SoundCategory.NEUTRAL, 0.5f, 0.3f);
        if (world.isClient()) {
            if (!nbtCompound.isEmpty()) {
                AstralAdditions.LOGGER.info("get");
                user.setVelocity(0, 0.2, 0);
                user.setPos(nbtCompound.getDouble(X_KEY), nbtCompound.getDouble(Y_KEY), nbtCompound.getDouble(Z_KEY));
                itemStack.setNbt(new NbtCompound());
                itemStack = new ItemStack(ModItems.OHNO_BROKEN);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack, world.isClient());
            }
        } else {
            AstralAdditions.LOGGER.info("erm");
            AstralAdditions.LOGGER.info(nbtCompound.toString());
            if (nbtCompound.isEmpty()){
                AstralAdditions.LOGGER.info("set");
                double i = user.getX();
                double j = user.getY();
                double k = user.getZ();

                nbtCompound.putDouble(X_KEY, i);
                nbtCompound.putDouble(Y_KEY, j);
                nbtCompound.putDouble(Z_KEY, k);
                user.getItemCooldownManager().set(this, 60);
                return TypedActionResult.success(itemStack, world.isClient());
            } else{
                itemStack = new ItemStack(ModItems.OHNO_BROKEN);
            }
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (itemStack.hasNbt()) {
            NbtCompound nbtCompound = itemStack.getNbt();
            String string = String.join(", ", String.valueOf(Math.round(nbtCompound.getDouble(X_KEY))), String.valueOf(Math.round(nbtCompound.getDouble(Y_KEY))), String.valueOf(Math.round(nbtCompound.getDouble(Z_KEY))));
            if (!StringHelper.isEmpty(string)) {
                tooltip.add(new LiteralText(string).formatted(Formatting.DARK_AQUA));
            }
        }
    }

}
