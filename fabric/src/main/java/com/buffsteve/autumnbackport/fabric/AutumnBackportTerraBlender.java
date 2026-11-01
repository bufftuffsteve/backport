package com.buffsteve.autumnbackport.fabric;

import com.buffsteve.autumnbackport.world.DappledForestRegion;
import terrablender.api.TerraBlenderApi;

/** TerraBlender calls this on Fabric when it's ready for regions (listed in fabric.mod.json). */
public class AutumnBackportTerraBlender implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        DappledForestRegion.register();
    }
}
