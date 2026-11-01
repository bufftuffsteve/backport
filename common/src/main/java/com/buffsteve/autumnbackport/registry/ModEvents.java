package com.buffsteve.autumnbackport.registry;

import com.buffsteve.autumnbackport.block.StrawBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Game rules the straw bed needs, since 1.21.1 has no bed rules to configure this.
 * Each loader calls these from its own events (or mixins, on Fabric).
 */
public final class ModEvents {
    /**
     * Straw beds never set your spawn point (no "Respawn point set" message either).
     * Returns true if setting the spawn at {@code spawn} should be stopped.
     */
    public static boolean shouldBlockSpawnPoint(Player player, @Nullable BlockPos spawn, boolean forced) {
        return spawn != null && !forced && player.level().getBlockState(spawn).getBlock() instanceof StrawBedBlock;
    }

    /**
     * 1.21.1 always lays sleepers at normal bed height (9px). A straw bed is only 4px tall,
     * so move sleepers down onto it, at the height 26.3 uses. Call after each player tick.
     */
    public static void lowerStrawBedSleeper(Player player) {
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
