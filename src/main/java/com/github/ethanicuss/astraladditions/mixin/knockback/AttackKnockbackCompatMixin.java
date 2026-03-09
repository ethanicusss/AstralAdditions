package com.github.ethanicuss.astraladditions.mixin.knockback;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class AttackKnockbackCompatMixin {

	@Redirect(method = "attack(Lnet/minecraft/entity/Entity;)V",
			at = @At(value = "FIELD",
					target = "Lnet/minecraft/entity/attribute/EntityAttributes;ATTACK_KNOCKBACK:Lnet/minecraft/entity/attribute/EntityAttribute;")
	)
	private EntityAttribute redirectAttackKnockbackAttribute() {
		return EntityAttributes.GENERIC_ATTACK_KNOCKBACK;
	}
}