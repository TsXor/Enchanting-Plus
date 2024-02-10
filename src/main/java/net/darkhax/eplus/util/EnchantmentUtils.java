package net.darkhax.eplus.util;

import net.darkhax.bookshelf.util.ModUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class EnchantmentUtils {
    // The following code about experience was adapted from OpenModsLib https://github.com/OpenMods/OpenModsLib
    public static int getExperience(PlayerEntity player) {
        return (int) (getExperienceForLevels(player.experienceLevel) + player.experienceProgress * player.getXpNeededForNextLevel());
    }

    public static void removeExperience(PlayerEntity player, int amount) {
        addExperience(player, -amount);
    }

    public static void addExperience(PlayerEntity player, int amount) {
        player.giveExperiencePoints(amount);
    }

    public static int getExperienceForLevels(int level) {
        if (level < 1) { return 0; }
        if (level < 17) { return level * level + 6 * level; }
        if (level < 32) { return (5 * level * level - 81 * level) / 2 + 360; }
        return (18 * level * level - 650 * level) / 4 + 2220;
    }

    public record EnchantAreaPos(int xOff, int zOff) {}
    public record EnchantAreaPart(EnchantAreaPos blockingPos, EnchantAreaPos[] poweringPos) {
        // calculate enchant power of this part, if blockingPos is not air, return 0
        public float getEnchantPowerProvided(World world, BlockPos pos) {
            for (int yOff = 0; yOff < ENCHANT_POWER_AREA_HEIGHT; yOff++) {
                BlockPos checkAirPos = pos.offset(blockingPos.xOff, yOff, blockingPos.zOff());
                if (!world.isEmptyBlock(checkAirPos)) return 0;
            }

            float power = 0;
            for (EnchantAreaPos powerPos : poweringPos) {
                for (int yOff = 0; yOff < ENCHANT_POWER_AREA_HEIGHT; yOff++) {
                    BlockPos bPos = pos.offset(powerPos.xOff, yOff, powerPos.zOff);
                    power += EnchantmentUtils.getEnchantPowerProvided(world, bPos);
                }
            }
            return power;
        }
    }

    public static final int ENCHANT_POWER_AREA_HEIGHT = 2;
    public static final EnchantAreaPart[] ENCHANT_POWER_AREA = {
        new EnchantAreaPart(
            new EnchantAreaPos(1, 1), new EnchantAreaPos[] {
                new EnchantAreaPos(1, 2),
                new EnchantAreaPos(2, 2),
                new EnchantAreaPos(2, 1),
            }
        ),
        new EnchantAreaPart(
            new EnchantAreaPos(1, 0), new EnchantAreaPos[] {
                new EnchantAreaPos(2, 0),
            }
        ),
        new EnchantAreaPart(
            new EnchantAreaPos(1, -1), new EnchantAreaPos[] {
                new EnchantAreaPos(2, -1),
                new EnchantAreaPos(2, -2),
                new EnchantAreaPos(1, -2),
            }
        ),
        new EnchantAreaPart(
            new EnchantAreaPos(0, -1), new EnchantAreaPos[] {
                new EnchantAreaPos(0, -2),
            }
        ),
        new EnchantAreaPart(
            new EnchantAreaPos(-1, -1), new EnchantAreaPos[] {
                new EnchantAreaPos(-1, -2),
                new EnchantAreaPos(-2, -2),
                new EnchantAreaPos(-2, -1),
            }
        ),
        new EnchantAreaPart(
            new EnchantAreaPos(-1, 0), new EnchantAreaPos[] {
                new EnchantAreaPos(-2, 0),
            }
        ),
        new EnchantAreaPart(
            new EnchantAreaPos(-1, 1), new EnchantAreaPos[] {
                new EnchantAreaPos(-2, 1),
                new EnchantAreaPos(-2, 2),
                new EnchantAreaPos(-1, 2),
            }
        ),
        new EnchantAreaPart(
            new EnchantAreaPos(0, 1), new EnchantAreaPos[] {
                new EnchantAreaPos(0, 2),
            }
        ),
    };

    public static float getEnchantPowerReceived(World world, BlockPos pos) {
        return Arrays.stream(ENCHANT_POWER_AREA)
                .map(ePart -> ePart.getEnchantPowerProvided(world, pos))
                .reduce(0f, Float::sum);
    }

    public static float getEnchantPowerProvided(World world, BlockPos pos) {
        // if you want to make your custom block provide enchant power, override `getEnchantPowerBonus` of Block class
        return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
    }

    public static String getTranslationKey(@Nullable Enchantment enchant) {
        if (enchant == null) return "NULL";
        ResourceLocation enchantRegistry = enchant.getRegistryName();
        if (enchantRegistry == null) return "NULL";
        return String.format("enchantment.%s.%s.desc", enchantRegistry.getNamespace(), enchantRegistry.getPath());
    }

    public static String getDescription(@Nullable Enchantment enchantment) {
        final String key = getTranslationKey(enchantment);
        String description = I18n.get(key);
        if (description.startsWith("enchantment.")) {
            String modName = ModUtils.getModName(Objects.requireNonNull(ModUtils.getOwner(enchantment))).getString();
            description = I18n.get("tooltip.eplus.missing", modName, key);
        }
        return description;
    }

    public static boolean isLevelUpgradable(int level, Enchantment enchantment) {
        return level < enchantment.getMaxLevel();
    }

    public static void applyEnchantment(ItemStack stack, EnchantmentData info)  {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        enchantments.put(info.enchantment, info.level);
        List<Enchantment> zeroLeveled = enchantments.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .toList();
        zeroLeveled.forEach(enchantments::remove);
        EnchantmentHelper.setEnchantments(enchantments, stack);
    }

    public static void upgradeEnchantment(ItemStack stack, EnchantmentData info, int nLevels)  {
        applyEnchantment(stack, new EnchantmentData(info.enchantment, info.level + nLevels));
    }
}
