package tfctech.client.gui;

import java.util.Map;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.client.gui.GuiContainerTE;
import net.dries007.tfc.objects.te.TECrucible;
import net.dries007.tfc.util.Alloy;
import tfctech.client.TechGuiHandler;
import tfctech.objects.tileentities.TEInductionCrucible;

import static net.dries007.tfc.api.capability.heat.CapabilityItemHeat.MAX_TEMPERATURE;
import static net.dries007.tfc.api.util.TFCConstants.MOD_ID;
import static tfctech.TFCTech.MODID;

public class GuiInductionCrucible extends GuiContainerTE<TEInductionCrucible>
{
    private static final ResourceLocation CRUCIBLE_BACKGROUND = new ResourceLocation(MODID, "textures/gui/induction_crucible.png");

    public GuiInductionCrucible(Container container, InventoryPlayer playerInv, TEInductionCrucible tile)
    {
        super(container, playerInv, tile, CRUCIBLE_BACKGROUND);

        this.ySize = 192;
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY)
    {
        if (mouseX > guiLeft + 128 && mouseX < guiLeft + 137 && mouseY > guiTop + 5 && mouseY < guiTop + 107)
        {
            int amount = tile.getAlloy().getAmount();
            drawHoveringText(I18n.format(MOD_ID + ".tooltip.units", amount), mouseX, mouseY);
        }
        if (mouseX >= guiLeft + 153 && mouseX <= guiLeft + 153 + 18 && mouseY >= guiTop + 27 && mouseY <= guiTop + 27 + 59)
        {
            int energy = tile.getField(1);
            drawHoveringText(String.format("%s RF/%s RF", energy, tile.getEnergyCapacity()), mouseX, mouseY);
        }
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        //137, 27 start of energy bar, 160, 31 start of heat bar
        //Draw the temperature bar
        mc.getTextureManager().bindTexture(TechGuiHandler.GUI_ELEMENTS);
        drawTexturedModalRect(this.guiLeft + 140, this.guiTop + 31, 39, 1, 9, 52);

        // Draw the temperature indicator
        int targetTemperature = tile.getField(0);
        int temperaturePixels = (int) (51 * Math.min(MAX_TEMPERATURE, targetTemperature) / MAX_TEMPERATURE); //Max temperature is brilliant white in tfc
        drawTexturedModalRect(guiLeft + 137, guiTop + 80 - temperaturePixels, 36, 54, 15, 5);

        // Draw the energy bar
        int energyPixels = 59 * tile.getField(1) / tile.getEnergyCapacity();
        int emptyPixels = 59 - energyPixels;
        drawTexturedModalRect(guiLeft + 153, guiTop + 27, 0, 0, 18, emptyPixels);
        drawTexturedModalRect(guiLeft + 153, guiTop + 27 + emptyPixels, 18, emptyPixels, 18, energyPixels);


        // Draw the filled amount
        Alloy alloy = tile.getAlloy();
        if (alloy.getAmount() > 0)
        {
            int fill = (int) (99f * alloy.getAmount() / TECrucible.CRUCIBLE_MAX_METAL_FLUID);
            drawTexturedModalRect(guiLeft + 129, guiTop + 106 - fill, 191, 0, 8, fill);

            // Draw Title:
            Metal result = tile.getAlloyResult();
            String resultText = TextFormatting.UNDERLINE + I18n.format(result.getTranslationKey()) + ":";
            fontRenderer.drawString(resultText, guiLeft + 7, guiTop + 7, 0x000000);

            // Draw Components
            int yPos = guiTop + 18;
            for (Map.Entry<Metal, Double> entry : alloy.getMetals().entrySet())
            {
                String metalName = I18n.format(entry.getKey().getTranslationKey());
                String displayText = String.format("%s: %s%2.2f%%", metalName, TextFormatting.DARK_GREEN, 100 * entry.getValue() / alloy.getAmount());
                fontRenderer.drawString(displayText, guiLeft + 7, yPos, 0x404040);
                yPos += 9;
            }
        }
    }
}