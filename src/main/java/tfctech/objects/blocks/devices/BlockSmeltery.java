package tfctech.objects.blocks.devices;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import net.dries007.tfc.objects.blocks.property.ILightableBlock;
import net.dries007.tfc.objects.items.ItemFireStarter;
import net.dries007.tfc.util.Helpers;
import tfctech.client.TechGuiHandler;
import tfctech.objects.tileentities.TESmeltery;

@ParametersAreNonnullByDefault
public class BlockSmeltery extends BlockHorizontal implements ILightableBlock
{
    // 1 - 4 = number of clay placed, 4 = formed, 5 = fired
    public static final PropertyInteger FORM = PropertyInteger.create("form", 1, 5);

    public BlockSmeltery()
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

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!player.isSneaking())
        {
            TESmeltery smeltery = Helpers.getTE(world, pos, TESmeltery.class);
            if (!state.getValue(LIT))
            {
                if (state.getValue(FORM) >= 4)
                {
                    if (!world.isRemote)
                    {
                        ItemStack held = player.getHeldItem(hand);
                        if (ItemFireStarter.onIgnition(held))
                        {
                            smeltery.onIgnite();
                        }
                        else if (held.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
                        {
                            IFluidHandler fluidHandler = smeltery.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
                            if (fluidHandler != null)
                            {
                                FluidUtil.interactWithFluidHandler(player, hand, fluidHandler);
                            }
                        }
                        else
                        {
                            TechGuiHandler.openGui(world, pos, player, TechGuiHandler.Type.SMELTERY);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
