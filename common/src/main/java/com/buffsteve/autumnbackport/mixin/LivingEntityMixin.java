package com.buffsteve.autumnbackport.mixin;

import com.buffsteve.autumnbackport.block.StrawBedBlock;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Straw bed hooks into lying down and getting up (see StrawBedBlock). */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    private BlockPos autumnbackport$leftBed;

    @Inject(method = "startSleeping", at = @At("TAIL"))
    private void autumnbackport$onLieDown(BlockPos pos, CallbackInfo ci) {
        StrawBedBlock.onLieDown((LivingEntity) (Object) this, pos);
    }

    @Inject(method = "stopSleeping", at = @At("HEAD"))
    private void autumnbackport$rememberBed(CallbackInfo ci) {
        Optional<BlockPos> pos = ((LivingEntity) (Object) this).getSleepingPos();
        autumnbackport$leftBed = pos.orElse(null);
    }

    @Inject(method = "stopSleeping", at = @At("TAIL"))
    private void autumnbackport$onGetUp(CallbackInfo ci) {
        if (autumnbackport$leftBed != null) {
            StrawBedBlock.onGetUp((LivingEntity) (Object) this, autumnbackport$leftBed);
            autumnbackport$leftBed = null;
        }
    }
}
