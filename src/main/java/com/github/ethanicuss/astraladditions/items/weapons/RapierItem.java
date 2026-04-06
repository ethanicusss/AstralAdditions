package com.github.ethanicuss.astraladditions.items.weapons;

import com.github.ethanicuss.astraladditions.registry.ModEffects;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class RapierItem extends Item {

    private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    public RapierItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 3, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", 0, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    private float dash = 2.6f;
    private boolean backDash = false;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
//        if (itemStack.hasNbt()) {
//            user.getStackInHand(hand).setNbt(new NbtCompound());}
        if (!world.isClient()) {
            if (!backDash) {
//                This was meant to change atributes on dash type but I decided that'd be too many features for a single weapon
//                ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
//                builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 12, EntityAttributeModifier.Operation.ADDITION));
//                builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3, EntityAttributeModifier.Operation.ADDITION));
//                this.attributeModifiers = builder.build();
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GOAT_LONG_JUMP, SoundCategory.NEUTRAL, 0.8f, 1.2f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            user.addStatusEffect(new StatusEffectInstance(ModEffects.PARRY, 9, 2));
            MinecraftClient.getInstance().player.setVelocity(Math.sin(Math.toRadians(-user.getYaw()))*dash, 0, Math.cos(Math.toRadians(-user.getYaw()))*dash);
            user.setVelocity(Math.sin(Math.toRadians(-user.getYaw())) * 0.45, Math.sin(Math.toRadians(-user.getPitch())) * 0.45, Math.cos(Math.toRadians(-user.getYaw())) * 0.45);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
                user.getItemCooldownManager().set(this, 8);
            backDash = true;
            } else {
//                ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
//                builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 8, EntityAttributeModifier.Operation.ADDITION));
//                builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", 0, EntityAttributeModifier.Operation.ADDITION));
//                this.attributeModifiers = builder.build();
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GOAT_LONG_JUMP, SoundCategory.NEUTRAL, 0.8f, 0.6f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                user.addStatusEffect(new StatusEffectInstance(ModEffects.PARRY, 9, 3));
                MinecraftClient.getInstance().player.setVelocity(Math.sin(Math.toRadians(-user.getYaw()))*-dash*1.1, 0, Math.cos(Math.toRadians(-user.getYaw()))*-dash*1.1);
                user.setVelocity(Math.sin(Math.toRadians(-user.getYaw())) * 0.45, Math.sin(Math.toRadians(-user.getPitch())) * 0.45, Math.cos(Math.toRadians(-user.getYaw())) * 0.45);
                user.getItemCooldownManager().set(this, 60);
                backDash = false;
            }
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

//    public boolean parry(DamageSource source, PlayerEntity user) {
//        if (user.hasStatusEffect(StatusEffects.RESISTANCE) && !source.isMagic() && source.getSource() instanceof LivingEntity) {
//            LivingEntity livingEntity = (LivingEntity)source.getSource();
//            if (!source.isExplosive()) {
//                livingEntity.damage(DamageSource.thorns(user), 2.0F);
//            }
//        }
//        return super.damage(source);
//    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

}
