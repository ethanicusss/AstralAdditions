package com.github.ethanicuss.astraladditions.fluids;

import org.jetbrains.annotations.Nullable;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class ModFluid extends FlowingFluid {

    /**
     * @return whether the given fluid an instance of this fluid
     */
    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == getSource() || fluid == getFlowing();
    }

    /**
     * @return whether the fluid is infinite like water
     */
    @Override
    protected boolean canConvertToSource() {
        return false;
    }

    /**
     * Perform actions when the fluid flows into a replaceable block. Water drops
     * the block's loot table. Lava plays the "block.lava.extinguish" sound.
     */
    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    /**
     * Lava returns true if it's FluidState is above a certain height and the
     * Fluid is Water.
     *
     * @return whether the given Fluid can flow into this FluidState
     */
    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }

    /**
     * Possibly related to the distance checks for flowing into nearby holes?
     * Water returns 4. Lava returns 2 in the Overworld and 4 in the Nether.
     */
    @Override
    protected int getSlopeFindDistance(LevelReader worldView) {
        return 5;
    }

    /**
     * Water returns 1. Lava returns 2 in the Overworld and 1 in the Nether.
     */
    @Override
    protected int getDropOff(LevelReader worldView) {
        return 1;
    }

    /**
     * Water returns 5. Lava returns 30 in the Overworld and 10 in the Nether.
     */
    @Override
    public int getTickDelay(LevelReader worldView) {
        return 5;
    }

    /**
     * Water and Lava both return 100.0F.
     */
    @Override
    protected float getExplosionResistance() {
        return 1.0F;
    }

    public void animateTick(Level world, BlockPos pos, FluidState state, Random random) {
        BlockPos blockPos = pos.above();
        if (world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos).isSolidRender(world, blockPos)) {
            if (random.nextInt(30) == 0) {
                double d = (double)pos.getX() + random.nextDouble();
                double e = (double)pos.getY() + 1.0;
                double f = (double)pos.getZ() + random.nextDouble();
                world.addParticle(ParticleTypes.DRAGON_BREATH, d, e, f, 0.0, 0.01, 0.0);
                world.playLocalSound(d, e, f, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(250) == 0) {
                world.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.4F + random.nextFloat() * 0.3F, false);
            }
        }
    }

    @Nullable
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    protected void spreadTo(LevelAccessor world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState) {
        if (direction == Direction.DOWN) {
            FluidState fluidState2 = world.getFluidState(pos);
            if (fluidState2.is(FluidTags.WATER)) {
                if (state.getBlock() instanceof LiquidBlock) {
                    world.setBlock(pos, Blocks.DEEPSLATE.defaultBlockState(), 3);
                }

//                this.playExtinguishEvent(world, pos);
                return;
            }
        }

        super.spreadTo(world, pos, state, direction, fluidState);
    }
}
