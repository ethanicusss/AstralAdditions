package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.cometball.CometballEntity;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeEntity;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class PylonItem extends Item {

    public static final String X_KEY = "coord_x";
    public static final String Y_KEY = "coord_y";
    public static final String Z_KEY = "coord_z";

    private static final int maxDistance = 128;
    private static final float pylonSize = 2.0f;

    private static final Predicate<PylonEntity> PYLON_PREDICATE = (entity) -> {
        return true;
    };
    public PylonItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbtCompound = itemStack.getOrCreateNbt();

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5f, 0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (world.isClient()) {//Client
            if (!nbtCompound.isEmpty()) {//TP
                double i = nbtCompound.getDouble(X_KEY);
                double j = nbtCompound.getDouble(Y_KEY);
                double k = nbtCompound.getDouble(Z_KEY);
                float f = pylonSize;
                Box box = new Box((float) i - f, (float) j - f, (float) k - f, (float) (i + 1) + f, (float) (j + 1) + f, (float) (k + 1) + f);
                List<PylonEntity> pylons = user.world.getEntitiesByType(TypeFilter.instanceOf(PylonEntity.class), box, PYLON_PREDICATE);
                if (getDistance(user.getX(), user.getY(), user.getZ(), i, j, k) < maxDistance) {
                    itemStack.setNbt(new NbtCompound());//Clear nbt
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                }
                else{
                    user.sendMessage(new TranslatableText(AstralAdditions.MOD_ID + ".text.pylon_too_far"), true);
                }

                return TypedActionResult.success(itemStack, world.isClient());
            }
        } else {//Server
            if (nbtCompound.isEmpty()){//PLACE
                double i = user.getX();
                double j = user.getY();
                double k = user.getZ();
                nbtCompound.putDouble(X_KEY, i);
                nbtCompound.putDouble(Y_KEY, j);
                nbtCompound.putDouble(Z_KEY, k);//Write user coords

                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundCategory.NEUTRAL, 0.7f, 0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                for (int amount = 0; amount < 5; amount++) {
                    ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.WITCH, user.getX(), user.getY(), user.getZ(), 1, 0.0 + world.getRandom().nextFloat() * 0.4f - 0.2f, 0.2 + world.getRandom().nextFloat() * 0.2f, 0.0 + world.getRandom().nextFloat() * 0.4f - 0.2f, 0);
                    }
                for (int amount = 0; amount < 10; amount++) {
                    ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.GLOW_SQUID_INK, user.getX(), user.getY() + 0.2f, user.getZ(), 1, Math.sin(amount*8.0f)*1.0f, 0, Math.cos(amount*8.0f)*1.0f, 0.1);
                }
                PylonEntity pylon = new PylonEntity(ModEntities.PYLON, user.world);
                pylon.setPlayer(user.getEntityName());
                pylon.setPos(user.getX(), user.getY() + 1, user.getZ());
                pylon.refreshPositionAndAngles(user.getX(), user.getY() + 1, user.getZ(), 0, 0);
                world.spawnEntity(pylon);//Place pylon entity

                user.getItemCooldownManager().set(this, 45);
                return TypedActionResult.success(itemStack, world.isClient());
            } else{//TP
                double i = nbtCompound.getDouble(X_KEY);
                double j = nbtCompound.getDouble(Y_KEY);
                double k = nbtCompound.getDouble(Z_KEY);
                float f = pylonSize;
                Box box = new Box((float) i - f, (float) j - f, (float) k - f, (float) (i + 1) + f, (float) (j + 1) + f, (float) (k + 1) + f);
                List<PylonEntity> pylons = user.world.getEntitiesByType(TypeFilter.instanceOf(PylonEntity.class), box, PYLON_PREDICATE);
                if (!pylons.isEmpty()) {
                    PylonEntity pylon = pylons.get(0);
                    if (getDistance(user.getX(), user.getY(), user.getZ(), i, j, k) < maxDistance) {
                        if (user.isSneaking()){
                            for (int amount = 0; amount < 45; amount++) {
                                ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.GLOW_SQUID_INK, pylon.getX(), pylon.getY() + 0.2f, pylon.getZ(), 1, Math.sin(amount*8.0f)*1.2f, 0, Math.cos(amount*8.0f)*1.2f, 1);
                                ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.SQUID_INK, pylon.getX(), pylon.getY() + 1.2f, pylon.getZ(), 1, Math.sin(amount*8.0f)*1.4f, 0, Math.cos(amount*8.0f)*1.4f, 1);
                                ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.GLOW_SQUID_INK, pylon.getX(), pylon.getY() + 2.2f, pylon.getZ(), 1, Math.sin(amount*8.0f)*1.2f, 0, Math.cos(amount*8.0f)*1.2f, 1);
                            }
                            world.playSound(null, pylon.getX(), pylon.getY(), pylon.getZ(), SoundEvents.BLOCK_BELL_USE, SoundCategory.NEUTRAL, 0.5f, 1.5f);
                            float strength = -0.16f;
                            float vStrength = 0.05f;
                            List<Entity> pl = world.getOtherEntities(pylon, new Box(pylon.getX()-16, pylon.getY()-32, pylon.getZ()-16, pylon.getX()+16, pylon.getY()+32, pylon.getZ()+16));
                            if (user.isInRange(pylon, 32)) {
                                System.out.println("player in range");
                                pl.add(user);
                            }
                            for (Entity p : pl) {
                                if (p instanceof LivingEntity){
                                    int strMult = 1;
                                    if (!(p instanceof PlayerEntity)) {
                                        strMult *= 2;
                                    }
                                    double xdiff = pylon.getX() - p.getX();
                                    double zdiff = pylon.getZ() - p.getZ();
                                    double dist = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(zdiff, 2));
                                    if (dist < 10) {
                                        if (xdiff == 0) {
                                            xdiff = 0.01;
                                        }
                                        if (zdiff == 0) {
                                            zdiff = 0.01;
                                        }
                                        double angleX = Math.atan(Math.abs(zdiff) / xdiff);
                                        double angleZ = Math.atan(Math.abs(xdiff) / zdiff);
                                        double cosX = Math.cos(angleX);
                                        double cosZ = Math.cos(angleZ);
                                        if (cosX == 0) {
                                            cosX = 0.01;
                                        }
                                        if (cosZ == 0) {
                                            cosZ = 0.01;
                                        }
                                        dist = -dist + 10;
                                        if (p instanceof PlayerEntity){//gotta somehow apply velocity on the client. or both client and server?
                                            ModUtils.setVelocityPlayer((ServerWorld)world, (PlayerEntity)p,dist * cosX * strength * strMult * (Math.abs(angleX) / angleX), Math.abs(dist * vStrength * strMult), dist * cosZ * strength * strMult * (Math.abs(angleZ) / angleZ));
                                        }
                                        p.addVelocity(dist * cosX * strength * strMult * (Math.abs(angleX) / angleX), Math.abs(dist * vStrength * strMult), dist * cosZ * strength * strMult * (Math.abs(angleZ) / angleZ));
                                    }
                                }
                            }
                        }
                        else{
                            for (int amount = 0; amount < 10; amount++) {
                                ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.GLOW_SQUID_INK, user.getX(), user.getY(), user.getZ(), 1, 0.0 + world.getRandom().nextFloat() * 0.2f - 0.1f, 0.3 + world.getRandom().nextFloat() * 0.7f, 0.0 + world.getRandom().nextFloat() * 0.2f - 0.1f, 0);
                                ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.GLOW_SQUID_INK, pylon.getX(), pylon.getY(), pylon.getZ(), 1, 0.0 + world.getRandom().nextFloat() * 0.2f - 0.1f, 0.3 + world.getRandom().nextFloat() * 0.7f, 0.0 + world.getRandom().nextFloat() * 0.2f - 0.1f, 0);
                                ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.WITCH, user.getX(), user.getY(), user.getZ(), 1, 0.0 + world.getRandom().nextFloat() * 0.8f - 0.4f, 0.4 + world.getRandom().nextFloat() * 0.3f, 0.0 + world.getRandom().nextFloat() * 0.8f - 0.4f, 0);
                                ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.WITCH, pylon.getX(), pylon.getY(), pylon.getZ(), 1, 0.0 + world.getRandom().nextFloat() * 0.8f - 0.4f, 0.4 + world.getRandom().nextFloat() * 0.3f, 0.0 + world.getRandom().nextFloat() * 0.8f - 0.4f, 0);
                            }
                            ((ServerPlayerEntity) user).networkHandler.requestTeleport(nbtCompound.getDouble(X_KEY), nbtCompound.getDouble(Y_KEY), nbtCompound.getDouble(Z_KEY), user.getYaw(), user.getPitch(), EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
                            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundCategory.NEUTRAL, 0.7f, 0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                            world.playSound(null, pylon.getX(), pylon.getY(), pylon.getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundCategory.NEUTRAL, 0.7f, 0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                        }
                        pylon.discard();
                        itemStack.setNbt(new NbtCompound());
                    } else{

                        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_HURT, SoundCategory.NEUTRAL, 0.5f, 0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                    }
                }
            }
        }
        return TypedActionResult.fail(itemStack);
    }

    private double getDistance(double x1, double y1, double z1, double x2, double y2, double z2){
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2) + Math.pow(z1-z2, 2));
    }
    /*
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5f, 0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (world.isClient()) {
        } else {
            double i = user.getX();
            double j = user.getY();
            double k = user.getZ();
            float f = 128.0f;
            Box box = new Box((float) i - f, (float) j - f, (float) k - f, (float) (i + 1) + f, (float) (j + 1) + f, (float) (k + 1) + f);
            List<PylonEntity> pylons = user.world.getEntitiesByType(TypeFilter.instanceOf(PylonEntity.class), box, pylonEntity -> PylonEntity.isOwner(pylonEntity, user.getEntityName()));
            if (!pylons.isEmpty()) {
                PylonEntity pylon = pylons.get(0);
                if (!user.isSneaking()) {
                    for (int amount = 0; amount < 10; amount++) {
                        MinecraftClient.getInstance().world.addParticle(ParticleTypes.GLOW_SQUID_INK, user.getX(), user.getY(), user.getZ(), 0.0 + world.getRandom().nextFloat() * 0.2f - 0.1f, 0.3 + world.getRandom().nextFloat() * 0.7f, 0.0 + world.getRandom().nextFloat() * 0.2f - 0.1f);
                        MinecraftClient.getInstance().world.addParticle(ParticleTypes.GLOW_SQUID_INK, pylon.getX(), pylon.getY(), pylon.getZ(), 0.0 + world.getRandom().nextFloat() * 0.2f - 0.1f, 0.3 + world.getRandom().nextFloat() * 0.7f, 0.0 + world.getRandom().nextFloat() * 0.2f - 0.1f);
                        MinecraftClient.getInstance().world.addParticle(ParticleTypes.WITCH, user.getX(), user.getY(), user.getZ(), 0.0 + world.getRandom().nextFloat() * 0.8f - 0.4f, 0.4 + world.getRandom().nextFloat() * 0.3f, 0.0 + world.getRandom().nextFloat() * 0.8f - 0.4f);
                        MinecraftClient.getInstance().world.addParticle(ParticleTypes.WITCH, pylon.getX(), pylon.getY(), pylon.getZ(), 0.0 + world.getRandom().nextFloat() * 0.8f - 0.4f, 0.4 + world.getRandom().nextFloat() * 0.3f, 0.0 + world.getRandom().nextFloat() * 0.8f - 0.4f);
                    }
                    ((ServerPlayerEntity) user).networkHandler.requestTeleport(pylon.getX(), pylon.getY(), pylon.getZ(), user.getYaw(), user.getPitch(), EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
                    MinecraftClient.getInstance().player.setVelocity(0.0f, 0.5f, 0.0f);
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundCategory.NEUTRAL, 0.7f, 0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                    world.playSound(null, pylon.getX(), pylon.getY(), pylon.getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundCategory.NEUTRAL, 0.7f, 0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                }
                else{
                    for (int amount = 0; amount < 45; amount++) {
                        MinecraftClient.getInstance().world.addParticle(ParticleTypes.GLOW_SQUID_INK, pylon.getX(), pylon.getY() + 0.2f, pylon.getZ(), Math.sin(amount*8.0f)*1.2f, 0, Math.cos(amount*8.0f)*1.2f);
                        MinecraftClient.getInstance().world.addParticle(ParticleTypes.SQUID_INK, pylon.getX(), pylon.getY() + 1.2f, pylon.getZ(), Math.sin(amount*8.0f)*1.4f, 0, Math.cos(amount*8.0f)*1.4f);
                        MinecraftClient.getInstance().world.addParticle(ParticleTypes.GLOW_SQUID_INK, pylon.getX(), pylon.getY() + 2.2f, pylon.getZ(), Math.sin(amount*8.0f)*1.2f, 0, Math.cos(amount*8.0f)*1.2f);
                    }
                    world.playSound(null, pylon.getX(), pylon.getY(), pylon.getZ(), SoundEvents.BLOCK_BELL_USE, SoundCategory.NEUTRAL, 0.5f, 1.5f);
                    float strength = -0.16f;
                    float vStrength = 0.05f;
                    List<Entity> pl = world.getOtherEntities(pylon, new Box(pylon.getX()-16, pylon.getY()-32, pylon.getZ()-16, pylon.getX()+16, pylon.getY()+32, pylon.getZ()+16));
                    pl.add(MinecraftClient.getInstance().player);
                    for (Entity p : pl) {
                        if (p instanceof LivingEntity){
                            int strMult = 1;
                            if (!(p instanceof PlayerEntity)) {
                                strMult *= 2;
                            }
                            double xdiff = pylon.getX() - p.getX();
                            double zdiff = pylon.getZ() - p.getZ();
                            double dist = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(zdiff, 2));
                            if (dist < 10) {
                                if (xdiff == 0) {
                                    xdiff = 0.01;
                                }
                                if (zdiff == 0) {
                                    zdiff = 0.01;
                                }
                                double angleX = Math.atan(Math.abs(zdiff) / xdiff);
                                double angleZ = Math.atan(Math.abs(xdiff) / zdiff);
                                double cosX = Math.cos(angleX);
                                double cosZ = Math.cos(angleZ);
                                if (cosX == 0) {
                                    cosX = 0.01;
                                }
                                if (cosZ == 0) {
                                    cosZ = 0.01;
                                }
                                dist = -dist + 10;
                                p.addVelocity(dist * cosX * strength * strMult * (Math.abs(angleX) / angleX), Math.abs(dist * vStrength * strMult), dist * cosZ * strength * strMult * (Math.abs(angleZ) / angleZ));
                            }
                        }
                    }
                }

                pylon.discard();
            } else {
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundCategory.NEUTRAL, 0.7f, 0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                for (int amount = 0; amount < 5; amount++) {
                    MinecraftClient.getInstance().world.addParticle(ParticleTypes.WITCH, user.getX(), user.getY(), user.getZ(), 0.0 + world.getRandom().nextFloat() * 0.4f - 0.2f, 0.2 + world.getRandom().nextFloat() * 0.2f, 0.0 + world.getRandom().nextFloat() * 0.4f - 0.2f);
                }
                for (int amount = 0; amount < 45; amount++) {
                    MinecraftClient.getInstance().world.addParticle(ParticleTypes.GLOW_SQUID_INK, user.getX(), user.getY() + 0.2f, user.getZ(), Math.sin(amount*8.0f)*1.0f, 0, Math.cos(amount*8.0f)*1.0f);
                }
                PylonEntity pylon = new PylonEntity(ModEntities.PYLON, user.world);
                pylon.setPlayer(user.getEntityName());
                pylon.setPos(user.getX(), user.getY() + 1, user.getZ());
                pylon.refreshPositionAndAngles(user.getX(), user.getY() + 1, user.getZ(), 0, 0);
                world.spawnEntity(pylon);
            }
        }
        user.getItemCooldownManager().set(this, 60);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

     */

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (itemStack.hasNbt()) {
            NbtCompound nbtCompound = itemStack.getNbt();
            String string = String.join(", ", String.valueOf(Math.round(nbtCompound.getDouble(X_KEY))), String.valueOf(Math.round(nbtCompound.getDouble(Y_KEY))), String.valueOf(Math.round(nbtCompound.getDouble(Z_KEY))));
            if (!StringHelper.isEmpty(string)) {
                tooltip.add(new LiteralText(string).formatted(Formatting.DARK_AQUA));
            }
        }
    }


}
