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
import net.minecraft.server.network.ServerPlayerEntity;
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

public class LunarWackerItem extends Item {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public static final String VARIANT_KEY = "Variant";

    public LunarWackerItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double)5, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", (double)-3, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        NbtCompound nbtCompound = stack.getOrCreateNbt();
        String variant = nbtCompound.getString(VARIANT_KEY);

        attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.BLOCK_SHROOMLIGHT_PLACE, SoundCategory.NEUTRAL, 1.1f, 0.8f);
        attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.NEUTRAL, 1.0f, 1.2f);
        if (!attacker.world.isClient()) {
            stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        switch (variant) {
            case "Withering":
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 1));
                break;
            case "Liftoff":
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20, 8));
                break;
            default:
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 45, 10));
        }
        double speed = 1.5;
        double dx = Math.sin(Math.toRadians(-attacker.getYaw())) * speed;
        double dy = Math.sin(Math.toRadians(-attacker.getPitch())) * speed;
        double dz = Math.cos(Math.toRadians(-attacker.getYaw())) * speed;
        target.setVelocity(dx, dy/2 + 0.6, dz);
        ModUtils.spawnForcedParticles((ServerWorld)attacker.world, ParticleTypes.CLOUD, target.getX(), target.getY()-1, target.getZ(), 3, 0.5 * target.world.getRandom().nextFloat(), 0.3, 0.5 * target.world.getRandom().nextFloat(), 0.2);
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
            tooltip.add(new LiteralText("Boing!").formatted(Formatting.DARK_PURPLE));
            String string = String.join(" ","Variant:", String.valueOf(nbtCompound.getString(VARIANT_KEY)));
            if (!StringHelper.isEmpty(string)) {
                tooltip.add(new LiteralText(string).formatted(Formatting.DARK_AQUA));
            }
        }
    }
}
