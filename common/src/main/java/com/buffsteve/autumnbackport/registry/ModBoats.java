package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.mixin.boat.BoatTypeAccessor;
import net.minecraft.world.entity.vehicle.Boat;

/** The poplar boat type added by BoatTypeMixin (Fabric and Forge). */
public final class ModBoats {
    public static Boat.Type poplar() {
        return Boat.Type.byName("poplar");
    }

    /** Broken poplar boats drop poplar planks. Call once blocks are registered. */
    public static void setPlanks() {
        ((BoatTypeAccessor) (Object) poplar()).autumnbackport$setPlanks(ModBlocks.POPLAR_PLANKS.get());
    }

    private ModBoats() {}
}
