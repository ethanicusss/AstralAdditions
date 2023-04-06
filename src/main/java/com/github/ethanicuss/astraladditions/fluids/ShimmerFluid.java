package com.github.ethanicuss.astraladditions.fluids;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class ShimmerFluid extends ModFluid {
    @Override
    public Fluid getSource() {
        return ModFluids.STILL_SHIMMER;
    }

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_SHIMMER;
    }

    @Override
    public Item getBucket() {
        return ModFluids.SHIMMER_BUCKET;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState fluidState) {
        return ModFluids.SHIMMER.defaultBlockState().setValue(BlockStateProperties.LEVEL, getLegacyLevel(fluidState));
    }

    public static class Flowing extends ShimmerFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends ShimmerFluid {
        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}