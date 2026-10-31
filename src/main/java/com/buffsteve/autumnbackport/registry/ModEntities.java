package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.entity.Cushion;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Entities, same "minecraft" namespace and IDs as 26.3. */
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, ModBlocks.VANILLA);

    // 1 block wide, 1/4 block tall. Never moves, so the server doesn't send position updates.
    public static final DeferredHolder<EntityType<?>, EntityType<Cushion>> CUSHION = ENTITY_TYPES.register("cushion",
            () -> EntityType.Builder.<Cushion>of(Cushion::new, MobCategory.MISC)
                    .sized(1.0F, 0.25F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("cushion"));

    private ModEntities() {}
}
