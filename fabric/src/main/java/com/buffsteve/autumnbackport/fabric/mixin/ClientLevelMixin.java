package com.buffsteve.autumnbackport.fabric.mixin;

import com.buffsteve.autumnbackport.client.AutumnBackportClientCommon;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.ColorResolver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Gives the leaf litter "dry foliage" tint its own color cache, like vanilla's grass,
 * foliage and water tints. Without it, chunk rendering crashes on leaf litter.
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Shadow
    @Final
    private Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void autumnbackport$addDryFoliageCache(CallbackInfo ci) {
        ClientLevel level = (ClientLevel) (Object) this;
        ColorResolver resolver = AutumnBackportClientCommon.DRY_FOLIAGE;
        tintCaches.put(resolver, new BlockTintCache(pos -> level.calculateBlockTint(pos, resolver)));
    }
}
