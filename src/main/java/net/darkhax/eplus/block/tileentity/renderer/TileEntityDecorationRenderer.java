package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

import java.awt.*;


@SuppressWarnings("deprecation")
public class TileEntityDecorationRenderer extends TileEntityBookRenderer {
    public TileEntityDecorationRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    float getHeightOffset(TileEntityWithBook tile) {
        float height = 0.35f;
        if (tile instanceof TileEntityDecoration decoTile){
            height += (float) decoTile.height / 100f;
        }
        return height;
    }

    @Override
    public void applyRenderEffects(TileEntityWithBook tile) {
        if (tile instanceof TileEntityDecoration decoTile) {
            final Color color = new Color(decoTile.color);
            float r = color.getRed(); float g = color.getGreen(); float b = color.getBlue();
            RenderSystem.color4f(r / 255f, g / 255f, b / 255f, 1.0f);
        }
    }
}
