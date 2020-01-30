package tfctech.compat.jei.categories;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.dries007.tfc.api.util.TFCConstants;
import net.dries007.tfc.compat.jei.BaseRecipeCategory;
import tfctech.compat.jei.wrappers.SmelteryRecipeWrapper;

@ParametersAreNonnullByDefault
public class SmelteryCategory extends BaseRecipeCategory<SmelteryRecipeWrapper>
{
    private static final ResourceLocation ICONS = new ResourceLocation(TFCConstants.MOD_ID, "textures/gui/jei/icons.png");
    private static final ResourceLocation BARREL_TEXTURES = new ResourceLocation(TFCConstants.MOD_ID, "textures/gui/barrel.png");

    private final IDrawableStatic fluidSlotBackgroound, fluidSlot;
    private final IDrawableStatic slot;
    private final IDrawableStatic fire;
    private final IDrawableAnimated fireAnimated;

    public SmelteryCategory(IGuiHelper helper, String Uid)
    {
        super(helper.createBlankDrawable(106, 62), Uid);
        fluidSlotBackgroound = helper.createDrawable(BARREL_TEXTURES, 7, 15, 18, 60);
        fluidSlot = helper.createDrawable(BARREL_TEXTURES, 176, 0, 18, 53);
        fire = helper.createDrawable(ICONS, 0, 0, 14, 14);
        IDrawableStatic arrowAnimated = helper.createDrawable(ICONS, 14, 0, 14, 14);
        this.fireAnimated = helper.createAnimatedDrawable(arrowAnimated, 160, IDrawableAnimated.StartDirection.TOP, true);
        this.slot = helper.getSlotDrawable();
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
        //Input
        slot.draw(minecraft, 5, 17);
        slot.draw(minecraft, 23, 17);
        slot.draw(minecraft, 5, 35);
        slot.draw(minecraft, 23, 35);

        fire.draw(minecraft, 55, 30);
        fireAnimated.draw(minecraft, 55, 30);

        //Output
        fluidSlotBackgroound.draw(minecraft, 80, 1);
        fluidSlot.draw(minecraft, 80, 5);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SmelteryRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        itemStackGroup.init(0, true, 5, 17);
        itemStackGroup.init(1, true, 23, 17);
        itemStackGroup.init(2, true, 5, 35);
        itemStackGroup.init(3, true, 23, 35);

        for (int i = 0; i < ingredients.getInputs(VanillaTypes.ITEM).size(); i++)
        {
            itemStackGroup.set(i, ingredients.getInputs(VanillaTypes.ITEM).get(i));
        }

        IGuiFluidStackGroup fluidStackGroup = recipeLayout.getFluidStacks();
        fluidStackGroup.init(0, false, 85, 6, 8, 50, ingredients.getOutputs(VanillaTypes.FLUID).get(0).get(0).amount, true, null);
        fluidStackGroup.set(0, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
    }
}
