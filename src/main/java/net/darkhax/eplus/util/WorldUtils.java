package net.darkhax.eplus.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtils {
    public static boolean isOnBeaconBase(World world, BlockPos tablePos) {
        BlockPos down = tablePos.below();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                final BlockPos currentPos = down.offset(x, 0, z);
                if (!world.getBlockState(currentPos).is(BlockTags.BEACON_BASE_BLOCKS)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@link World#getMoonPhase()} is not available on server side, so I copy it here.
     */
    public static int getMoonPhase(World world) {
        return world.dimensionType().moonPhase(world.dayTime());
    }

    public static boolean isWickedNight(World world) {
        // Note: `getCelestialAngle` is renamed to `getTimeOfDay`
        final float skyAngle = world.getTimeOfDay(1f);
        final boolean isNightRange = skyAngle > 0.40 && skyAngle < 0.60;
        final boolean isFullMoon = getMoonPhase(world) == 0;
        return isFullMoon && isNightRange;
    }
}
