package tfctech.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.Vec3i;

import tfctech.objects.tileentities.TELatexExtractor;

import static net.minecraft.block.BlockHorizontal.FACING;
import static tfctech.objects.blocks.devices.BlockLatexExtractor.*;

public class TESRLatexExtractor extends TileEntitySpecialRenderer<TELatexExtractor>
{
    //todo draw flowing latex fluid here

    @Override
    public void render(TELatexExtractor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.hasWorld())
        {
            IBlockState state = te.getBlockState();

            //Update state values according to TE

            //noinspection ConstantConditions
            state = state.withProperty(BASE, te.hasBase())
                    .withProperty(POT, te.hasPot())
                    .withProperty(CUT, te.cutState());

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

            Vec3i vec = state.getValue(FACING).getOpposite().getDirectionVec();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x + vec.getX(), y, z + vec.getZ());
            GlStateManager.rotate(-90, 0, 1, 0);

            //Render the static model
            //This is done here so i can offset the model (bypass the maximum coordinates of a blockstate)
            IBakedModel ibakedmodel = renderer.getModelForState(state);
            renderer.getBlockModelRenderer().renderModelBrightness(ibakedmodel, state, 1.0F, true);

            GlStateManager.popMatrix();
        }
    }
}
