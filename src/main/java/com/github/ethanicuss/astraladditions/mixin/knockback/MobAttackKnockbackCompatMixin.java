package com.github.ethanicuss.astraladditions.mixin.knockback;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobEntity.class)
public class MobAttackKnockbackCompatMixin {

	@Redirect(method = "tryAttack(Lnet/minecraft/entity/Entity;)Z",
			at = @At(value = "FIELD",
					target = "Lnet/minecraft/entity/attribute/EntityAttributes;ATTACK_KNOCKBACK:Lnet/minecraft/entity/attribute/EntityAttribute;"),
			require = 0
	)
	private EntityAttribute redirectAttackKnockbackAttribute() {
		return EntityAttributes.GENERIC_ATTACK_KNOCKBACK;
	}
}