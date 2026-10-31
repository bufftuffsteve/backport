package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.block.StrawBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Game events the straw bed needs, since 1.21.1 has no bed rules to configure this. */
public final class ModEvents {
    /** Straw beds never set your spawn point (no "Respawn point set" message either). */
    public static void keepSpawnOffStrawBeds(PlayerSetSpawnEvent event) {
        BlockPos spawn = event.getNewSpawn();
        if (spawn != null && !event.isForced()
                && event.getEntity().level().getBlockState(spawn).getBlock() instanceof StrawBedBlock) {
            event.setCanceled(true);
        }
    }

    /**
     * 1.21.1 always lays sleepers at normal bed height (9px). A straw bed is only 4px tall,
     * so move sleepers down onto it, at the height 26.3 uses.
     */
    public static void lowerStrawBedSleepers(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.isSleeping()) {
            return;
        }
        player.getSleepingPos().ifPresent(pos -> {
            if (player.level().getBlockState(pos).getBlock() instanceof StrawBedBlock) {
                double y = pos.getY() + StrawBedBlock.SLEEP_HEIGHT;
                if (Math.abs(player.getY() - y) > 1.0E-4) {
                    player.setPos(player.getX(), y, player.getZ());
                }
            }
        });
    }

    private ModEvents() {}
}
