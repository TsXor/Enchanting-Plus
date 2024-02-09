package net.darkhax.eplus.util;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.Objects;

public class NBTUtils {
    public static boolean containsAllTags(@Nullable CompoundNBT superset, @Nullable CompoundNBT subset) {
        if (subset == null) return true;
        if (superset == null) return false;
        for (final String key : subset.getAllKeys()) {
            if (!superset.contains(key) || !Objects.equals(superset.get(key), subset.get(key))) {
                return false;
            }
        }
        return true;
    }
}
