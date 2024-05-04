package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.SmallShimmerballEntity;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class ShimmerBlowerItem extends Item {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public ShimmerBlowerItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double)7, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", (double)-2, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage()<itemStack.getMaxDamage()) {
            if (world.isClient()) {
                user.setPitch(user.getPitch() - 1);
            } else {
                ModUtils.playSound((ServerWorld) user.world, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SHULKER_OPEN, SoundCategory.HOSTILE, 1.0f, 1.1f + user.world.random.nextFloat() * 0.2f, true);
                user.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 0.5f, 1.1f + user.world.random.nextFloat() * 0.2f);
                double px = user.getX();
                double py = user.getY() + 1;
                double pz = user.getZ();
                double speed = 0.4;
                double spread = 0.05;
                double tx = Math.sin(Math.toRadians(-user.getYaw())) * speed;
                double ty = Math.sin(Math.toRadians(-user.getPitch())) * speed;
                double tz = Math.cos(Math.toRadians(-user.getYaw())) * speed;
                SmallShimmerballEntity smallShimmerballEntity = new SmallShimmerballEntity(ModEntities.SMALL_SHIMMERBALL, user.world);
                smallShimmerballEntity.setPosition(user.getX(), user.getBodyY(0.5) + 0.5, user.getZ());
                smallShimmerballEntity.refreshPositionAndAngles(user.getX(), user.getBodyY(0.5) + 0.5, user.getZ(), 0.0f, 0.0f);
                smallShimmerballEntity.setVelocity(tx + user.getRandom().nextGaussian() * spread, ty + user.getRandom().nextGaussian() * spread, tz + user.getRandom().nextGaussian() * spread);
                user.world.spawnEntity(smallShimmerballEntity);
                itemStack.damage(1, world.random, (ServerPlayerEntity) user);
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE, SoundCategory.NEUTRAL, 0.5f, 0.5f);
        stack.setDamage(stack.getDamage()-2);
        if (stack.getDamage()<0){
            stack.setDamage(0);
        }
        ModUtils.spawnForcedParticles((ServerWorld)attacker.world, ParticleTypes.WITCH, target.getX(), target.getY(), target.getZ(), 3, 0.5 * target.world.getRandom().nextFloat(), 0.3, 0.5 * target.world.getRandom().nextFloat(), 0.2);
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }
}
