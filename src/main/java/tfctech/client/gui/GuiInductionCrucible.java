package tfctech.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

import net.dries007.tfc.client.gui.GuiCrucible;
import tfctech.client.TechGuiHandler;
import tfctech.objects.tileentities.TEInductionCrucible;

public class GuiInductionCrucible extends GuiCrucible
{
    public GuiInductionCrucible(Container container, InventoryPlayer playerInv, TEInductionCrucible tile)
    {
        super(container, playerInv, tile);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY)
    {
        super.renderHoveredToolTip(mouseX, mouseY);
        if (mouseX >= guiLeft + 8 && mouseX <= guiLeft + 8 + 16 && mouseY >= guiTop + 79 && mouseY <= guiTop + 79 + 57)
        {
            int energy = tile.getField(1);
            drawHoveringText(String.format("%s RF/%s RF", energy, ((TEInductionCrucible) tile).getEnergyCapacity()), mouseX, mouseY);
        }
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        // Draw the energy bar
        mc.getTextureManager().bindTexture(TechGuiHandler.GUI_ELEMENTS);
        int energyPixels = 59 * tile.getField(1) / ((TEInductionCrucible) tile).getEnergyCapacity();
        int emptyPixels = 59 - energyPixels;
        drawTexturedModalRect(guiLeft + 7, guiTop + 78, 0, 0, 18, emptyPixels);
        drawTexturedModalRect(guiLeft + 7, guiTop + 78 + emptyPixels, 18, emptyPixels, 18, energyPixels);
    }
}