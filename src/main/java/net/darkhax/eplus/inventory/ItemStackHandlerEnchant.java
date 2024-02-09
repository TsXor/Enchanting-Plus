package net.darkhax.eplus.inventory;

import net.darkhax.eplus.api.Blacklist;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class ItemStackHandlerEnchant extends ItemStackHandler {
    public static final int ENCHANT_SLOT_IDX = 0;

    protected TileEntity tableTile;

    public ItemStackHandlerEnchant(TileEntity tableTile) {
        super(1);
        this.tableTile = tableTile;
    }

    public ItemStack getEnchantingStack() {
        return this.getStackInSlot(ENCHANT_SLOT_IDX);
    }

    @Override @Nonnull
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!Blacklist.canItemStackEnchant(stack)) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
    
    @Override
    protected void onContentsChanged(int slot) {
        tableTile.setChanged();
    }
}
