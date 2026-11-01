package com.buffsteve.autumnbackport.mixin.boat;

import java.util.Arrays;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds poplar to 1.21.1's fixed list of boat types (Fabric and Forge; NeoForge uses its
 * enum extension file instead). It's added right after vanilla's own types are created and
 * before the game builds its name and number lookups, so poplar boats save, load and sync
 * like any other. Textures: textures/entity/boat/poplar.png and textures/entity/chest_boat/poplar.png.
 */
@Mixin(Boat.Type.class)
public abstract class BoatTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static Boat.Type[] $VALUES;

    @Invoker("<init>")
    private static Boat.Type autumnbackport$create(String internalName, int ordinal, Block planks, String name) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC,
            target = "Lnet/minecraft/world/entity/vehicle/Boat$Type;$VALUES:[Lnet/minecraft/world/entity/vehicle/Boat$Type;",
            shift = At.Shift.AFTER))
    private static void autumnbackport$addPoplar(CallbackInfo ci) {
        Boat.Type[] values = Arrays.copyOf($VALUES, $VALUES.length + 1);
        // Planks are swapped for poplar planks once blocks are registered (see BoatTypeAccessor).
        values[values.length - 1] = autumnbackport$create("AUTUMNBACKPORT_POPLAR", values.length - 1, Blocks.OAK_PLANKS, "poplar");
        $VALUES = values;
    }
}
