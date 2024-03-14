package com.github.ethanicuss.astraladditions.entities.pylon;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Objects;

public class PylonEntity extends Entity {

    private static final TrackedData<String> playerOwner;
    private String player;
    public int pylonAge;

    public PylonEntity(EntityType<? extends Entity> entityType, World world) {
        super((EntityType<? extends Entity>)entityType, world);
        this.setNoGravity(true);
        this.pylonAge = this.random.nextInt(100000);
        this.setPlayer(this.getDataTracker().get(playerOwner));
    }

    @Override
    public void tick() {
        ++this.pylonAge;
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    @Override
    public boolean collides() {
        return false;
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(playerOwner, "");
    }

    static {
        playerOwner = DataTracker.registerData(PylonEntity.class, TrackedDataHandlerRegistry.STRING);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.player != null) {
            nbt.putString("playerOwner", this.player);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("playerOwner")){
            this.player = nbt.getString("playerOwner");
        }
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(ModItems.PYLON);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public boolean shouldRender(double distance) {
        return super.shouldRender(distance);
    }

    public static boolean isOwner(PylonEntity pylon, String name){
        return (Objects.equals(name, pylon.getPlayer()) || Objects.equals(name, pylon.dataTracker.get(playerOwner)));
    }

    public void setPlayer(String name){
        this.player = name;
        this.getDataTracker().set(playerOwner, this.player);
    }

    public String getPlayer(){
        return this.player;
    }
}
