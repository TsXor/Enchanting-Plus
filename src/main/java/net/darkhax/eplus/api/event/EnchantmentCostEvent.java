package net.darkhax.eplus.api.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is fired on the client and the server when the cost of an enchantment is
 * calculated for e+.
 */
public class EnchantmentCostEvent extends Event {
    public final Enchantment enchantment;
    public final int level;
    public final int originalCost;
    public int cost;

    public EnchantmentCostEvent (int cost, Enchantment enchantment, int level) {
        super();
        this.cost = cost;
        this.originalCost = cost;
        this.enchantment = enchantment;
        this.level = level;
    }
}