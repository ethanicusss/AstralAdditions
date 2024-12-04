package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DesizerCasingBlock extends HorizontalFacingBlock {

    public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

    public DesizerCasingBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(TYPE, Type.BASE));

    }
    //! This can be removed in a later update
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            if (state.getBlock() != ModBlocks.DESIZER_BASE) {
                BlockState newBlockState = ModBlocks.DESIZER_BASE.getDefaultState();
                world.setBlockState(pos, newBlockState);
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(TYPE, Type.BASE);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    public enum Type implements StringIdentifiable {
        BASE("base"),
        TOPLEFT("topleft"), TOPMIDDLE("topmiddle"), TOPRIGHT("topright"),
        MIDDLELEFT("middleleft"), MIDDLERIGHT("middleright"),
        BOTTOMLEFT("bottomleft"), BOTTOMMIDDLE("bottommiddle"), BOTTOMRIGHT("bottomright");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

}
