package com.github.ethanicuss.astraladditions.blocks;

import com.github.ethanicuss.astraladditions.recipes.DesizerRecipe;
import com.github.ethanicuss.astraladditions.recipes.DesizerRecipes;
import com.github.ethanicuss.astraladditions.registry.ModData;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Optional;

public class DesizerControllerBlock extends HorizontalFacingBlock{

    boolean hasTicked = false;

    public DesizerControllerBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)));
    }



    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            //this.tryShrink(world, pos, state);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            if (!this.hasTicked) {
                this.hasTicked = true;
                this.tryShrink(world, pos, state);//It's neighbor updating itself!!!! silly!!!
                this.hasTicked = false;
            }
            else{
                this.hasTicked = false;
            }
        }
    }

    private void tryShrink(World world, BlockPos pos, BlockState state) {

        Direction direction = state.get(FACING);

        boolean bl = this.shouldShrink(world, pos, direction);

        if (bl) {//shrink time
            int _X = 0;
            int _Y = 0;
            int _Z = 0;

            switch (direction) {
                case NORTH -> {
                    _X = -1;
                    _Y = 1;
                    _Z = -3;
                }
                case EAST -> {
                    _X = 3;
                    _Y = 1;
                    _Z = -1;
                }
                case SOUTH -> {
                    _X = 1;
                    _Y = 1;
                    _Z = 3;
                }
                case WEST -> {
                    _X = -3;
                    _Y = 1;
                    _Z = 1;
                }
            }

            DesizerRecipes.RecipeStructure input = new DesizerRecipes.RecipeStructure();

            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    for (int k = 0; k < 3; k++){//hurm.... complicated....
                        Block block = world.getBlockState(new BlockPos(
                                pos.getX() + _X + k * isDirection("x", direction) + j * isDirection("z", direction),
                                pos.getY() + _Y - i,
                                pos.getZ() + _Z - k * isDirection("z", direction) + j * isDirection("x", direction)
                        )).getBlock();
                        String blockString = block.toString().replace("Block{", "").replace("}", "");

                        //System.out.println("im seeing: " + i + ", " + j + ", " + k + " - " + block);
                        if (block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.VOID_AIR){
                            block = Blocks.BEDROCK;
                        }
                        input.blocks[i][j][k] = block;
                    }
                }
            }

            SimpleInventory inventory = new SimpleInventory(27);//CRAFT CHECK!!!
            int slotCount = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        inventory.setStack(slotCount, new ItemStack(input.blocks[i][j][k].asItem(), 1));
                        slotCount++;
                    }
                }
            }
            Optional<DesizerRecipe> match = world.getRecipeManager()
                    .getFirstMatch(DesizerRecipe.Type.INSTANCE, inventory, world);

            if (match.isPresent()){
                Item resultItem = match.get().getOutput().getItem();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 3; k++) {//Clear board
                            BlockPos returnBlockPos = new BlockPos(
                                pos.getX() + _X + k * isDirection("x", direction) + j * isDirection("z", direction),
                                pos.getY() + _Y - i,
                                pos.getZ() + _Z - k * isDirection("z", direction) + j * isDirection("x", direction)
                            );
                            world.setBlockState(returnBlockPos, Blocks.AIR.getDefaultState());
                        }
                    }
                }
                switch (direction) {
                    case NORTH -> {
                        _X = 0;
                        _Y = 1;
                        _Z = -2;
                    }
                    case EAST -> {
                        _X = 2;
                        _Y = 1;
                        _Z = 0;
                    }
                    case SOUTH -> {
                        _X = 0;
                        _Y = 1;
                        _Z = 2;
                    }
                    case WEST -> {
                        _X = -2;
                        _Y = 1;
                        _Z = 0;
                    }
                }
                BlockPos returnPos = new BlockPos(pos.getX() + _X, pos.getY() + _Y, pos.getZ() + _Z);
                dropResult(world, returnPos, new ItemStack(resultItem, 1));//Spawn result

                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CONDUIT_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.5f);
                particles(world, pos, direction, true);
            }
            else{//If not a valid recipe
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 3; k++) {
                            Block returnBlock = input.blocks[i][j][k];
                            if (!returnBlock.getDefaultState().isIn(ModData.DESIZER_IGNORE_BLOCKS)){
                                BlockPos returnBlockPos = new BlockPos(
                                        pos.getX() + _X + k * isDirection("x", direction) + j * isDirection("z", direction),
                                        pos.getY() + _Y - i,
                                        pos.getZ() + _Z - k * isDirection("z", direction) + j * isDirection("x", direction)
                                );
                                dropResult(world, returnBlockPos, new ItemStack(returnBlock.asItem(), 1));//Drop every block
                                world.setBlockState(returnBlockPos, Blocks.AIR.getDefaultState());//Clear the board
                            }
                        }
                    }
                }
                //world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.8f, 0.5f);
                particles(world, pos, direction, false);
            }
        }
    }

    private static void particles(World world, BlockPos pos, Direction direction, Boolean success){
        if (world.isClient()) { return; }

        double _X = 0;
        double _Y = 0;
        double _Z = 0;

        switch (direction) {
            case NORTH -> {
                _X = 0;
                _Y = 0;
                _Z = -2;
            }
            case EAST -> {
                _X = 2;
                _Y = 0;
                _Z = 0;
            }
            case SOUTH -> {
                _X = 0;
                _Y = 0;
                _Z = 2;
            }
            case WEST -> {
                _X = -2;
                _Y = 0;
                _Z = 0;
            }
        }
        if (success){
            float cubeDensity = 10;

            for (int k = 0; k < 2; k++) {
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < cubeDensity; i++) {
                        ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD,
                                pos.getX() + _X - 1 + ((float) i / cubeDensity * 3.0), pos.getY() + _Y - 1 + j * 3, pos.getZ() + _Z - 1 + k * 3, 1, 0, 0, 0, 0);
                    }
                }
            }
            for (int k = 0; k < 2; k++) {
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < cubeDensity; i++) {
                        ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD,
                                pos.getX() + _X - 1 + j * 3, pos.getY() + _Y - 1 + ((float) i / cubeDensity * 3.0), pos.getZ() + _Z - 1 + k * 3, 1, 0, 0, 0, 0);
                    }
                }
            }
            for (int k = 0; k < 2; k++) {
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < cubeDensity; i++) {
                        ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD,
                                pos.getX() + _X - 1 + j * 3, pos.getY() + _Y - 1 + k * 3, pos.getZ() + _Z - 1 + ((float) i / cubeDensity * 3.0), 1, 0, 0, 0, 0);
                    }
                }
            }
            ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD, pos.getX() + _X + 0.5, pos.getY() + _Y + 0.5, pos.getZ() + _Z, 5, 0, 0, 0, 0.02);
        }
        else{
            ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.END_ROD, pos.getX() + _X + 0.5, pos.getY() + _Y + 1, pos.getZ() + _Z + 0.5, 30, 0.5, 0.5, 0.5, 0.2);
            ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.SQUID_INK, pos.getX() + _X + 0.5, pos.getY() + _Y + 1, pos.getZ() + _Z + 0.5, 10, 0.5, 0.5, 0.5, 0.2);
        }
    }

    private static void dropResult(World world, BlockPos pos, ItemStack stack) {
        float f = EntityType.ITEM.getHeight() / 2.0f;
        double d = (double)((float)pos.getX() + 0.5f) + MathHelper.nextDouble(world.random, -0.25, 0.25);
        double e = (double)((float)pos.getY() + 0.5f) + MathHelper.nextDouble(world.random, -0.25, 0.25) - (double)f;
        double g = (double)((float)pos.getZ() + 0.5f) + MathHelper.nextDouble(world.random, -0.25, 0.25);

        Block.dropStack(world, new BlockPos(d, e, g), stack);
    }

    private int isDirection(String xOrZ, Direction direction){
        if (xOrZ == "x"){
            if (direction == Direction.NORTH) { return 1; }
            if (direction == Direction.SOUTH) { return -1; }
        }
        if (xOrZ == "z"){
            if (direction == Direction.EAST) { return -1; }
            if (direction == Direction.WEST) { return 1; }
        }
        return 0;
    }
    private boolean shouldShrink(World world, BlockPos pos, Direction pistonFace) {

        int casingCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {//hurm.... complicated....
                    if (world.getBlockState(new BlockPos(pos.getX() - 1 + i, pos.getY() - 1 + j, pos.getZ() - 1 + k)).isIn(ModData.DESIZER_CASING_BLOCKS)){
                        casingCount++;
                    }
                }
            }
        }

        if (casingCount < 8) {return false;}

        for (Direction direction : Direction.values()) {
            if (direction == pistonFace || !world.isEmittingRedstonePower(pos.offset(direction), direction)) continue;
            return true;
        }
        return false;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
