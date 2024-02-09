package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.tileentity.EnchantmentTableTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class TileEntityBookRenderer extends TileEntityRenderer<TileEntityWithBook> {
    private final BookModel bookModel = new BookModel();

    public TileEntityBookRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    /**
     * This method is adapted from {@link EnchantmentTableTileEntityRenderer#render(EnchantingTableTileEntity, float, MatrixStack, IRenderTypeBuffer, int, int)}.
     */
    @Override @ParametersAreNonnullByDefault
    public void render(
        TileEntityWithBook blockEntity, float partialTicks, MatrixStack matrixStack,
        IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay
    ) {
        matrixStack.pushPose();
        matrixStack.translate(0.5D, this.getHeightOffset(blockEntity), 0.5D);
        float ticks = (float)blockEntity.tickCount + partialTicks;
        matrixStack.translate(0.0D, (double)(0.1F + MathHelper.sin(ticks * 0.1F) * 0.01F), 0.0D);
        this.applyRenderEffects(blockEntity);

        float rotDiff = blockEntity.bookRotation - blockEntity.bookRotationPrev;
        while (rotDiff >= (float)Math.PI) { rotDiff -= ((float)Math.PI * 2F); }
        while(rotDiff < -(float)Math.PI) { rotDiff += ((float)Math.PI * 2F); }

        float rotAngle = blockEntity.bookRotationPrev + rotDiff * partialTicks;
        matrixStack.mulPose(Vector3f.YP.rotation(-rotAngle));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(80.0F));
        float pageFlip = MathHelper.lerp(partialTicks, blockEntity.pageFlipPrev, blockEntity.pageFlip);
        float leftPageFlip = MathHelper.frac(pageFlip + 0.25F) * 1.6F - 0.3F;
        float rightPageFlip = MathHelper.frac(pageFlip + 0.75F) * 1.6F - 0.3F;
        float bookSpread = MathHelper.lerp(partialTicks, blockEntity.bookSpreadPrev, blockEntity.bookSpread);
        this.bookModel.setupAnim(ticks, MathHelper.clamp(leftPageFlip, 0.0F, 1.0F),
                MathHelper.clamp(rightPageFlip, 0.0F, 1.0F), bookSpread);
        IVertexBuilder vertexBuilder = blockEntity.getBookTexture().material.buffer(buffer, RenderType::entitySolid);
        this.bookModel.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay,
                1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
    }

    abstract float getHeightOffset(TileEntityWithBook tile);

    public void applyRenderEffects(TileEntityWithBook tile) {}
}