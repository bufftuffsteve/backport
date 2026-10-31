package com.buffsteve.autumnbackport;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * Client-only entry point. Never loads on a dedicated server.
 * Renderers (cushion, signs, falling-leaf particles) get registered here later.
 */
@Mod(value = AutumnBackport.MODID, dist = Dist.CLIENT)
public class AutumnBackportClient {
    public AutumnBackportClient(ModContainer container) {
    }
}
