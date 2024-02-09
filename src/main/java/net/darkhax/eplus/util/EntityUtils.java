package net.darkhax.eplus.util;

import net.minecraft.inventory.EquipmentSlotType;

public class EntityUtils {
    public static final EquipmentSlotType[] EQUIPMENT_SLOTS = new EquipmentSlotType[] {
            EquipmentSlotType.HEAD,
            EquipmentSlotType.CHEST,
            EquipmentSlotType.LEGS,
            EquipmentSlotType.FEET
    };

    public static EquipmentSlotType getEquipmentSlot(int index) {
        if (index >= 0 && index < EQUIPMENT_SLOTS.length) {
            return EQUIPMENT_SLOTS[index];
        }
        return null;
    }
}
