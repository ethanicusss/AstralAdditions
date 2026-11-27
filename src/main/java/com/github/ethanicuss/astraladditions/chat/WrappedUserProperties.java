package com.github.ethanicuss.astraladditions.chat;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.ethanicuss.astraladditions.config.AstralAdditionsConfig;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.minecraft.UserApiService.UserFlag;
import com.mojang.authlib.minecraft.UserApiService.UserProperties;

public final class WrappedUserProperties {

    private WrappedUserProperties() { throw new UnsupportedOperationException("Can't touch this"); }

    public static UserProperties of(UserProperties properties) {
        ImmutableSet.Builder<UserFlag> flags = ImmutableSet.builder();

        flags.add(UserFlag.CHAT_ALLOWED);
        flags.add(UserFlag.SERVERS_ALLOWED);
        flags.add(UserFlag.REALMS_ALLOWED);

        if (AstralAdditionsConfig.chatTelemetryEnabled) {
            addOptionalFlag(UserFlag.TELEMETRY_ENABLED, flags, properties);
        }

        if (AstralAdditionsConfig.chatProfanityFilterEnabled) {
            addOptionalFlag(UserFlag.PROFANITY_FILTER_ENABLED, flags, properties);
        }

        return new UserProperties(flags.build());

    }

    private static void addOptionalFlag(UserFlag flag, ImmutableSet.Builder<UserFlag> builder, UserProperties properties) {
        if (properties.flag(flag)) {
            builder.add(flag);
        }
    }

}
