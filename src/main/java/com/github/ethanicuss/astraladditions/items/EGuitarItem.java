package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.meteor_mitts.MeteorPunchEntity;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class EGuitarItem extends Item {


    public static final String CHARGE_KEY = "charge";
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    private final float[] notes = {getNote(15-10), getNote(17-10), getNote(18-10), getNote(22-10), getNote(27-10), getNote(34-10)};
    public EGuitarItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double)8, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", (double)-1.5, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    private float getNote(int i){
        float f = (float)Math.pow(2.0, (double)(i - 12) / 12.0);
        return f;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbtCompound = itemStack.getOrCreateNbt();
        if (nbtCompound.isEmpty()){
            nbtCompound.putInt(CHARGE_KEY, 0);
        }

        if (nbtCompound.getInt(CHARGE_KEY) >= 3) {
            if (world.isClient()) {
                user.setPitch(user.getPitch()-nbtCompound.getInt(CHARGE_KEY));
            } else {
                if (nbtCompound.getInt(CHARGE_KEY) >= 5) {
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.NEUTRAL, 0.7f, notes[0]);
                }
                if (nbtCompound.getInt(CHARGE_KEY) >= 4) {
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.NEUTRAL, 0.7f, notes[nbtCompound.getInt(CHARGE_KEY) - 3]);
                }
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.NEUTRAL, 0.7f, notes[nbtCompound.getInt(CHARGE_KEY) - 1]);
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.NEUTRAL, 0.7f, notes[nbtCompound.getInt(CHARGE_KEY)]);
                double px = user.getX();
                double py = user.getY() + 1;
                double pz = user.getZ();
                double tx = px + Math.sin(Math.toRadians(-user.getYaw()))*32;
                double ty = py + Math.sin(Math.toRadians(-user.getPitch()))*32;
                double tz = pz + Math.cos(Math.toRadians(-user.getYaw()))*32;
                double dx = tx-px;
                double dy = ty-py;
                double dz = tz-pz;
                for (double i = 0; i < 32; i += 0.5) {//particles not being passed from server to clients, ModUtils? check some githubs.
                    ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.END_ROD, px + dx/32*i, py + dy/32*i, pz + dz/32*i, 1, 0.0, 0.1, 0.0, 0);
                    //world.spawnParticles(ParticleTypes.END_ROD, px + dx/32*i, py + dy/32*i, pz + dz/32*i, 0.0, 0.1, 0.0);
                }
                for (double i = 0; i < 32; i += 2) {
                    double ix = px + dx/32*i;
                    double iy = py + dy/32*i;
                    double iz = pz + dz/32*i;
                    if (nbtCompound.getInt(CHARGE_KEY) == 5) {
                        ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.EXPLOSION, ix, iy, iz, 1, 0.0, 0.1, 0.0, 0);
                    }
                    float angle = world.getRandom().nextFloat()*360;
                    for (double j = 0; j < 5; j++) {
                        ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD, ix + Math.sin(Math.toRadians(angle) * (j / 2)), iy + Math.sin(Math.toRadians(-angle * 5) * (j / 2)), iz + Math.cos(Math.toRadians(angle) * (j / 2)), nbtCompound.getInt(CHARGE_KEY) - 2, 0.0, 0.1, 0.0, 0.1);
                    }
                    float f = 3.0f;
                    Box box = new Box((float) ix - f, (float) iy - f, (float) iz - f, (float) (ix + 1) + f, (float) (iy + 1) + f, (float) (iz + 1) + f);
                    List<Entity> list = world.getOtherEntities(user, box);
                    for (Entity p : list) {
                        if (p instanceof LivingEntity) {
                            if (((LivingEntity) p).hurtTime == 0){
                                p.damage(DamageSource.player(user), 10 + (nbtCompound.getInt(CHARGE_KEY)-3)*5);
                                if (p instanceof EnderDragonEntity) {
                                    ((EnderDragonEntity) p).damagePart(((EnderDragonEntity) p).getBodyParts()[2], DamageSource.player(user), 10 + (nbtCompound.getInt(CHARGE_KEY)-3)*5);
                                }
                            }
                        }
                    }
                }
                nbtCompound.putInt(CHARGE_KEY, 0);
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        //target.damage(DamageSource.mob(attacker), 8);
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (nbtCompound.getInt(CHARGE_KEY) < 5) {
            nbtCompound.putInt(CHARGE_KEY, nbtCompound.getInt(CHARGE_KEY)+1);
        }
        attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.NEUTRAL, 0.5f + 0.2f*nbtCompound.getInt(CHARGE_KEY), notes[nbtCompound.getInt(CHARGE_KEY) - 1]);

        ModUtils.spawnForcedParticles((ServerWorld)attacker.world, ParticleTypes.END_ROD, target.getX(), target.getY(), target.getZ(), 3 + nbtCompound.getInt(CHARGE_KEY), 0.5 * target.world.getRandom().nextFloat(), 0.3, 0.5 * target.world.getRandom().nextFloat(), 0.5);
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

}
