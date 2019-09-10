package tfctech.objects.items.tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.metal.IMetalItem;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.objects.items.ItemTFC;
import net.dries007.tfc.objects.items.metal.ItemMetal;
import tfctech.objects.blocks.ModBlocks;
import tfctech.objects.blocks.devices.BlockLatexExtractor;

import static net.minecraft.block.BlockHorizontal.FACING;

@MethodsReturnNonnullByDefault
public class ItemTreeTapper extends ItemTFC implements IMetalItem
{
    public ItemTreeTapper()
    {
        super();
    }

    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack itemStack)
    {
        return Size.SMALL;
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack itemStack)
    {
        return Weight.LIGHT;
    }

    @Nullable
    @Override
    public Metal getMetal(ItemStack itemStack)
    {
        return Metal.WROUGHT_IRON;
    }

    @Override
    public int getSmeltAmount(ItemStack itemStack)
    {
        return 200;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.getBlockState(pos).getBlock() instanceof BlockLog)
        {
            //Find if no other treetap is placed in this log
            if (!worldIn.getBlockState(pos.offset(facing)).getMaterial().isReplaceable())
            {
                return EnumActionResult.PASS; //Can't place
            }
            for (EnumFacing face : EnumFacing.HORIZONTALS)
            {
                if (worldIn.getBlockState(pos.offset(face)).getBlock() instanceof BlockLatexExtractor)
                {
                    return EnumActionResult.PASS; //Found one, cancel
                }
            }
            int hammerSlot = -1;
            for (int i = 0; i < 9; i++)
            {
                if (player.inventory.getStackInSlot(i).getItem() instanceof ItemMetal)
                {
                    ItemMetal tool = (ItemMetal) player.inventory.getStackInSlot(i).getItem();
                    if (tool.getType() == Metal.ItemType.HAMMER)
                    {
                        hammerSlot = i;
                        break;
                    }
                }
            }
            if (hammerSlot > -1)
            {
                player.inventory.getStackInSlot(hammerSlot).damageItem(1, player);
                player.getHeldItem(hand).shrink(1);
                if (!worldIn.isRemote)
                {
                    worldIn.playSound(null, pos, TFCSounds.ANVIL_IMPACT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(pos.offset(facing), ModBlocks.LATEX_EXTRACTOR.getDefaultState().withProperty(FACING, facing));
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }
}
