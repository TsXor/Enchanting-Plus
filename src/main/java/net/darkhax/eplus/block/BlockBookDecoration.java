package net.darkhax.eplus.block;

import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.darkhax.eplus.setup.ConfigurationHandler;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public abstract class BlockBookDecoration extends ContainerBlock implements IBookTexture {
    private static final VoxelShape BOUNDS = box(4.8d, 7.2d, 4.8d, 11.2d, 8.8d, 11.2d);

    public BlockBookDecoration() {
        super(
            Properties.of(Material.WOOD)
                .strength(1.5F)
                .lightLevel(state -> 10)
                .noOcclusion()
        );
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override @Nullable
    public TileEntity newBlockEntity(IBlockReader world) {
        return new TileEntityDecoration();
    }

    @Override @Nonnull
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    @Override @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.block();
    }

    @Override @Nonnull
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (world.getBlockEntity(pos) instanceof TileEntityDecoration tileEntityDecoration) {
            return BOUNDS.move(0, (double) tileEntityDecoration.height / 100d, 0);
        }
        return BOUNDS;
    }

    @Override @Nonnull
    public VoxelShape getVisualShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return this.getCollisionShape(state, world, pos, context);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) {
        return ConfigurationHandler.FLOATING_BOOK_BONUS.get().floatValue();
    }

    @Override @Nonnull
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        // TODO: implement a simple UI for controlling height and color
        ItemStack heldStack = player.getItemInHand(hand);
        if (!heldStack.isEmpty()) {
            if (world.getBlockEntity(pos) instanceof TileEntityDecoration deco) {
                Item heldItem = heldStack.getItem();
                boolean heightChanged = false;
                /*--*/ if (heldItem == Items.FEATHER) {
                    deco.increaseHeight();
                    heightChanged = true;
                } else if (heldItem == Items.IRON_INGOT) {
                    deco.decreaseHeight();
                    heightChanged = true;
                }
                if (heightChanged && !world.isClientSide) {
                    world.setBlockAndUpdate(pos, state);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override @Nonnull
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        // attach height and color to dropped stack
        // this is now disabled, maybe one day someone will enable it ;)
//        TileEntity tileEntity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
//        if (tileEntity instanceof TileEntityDecoration decoEntity) {
//            final CompoundNBT tag = new CompoundNBT();
//            tag.putInt("Height", decoEntity.height);
//            tag.putInt("Color", decoEntity.color);
//            drops.forEach(stack -> {
//                if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof BlockBookDecoration) {
//                    stack.addTagElement("BlockEntityTag", tag);
//                }
//            });
//        }
        return drops;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        final TileEntity tile = worldIn.getBlockEntity(pos);
        return tile instanceof TileEntityWithBook deco && deco.isOpen() ? 15 : 0;
    }
}