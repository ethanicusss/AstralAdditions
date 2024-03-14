package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class CosmicHourglassItem extends Item {

    public CosmicHourglassItem(Settings settings) {
        super(settings);
    }

    private boolean ghost = false;
    private float dash = 2.5f;
    private Vec3d userPosOld;
    private float playerBoost = 1.005f;
    private int boostTime = 0;
    private int boostMaxTime = 40;
    private boolean jumped = false;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient()) {
            this.userPosOld = user.getPos();
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5f, 0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            MinecraftClient.getInstance().player.setVelocity(Math.sin(Math.toRadians(-user.getYaw()))*dash, 0, Math.cos(Math.toRadians(-user.getYaw()))*dash);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 20), user);
            ghost = true;
            boostTime = boostMaxTime;
            user.getItemCooldownManager().set(this, 60);//200);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity user = (PlayerEntity) entity;
            if (ghost) {
                if (boostTime == 0) {
                    ghost = false;
                    jumped = false;
                } else {
                    float boostMult = 0.5f;
                    double xSpeed = user.getX() - this.userPosOld.x;
                    double ySpeed = user.getY() - this.userPosOld.y;
                    double zSpeed = user.getZ() - this.userPosOld.z;
                    //if (user.isOnGround()) {boostMult += 2;}
                    AstralAdditions.LOGGER.info(Boolean.toString(user.isOnGround()));
                    /*if (Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(zSpeed, 2)) < 2.1 - (boostMult/2)) {
                        user.addVelocity(xSpeed * playerBoost * boostMult, 0, zSpeed * playerBoost * boostMult);
                    }
                    else{*/
                        //user.setVelocity(Math.sin(Math.toRadians(-user.getYaw())) * (2.1 - (boostMult/2)), Math.sin(Math.toRadians(-user.getPitch())) * (2.1 - (boostMult/2)), Math.cos(Math.toRadians(-user.getYaw())) * (2.1 - (boostMult/2)));
                        user.setVelocity(Math.sin(Math.toRadians(-user.getYaw())) * 0.45, Math.sin(Math.toRadians(-user.getPitch())) * 0.45, Math.cos(Math.toRadians(-user.getYaw())) * 0.45);
                    //}
                    /*if (ySpeed > 0 && !jumped) {
                        jumped = true;
                        user.addVelocity(0, 0.7, 0);
                    }*/
                    this.userPosOld = user.getPos();
                    world.addParticle(ParticleTypes.SQUID_INK, user.getX(), user.getY(), user.getZ(), 0, 0 + world.getRandom().nextFloat() * 0.5f, 0);
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.NEUTRAL, 0.1f, 0.2f);
                }
                boostTime--;
            }
        }
    }


}
