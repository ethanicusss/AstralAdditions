package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class DesizerControllerBlockEntity extends BlockEntity {

    public DesizerControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.JAR_BLOCKENTITY, pos, state);
    }
}
