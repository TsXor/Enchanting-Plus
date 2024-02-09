package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darkhax.bookshelf.util.MathsUtils;
import net.darkhax.eplus.EnchLogic;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.darkhax.eplus.util.ParticleUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public class TileEntityAdvancedTableRenderer extends TileEntityBookRenderer {
    public TileEntityAdvancedTableRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    float getHeightOffset (TileEntityWithBook tile) {
        return 0.75f;
    }

    @Override @ParametersAreNonnullByDefault
    public void render(
            TileEntityWithBook blockEntity, float partialTicks, MatrixStack matrixStack,
            IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay
    ) {
        World world = Objects.requireNonNull(blockEntity.getLevel());
        BlockPos pos = Objects.requireNonNull(blockEntity.getBlockPos());
        if (EnchLogic.isCurseAvailable(world, pos) && blockEntity.bookSpread != 0) {
            ParticleUtils.spawnParticleRing(
                    world, ParticleTypes.PORTAL,
                    pos.offset(0, 1, 0),
                    0, -1, 0, 0.45
            );
        } else if (MathsUtils.tryPercentage(0.5) && EnchLogic.isTreasureAvailable(world, pos)) {
            int redSpeedX = world.getGameTime() % 2 == 0 ? -1 : 0;
            ParticleUtils.spawnParticleRing(
                    world, RedstoneParticleData.REDSTONE,
                    pos.offset(0, 1, 0),
                    redSpeedX, 1, 0, 0.45
            );
        }
        super.render(blockEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }
}
