package com.github.ethanicuss.astraladditions.entities.shimmerfishingrod;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.mixin.fishing.FishingBobberEntityAccessor;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import com.github.ethanicuss.astraladditions.util.FishingBobberStateHelper;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShimmerFishingBobberEntity extends FishingBobberEntity {

	private final Random velocityRandom = new Random();

	public ShimmerFishingBobberEntity(EntityType<? extends FishingBobberEntity> entityType, World world) {
		super(entityType, world);
	}

	public ShimmerFishingBobberEntity(PlayerEntity player, World world, int luckOfTheSeaLevel, int lureLevel) {
		super(ModEntities.SHIMMER_FISHING_BOBBER, world);

		this.setOwner(player);

		((FishingBobberEntityAccessor)(Object)this).setLuckOfTheSeaLevel(Math.max(0, luckOfTheSeaLevel));
		((FishingBobberEntityAccessor)(Object)this).setLureLevel(Math.max(0, lureLevel));

		float f = player.getPitch();
		float g = player.getYaw();
		float h = MathHelper.cos(-g * ((float)Math.PI / 180F) - (float)Math.PI);
		float i = MathHelper.sin(-g * ((float)Math.PI / 180F) - (float)Math.PI);
		float j = -MathHelper.cos(-f * ((float)Math.PI / 180F));
		float k = MathHelper.sin(-f * ((float)Math.PI / 180F));
		double d = player.getX() - (double)i * 0.3;
		double e = player.getEyeY();
		double l = player.getZ() - (double)h * 0.3;
		this.refreshPositionAndAngles(d, e, l, g, f);
		Vec3d vec3d = new Vec3d((double)(-i), (double)MathHelper.clamp(-(k / j), -5.0F, 5.0F), (double)(-h));
		double m = vec3d.length();
		vec3d = vec3d.multiply(0.6 / m + (double)0.5F + this.random.nextGaussian() * 0.0045, 0.6 / m + (double)0.5F + this.random.nextGaussian() * 0.0045, 0.6 / m + (double)0.5F + this.random.nextGaussian() * 0.0045);
		this.setVelocity(vec3d);
		this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI)));
		this.setPitch((float)(MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * (double)(180F / (float)Math.PI)));
		this.prevYaw = this.getYaw();
		this.prevPitch = this.getPitch();

	}

	@Override
	public void tick() {
		this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.world.getTime());
		super.baseTick();

		PlayerEntity player = this.getPlayerOwner();
		if (player == null) {
			this.discard();
			return;
		}

		if (!this.world.isClient && !this.isStillHoldingFishingRod(player)) {
			this.discard();
			return;
		}

		FishingBobberEntityAccessor accessor = (FishingBobberEntityAccessor)(Object)this;
		Object currentState = FishingBobberStateHelper.getState(this);
		String stateName = ((Enum<?>)currentState).name();

		if (this.onGround) {
			accessor.setRemovalTimer(accessor.getRemovalTimer() + 1);
			if (accessor.getRemovalTimer() >= 1200) {
				this.discard();
				return;
			}
		} else {
			accessor.setRemovalTimer(0);
		}

		float fluidHeight = 0.0F;
		BlockPos blockPos = this.getBlockPos();
		FluidState fluidState = this.world.getFluidState(blockPos);

		if (fluidState.isIn(FluidTags.WATER)) {
			fluidHeight = fluidState.getHeight(this.world, blockPos);
		}

		boolean isInWater = fluidHeight > 0.0F;

		if (stateName.equals("FLYING")) {
			if (accessor.getHookedEntity() != null) {
				this.setVelocity(Vec3d.ZERO);
				FishingBobberStateHelper.setState(this, getEnumStateByName("HOOKED_IN_ENTITY"));
				return;
			}

			if (isInWater) {
				this.setVelocity(this.getVelocity().multiply(0.3, 0.2, 0.3));
				FishingBobberStateHelper.setState(this, getEnumStateByName("BOBBING"));
				return;
			}

			accessor.callCheckForCollision();
		} else if (stateName.equals("HOOKED_IN_ENTITY")) {
			if (accessor.getHookedEntity() != null) {
				if (!accessor.getHookedEntity().isRemoved() && accessor.getHookedEntity().world.getRegistryKey() == this.world.getRegistryKey()) {
					this.setPosition(accessor.getHookedEntity().getX(), accessor.getHookedEntity().getBodyY(0.8), accessor.getHookedEntity().getZ());
				} else {
					accessor.callUpdateHookedEntityId(null);
					FishingBobberStateHelper.setState(this, getEnumStateByName("FLYING"));
				}
			}
			return;
		} else if (stateName.equals("BOBBING")) {
			Vec3d vec3d = this.getVelocity();
			double dy = this.getY() + vec3d.y - (double)blockPos.getY() - (double)fluidHeight;

			if (Math.abs(dy) < 0.01) {
				dy += Math.signum(dy) * 0.1;
			}

			this.setVelocity(vec3d.x * 0.9, vec3d.y - dy * (double)this.random.nextFloat() * 0.2, vec3d.z * 0.9);

			if (accessor.getHookCountdown() <= 0 && accessor.getFishTravelCountdown() <= 0) {
				accessor.setInOpenWater(true);
			} else {
				accessor.setInOpenWater(accessor.isInOpenWater() && accessor.getOutOfOpenWaterTicks() < 10 && accessor.callIsOpenOrWaterAround(blockPos));
			}

			if (isInWater) {
				accessor.setOutOfOpenWaterTicks(Math.max(0, accessor.getOutOfOpenWaterTicks() - 1));
				if (accessor.isCaughtFish()) {
					this.setVelocity(this.getVelocity().add(0.0, -0.1 * (double)this.velocityRandom.nextFloat() * (double)this.velocityRandom.nextFloat(), 0.0));
				}

				if (!this.world.isClient) {
					accessor.callTickFishingLogic(blockPos);
				}
			} else {
				accessor.setOutOfOpenWaterTicks(Math.min(10, accessor.getOutOfOpenWaterTicks() + 1));
			}
		}

		if (!isInWater) {
			this.setVelocity(this.getVelocity().add(0.0, -0.03, 0.0));
		}

		this.move(MovementType.SELF, this.getVelocity());
		this.updateRotation();

		Object stateAfterMove = FishingBobberStateHelper.getState(this);
		if (((Enum<?>)stateAfterMove).name().equals("FLYING") && (this.onGround || this.horizontalCollision)) {
			this.setVelocity(Vec3d.ZERO);
		}

		this.setVelocity(this.getVelocity().multiply(0.92));
		this.refreshPosition();
	}


	@Override
	public int use(ItemStack usedItem) {
		PlayerEntity player = this.getPlayerOwner();
		if (!this.world.isClient && player != null && this.isStillHoldingFishingRod(player)) {
			int i = 0;
			FishingBobberEntityAccessor accessor = (FishingBobberEntityAccessor)(Object)this;

			if (accessor.getHookedEntity() != null) {
				this.pullHookedEntity(accessor.getHookedEntity());
				Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)player, usedItem, this, Collections.emptyList());
				this.world.sendEntityStatus(this, (byte)31);
				i = accessor.getHookedEntity() instanceof ItemEntity ? 3 : 5;
			} else if (accessor.getHookCountdown() > 0) {
				LootContext.Builder builder = new LootContext.Builder((ServerWorld)this.world)
						.parameter(LootContextParameters.ORIGIN, this.getPos())
						.parameter(LootContextParameters.TOOL, usedItem)
						.parameter(LootContextParameters.THIS_ENTITY, this)
						.random(this.random)
						.luck((float)accessor.getLuckOfTheSeaLevel() + player.getLuck());

				LootTable lootTable = this.world.getServer().getLootManager().getTable(new Identifier("astraladditions", "gameplay/fishing/shimmer_fishing"));
				List<ItemStack> loot = lootTable.generateLoot(builder.build(LootContextTypes.FISHING));
				Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)player, usedItem, this, loot);

				for (ItemStack itemStack : loot) {
					ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), itemStack);
					double dx = player.getX() - this.getX();
					double dy = player.getY() - this.getY();
					double dz = player.getZ() - this.getZ();
					itemEntity.setVelocity(dx * 0.1, dy * 0.1 + Math.sqrt(Math.sqrt(dx * dx + dy * dy + dz * dz)) * 0.08, dz * 0.1);
					this.world.spawnEntity(itemEntity);

					player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.getX(), player.getY() + 0.5, player.getZ() + 0.5, this.random.nextInt(6) + 1));

					if (itemStack.isIn(ItemTags.FISHES)) {
						player.increaseStat(Stats.FISH_CAUGHT, 1);
					}
				}
				i = 1;
			}

			if (this.onGround) {
				i = 2;
			}

			this.discard();
			return i;
		} else {
			return 0;
		}
	}

	@Override
	protected void pullHookedEntity(Entity entity) {
		Entity owner = this.getOwner();
		if (owner != null) {
			Vec3d pull = new Vec3d(
					owner.getX() - this.getX(),
					owner.getY() - this.getY(),
					owner.getZ() - this.getZ()
			).multiply(0.2);

			entity.setVelocity(entity.getVelocity().add(pull));
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);

		if (!this.world.isClient) {
			((FishingBobberEntityAccessor)(Object)this).callUpdateHookedEntityId(entityHitResult.getEntity());

			Entity entity = entityHitResult.getEntity();
			if (entity instanceof LivingEntity living) {
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 32780, 0, false, false));
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 32780, 0, false, false));
			}
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		Entity hooked = ((FishingBobberEntityAccessor)(Object)this).getHookedEntity();
		if (hooked instanceof LivingEntity living) {
			living.removeStatusEffect(StatusEffects.GLOWING);
			living.removeStatusEffect(StatusEffects.NIGHT_VISION);
		}
		super.remove(reason);
	}

	private boolean isStillHoldingFishingRod(PlayerEntity player) {
		ItemStack mainHand = player.getMainHandStack();
		ItemStack offHand = player.getOffHandStack();
		return mainHand.isOf(ModItems.SHIMMER_FISHING_ROD) || offHand.isOf(ModItems.SHIMMER_FISHING_ROD);
	}

	private Object getEnumStateByName(String name) {
		Object currentState = FishingBobberStateHelper.getState(this);
		if (currentState == null) {
			throw new IllegalStateException("State is null");
		}

		Object[] constants = currentState.getClass().getEnumConstants();
		if (constants == null) {
			throw new IllegalStateException("Cannot read enum constants");
		}

		for (Object constant : constants) {
			if (((Enum<?>)constant).name().equals(name)) {
				return constant;
			}
		}
		throw new IllegalArgumentException("No such FishingBobberEntity.State: " + name);
	}

}