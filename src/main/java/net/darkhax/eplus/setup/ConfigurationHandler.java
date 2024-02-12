package net.darkhax.eplus.setup;

import com.google.common.collect.ImmutableList;
import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.api.Blacklist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;


@Mod.EventBusSubscriber(modid = EnchantingPlus.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ConfigurationHandler {
    public static ForgeConfigSpec SERVER_CONFIG;

    public static final String CATEGORY_PARAMETERS = "parameters";
    public static ForgeConfigSpec.DoubleValue COST_FACTOR;
    public static ForgeConfigSpec.DoubleValue TREASURE_FACTOR;
    public static ForgeConfigSpec.DoubleValue CURSE_FACTOR;
    public static ForgeConfigSpec.IntValue BASE_COST;
    public static ForgeConfigSpec.DoubleValue FLOATING_BOOK_BONUS;

    public static final String CATEGORY_BLACKLISTS = "blacklists";
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_ITEMS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_ENCHANTMENTS;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.comment("Enchantment calculation parameter settings").push(CATEGORY_PARAMETERS);
        COST_FACTOR = SERVER_BUILDER.comment("Cost of applying all enchantments is multiplied with this value. Default: 1.5")
                .defineInRange("costFactor", 1.5, 0, Double.MAX_VALUE);
        TREASURE_FACTOR = SERVER_BUILDER.comment("Cost of applying non-curse treasure enchantments is multiplied with this value. Default: 4")
                .defineInRange("treasureFactor", 4, 0, Double.MAX_VALUE);
        CURSE_FACTOR = SERVER_BUILDER.comment("Cost of applying curse enchantments is multiplied with this value. Default: 3")
                .defineInRange("curseFactor", 3, 0, Double.MAX_VALUE);
        BASE_COST = SERVER_BUILDER.comment("Base cost of enchantment process. Default: 45")
                .defineInRange("baseCost", 45, 0, Integer.MAX_VALUE);
        FLOATING_BOOK_BONUS = SERVER_BUILDER.comment("Bonus enchant power every decorating book can provide. Default: 1")
                .defineInRange("floatingBookBonus", 1, 0, Double.MAX_VALUE);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("Blacklist things, loved by server owners ;)").push(CATEGORY_BLACKLISTS);
        BLACKLISTED_ITEMS = SERVER_BUILDER.comment("List of blacklisted items. Format is modId:itemId, modId can be omitted for vanilla (you may need to understand something about \"flattening\"). Default: empty :]")
                .defineList("blacklistedItems", ImmutableList.of(), obj -> true);
        BLACKLISTED_ENCHANTMENTS = SERVER_BUILDER.comment("List of blacklisted items. Format is modId:enchantmentId, modId can be omitted for vanilla. Default: empty :]")
                .defineList("blacklistedEnchantments", ImmutableList.of(), obj -> true);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        BLACKLISTED_ITEMS.get().forEach(Blacklist::blacklistItem);
        BLACKLISTED_ENCHANTMENTS.get().forEach(Blacklist::blacklistEnchantment);
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
        Blacklist.clearItemBlacklist();
        Blacklist.clearEnchantmentBlacklist();
        BLACKLISTED_ITEMS.get().forEach(Blacklist::blacklistItem);
        BLACKLISTED_ENCHANTMENTS.get().forEach(Blacklist::blacklistEnchantment);
    }
}