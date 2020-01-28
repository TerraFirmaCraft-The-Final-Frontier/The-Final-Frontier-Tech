package tfctech.api.recipes;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import tfctech.registry.TechRegistries;

public class SmelteryRecipe extends IForgeRegistryEntry.Impl<SmelteryRecipe>
{
    private IIngredient<ItemStack>[] ingredients;
    private ItemStack[] outputStack;
    private FluidStack outputFluid;
    private float solidifyTemp;

    private SmelteryRecipe()
    {
    }

    @Nullable
    public static SmelteryRecipe get(ItemStack... ingredients)
    {
        return TechRegistries.SMELTERY.getValuesCollection().stream().filter(x -> x.isValidInput(ingredients)).findFirst().orElse(null);
    }

    /**
     * For JEI only
     */
    public IIngredient<ItemStack>[] getIngredients()
    {
        return ingredients;
    }

    public boolean isFluid()
    {
        return outputFluid != null;
    }

    public ItemStack[] getOutputStack(@Nullable List<ItemStack> inputs)
    {
        int recipeCount = this.getRecipeCount(inputs);
        ItemStack[] output = new ItemStack[this.outputStack.length];
        for (int i = 0; i < output.length; i++)
        {
            output[i] = this.outputStack[i].copy();
            output[i].setCount(output[i].getCount() * recipeCount);
        }
        return output;
    }

    public FluidStack getOutputFluid(@Nullable List<ItemStack> inputs)
    {
        FluidStack output = this.outputFluid.copy();
        output.amount *= this.getRecipeCount(inputs);
        return output;
    }

    private int getRecipeCount(@Nullable List<ItemStack> inputs)
    {
        if (inputs == null) return 1;
        int recipeCount = Integer.MAX_VALUE;
        for (IIngredient<ItemStack> ingredient : ingredients)
        {
            for (ItemStack input : inputs)
            {
                if (ingredient.test(input))
                {
                    recipeCount = Math.min(recipeCount, input.getCount() / ingredient.getAmount());
                    break;
                }
            }
        }
        return recipeCount;
    }

    public float getSolidifyTemp()
    {
        return this.solidifyTemp;
    }

    private boolean isValidInput(ItemStack... ingredients)
    {
        for (IIngredient<ItemStack> ingredient : this.ingredients)
        {
            boolean pass = false;
            for (ItemStack stack : ingredients)
            {
                if (ingredient.test(stack))
                {
                    pass = true;
                    break;
                }
            }
            if (!pass)
            {
                return false;
            }
        }
        return true;
    }

    public static class Builder<T extends Builder>
    {
        public static ItemBuilder newItemBuilder(ResourceLocation registryName)
        {
            return new ItemBuilder(registryName);
        }

        public static FluidBuilder newFluidBuilder(ResourceLocation registryName)
        {
            return new FluidBuilder(registryName);
        }

        SmelteryRecipe recipe;
        List<IIngredient<ItemStack>> listInput;

        private Builder(ResourceLocation registryName)
        {
            recipe = new SmelteryRecipe();
            recipe.setRegistryName(registryName);
            listInput = new ArrayList<>();
        }

        public T addInput(@Nonnull IIngredient<ItemStack> ingredient)
        {
            if (this.listInput.size() < 4)
            {
                this.listInput.add(ingredient);
            }
            else
            {
                throw new IllegalStateException("Smeltery recipes must have at most 4 ingredients!");
            }
            //noinspection unchecked
            return (T) this;
        }

        public SmelteryRecipe build()
        {
            if (this.listInput.isEmpty())
            {
                throw new IllegalStateException("Smeltery recipes must have at least 1 ingredient!");
            }
            else
            {
                //noinspection unchecked
                this.recipe.ingredients = listInput.toArray(new IIngredient[0]);
                return this.recipe;
            }
        }

        public static class FluidBuilder extends Builder<FluidBuilder>
        {
            private FluidBuilder(ResourceLocation registryName)
            {
                super(registryName);
            }

            public FluidBuilder setOutput(@Nonnull FluidStack fluidStack, float solidifyTemp)
            {
                this.recipe.outputFluid = fluidStack;
                this.recipe.solidifyTemp = solidifyTemp;
                this.recipe.outputStack = null;
                return this;
            }
        }

        public static class ItemBuilder extends Builder<ItemBuilder>
        {
            private ItemBuilder(ResourceLocation registryName)
            {
                super(registryName);
            }

            public ItemBuilder setOutput(ItemStack... stacks) throws IllegalArgumentException
            {
                if (stacks.length <= 0 || stacks.length > 4)
                    throw new IllegalArgumentException("Output must be [1-4] ItemStacks");
                this.recipe.outputStack = stacks;
                this.recipe.solidifyTemp = 0;
                this.recipe.outputFluid = null;
                return this;
            }
        }
    }
}
