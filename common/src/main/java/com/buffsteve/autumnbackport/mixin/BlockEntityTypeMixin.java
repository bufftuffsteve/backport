package com.buffsteve.autumnbackport.mixin;

import com.buffsteve.autumnbackport.registry.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets the vanilla sign block entities (which store the text) work in poplar signs.
 * Vanilla only accepts the blocks listed when the sign block entity type was made.
 */
@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void autumnbackport$acceptPoplarSigns(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Object self = this;
        Block block = state.getBlock();
        if (self == BlockEntityType.SIGN && (block == ModBlocks.POPLAR_SIGN.get() || block == ModBlocks.POPLAR_WALL_SIGN.get())) {
            cir.setReturnValue(true);
        } else if (self == BlockEntityType.HANGING_SIGN
                && (block == ModBlocks.POPLAR_HANGING_SIGN.get() || block == ModBlocks.POPLAR_WALL_HANGING_SIGN.get())) {
            cir.setReturnValue(true);
        }
    }
}
