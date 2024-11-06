package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntity;
import com.github.ethanicuss.astraladditions.entities.meteor_mitts.MeteorPunchEntity;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class MeteorMittsItem extends Item {

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public MeteorMittsItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double)7, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", (double)-2.5, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.NEUTRAL, 0.5f, 0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (world.isClient()) {
        } else {
            MeteorPunchEntity punchEntity = new MeteorPunchEntity(ModEntities.METEOR_FIST, user.getWorld());
            punchEntity.setOwner(user);
            punchEntity.setPitch(-user.getPitch());
            punchEntity.setYaw(-user.getYaw());
            punchEntity.updatePosition(user.getX(), user.getEyeY() - 0.3f, user.getZ());
            punchEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0.5f);
            if (user.isSneaking()) {
                punchEntity.setVelocity(Math.sin(Math.toRadians(-user.getYaw()))*0.5, 2, Math.cos(Math.toRadians(-user.getYaw()))*0.5);
                punchEntity.updatePosition(punchEntity.getX() + Math.sin(Math.toRadians(-user.getYaw()))*1.5, /*punchEntity.getY() + Math.sin(Math.toRadians(user.getPitch())) * -1 - 2.5*/ user.getY()-1, punchEntity.getZ() + Math.cos(Math.toRadians(-user.getYaw()))*1.5);
            }
            world.spawnEntity(punchEntity);
            itemStack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        user.getItemCooldownManager().set(this, 7);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.world.isClient()) {
            stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        target.setVelocity(0, 0.5, 0);
        target.setYaw(target.getYaw()+180);
        attacker.world.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.NEUTRAL, 1.5f, 0.5f);
        attacker.world.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.NEUTRAL, 1.5f, 0.6f);
        for (int i = 0; i < 5; i++) {
            attacker.world.addParticle(ParticleTypes.END_ROD, target.getX(), target.getY(), target.getZ(), 0.5 * target.world.getRandom().nextFloat(), 0.3, 0.5 * target.world.getRandom().nextFloat());
        }
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0f) {
            stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

}
