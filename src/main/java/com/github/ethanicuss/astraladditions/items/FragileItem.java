package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class FragileItem extends Item {

    Identifier parts;

    public FragileItem(Settings settings, Identifier _parts) {
        super(settings);
        parts = _parts;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {
            LootTable lootTable = world.getServer().getLootManager().getTable(parts);
            PlayerEntity player = (PlayerEntity)entity;
            player.getInventory().setStack(slot, ItemStack.EMPTY);
            ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.SMOKE, entity.getX(), entity.getY(), entity.getZ(), 15, 0.1, 0.1, 0.1, 0.3);
            List<ItemStack> itemList = lootTable.generateLoot((new LootContext.Builder((ServerWorld) world)).parameter(LootContextParameters.ORIGIN, new Vec3d(entity.getX(), entity.getY(), entity.getZ())).random(world.random).build(LootContextTypes.COMMAND));
            if (!itemList.isEmpty()) {
                Iterator var3 = itemList.iterator();
                while (var3.hasNext()) {
                    ItemStack itemStackGive = (ItemStack) var3.next();
                    ((PlayerEntity) entity).dropItem(itemStackGive, true, true);
                }
            }
        }
        else{
            entity.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.1f, 0.9f);
        }
    }
}
