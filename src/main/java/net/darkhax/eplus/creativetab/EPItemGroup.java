package net.darkhax.eplus.creativetab;

import net.darkhax.eplus.registry.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class EPItemGroup extends ItemGroup {
    public EPItemGroup(String label) {
        super(label);
    }

    @OnlyIn(Dist.CLIENT)
    @Override @Nonnull
    public ItemStack makeIcon() {
        return new ItemStack(ModBlocks.ADVANCED_ENCHANTING_TABLE.get());
    }
}
