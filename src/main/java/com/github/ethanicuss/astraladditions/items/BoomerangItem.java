package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.boomerang.BoomerangEntity;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.StringHelper;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class BoomerangItem
        extends Item {

    public int DISTANCE = 0;
    public float SPEED = 0;
    public float DAMAGE = 0;
    public float CURVE = 0;

    public BoomerangItem(Item.Settings settings, float damage, int distance, float speed, float curve) {
        super(settings);
        this.DAMAGE = damage;
        this.DISTANCE = distance;
        this.SPEED = speed;
        this.CURVE = curve;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            BoomerangEntity snowballEntity = new BoomerangEntity(ModEntities.BOOMERANG, user.getWorld());
            snowballEntity.setOwner(user);
            snowballEntity.updatePosition(user.getX(), user.getEyeY(), user.getZ());

            snowballEntity.setRangItem(this.asItem().toString());
            snowballEntity.setRangDamage(this.DAMAGE);
            snowballEntity.setMaxAge(this.DISTANCE);
            snowballEntity.setRangSpeed(this.SPEED);
            snowballEntity.setCurve(this.CURVE);
            snowballEntity.setVelocity(user, user.getPitch(), user.getYaw()-this.CURVE*5, 0.0f, this.SPEED, 0.5f);

            snowballEntity.setOwner(user.getUuidAsString());
            world.spawnEntity(snowballEntity);
        }
        user.getItemCooldownManager().set(this, 5);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (itemStack.hasNbt()) {
            NbtCompound nbtCompound = itemStack.getNbt();
            tooltip.add(new LiteralText("Damage: " + this.DAMAGE).formatted(Formatting.BLUE));
            tooltip.add(new LiteralText("Speed: " + this.SPEED).formatted(Formatting.BLUE));
            tooltip.add(new LiteralText("Duration: " + this.DISTANCE).formatted(Formatting.BLUE));
            tooltip.add(new LiteralText("Curve: " + this.CURVE).formatted(Formatting.BLUE));
        }
    }
}