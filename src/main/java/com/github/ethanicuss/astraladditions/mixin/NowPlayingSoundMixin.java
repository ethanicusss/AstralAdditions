package com.github.ethanicuss.astraladditions.mixin;

import com.github.ethanicuss.astraladditions.musicplayer.MusicPlayer;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SoundSystem.class)
public class NowPlayingSoundMixin {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/Sound;getIdentifier()Lnet/minecraft/util/Identifier;"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )

    private void onPlay(SoundInstance instance, CallbackInfo ci,
                        WeightedSoundSet weightedSoundSet, Identifier soundEventId,
                        Sound sound, float volume, float pitch, SoundCategory category,
                        float x, float y, SoundInstance.AttenuationType attenuationType,
                        boolean looping
    ) {
        if (instance.getCategory() != SoundCategory.MUSIC) return;

        Identifier chosen = sound.getIdentifier();
        MusicPlayer.onTrackStarted(chosen);
    }
}