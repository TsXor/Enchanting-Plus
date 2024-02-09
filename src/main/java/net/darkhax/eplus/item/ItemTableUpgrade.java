package net.darkhax.eplus.item;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


/**
 * An item that can be used on an advanced enchanting table.
 * <br/>Currently, it has no effect... maybe.
 * <br/>TODO: give this some effects
 */
@ParametersAreNonnullByDefault
public class ItemTableUpgrade extends Item {
    public ItemTableUpgrade () {
        super(new Item.Properties().stacksTo(16).tab(EnchantingPlus.ITEM_GROUP));
    }

    @Override @Nonnull
    public ActionResultType useOn(ItemUseContext context) {
        final World world = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final Block block = world.getBlockState(pos).getBlock();

        if (block != ModBlocks.ADVANCED_ENCHANTING_TABLE.get()) return ActionResultType.PASS;

        final PlayerEntity player = context.getPlayer();
        final Hand hand = context.getHand();
        world.setBlock(pos, ModBlocks.ADVANCED_ENCHANTING_TABLE.get().defaultBlockState(), 1);
        world.setBlockEntity(pos, new TileEntityAdvancedTable());
        if (player != null && !player.isCreative()) {
            player.getItemInHand(hand).shrink(1);
        }

        return ActionResultType.SUCCESS;
    }
}