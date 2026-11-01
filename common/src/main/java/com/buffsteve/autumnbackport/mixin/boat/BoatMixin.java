package com.buffsteve.autumnbackport.mixin.boat;

import com.buffsteve.autumnbackport.registry.ModBoats;
import com.buffsteve.autumnbackport.registry.ModItems;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla picks a boat's item with a fixed switch over the boat types, so an added type would
 * fall back to oak. The item decides what a broken boat drops, what pick-block gives and the
 * boat's name.
 */
@Mixin(Boat.class)
public abstract class BoatMixin {
    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    private void autumnbackport$poplarBoatItem(CallbackInfoReturnable<Item> cir) {
        if (((Boat) (Object) this).getVariant() == ModBoats.poplar()) {
            cir.setReturnValue(ModItems.POPLAR_BOAT.get());
        }
    }
}
