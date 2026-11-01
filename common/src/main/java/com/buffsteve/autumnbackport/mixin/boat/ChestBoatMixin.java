package com.buffsteve.autumnbackport.mixin.boat;

import com.buffsteve.autumnbackport.registry.ModBoats;
import com.buffsteve.autumnbackport.registry.ModItems;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Same as BoatMixin, for boats with a chest (they have their own switch). */
@Mixin(ChestBoat.class)
public abstract class ChestBoatMixin {
    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    private void autumnbackport$poplarChestBoatItem(CallbackInfoReturnable<Item> cir) {
        if (((ChestBoat) (Object) this).getVariant() == ModBoats.poplar()) {
            cir.setReturnValue(ModItems.POPLAR_CHEST_BOAT.get());
        }
    }
}
