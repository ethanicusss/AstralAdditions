package com.github.ethanicuss.astraladditions.fluids;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

public abstract class SputumFluid extends SputumFlowableFluid {
    @Override
    public Fluid getStill() {
        return ModFluids.STILL_SPUTUM;
    }

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_SPUTUM;
    }

    @Override
    public Item getBucketItem() {
        return ModFluids.SPUTUM_BUCKET;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return ModFluids.SPUTUM.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends SputumFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends SputumFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 4;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}