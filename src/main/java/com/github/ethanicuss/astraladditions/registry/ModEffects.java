package com.github.ethanicuss.astraladditions.registry;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.github.ethanicuss.astraladditions.effects.SinkEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModEffects {

    public static StatusEffect SINK;

    public static StatusEffect registerStatusEffect(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(AstralAdditions.MOD_ID, name),
                new SinkEffect(StatusEffectCategory.NEUTRAL, 3124687));
    }

    public static void registerEffects() {
        SINK = registerStatusEffect("sink");
    }
}
