package tfctech.objects.items.glassworking;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import tfctech.client.TechGuiHandler;

public class ItemBlowpipe extends ItemGlassMolder
{
    public ItemBlowpipe()
    {
        super(ItemGlassMolder.BLOWPIPE_TANK);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && !player.isSneaking())
        {
            IItemHeat cap = stack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
            if (cap instanceof GlassMolderCapability && ((GlassMolderCapability) cap).canWork())
            {
                TechGuiHandler.openGui(world, player.getPosition(), player, TechGuiHandler.Type.GLASSWORKING);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
