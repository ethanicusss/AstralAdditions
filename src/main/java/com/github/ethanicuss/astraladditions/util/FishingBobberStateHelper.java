package com.github.ethanicuss.astraladditions.util;

import net.minecraft.entity.projectile.FishingBobberEntity;

import java.lang.reflect.Field;

public class FishingBobberStateHelper {

	private static Field stateField;

	static {
		try {
			for (Field field : FishingBobberEntity.class.getDeclaredFields()) {
				if (field.getType().isEnum()) {
					stateField = field;
					stateField.setAccessible(true);
					break;
				}
			}

			if (stateField == null) {
				throw new RuntimeException("Could not find FishingBobberEntity 'state' field! No enum field found.");
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize FishingBobberStateHelper", e);
		}
	}

	public static Object getState(FishingBobberEntity entity) {
		try {
			return stateField.get(entity);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get state from bobber entity", e);
		}
	}

	public static void setState(FishingBobberEntity entity, Object state) {
		try {
			stateField.set(entity, state);
		} catch (Exception e) {
			throw new RuntimeException("Failed to set state on bobber entity", e);
		}
	}
}