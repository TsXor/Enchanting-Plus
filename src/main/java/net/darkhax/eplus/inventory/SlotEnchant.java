package net.darkhax.eplus.inventory;

import net.darkhax.eplus.api.Blacklist;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class SlotEnchant extends SlotItemHandler {
    protected final IItemChangeHandler onItemChange;

    public SlotEnchant(ItemStackHandler inventory, int index, int xPosition, int yPosition, IItemChangeHandler onItemChange) {
        super(inventory, index, xPosition, yPosition);
        this.onItemChange = onItemChange;
    }

    @Override
    public void setChanged() {
        this.onItemChange.onItemChange(this);
        super.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return Blacklist.canItemStackEnchant(stack);
    }

    public interface IItemChangeHandler {
        void onItemChange(SlotEnchant slot);
    }
}