package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.SmallShimmerballEntity;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.StringHelper;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ShimmerBlowerItem extends Item {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public static final String VARIANT_KEY = "Variant";
    public static final String HELDTIME_KEY = "HeldTime";
    public static final String NOTHELDTIME_KEY = "NotHeldTime";

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

        NbtCompound nbtCompound = itemStack.getOrCreateNbt();
        String variant = nbtCompound.getString(VARIANT_KEY);
        int heldTime = nbtCompound.getInt(HELDTIME_KEY);

        if (itemStack.getDamage()<itemStack.getMaxDamage()) {
            if (world.isClient()) {
                user.setPitch(user.getPitch() - 1);
            } else {
                //ModUtils.playSound((ServerWorld) user.world, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SHULKER_OPEN, SoundCategory.HOSTILE, 1.0f, 1.1f + user.world.random.nextFloat() * 0.2f, true);
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SHULKER_OPEN, SoundCategory.HOSTILE, 1.0f, 1.1f + user.world.random.nextFloat() * 0.2f);
                user.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 0.5f, 1.1f + user.world.random.nextFloat() * 0.2f);
                double px = user.getX();
                double py = user.getY() + 1;
                double pz = user.getZ();
                double speed = 0.4;
                double spread = 0.05;
                //if (Objects.equals(variant, "Pin point")) { spread = 0.01; speed = 0.5; }
                if (Objects.equals(variant, "Zooming")) { speed = 1; spread = 0.04; }
                if (user.isSneaking()) { spread = 0.02;}
                double tx = Math.sin(Math.toRadians(-user.getYaw())) * speed;
                double ty = Math.sin(Math.toRadians(-user.getPitch())) * speed;
                double tz = Math.cos(Math.toRadians(-user.getYaw())) * speed;
                if (!Objects.equals(variant, "Fuming")) {
                    SmallShimmerballEntity smallShimmerballEntity = new SmallShimmerballEntity(ModEntities.SMALL_SHIMMERBALL, user.world);
                    smallShimmerballEntity.setPosition(user.getX(), user.getBodyY(0.5) + 0.5, user.getZ());
                    smallShimmerballEntity.refreshPositionAndAngles(user.getX(), user.getBodyY(0.5) + 0.5, user.getZ(), 0.0f, 0.0f);
                    smallShimmerballEntity.setVelocity(tx + user.getRandom().nextGaussian() * spread, ty + user.getRandom().nextGaussian() * spread, tz + user.getRandom().nextGaussian() * spread);
                    user.world.spawnEntity(smallShimmerballEntity);
                }
                else{
                    //ModUtils.playSound((ServerWorld) user.world, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 1.0f, 0.5f + heldTime/25.0f, true);
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1.0f, 0.5f + heldTime/25.0f);
                    nbtCompound.putInt(HELDTIME_KEY, heldTime+1);
                    nbtCompound.putInt(NOTHELDTIME_KEY, 30);
                    tx = px + Math.sin(Math.toRadians(-user.getYaw()))*16;
                    ty = py + Math.sin(Math.toRadians(-user.getPitch()))*16;
                    tz = pz + Math.cos(Math.toRadians(-user.getYaw()))*16;
                    double dx = tx-px;
                    double dy = ty-py;
                    double dz = tz-pz;
                    double range = 8;
                    for (double i = 0; i < range; i += 0.5) {
                        ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.WITCH, px + dx/16*i, py + dy/16*i, pz + dz/16*i, heldTime/4, 0.2, 0.2, 0.2, 0.2);
                        if (heldTime > 5) {
                            ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD, px + dx / 16 * i, py + dy / 16 * i, pz + dz / 16 * i, 1, 0.2, 0.2, 0.2, 0.01);
                        }
                        if (heldTime > 10) {
                            ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, px + dx / 16 * i, py + dy / 16 * i, pz + dz / 16 * i, heldTime / 2-9/2, 0.2, 0.2, 0.2, 0.2);
                        }
                        if (heldTime > 15) {
                            ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.ENCHANTED_HIT, px + dx / 16 * i, py + dy / 16 * i, pz + dz / 16 * i, heldTime / 2-14/2, 0.2, 0.2, 0.2, 0.5);
                        }
                        if (heldTime > 20) {
                            ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.SOUL_FIRE_FLAME, px + dx / 16 * i, py + dy / 16 * i, pz + dz / 16 * i, heldTime / 2-19/2, 0.2, 0.2, 0.2, 0.3);
                        }
                    }
                    for (double i = 0; i < range; i += 2) {
                        double ix = px + dx/range*i;
                        double iy = py + dy/range*i;
                        double iz = pz + dz/range*i;
                        float f = 3.0f;
                        Box box = new Box((float) ix - f, (float) iy - f, (float) iz - f, (float) (ix + 1) + f, (float) (iy + 1) + f, (float) (iz + 1) + f);
                        List<Entity> list = world.getOtherEntities(user, box);
                        for (Entity p : list) {
                            if (p instanceof LivingEntity) {
                                if (((LivingEntity) p).hurtTime == 0){
                                    p.damage(DamageSource.player(user), heldTime/2+2);
                                    if (p instanceof EnderDragonEntity) {
                                        ((EnderDragonEntity) p).damagePart(((EnderDragonEntity) p).getBodyParts()[2], DamageSource.player(user), heldTime/2+2);
                                    }
                                }
                            }
                        }
                    }
                }
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

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if (entity instanceof PlayerEntity) {
            NbtCompound nbtCompound = stack.getOrCreateNbt();
            String variant = nbtCompound.getString(VARIANT_KEY);

            if (Objects.equals(variant, "Fuming")) {
                int heldTime = nbtCompound.getInt(HELDTIME_KEY);
                int notHeldTime = nbtCompound.getInt(NOTHELDTIME_KEY);
                if (notHeldTime > 0) {
                    nbtCompound.putInt(NOTHELDTIME_KEY, notHeldTime-1);
                    if (notHeldTime == 1){
                        nbtCompound.putInt(HELDTIME_KEY, 0);
                    }
                }
            }
            if (entity.age%90 == 0) {
                stack.setDamage(stack.getDamage() - 1);
                if (stack.getDamage() < 0) {
                    stack.setDamage(0);
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (itemStack.hasNbt()) {
            NbtCompound nbtCompound = itemStack.getNbt();
            tooltip.add(new LiteralText("Shoot balls!").formatted(Formatting.DARK_PURPLE));
            tooltip.add(new LiteralText("Recharge by striking foes").formatted(Formatting.DARK_PURPLE));
            if (!Objects.equals(nbtCompound.getString(VARIANT_KEY), "Fuming")) {
                tooltip.add(new LiteralText("Crouch to increase accuracy").formatted(Formatting.DARK_PURPLE));
            }
            else{
                tooltip.add(new LiteralText("Increases damage over time").formatted(Formatting.DARK_PURPLE));
            }
            if (String.valueOf(nbtCompound.getString(VARIANT_KEY)) != "") {
                String string = String.join(" ", "Variant:", String.valueOf(nbtCompound.getString(VARIANT_KEY)));
                if (!StringHelper.isEmpty(string)) {
                    tooltip.add(new LiteralText(string).formatted(Formatting.DARK_AQUA));
                }
            }
        }
    }
}
