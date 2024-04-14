package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.registry.ModItems;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.StringHelper;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OHNOBrokenItem extends Item {//Orbital Homeward Navigation Orb - O.H-N.O

    public static final String SFX_KEY = "has_played_sfx";

    public OHNOBrokenItem(Settings settings) {
        super(settings);
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        if (nbtCompound.isEmpty()) {
            nbtCompound.putBoolean(SFX_KEY, true);
            entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_DEATH, SoundCategory.NEUTRAL, 0.2f, 0.6f);
            entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 0.5f, 0.1f);
            entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 0.5f, 0.8f);
            if (!world.isClient()) {
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 20, 0.2, 0.2, 0.2, 0.05);
            }
        }
    }
}
