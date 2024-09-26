package com.github.ethanicuss.astraladditions.items;

import com.github.ethanicuss.astraladditions.registry.ChromaticVacuumRecipe;
import com.github.ethanicuss.astraladditions.registry.DesizerRecipe;
import com.github.ethanicuss.astraladditions.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class ChromaticVacuumItem extends Item {

    boolean success = false;
    public ChromaticVacuumItem(Settings settings) {
        super(settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockState block = world.getBlockState(new BlockPos(context.getBlockPos()));

        if (!world.isClient()) {
            SimpleInventory inventory = new SimpleInventory(27);//CRAFT CHECK!!!
            inventory.setStack(0, new ItemStack(block.getBlock().asItem(), 1));
            Optional<ChromaticVacuumRecipe> match = world.getRecipeManager()
                    .getFirstMatch(ChromaticVacuumRecipe.Type.INSTANCE, inventory, world);

            if (match.isPresent()) {
                this.success = true;
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, context.getBlockPos().getX(), context.getBlockPos().getY() + 1, context.getBlockPos().getZ(), 1, 0, 0, 0, 0.01);
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, context.getBlockPos().getX() + 1, context.getBlockPos().getY() + 1, context.getBlockPos().getZ(), 1, 0, 0, 0, 0.01);
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, context.getBlockPos().getX(), context.getBlockPos().getY() + 1, context.getBlockPos().getZ() + 1, 1, 0, 0, 0, 0.01);
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, context.getBlockPos().getX() + 1, context.getBlockPos().getY() + 1, context.getBlockPos().getZ() + 1, 1, 0, 0, 0, 0.01);
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), 1, 0, 0, 0, 0.01);
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, context.getBlockPos().getX() + 1, context.getBlockPos().getY(), context.getBlockPos().getZ(), 1, 0, 0, 0, 0.01);
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ() + 1, 1, 0, 0, 0, 0.01);
                ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.CLOUD, context.getBlockPos().getX() + 1, context.getBlockPos().getY(), context.getBlockPos().getZ() + 1, 1, 0, 0, 0, 0.01);
                if (match.get().getRemainder().getItem() instanceof BlockItem) {
                    BlockState remainderBlock = ((BlockItem) match.get().getRemainder().getItem()).getBlock().getDefaultState();
                    world.setBlockState(new BlockPos(context.getBlockPos()), remainderBlock, 0);
                } else {
                    world.setBlockState(new BlockPos(context.getBlockPos()), Blocks.TUFF.getDefaultState(), 0);
                }
                context.getPlayer().giveItemStack(match.get().getOutput());
                context.getPlayer().incrementStat(Stats.USED.getOrCreateStat(this));
                context.getStack().damage(1, context.getPlayer(), e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
        }
        else{
            if (this.success) {
                world.playSound((double) context.getBlockPos().getX(), (double) context.getBlockPos().getY(), (double) context.getBlockPos().getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_INSIDE, SoundCategory.PLAYERS, 0.8f, 1.3f, true);

                this.success = false;
            }
        }

        return ActionResult.SUCCESS;
    }
}
