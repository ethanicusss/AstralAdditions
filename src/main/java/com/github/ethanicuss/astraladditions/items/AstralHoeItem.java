package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntity;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeRainEntity;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AstralHoeItem extends Item {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    private static final Predicate<ShimmerBlazeRainEntity> RAIN_PREDICATE = (entity) -> {
        return true;//entity.getAge() == 0;
    };
    protected static final Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of(itemUsageContext -> true, HoeItem.createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));


    public AstralHoeItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double)11, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", (double)-1, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.ENTITY_SHULKER_OPEN, SoundCategory.NEUTRAL, 1.1f, 0.8f);
        attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.NEUTRAL, 1.0f, 1.2f);
        if (!attacker.world.isClient()) {
            stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 3));
        double speed = 0.5;
        double dx = Math.sin(Math.toRadians(-attacker.getYaw())) * speed;
        double dy = Math.sin(Math.toRadians(-attacker.getPitch())) * speed;
        double dz = Math.cos(Math.toRadians(-attacker.getYaw())) * speed;
        target.setVelocity(dx, dy/2 + 0.6, dz);
        ModUtils.spawnForcedParticles((ServerWorld)attacker.world, ParticleTypes.WITCH, target.getX(), target.getY()-0.5, target.getZ(), 3, 0.5 * target.world.getRandom().nextFloat(), 0.3, 0.5 * target.world.getRandom().nextFloat(), 0.15);
        if (!attacker.world.isClient) {
            for (int i = 1; i < 11; i++) {
                if (!attacker.world.isClient) {
                    ShimmerBlazeRainEntity snowballEntity = new ShimmerBlazeRainEntity(ModEntities.SHIMMER_RAIN, attacker.getWorld());
                    snowballEntity.updatePosition(attacker.getX() + 0.25 + Math.sin(Math.toRadians(-attacker.getYaw())) * 1.5 * i, attacker.getY() - 1, attacker.getZ() + 0.25 + Math.cos(Math.toRadians(-attacker.getYaw())) * 1.5 * i);
                    attacker.world.spawnEntity(snowballEntity);
                    snowballEntity.setAge(40-i*2);
                    snowballEntity = new ShimmerBlazeRainEntity(ModEntities.SHIMMER_RAIN, attacker.getWorld());
                    snowballEntity.updatePosition(attacker.getX() - 0.25 + Math.sin(Math.toRadians(-attacker.getYaw())) * 1.5 * i, attacker.getY() - 1, attacker.getZ() - 0.25 + Math.cos(Math.toRadians(-attacker.getYaw())) * 1.5 * i);
                    attacker.world.spawnEntity(snowballEntity);
                    snowballEntity.setAge(40-i*2+1);
                }
            }
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

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = TILLING_ACTIONS.get(world.getBlockState(blockPos = context.getBlockPos()).getBlock());
        if (pair == null) {
            return ActionResult.PASS;
        }
        Predicate<ItemUsageContext> predicate = pair.getFirst();
        Consumer<ItemUsageContext> consumer = pair.getSecond();
        if (predicate.test(context)) {
            PlayerEntity playerEntity = context.getPlayer();
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            if (!world.isClient) {
                consumer.accept(context);
                if (playerEntity != null) {
                    context.getStack().damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
                }
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }
}
