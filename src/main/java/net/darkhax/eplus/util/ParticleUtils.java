package net.darkhax.eplus.util;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleUtils {
    public static void spawnParticleRing(
        World world, IParticleData particle,
        double x, double y, double z,
        double velocityX, double velocityY, double velocityZ,
        double step
    ) {
        for (double degree = 0.0d; degree < 2 * Math.PI; degree += step) {
            world.addParticle(particle, x + 0.5 + Math.cos(degree), y, z + 0.5 + Math.sin(degree), velocityX, velocityY, velocityZ);
        }
    }
    public static void spawnParticleRing(
        World world, IParticleData particle,
        BlockPos pos, double velocityX, double velocityY, double velocityZ,
        double step
    ) {
        spawnParticleRing(world, particle, pos.getX(), pos.getY(), pos.getZ(), velocityX, velocityY, velocityZ, step);
    }
}
