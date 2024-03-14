package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntity;
import com.github.ethanicuss.astraladditions.entities.meteor_mitts.MeteorPunchEntity;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class MeteorMittsItem extends Item {

    public MeteorMittsItem(Settings settings) {
        super(settings);
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
        }
        user.getItemCooldownManager().set(this, 7);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setVelocity(0, 0.5, 0);
        target.setYaw(target.getYaw()+180);
        target.damage(DamageSource.mob(attacker), 5);
        attacker.world.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.NEUTRAL, 1.5f, 0.5f);
        attacker.world.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.NEUTRAL, 1.5f, 0.6f);
        for (int i = 0; i < 5; i++) {
            attacker.world.addParticle(ParticleTypes.END_ROD, target.getX(), target.getY(), target.getZ(), 0.5 * target.world.getRandom().nextFloat(), 0.3, 0.5 * target.world.getRandom().nextFloat());
        }
        return true;
    }

}
