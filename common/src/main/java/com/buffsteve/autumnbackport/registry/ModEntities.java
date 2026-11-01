package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.entity.Cushion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import com.buffsteve.autumnbackport.platform.RegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;

/** Entities, same "minecraft" namespace and IDs as 26.3. */
public final class ModEntities {
    // 1 block wide, 1/4 block tall. Never moves, so the server doesn't send position updates.
    public static final RegistryEntry<EntityType<Cushion>> CUSHION = ModRegistry.register(BuiltInRegistries.ENTITY_TYPE, "cushion",
            () -> EntityType.Builder.<Cushion>of(Cushion::new, MobCategory.MISC)
                    .sized(1.0F, 0.25F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("cushion"));

    private ModEntities() {}
}
