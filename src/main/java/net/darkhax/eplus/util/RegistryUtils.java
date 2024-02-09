package net.darkhax.eplus.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;


@SuppressWarnings("deprecation")
public class RegistryUtils {
    public static Item getItemById(String itemId) {
        return Registry.ITEM.get(ResourceLocation.of(itemId, ':'));
    }

    public static Enchantment getEnchantmentById(String enchantmentId) {
        return Registry.ENCHANTMENT.get(ResourceLocation.of(enchantmentId, ':'));
    }
}
