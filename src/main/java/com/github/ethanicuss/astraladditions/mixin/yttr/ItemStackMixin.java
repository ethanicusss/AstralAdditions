package com.github.ethanicuss.astraladditions.mixin.yttr;

import com.unascribed.yttr.inventory.DSUScreenHandler;
import com.unascribed.yttr.mixin.deep.MixinItemStack;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//The reason this is in the YTTR directory is that YTTR also Mixins into this class (MixinItemStack.class) and this overwrites it.
//Idk how stable overwriting a Mixin is but for now it seems to work!
@Mixin({ItemStack.class})
public class ItemStackMixin {
    public ItemStackMixin() {
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"getMaxCount"},
            cancellable = true
    )
    public void getMaxCount(CallbackInfoReturnable<Integer> ci) {
        if (((MutableInt) DSUScreenHandler.increaseStackSize.get()).intValue() > 0) {
                ci.setReturnValue(512);
        }

    }
}
