package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.registry.ModEntities;
import com.github.ethanicuss.astraladditions.entities.pylon.PylonEntity;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class PylonItem extends Item {
	public static final String X_KEY = "coord_x";
	public static final String Y_KEY = "coord_y";
	public static final String Z_KEY = "coord_z";

	private static final int maxDistance = 128;
	private static final double maxDistanceSqrt = (double) maxDistance * maxDistance;

	private static final float pylonSize = 2.0f;

	private static final Predicate<PylonEntity> anyPylon = entity -> true;

	public PylonItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();

		world.playSound(null, user.getX(), user.getY(), user.getZ(),
				SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL,
				0.5f,
				0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
		);

		//?Client
		if (world.isClient) {
			if (!hasPylonNbt(nbtCompound)) {
				return TypedActionResult.success(itemStack, true);
			}

			double px = nbtCompound.getDouble(X_KEY);
			double py = nbtCompound.getDouble(Y_KEY);
			double pz = nbtCompound.getDouble(Z_KEY);

			if (isWithinRangeSq(user, px, py, pz)) {
				clearPylonNbt(itemStack);
				user.incrementStat(Stats.USED.getOrCreateStat(this));
			} else {
				user.sendMessage(new TranslatableText(AstralAdditions.MOD_ID + ".text.pylon_too_far"), true);
			}
			return TypedActionResult.success(itemStack, true);
		}

		//?Server
		if (!hasPylonNbt(nbtCompound)) {
			placePylon(world, user, itemStack, nbtCompound);
			return TypedActionResult.success(itemStack, false);
		}

		return tryUseExistingPylon(world, user, itemStack, nbtCompound);
	}


	//?Server logic
	private void placePylon(World world, PlayerEntity user, ItemStack stack, NbtCompound nbt) {
		nbt.putDouble(X_KEY, user.getX());
		nbt.putDouble(Y_KEY, user.getY());
		nbt.putDouble(Z_KEY, user.getZ());

		world.playSound(null, user.getX(), user.getY(), user.getZ(),
				SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundCategory.NEUTRAL,
				0.7f,
				0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
		);

		spawnPlaceParticles((ServerWorld) world, user);

		PylonEntity pylon = new PylonEntity(ModEntities.PYLON, world);
		pylon.setPlayer(user.getEntityName());
		pylon.refreshPositionAndAngles(user.getX(), user.getY() + 1, user.getZ(), 0, 0);
		world.spawnEntity(pylon);

		user.getItemCooldownManager().set(this, 45);
	}

	private TypedActionResult<ItemStack> tryUseExistingPylon(World world, PlayerEntity user, ItemStack stack, NbtCompound nbt) {
		double px = nbt.getDouble(X_KEY);
		double py = nbt.getDouble(Y_KEY);
		double pz = nbt.getDouble(Z_KEY);

		PylonEntity pylon = findPylon(world, px, py, pz);
		if (pylon == null) return TypedActionResult.fail(stack);

		if (!isWithinRangeSq(user, px, py, pz)) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(),
					SoundEvents.ENTITY_ENDERMAN_HURT, SoundCategory.NEUTRAL,
					0.5f,
					0.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
			);
			return TypedActionResult.fail(stack);
		}

		if (user.isSneaking()) {
			doSneakPulse((ServerWorld) world, user, pylon);

		} else {
			doTeleport((ServerWorld) world, (ServerPlayerEntity) user, pylon, px, py, pz);
		}

		pylon.discard();
		clearPylonNbt(stack);

		return TypedActionResult.success(stack, false);
	}


	//?Helpers
	private boolean hasPylonNbt(NbtCompound nbt) {
		return nbt != null && nbt.contains(X_KEY) && nbt.contains(Y_KEY) && nbt.contains(Z_KEY);
	}

	private void clearPylonNbt(ItemStack stack) {
		stack.setNbt(new NbtCompound());
	}

	private boolean isWithinRangeSq(PlayerEntity user, double x, double y, double z) {
		return user.squaredDistanceTo(x, y, z) < maxDistanceSqrt;
	}

	@Nullable
	private PylonEntity findPylon(World world, double x, double y, double z) {
		float f = pylonSize;
		Box box = new Box(
				x - f, y - f, z - f,
				(x + 1) + f, (y + 1) + f, (z + 1) + f
		);

		List<PylonEntity> pylons = world.getEntitiesByType(TypeFilter.instanceOf(PylonEntity.class), box, anyPylon);
		return pylons.isEmpty() ? null : pylons.get(0);
	}

	private void spawnPlaceParticles(ServerWorld world, PlayerEntity user) {
		for (int i = 0; i < 5; i++) {
			ModUtils.spawnForcedParticles(world, ParticleTypes.WITCH,
					user.getX(), user.getY(), user.getZ(),
					1,
					world.getRandom().nextFloat() * 0.4f - 0.2f,
					0.2f + world.getRandom().nextFloat() * 0.2f,
					world.getRandom().nextFloat() * 0.4f - 0.2f,
					0
			);
		}

		for (int i = 0; i < 10; i++) {
			ModUtils.spawnForcedParticles(world, ParticleTypes.GLOW_SQUID_INK,
					user.getX(), user.getY() + 0.2f, user.getZ(),
					1,
					Math.sin(i * 8.0f) * 1.0f,
					0,
					Math.cos(i * 8.0f) * 1.0f,
					0.1
			);
		}
	}

	private void doTeleport(ServerWorld world, ServerPlayerEntity user, PylonEntity pylon, double x, double y, double z) {
		for (int i = 0; i < 10; i++) {
			spawnTeleportParticles(world, user.getX(), user.getY(), user.getZ());
			spawnTeleportParticles(world, pylon.getX(), pylon.getY(), pylon.getZ());
		}

		user.networkHandler.requestTeleport(x, y, z, user.getYaw(), user.getPitch(), EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));

		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundCategory.NEUTRAL, 0.7f, 0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
		world.playSound(null, pylon.getX(), pylon.getY(), pylon.getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundCategory.NEUTRAL, 0.7f, 0.8f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
	}

	private void spawnTeleportParticles(ServerWorld world, double x, double y, double z) {
		ModUtils.spawnForcedParticles(world, ParticleTypes.GLOW_SQUID_INK,
				x, y, z, 1,
				world.getRandom().nextFloat() * 0.2f - 0.1f,
				0.3f + world.getRandom().nextFloat() * 0.7f,
				world.getRandom().nextFloat() * 0.2f - 0.1f,
				0
		);
		ModUtils.spawnForcedParticles(world, ParticleTypes.WITCH, x, y, z, 1,
				world.getRandom().nextFloat() * 0.8f - 0.4f,
				0.4f + world.getRandom().nextFloat() * 0.3f,
				world.getRandom().nextFloat() * 0.8f - 0.4f,
				0
		);
	}

	private void doSneakPulse(ServerWorld world, PlayerEntity user, PylonEntity pylon) {
		for (int i = 0; i < 45; i++) {
			double sinX = Math.sin(i * 8.0f);
			double cosX = Math.cos(i * 8.0f);

			ModUtils.spawnForcedParticles(world, ParticleTypes.GLOW_SQUID_INK, pylon.getX(), pylon.getY() + 0.2f, pylon.getZ(),
					1, sinX * 1.2f, 0, cosX * 1.2f, 1);

			ModUtils.spawnForcedParticles(world, ParticleTypes.SQUID_INK, pylon.getX(), pylon.getY() + 1.2f, pylon.getZ(),
					1, sinX * 1.4f, 0, cosX * 1.4f, 1);

			ModUtils.spawnForcedParticles(world, ParticleTypes.GLOW_SQUID_INK, pylon.getX(), pylon.getY() + 2.2f, pylon.getZ(),
					1, sinX * 1.2f, 0, cosX * 1.2f, 1);
		}

		world.playSound(null, pylon.getX(), pylon.getY(), pylon.getZ(), SoundEvents.BLOCK_BELL_USE, SoundCategory.NEUTRAL, 0.5f, 1.5f);

		float strength = -0.16f;
		float vStrength = 0.05f;

		List<Entity> targets = world.getOtherEntities(pylon, new Box(pylon.getX() - 16, pylon.getY() - 32, pylon.getZ() - 16, pylon.getX() + 16, pylon.getY() + 32, pylon.getZ() + 16));

		if (user.isInRange(pylon, 32)) {
			targets.add(user);
		}

		for (Entity entity : targets) {
			if (!(entity instanceof LivingEntity)) continue;

			int mult = (entity instanceof PlayerEntity) ? 1 : 2;

			double dx = pylon.getX() - entity.getX();
			double dz = pylon.getZ() - entity.getZ();
			double dist = Math.sqrt(dx * dx + dz * dz);

			if (dist >= 10) continue;

			if (dx == 0) dx = 0.01;
			if (dz == 0) dz = 0.01;

			double angleX = Math.atan(Math.abs(dz) / dx);
			double angleZ = Math.atan(Math.abs(dx) / dz);

			double cosX = Math.cos(angleX);
			double cosZ = Math.cos(angleZ);
			if (cosX == 0) cosX = 0.01;
			if (cosZ == 0) cosZ = 0.01;

			double push = -dist + 10;

			entity.addVelocity(push * cosX * strength * mult * (Math.abs(angleX) / angleX), Math.abs(push * vStrength * mult), push * cosZ * strength * mult * (Math.abs(angleZ) / angleZ));

			if (entity instanceof PlayerEntity) {
				((PlayerEntity) entity).velocityModified = true;
			}
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (!stack.hasNbt()) return;

		NbtCompound nbt = stack.getNbt();
		if (!hasPylonNbt(nbt)) return;

		String coords = String.join(", ", String.valueOf(Math.round(nbt.getDouble(X_KEY))), String.valueOf(Math.round(nbt.getDouble(Y_KEY))), String.valueOf(Math.round(nbt.getDouble(Z_KEY))));

		if (!StringHelper.isEmpty(coords)) {
			tooltip.add(new LiteralText(coords).formatted(Formatting.DARK_AQUA));
		}
	}
}