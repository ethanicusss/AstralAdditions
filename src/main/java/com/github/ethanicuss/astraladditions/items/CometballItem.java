package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CometballItem
        extends Item {
    public CometballItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5f, 0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (user.getPitch() >= 80) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundCategory.NEUTRAL, 0.7f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            float yaw = user.getYaw();
            float pitch = user.getPitch() - 90;
            float f = -MathHelper.sin(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
            float g = -MathHelper.sin((pitch) * ((float) Math.PI / 180));
            float h = MathHelper.cos(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));

            user.setVelocity(0.0f, 1.0f, 0.0f);
            Vec3d vec3d = new Vec3d(f, g, h).normalize().multiply(0.5).add(user.getVelocity());
            user.setVelocity(vec3d);
            user.damage(DamageSource.FALL, 2);
            world.addParticle(ParticleTypes.GLOW_SQUID_INK, user.getX(), user.getY(), user.getZ(), 0.0, 0.0, 0.0);
        }
        else{
            if (!world.isClient) {
                CometballEntity snowballEntity = new CometballEntity(ModEntities.COMETBALL, user.getWorld());
                snowballEntity.setOwner(user);

                snowballEntity.updatePosition(user.getX(), user.getEyeY(), user.getZ());
                snowballEntity.setItem(itemStack);
                snowballEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0.5f);
                world.spawnEntity(snowballEntity);
            }
        }
        user.getItemCooldownManager().set(this, 18);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
