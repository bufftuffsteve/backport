package com.buffsteve.autumnbackport.fabric.mixin;

import com.buffsteve.autumnbackport.registry.ModEvents;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** After each player tick: lays straw bed sleepers at the bed's real height. */
@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void autumnbackport$lowerStrawBedSleeper(CallbackInfo ci) {
        ModEvents.lowerStrawBedSleeper((Player) (Object) this);
    }
}
