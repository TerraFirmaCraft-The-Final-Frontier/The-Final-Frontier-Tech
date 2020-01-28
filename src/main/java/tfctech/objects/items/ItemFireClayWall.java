package tfctech.objects.items;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.util.Helpers;
import tfctech.objects.blocks.TechBlocks;
import tfctech.objects.tileentities.TESmeltery;

@SuppressWarnings("WeakerAccess")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemFireClayWall extends ItemMiscTech
{
    public ItemFireClayWall()
    {
        super(Size.NORMAL, Weight.LIGHT);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TESmeltery smeltery = Helpers.getTE(world, pos, TESmeltery.class);
        if (smeltery != null)
        {
            // todo upgrade form
        }
        else if (facing == EnumFacing.UP && world.getBlockState(pos.up()).getMaterial().isReplaceable())
        {
            ItemStack stack = player.getHeldItem(hand);
            stack.shrink(1);
            world.setBlockState(pos.up(), TechBlocks.SMELTERY.getDefaultState().withProperty(BlockHorizontal.FACING, player.getHorizontalFacing().getOpposite()));
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
}
