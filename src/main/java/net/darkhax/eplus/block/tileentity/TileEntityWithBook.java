package net.darkhax.eplus.block.tileentity;

import net.darkhax.bookshelf.block.tileentity.TileEntityBasicTickable;
import net.darkhax.eplus.api.BookTexture;
import net.darkhax.eplus.block.IBookTexture;
import net.darkhax.eplus.registry.ModBookTextures;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;


public abstract class TileEntityWithBook extends TileEntityBasicTickable {
    private static final Random RANDOM = new Random();

    // book status
    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float flipRandom;
    public float flipTurn;
    public float bookSpread;
    public float bookSpreadPrev;
    public float bookRotation;
    public float bookRotationPrev;
    public float offset;

    public TileEntityWithBook(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Nonnull
    public BookTexture getBookTexture() {
        BookTexture DEFAULT = ModBookTextures.DEFAULT.get();
        Block myBlock = this.getBlockState().getBlock();
        return myBlock instanceof IBookTexture blockWithBook
                ? Objects.requireNonNullElse(blockWithBook.getBookTexture(), DEFAULT) : DEFAULT;
    }

    public boolean isOpen () {
        return this.bookSpread >= 1;
    }

    /**
     * This method is adapted from {@link EnchantingTableTileEntity#tick()}.
     */
    @Override
    public void onTileTick() {
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        final PlayerEntity player = Objects.requireNonNull(this.getLevel())
                .getNearestPlayer(
                        this.worldPosition.getX() + 0.5F,
                        this.worldPosition.getY() + 0.5F,
                        this.worldPosition.getZ() + 0.5F,
                        3.0D, false
                );

        if (player != null) {
            final double distX = player.getX() - (this.worldPosition.getX() + 0.5F);
            final double distZ = player.getZ() - (this.worldPosition.getZ() + 0.5F);
            this.offset = (float) MathHelper.atan2(distZ, distX);
            this.bookSpread += 0.1F;

            if (this.bookSpread < 0.5F || RANDOM.nextInt(40) == 0) {
                final float originalFlip = this.flipRandom;
                do {
                    this.flipRandom += RANDOM.nextInt(4) - RANDOM.nextInt(4);
                } while (originalFlip == this.flipRandom);
            }
        } else {
            this.offset += 0.02F;
            this.bookSpread -= 0.1F;
        }

        while (this.bookRotation >= (float) Math.PI) { this.bookRotation -= (float) Math.PI * 2F; }
        while (this.bookRotation < -(float) Math.PI) { this.bookRotation += (float) Math.PI * 2F; }

        while (this.offset >= (float) Math.PI) { this.offset -= (float) Math.PI * 2F; }
        while (this.offset < -(float) Math.PI) { this.offset += (float) Math.PI * 2F; }

        float offsetDiff = this.offset - this.bookRotation;
        while (offsetDiff >= (float) Math.PI) { offsetDiff -= (float) Math.PI * 2F; }
        while (offsetDiff < -(float) Math.PI) { offsetDiff += (float) Math.PI * 2F; }

        this.bookRotation += offsetDiff * 0.4F;
        this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float filpDiff = (this.flipRandom - this.pageFlip) * 0.4F;
        final float flipRange = 0.2F;
        filpDiff = MathHelper.clamp(filpDiff, -flipRange, flipRange);
        this.flipTurn += (filpDiff - this.flipTurn) * 0.9F;
        this.pageFlip += this.flipTurn;
    }
}
