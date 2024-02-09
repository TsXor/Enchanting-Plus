package net.darkhax.eplus.block.tileentity;

import net.darkhax.eplus.registry.ModTileEntityTypes;
import net.minecraft.nbt.CompoundNBT;

import java.awt.*;

public class TileEntityDecoration extends TileEntityWithBook {
    public int height = 0;
    public int color = Color.WHITE.getRGB();

    public TileEntityDecoration() {
        super(ModTileEntityTypes.DECORATIVE_BOOK.get());
    }

    public void decreaseHeight () {
        this.height -= 5;
        if (this.height < -35) {
            this.height = -35;
        }
    }

    public void increaseHeight () {
        this.height += 5;
        if (this.height > 35) {
            this.height = 35;
        }
    }

    @Override
    public void deserialize(CompoundNBT dataTag) {
        this.height = dataTag.getInt("Height");
        this.color = dataTag.getInt("Color");
    }

    @Override
    public void serialize(CompoundNBT dataTag) {
        dataTag.putInt("Height", this.height);
        dataTag.putInt("Color", this.color);
    }
}