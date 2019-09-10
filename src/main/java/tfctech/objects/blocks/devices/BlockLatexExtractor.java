package tfctech.objects.blocks.devices;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.util.Helpers;
import tfctech.objects.tileentities.TELatexExtractor;

import static net.minecraft.block.BlockHorizontal.FACING;
import static net.minecraft.util.EnumFacing.NORTH;

@SuppressWarnings("WeakerAccess")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockLatexExtractor extends Block
{

    public static final PropertyBool BASE = PropertyBool.create("base"); //from TE
    public static final PropertyBool POT = PropertyBool.create("pot"); //from TE
    //public static final PropertyBool TAP = PropertyBool.create("tap"); //always true. start by placing this one and only allow removal when user removes the rest
    public static final PropertyInteger CUT = PropertyInteger.create("cut", 0, 2); //from TE

    public BlockLatexExtractor()
    {
        super(Material.IRON);
        setDefaultState(blockState.getBaseState()
                .withProperty(FACING, NORTH)
                .withProperty(BASE, false)
                .withProperty(POT, false)
                .withProperty(CUT, 0));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TELatexExtractor te = Helpers.getTE(worldIn, pos, TELatexExtractor.class);
        if (te != null)
        {
            return state.withProperty(BASE, te.hasBase())
                    .withProperty(POT, te.hasPot())
                    .withProperty(CUT, te.cutState());
        }
        return super.getActualState(state, worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        //todo return pot + holder bounding box
        //if there's only the tap, return it's bounding box
        return super.getBoundingBox(state, source, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        //todo dump items in world
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TELatexExtractor te = Helpers.getTE(world, pos, TELatexExtractor.class);
        if (te != null)
        {
            ItemStack stack = player.getHeldItem(hand);
            //todo shift clicking must place/remove pot
            if (stack.getItem().getHarvestLevel(stack, "knife", player, state) != -1)
            {
                te.makeCut();
                return true;
            }
            else if (!te.hasPot() && te.isValidPot(stack))
            {
                te.addPot(stack.splitStack(1));
                return true;
            }
            else if (!te.hasBase() && te.isValidBase(stack))
            {
                te.addBase(stack.splitStack(1));
                return true;
            }
            else if (stack.isEmpty() && te.hasFluid())
            {
                //todo get fluid;
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, BASE, POT, CUT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
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
        return new TELatexExtractor();
    }
}
