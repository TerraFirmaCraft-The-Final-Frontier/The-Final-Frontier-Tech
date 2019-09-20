package tfctech.objects.blocks.devices;

import java.util.Random;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.util.Helpers;
import tfctech.objects.tileentities.TEFridge;
import tfctech.objects.tileentities.TEWireDrawBench;

import static net.minecraft.util.EnumFacing.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockFridge extends BlockHorizontal
{
    public static final PropertyBool UPPER = PropertyBool.create("upper"); //true if this is the upper half

    public BlockFridge()
    {
        super(Material.IRON);
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 0);
        setDefaultState(blockState.getBaseState().withProperty(FACING, NORTH).withProperty(UPPER, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta)).withProperty(UPPER, meta > 3);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(UPPER) ? 4 : 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!state.getValue(UPPER))
        {
            if (worldIn.getBlockState(pos.up()).getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.down()).getBlock() != this)
        {
            if (!worldIn.isRemote)
            {
                spawnAsEntity(worldIn, pos, new ItemStack(this));
            }
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(UPPER) ? Item.getItemFromBlock(this) : Items.AIR;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (state.getValue(UPPER))
        {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, UPPER);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return state.getValue(UPPER);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return state.getValue(UPPER) ? new TEFridge() : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        BlockPos TEPos = pos;
        if (!state.getValue(UPPER))
        {
            TEPos = pos.up();
        }
        TEFridge te = Helpers.getTE(world, TEPos, TEFridge.class);
        if (te != null && !te.isAnimating() && hand == EnumHand.MAIN_HAND && facing == state.getValue(FACING))
        {
            if(te.isOpen())
            {
                ItemStack stack = player.getHeldItem(hand);
                if (!stack.isEmpty())
                {
                    //todo store food
                }
                else
                {
                    return te.setOpening(false);
                }
            }
            else
            {
                if(!player.isSneaking())
                {
                    return te.setOpening(true);
                }
            }
        }
        return false;
    }
}
