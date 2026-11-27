package com.github.ethanicuss.astraladditions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.chat.WrappedUserApiService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;

@Mixin(MinecraftClient.class)
public class ChatMixin {

    @Inject(method = "createUserApiService", at = @At("RETURN"), cancellable = true)
    public void onCreateUserAPI(YggdrasilAuthenticationService authService, RunArgs runArgs, CallbackInfoReturnable<UserApiService> info) {
        UserApiService returnedService = info.getReturnValue();
        assert returnedService != null;
        info.setReturnValue(new WrappedUserApiService(returnedService));

        AstralAdditions.LOGGER.info("Successfully supplanted UserApiService with a wrapped version.");
    }

}
