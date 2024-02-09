package net.darkhax.eplus.api.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.Map;

/**
 * This event is fired on both the client and server, and is used to determine the types of
 * enchantments that can be applied to an item at the advanced table.
 */
public class AvailableEnchantmentsEvent extends Event {
    public final ItemStack stack;
    public Map<Enchantment, Integer> enchantments;

    public AvailableEnchantmentsEvent (ItemStack stack, Map<Enchantment, Integer> enchantments) {
        super();
        this.stack = stack;
        this.enchantments = enchantments;
    }
}
