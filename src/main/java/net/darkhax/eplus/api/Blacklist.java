package net.darkhax.eplus.api;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.util.RegistryUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * TODO: allow more advanced control of blacklist
 */
public final class Blacklist {
    private static final Set<Item> BLACKLIST_ITEMS = new HashSet<>();
    private static final Set<Enchantment> BLACKLIST_ENCHANTMENTS = new HashSet<>();
    private static final Map<Enchantment, Integer> MAX_LEVELS = new HashMap<>();

    public static void blacklistItem(Item item) {
        BLACKLIST_ITEMS.add(item);
    }

    public static void blacklistEnchantment(Enchantment enchantment) {
        BLACKLIST_ENCHANTMENTS.add(enchantment);
    }

    public static boolean isItemBlacklisted(Item item) {
        return BLACKLIST_ITEMS.contains(item);
    }

    public static boolean isEnchantmentBlacklisted(Enchantment enchantment) {
        return BLACKLIST_ENCHANTMENTS.contains(enchantment);
    }

    public static boolean canItemStackEnchant(ItemStack stack) {
        return !Blacklist.isItemBlacklisted(stack.getItem())
            && (stack.isEnchantable() || stack.isEnchanted() || stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK);
    }

    public static void blacklistItem(String itemId) {
        final Item item = RegistryUtils.getItemById(itemId);
        if (item != null) {
            Blacklist.blacklistItem(item);
        } else {
            EnchantingPlus.LOGGER.error("Tried to blacklist item {} but it does not exist.", itemId);
        }
    }

    public static void blacklistEnchantment(String enchantmentId) {
        final Enchantment ench = RegistryUtils.getEnchantmentById(enchantmentId);
        if (ench != null) {
            Blacklist.blacklistEnchantment(ench);
        } else {
            EnchantingPlus.LOGGER.error("Tried to blacklist enchantment {} but it does not exist.", enchantmentId);
        }
    }

    public static void clearItemBlacklist() {
        BLACKLIST_ITEMS.clear();
    }

    public static void clearEnchantmentBlacklist() {
        BLACKLIST_ENCHANTMENTS.clear();
    }
}