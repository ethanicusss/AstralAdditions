package com.github.ethanicuss.astraladditions.mixin.fishing;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FishingBobberEntity.class)
public interface FishingBobberEntityAccessor {

	@Accessor("removalTimer")
	int getRemovalTimer();

	@Accessor("removalTimer")
	void setRemovalTimer(int removalTimer);

	@Accessor("caughtFish")
	boolean isCaughtFish();

	@Accessor("hookCountdown")
	int getHookCountdown();

	@Accessor("fishTravelCountdown")
	int getFishTravelCountdown();

	@Accessor("outOfOpenWaterTicks")
	int getOutOfOpenWaterTicks();

	@Accessor("outOfOpenWaterTicks")
	void setOutOfOpenWaterTicks(int ticks);

	@Accessor("hookedEntity")
	Entity getHookedEntity();

	@Accessor("inOpenWater")
	boolean isInOpenWater();

	@Accessor("inOpenWater")
	void setInOpenWater(boolean inOpenWater);

	@Accessor("luckOfTheSeaLevel")
	int getLuckOfTheSeaLevel();

	@Invoker("updateHookedEntityId")
	void callUpdateHookedEntityId(Entity entity);

	@Invoker("isOpenOrWaterAround")
	boolean callIsOpenOrWaterAround(BlockPos pos);

	@Invoker("tickFishingLogic")
	void callTickFishingLogic(BlockPos pos);

	@Invoker("checkForCollision")
	void callCheckForCollision();

	@Mutable
	@Accessor("luckOfTheSeaLevel")
	void setLuckOfTheSeaLevel(int luck);

	@Mutable
	@Accessor("lureLevel")
	void setLureLevel(int lure);

}