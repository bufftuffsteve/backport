package com.buffsteve.autumnbackport.datagen;

import com.buffsteve.autumnbackport.AutumnBackport;
import com.buffsteve.autumnbackport.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** English names. Use the same names as vanilla 26.3. */
public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AutumnBackport.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.POPLAR_LOG, "Poplar Log");
        addBlock(ModBlocks.POPLAR_WOOD, "Poplar Wood");
        addBlock(ModBlocks.STRIPPED_POPLAR_LOG, "Stripped Poplar Log");
        addBlock(ModBlocks.STRIPPED_POPLAR_WOOD, "Stripped Poplar Wood");
        addBlock(ModBlocks.POPLAR_PLANKS, "Poplar Planks");
        addBlock(ModBlocks.POPLAR_STAIRS, "Poplar Stairs");
        addBlock(ModBlocks.POPLAR_SLAB, "Poplar Slab");
        addBlock(ModBlocks.POPLAR_FENCE, "Poplar Fence");
        addBlock(ModBlocks.POPLAR_FENCE_GATE, "Poplar Fence Gate");
        addBlock(ModBlocks.POPLAR_DOOR, "Poplar Door");
        addBlock(ModBlocks.POPLAR_TRAPDOOR, "Poplar Trapdoor");
        addBlock(ModBlocks.POPLAR_PRESSURE_PLATE, "Poplar Pressure Plate");
        addBlock(ModBlocks.POPLAR_BUTTON, "Poplar Button");
    }
}
