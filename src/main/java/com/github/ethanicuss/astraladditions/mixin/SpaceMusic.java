package com.github.ethanicuss.astraladditions.mixin;

import com.github.ethanicuss.astraladditions.musicplayer.MusicPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftClient.class)
public class SpaceMusic {
    @Shadow public boolean wireFrame;
    @Shadow @Final private MusicTracker musicTracker;
    @Inject(method = "getMusicType", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void getMusicType(CallbackInfoReturnable<MusicSound> cir){
        cir.setReturnValue(MusicPlayer.findMusic(musicTracker));
    }
}
