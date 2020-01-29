package tfctech.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import net.dries007.tfc.api.capability.heat.Heat;
import net.dries007.tfc.client.FluidSpriteCache;
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
        if (tile.hasFluid())
        {
            int posX = guiLeft + 52;
            int posY = guiTop + 20;
            drawTexturedModalRect(posX, posY, 176, 0, 78, 18);

            // Fluid
            FluidStack fluid = tile.getFluid();
            int fillPixels = (int) Math.min(Math.ceil((fluid.amount / (float) TESmeltery.FLUID_CAPACITY) * 18), 18);
            TextureAtlasSprite sprite = FluidSpriteCache.getStillSprite(fluid.getFluid());
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();

            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            int color = fluid.getFluid().getColor();

            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;
            float a = ((color >> 24) & 0xFF) / 255f;

            GlStateManager.color(r, g, b, a);

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            int startX = posX + 1;
            int startY = posY + 19 - fillPixels;
            int endX = posX + 71;
            int endY = posY + 17;

            buffer.pos(startX, startY, 0).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            buffer.pos(startX, endY, 0).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            buffer.pos(endX, endY, 0).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(endX, startY, 0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();

            Tessellator.getInstance().draw();

            Minecraft.getMinecraft().renderEngine.bindTexture(SMELTERY_BACKGROUND);
            GlStateManager.color(1, 1, 1, 1);

            // Overlay
            drawTexturedModalRect(posX, posY, 176, 18, 78, 18);


            // Molten / Solidified and temperature
            float x = 87F;
            float y = 9F;
            String text = Heat.getTooltip(tile.getTemp());
            x = x - fontRenderer.getStringWidth(text) / 2.0f;
            fontRenderer.drawString(text, guiLeft + x, guiTop + y, 0xFFFFFF, false);
        }
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY)
    {
        super.renderHoveredToolTip(mouseX, mouseY);
        int relX = mouseX - guiLeft;
        int relY = mouseY - guiTop;
        if (relX >= 52 && relY >= 20 && relX < 130 && relY < 38 && tile.hasFluid())
        {
            FluidStack fluid = tile.getFluid();
            List<String> tooltip = new ArrayList<>();
            tooltip.add(fluid.getLocalizedName());
            tooltip.add(fluid.amount + " / " + TESmeltery.FLUID_CAPACITY);
            if (tile.isSolidified())
            {
                tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.tfctech.smeltery.solid"));
            }
            else
            {
                tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.tfctech.smeltery.molten"));
            }
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
        }
    }
}
