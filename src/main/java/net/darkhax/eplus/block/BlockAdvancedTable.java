package net.darkhax.eplus.block;

import net.darkhax.eplus.api.BookTexture;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.darkhax.eplus.registry.ModBookTextures;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.UUID;


@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockAdvancedTable extends HorizontalBlock implements IBookTexture {
    private static final VoxelShape BOUNDS = box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public BlockAdvancedTable() {
        super(
            Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE)
                .isViewBlocking((state, world, pos) -> true)
                .strength(5.0F, 2000.0F)
                .lightLevel(state -> 15) // as bright as lava
        );
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            final TileEntity tileentity = level.getBlockEntity(pos);
            if (tileentity instanceof TileEntityAdvancedTable advancedTableEntity) {
                final Map<UUID, ItemStackHandlerEnchant> inventories = advancedTableEntity.getInventories();
                for (final ItemStackHandlerEnchant inventory : inventories.values()) {
                    InventoryHelper.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getEnchantingStack());
                    inventory.setStackInSlot(ItemStackHandlerEnchant.ENCHANT_SLOT_IDX, ItemStack.EMPTY);
                }
                inventories.clear();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityAdvancedTable();
    }

    @Override @Nonnull
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    @Override @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDS;
    }

    @Override @Nonnull
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            final TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TileEntityAdvancedTable tileEntityAET) {
                NetworkHooks.openGui((ServerPlayerEntity) player, tileEntityAET, pos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public BookTexture getBookTexture() {
        return ModBookTextures.DEFAULT.get();
    }
}