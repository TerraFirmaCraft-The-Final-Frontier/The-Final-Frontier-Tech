package tfctech.compat.waila;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import mcp.MethodsReturnNonnullByDefault;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.dries007.tfc.util.Helpers;
import tfctech.objects.blocks.devices.BlockFridge;
import tfctech.objects.blocks.devices.BlockLatexExtractor;
import tfctech.objects.blocks.devices.BlockWireDrawBench;
import tfctech.objects.tileentities.TEFridge;
import tfctech.objects.tileentities.TELatexExtractor;
import tfctech.objects.tileentities.TEWireDrawBench;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class WailaPlugin implements IWailaDataProvider
{
    public static void init()
    {
        FMLInterModComms.sendMessage("waila", "register", "tfctech.compat.waila.WailaPlugin.callbackRegister");
    }

    public static void callbackRegister(IWailaRegistrar registrar)
    {
        WailaPlugin dataProvider = new WailaPlugin();
        registrar.registerBodyProvider(dataProvider, BlockWireDrawBench.class);
        registrar.registerBodyProvider(dataProvider, BlockFridge.class);
        registrar.registerBodyProvider(dataProvider, TELatexExtractor.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        Block b = accessor.getBlock();
        if (b instanceof BlockWireDrawBench)
        {
            BlockPos TEPos = accessor.getPosition();
            if (!accessor.getBlockState().getValue(BlockWireDrawBench.UPPER))
            {
                TEPos = TEPos.offset(accessor.getBlockState().getValue(BlockWireDrawBench.FACING));
            }
            TEWireDrawBench bench = Helpers.getTE(accessor.getWorld(), TEPos, TEWireDrawBench.class);
            if (bench != null)
            {
                if (bench.getProgress() > 0)
                {
                    currenttip.add((new TextComponentTranslation("waila.tfctech.wiredraw.progress", bench.getProgress())).getFormattedText());
                }
            }
        }
        if (b instanceof BlockFridge)
        {
            BlockPos TEPos = accessor.getPosition();
            if (!accessor.getBlockState().getValue(BlockWireDrawBench.UPPER))
            {
                TEPos = TEPos.up();
            }
            TEFridge fridge = Helpers.getTE(accessor.getWorld(), TEPos, TEFridge.class);
            if (fridge != null)
            {
                currenttip.add((new TextComponentTranslation("waila.tfctech.fridge.efficiency", (int) fridge.getEfficiency())).getFormattedText());
            }
        }
        if (b instanceof BlockLatexExtractor)
        {
            BlockPos TEPos = accessor.getPosition();
            TELatexExtractor extractor = Helpers.getTE(accessor.getWorld(), TEPos, TELatexExtractor.class);
            if (extractor != null)
            {
                currenttip.add((new TextComponentTranslation("waila.tfctech.latex.quantity", extractor.getFluidAmount())).getFormattedText());
            }
        }
        return currenttip;
    }
}
