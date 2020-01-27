package tfctech.objects.blocks.devices;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.dries007.tfc.objects.blocks.property.ILightableBlock;
import tfctech.objects.tileentities.TESmeltery;

@ParametersAreNonnullByDefault
public class BlockSmeltery extends BlockHorizontal implements ILightableBlock
{
    // 1 - 4 = number of clay placed, 4 = formed, 5 = fired
    public static final PropertyInteger FORM = PropertyInteger.create("form", 1, 5);

    protected BlockSmeltery()
    {
        super(Material.IRON);
        setDefaultState(blockState.getBaseState().withProperty(LIT, false).withProperty(FORM, 5).withProperty(FACING,  EnumFacing.NORTH));
        setHardness(3.0F);
        setSoundType(SoundType.STONE);
        setLightLevel(1.0F);
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TESmeltery();
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LIT, FORM, FACING);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(LIT) ? super.getLightValue(state, world, pos) : 0;
    }
}
