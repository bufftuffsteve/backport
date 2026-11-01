package com.buffsteve.autumnbackport;

import com.buffsteve.autumnbackport.registry.ModFlammability;
import com.buffsteve.autumnbackport.registry.ModRegistry;
import com.buffsteve.autumnbackport.registry.ModStats;
import com.buffsteve.autumnbackport.registry.ModStripping;
import com.buffsteve.autumnbackport.registry.ModWoodTypes;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/**
 * Shared startup for every loader. Each loader's entry point calls {@link #init()} as early
 * as it can, then {@link #setup()} once registration is done (on the main thread).
 * The Dappled Forest itself is added by {@link com.buffsteve.autumnbackport.world.DappledForestRegion#register()},
 * called when TerraBlender is ready.
 */
public final class AutumnBackport {
    /** Must match mod_id in gradle.properties. */
    public static final String MODID = "autumnbackport";
    public static final Logger LOGGER = LogUtils.getLogger();

    /** Queues (or, on Fabric, performs) all registration. */
    public static void init() {
        // Load the poplar wood type before the game builds its list of sign textures.
        LOGGER.debug("Wood type {} ready", ModWoodTypes.POPLAR.name());
        ModRegistry.init();
    }

    /** Things that need registered blocks. Vanilla's tables here aren't thread-safe, so run on the main thread. */
    public static void setup() {
        ModFlammability.register();
        ModStats.register();
        ModStripping.register();
    }

    private AutumnBackport() {}
}
