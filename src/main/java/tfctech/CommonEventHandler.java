package tfctech;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.dries007.tfc.Constants;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.ItemHeatHandler;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import net.dries007.tfc.objects.items.rock.ItemRock;
import tfctech.objects.items.TechItems;

import static tfctech.TFCTech.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public final class CommonEventHandler
{
    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();
        if (!stack.isEmpty())
        {
            // Attach missing heat capability to rocks
            if (item instanceof ItemRock)
            {
                ICapabilityProvider heatCap = new ItemHeatHandler(stack.getTagCompound(), 0.2f, 2000f);
                event.addCapability(CapabilityItemHeat.KEY, heatCap);
            }
        }
    }

    @SubscribeEvent
    public static void onHarvestEvent(BlockEvent.HarvestDropsEvent event)
    {
        if (event.getHarvester() != null && !event.getWorld().isRemote)
        {
            final ItemStack heldItem = event.getHarvester().getHeldItemMainhand();
            if (event.getState().getBlock() instanceof BlockLogTFC && heldItem.getItem().getToolClasses(heldItem).contains("saw") && Constants.RNG.nextFloat() < 0.5F)
            {
                if (Constants.RNG.nextFloat() < 0.5F)
                {
                    event.getDrops().clear(); // Also, consume log
                }
                event.getDrops().add(new ItemStack(TechItems.WOOD_POWDER, 1));

            }
        }
    }
}
