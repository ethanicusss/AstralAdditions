package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.entities.prismatic_geyser.PrismaticGeyserEntity;
import com.github.ethanicuss.astraladditions.entities.shimmerblaze.ShimmerBlazeRainEntity;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import io.github.fabricators_of_create.porting_lib.data.SoundDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class GeyserBlock extends Block {

    public GeyserBlock(Settings settings) {
        super(settings);
    }

    public ActionResult onUse(BlockState blockState, World world, BlockPos pos, PlayerEntity placedBy, Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            world.playSound(pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 0.5f, true);
        }

        return ActionResult.PASS;
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+1.5, pos.getZ()+1);
        List<Entity> ls = world.getOtherEntities(entity, box);
        for (Entity p : ls) {
            if (p instanceof PrismaticGeyserEntity) {
                return;
            }
        }

        PrismaticGeyserEntity geyser = new PrismaticGeyserEntity(ModEntities.PRISMATIC_GEYSER, world);
        geyser.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        geyser.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0f, 0.0f);
        world.spawnEntity(geyser);

        if (world instanceof ServerWorld) {
            ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.1);
            ModUtils.spawnForcedParticles((ServerWorld)world, ParticleTypes.SNOWFLAKE, entity.getX(), (double)(pos.getY() + 1), entity.getZ(), 1, (double)(MathHelper.nextBetween(world.random, -1.0F, 1.0F) * 0.083333336F), 0.05000000074505806D, (double)(MathHelper.nextBetween(world.random, -1.0F, 1.0F) * 0.083333336F), 0);

        }


        super.onSteppedOn(world, pos, state, entity);
    }
}
