package tfctech.objects.blocks.devices;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.objects.blocks.BlockCharcoalPile;
import net.dries007.tfc.objects.blocks.property.ILightableBlock;
import net.dries007.tfc.objects.items.ItemFireStarter;
import net.dries007.tfc.util.Helpers;
import tfctech.client.TechGuiHandler;
import tfctech.objects.tileentities.TESmelteryFirebox;

@SuppressWarnings("WeakerAccess")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockSmelteryFirebox extends BlockHorizontal implements ILightableBlock, IItemSize
{
    public static boolean isValidPlacement(World world, BlockPos pos, EnumFacing facing)
    {
        BlockPos charcoalPos = pos.offset(facing.getOpposite());
        IBlockState charcoalPile = world.getBlockState(charcoalPos);
        if (charcoalPile.getBlock() instanceof BlockCharcoalPile && charcoalPile.getValue(BlockCharcoalPile.LAYERS) == 8)
        {
            int fireboxes = 0;
            for (EnumFacing face : EnumFacing.HORIZONTALS)
            {
                if (world.getBlockState(charcoalPos.offset(face)).getBlock() instanceof BlockSmelteryFirebox)
                {
                    // don't allow two fireboxes on the same structure
                    fireboxes++;
                }
            }
            return fireboxes == 1;
        }
        return false;
    }

    public BlockSmelteryFirebox()
    {
        super(Material.IRON);
        setHardness(3.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);
    }

    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack itemStack)
    {
        return Size.LARGE;
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack itemStack)
    {
        return Weight.MEDIUM;
    }

    @Override
    @SuppressWarnings("deprecation")
    @Nonnull
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState()
            .withProperty(FACING, EnumFacing.byHorizontalIndex(meta % 4))
            .withProperty(LIT, meta / 4 % 2 != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex()
            + (state.getValue(LIT) ? 4 : 0);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            IBlockState state = world.getBlockState(pos.offset(facing));
            if (state.getBlock() instanceof BlockCharcoalPile && state.getValue(BlockCharcoalPile.LAYERS) == 8)
            {
                for (EnumFacing face : EnumFacing.HORIZONTALS)
                {
                    if (world.getBlockState(pos.offset(facing).offset(face)).getBlock() instanceof BlockSmelteryFirebox)
                    {
                        // don't allow two fireboxes on the same structure
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!player.isSneaking() && isValidPlacement(world, pos, state.getValue(FACING)))
        {
            if (!world.isRemote)
            {
                TESmelteryFirebox firebox = Helpers.getTE(world, pos, TESmelteryFirebox.class);
                ItemStack held = player.getHeldItem(hand);
                if (ItemFireStarter.canIgnite(held) && firebox.onIgnite())
                {
                    ItemFireStarter.onIgnition(held);
                }
                else
                {
                    TechGuiHandler.openGui(world, pos, player, TechGuiHandler.Type.SMELTERY);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LIT, FACING);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TESmelteryFirebox();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        for (EnumFacing face : EnumFacing.HORIZONTALS)
        {
            IBlockState state = world.getBlockState(pos.offset(face));
            if (state.getBlock() instanceof BlockCharcoalPile && state.getValue(BlockCharcoalPile.LAYERS) == 8)
            {
                return getDefaultState().withProperty(FACING, face.getOpposite());
            }
        }
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }
}
