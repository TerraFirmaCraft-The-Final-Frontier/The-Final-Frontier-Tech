package tfctech.client.render.teisr;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

import mcp.MethodsReturnNonnullByDefault;
import tfctech.objects.tileentities.TEWireDrawBench;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEISRWireDrawBench extends TileEntityItemStackRenderer
{
    private final TEWireDrawBench te = new TEWireDrawBench();

    @Override
    public void renderByItem(ItemStack itemStackIn, float partialTicks)
    {
        TileEntityRendererDispatcher.instance.render(te, 0.0D, 0.0D, 0.0D, 0.0F);
    }
}
