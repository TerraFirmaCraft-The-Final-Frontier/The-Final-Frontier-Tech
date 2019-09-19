package tfctech.objects.items.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import tfctech.client.TechSounds;
import tfctech.objects.blocks.TechBlocks;
import tfctech.objects.blocks.devices.BlockLatexExtractor;
import tfctech.registry.TechTrees;

import static net.dries007.tfc.objects.blocks.wood.BlockLogTFC.PLACED;
import static net.minecraft.block.BlockHorizontal.FACING;

@MethodsReturnNonnullByDefault
public class ItemGroove extends ItemTechMetal
{
    public ItemGroove(Metal metal, ItemTechMetal.ItemType type)
    {
        super(metal, type);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getBlock() instanceof BlockLogTFC)
        {
            BlockLogTFC log = (BlockLogTFC) state.getBlock();
            if (log.getWood() == TechTrees.HEVEA && !state.getValue(PLACED))
            {
                //Check if we can place this treetap here
                for (EnumFacing face : EnumFacing.HORIZONTALS)
                {
                    if (!worldIn.isAirBlock(pos.offset(face)))
                    {
                        return EnumActionResult.PASS; //Can't place
                    }
                }


                //Find if no other treetap is placed in this tree
                BlockPos trunkPos = pos;
                while (worldIn.getBlockState(trunkPos.down()).getBlock() instanceof BlockLogTFC)
                {
                    trunkPos = trunkPos.down();
                }
                do
                {
                    for (EnumFacing face : EnumFacing.HORIZONTALS)
                    {
                        if (worldIn.getBlockState(trunkPos.offset(face)).getBlock() instanceof BlockLatexExtractor)
                        {
                            return EnumActionResult.PASS; //Found one, cancel
                        }
                    }
                    trunkPos = trunkPos.up();
                } while (worldIn.getBlockState(trunkPos).getBlock() instanceof BlockLogTFC);

                //Check if player has hammer in toolbar
                int hammerSlot = -1;
                for (int i = 0; i < 9; i++)
                {
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if (stack.getItem().getToolClasses(stack).contains("hammer"))
                    {
                        hammerSlot = i;
                        break;
                    }
                }

                //Place latex extractor
                if (hammerSlot > -1)
                {
                    player.inventory.getStackInSlot(hammerSlot).damageItem(1, player);
                    player.getHeldItem(hand).shrink(1);
                    if (!worldIn.isRemote)
                    {
                        worldIn.playSound(null, pos, TechSounds.RUBBER_GROOVE_FIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        worldIn.setBlockState(pos.offset(facing), TechBlocks.LATEX_EXTRACTOR.getDefaultState().withProperty(FACING, facing));
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }
}
