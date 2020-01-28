package tfctech.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import net.dries007.tfc.client.gui.GuiContainerTE;
import tfctech.objects.tileentities.TESmeltery;

import static tfctech.TFCTech.MODID;

public class GuiSmeltery extends GuiContainerTE<TESmeltery>
{
    private static final ResourceLocation SMELTERY_BACKGROUND = new ResourceLocation(MODID, "textures/gui/smeltery.png");

    public GuiSmeltery(Container container, InventoryPlayer playerInv, TESmeltery tile)
    {
        super(container, playerInv, tile, SMELTERY_BACKGROUND);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }
}
