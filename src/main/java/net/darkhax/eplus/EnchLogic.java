package net.darkhax.eplus;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.api.event.EnchantmentCostEvent;
import net.darkhax.eplus.setup.ConfigurationHandler;
import net.darkhax.eplus.util.WorldUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public final class EnchLogic {
    public static int EnchantmentSeed;

    public static boolean isGoodTreasure(Enchantment enchantment) {
        return enchantment.isTreasureOnly() && !enchantment.isCurse();
    }

    public static int calculateNewEnchCost(Enchantment enchantment, int level) {
        // Base cost is equal to roughly 2.5 levels of EXP.
        int cost = ConfigurationHandler.BASE_COST.get();
        // Cost is multiplied up to 10, based on rarity of the enchant.
        // Rarer the enchant, higher the cost.
        cost *= Math.max(11 - enchantment.getRarity().getWeight(), 1);
        // convert cost to float to multiply with factors
        float costWithFactor = cost;
        // Increase 10% cost every level.
        costWithFactor *= (1f + (float)(level - 1) / 10);
        // The cost factor is applied. Default is 1.5.
        costWithFactor *= ConfigurationHandler.COST_FACTOR.get();
        if (enchantment.isCurse()) {
            // Curses cost even more to apply
            costWithFactor *= ConfigurationHandler.CURSE_FACTOR.get();
        } else if (enchantment.isTreasureOnly()) {
            // Treasures cost more to apply
            costWithFactor *= ConfigurationHandler.TREASURE_FACTOR.get();
        }
        final EnchantmentCostEvent event = new EnchantmentCostEvent((int)costWithFactor, enchantment, level);
        MinecraftForge.EVENT_BUS.post(event);
        return event.cost;
    }

    public static int calculateEnchUpgradeCost(EnchantmentData info) {
        return calculateNewEnchCost(info.enchantment, info.level + 1);
    }

    public static boolean isCurseAvailable(World world, BlockPos pos) {
        return WorldUtils.isWickedNight(world);
    }
    public static boolean isTreasureAvailable(World world, BlockPos pos) {
        return world.isDay() && WorldUtils.isOnBeaconBase(world, pos);
    }

    public static boolean isEnchantmentAvailable(Enchantment enchantment, World world, BlockPos pos) {
        if (Blacklist.isEnchantmentBlacklisted(enchantment)) return false;
        if (enchantment.isCurse()) return isCurseAvailable(world, pos);
        if (isGoodTreasure(enchantment)) return isTreasureAvailable(world, pos);
        return true;
    }

    public static boolean canItemEnchant(Item item, Enchantment enchantment) {
        // TODO: enable custom enchantment coexistence rules
        boolean isBook = item == Items.BOOK || item == Items.ENCHANTED_BOOK;
        return isBook || enchantment.category.canEnchant(item);
    }

    @SuppressWarnings("deprecation")
    public static List<Enchantment> getValidEnchantments(ItemStack stack, World world, BlockPos pos) {
        if (stack.isEmpty()) return new ArrayList<>();
        return Registry.ENCHANTMENT.stream()
                .filter(enchantment -> canItemEnchant(stack.getItem(), enchantment) && isEnchantmentAvailable(enchantment, world, pos))
                .toList();
    }
}