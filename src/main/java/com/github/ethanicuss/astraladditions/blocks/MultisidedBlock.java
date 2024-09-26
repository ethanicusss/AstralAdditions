package com.github.ethanicuss.astraladditions.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.Wearable;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

public class MultisidedBlock extends HorizontalFacingBlock {

    public static final DirectionProperty FACING;

    public MultisidedBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }
    static {
        FACING = HorizontalFacingBlock.FACING;
    }
}
