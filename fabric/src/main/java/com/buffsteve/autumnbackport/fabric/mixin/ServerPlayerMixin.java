package com.buffsteve.autumnbackport.fabric.mixin;

import com.buffsteve.autumnbackport.registry.ModEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Straw beds never set your spawn point. */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void autumnbackport$keepSpawnOffStrawBeds(ResourceKey<Level> dimension, @Nullable BlockPos pos, float angle,
                                                      boolean forced, boolean sendMessage, CallbackInfo ci) {
        if (ModEvents.shouldBlockSpawnPoint((ServerPlayer) (Object) this, pos, forced)) {
            ci.cancel();
        }
    }
}
