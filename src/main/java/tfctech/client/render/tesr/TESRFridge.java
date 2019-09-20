package tfctech.client.render.tesr;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import tfctech.client.render.models.ModelFridge;
import tfctech.client.render.models.ModelWireDrawBench;
import tfctech.objects.tileentities.TEFridge;
import tfctech.objects.tileentities.TEWireDrawBench;

import static tfctech.TFCTech.MODID;

public class TESRFridge extends TileEntitySpecialRenderer<TEFridge>
{
    private static final ResourceLocation FRIDGE_TEXTURES = new ResourceLocation(MODID, "textures/models/fridge.png");
    private final ModelFridge model = new ModelFridge();

    @Override
    public void render(TEFridge te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.hasWorld())
        {
            GlStateManager.pushMatrix(); // start
            GlStateManager.translate(x + 0.5F, y + 0.5F, z + 0.5F); // position

            bindTexture(FRIDGE_TEXTURES); // texture

            GlStateManager.rotate(180, 1, 0, 0);
            switch (te.getRotation())
            {
                case NORTH:
                    GlStateManager.rotate(-180, 0, 1, 0);
                    break;
                case SOUTH:
                    break;
                case EAST:
                    GlStateManager.rotate(-90, 0, 1, 0);
                    break;
                case WEST:
                    GlStateManager.rotate(-270, 0, 1, 0);
                    break;
            }

            model.setOpen(te.getOpen());

            model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

            GlStateManager.popMatrix(); // end
        }
        else
        {
            //For itemstacks rendering

            GlStateManager.pushMatrix(); // start
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            GlStateManager.rotate(180, 1, 0, 0);
            GlStateManager.rotate(180, 0, 1, 0);

            bindTexture(FRIDGE_TEXTURES); // texture

            model.setOpen(0);

            model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

            GlStateManager.popMatrix(); // end
        }
    }

    @Override
    public boolean isGlobalRenderer(TEFridge te)
    {
        return true;
    }
}
