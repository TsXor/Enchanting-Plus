package net.darkhax.eplus.registry;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, EnchantingPlus.ID);

    public static final RegistryObject<ContainerType<ContainerAdvancedTable>> ADVANCED_ENCHANTING_TABLE =
            CONTAINER_TYPES.register("advanced_table", () -> IForgeContainerType.create(ContainerAdvancedTable::new));
}
