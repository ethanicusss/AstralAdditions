package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.util.ModUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulstealDaggerItem extends Item {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public static final String VARIANT_KEY = "Variant";

    public SoulstealDaggerItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double)4.5, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", (double)-2, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        NbtCompound nbtCompound = stack.getOrCreateNbt();
        String variant = nbtCompound.getString(VARIANT_KEY);

        attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.NEUTRAL, 1.1f, 0.8f);
        attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.BLOCK_SOUL_SOIL_HIT, SoundCategory.NEUTRAL, 1.0f, 1.2f);
        if (!attacker.world.isClient()) {
            stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }

        switch (variant) {
            case "Life":
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 2));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 90, 0));
                break;
            case "Death":
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 0));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 120, 3));
                break;
            default:
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 0));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 90, 0));
                break;
        }
        ModUtils.spawnForcedParticles((ServerWorld)attacker.world, ParticleTypes.SOUL_FIRE_FLAME, target.getX(), target.getY()-0.5, target.getZ(), 3, 0.5 * target.world.getRandom().nextFloat(), 0.3, 0.5 * target.world.getRandom().nextFloat(), 0.1);
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

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (itemStack.hasNbt()) {
            NbtCompound nbtCompound = itemStack.getNbt();
            tooltip.add(new LiteralText("Steal the life of foes").formatted(Formatting.DARK_PURPLE));
            if (String.valueOf(nbtCompound.getString(VARIANT_KEY)) != "") {
                String string = String.join(" ", "Variant:", String.valueOf(nbtCompound.getString(VARIANT_KEY)));
                if (!StringHelper.isEmpty(string)) {
                    tooltip.add(new LiteralText(string).formatted(Formatting.DARK_AQUA));
                }
            }
        }
    }

}
