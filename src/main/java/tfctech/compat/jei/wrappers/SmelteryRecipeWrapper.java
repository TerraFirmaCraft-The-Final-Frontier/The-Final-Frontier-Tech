package tfctech.compat.jei.wrappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import tfctech.api.recipes.SmelteryRecipe;

@ParametersAreNonnullByDefault
public class SmelteryRecipeWrapper implements IRecipeWrapper
{
    private SmelteryRecipe recipe;

    public SmelteryRecipeWrapper(SmelteryRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        List<List<ItemStack>> allInputs = new ArrayList<>();
        for (IIngredient<ItemStack> ingredient : recipe.getIngredients())
        {
            allInputs.add(ingredient.getValidIngredients());
        }
        ingredients.setInputLists(VanillaTypes.ITEM, allInputs);

        List<List<ItemStack>> allOutputs = new ArrayList<>();
        List<List<FluidStack>> allOutputFluids = new ArrayList<>();

        if (recipe.isFluid())
        {
            allOutputFluids.add(NonNullList.withSize(1, recipe.getOutputFluid(null)));
        }
        else
        {
            for (ItemStack stack : recipe.getOutputStack(null))
            {
                allOutputs.add(NonNullList.withSize(1, stack));
            }
        }
        ingredients.setOutputLists(VanillaTypes.ITEM, allOutputs);
        ingredients.setOutputLists(VanillaTypes.FLUID, allOutputFluids);
    }
}
