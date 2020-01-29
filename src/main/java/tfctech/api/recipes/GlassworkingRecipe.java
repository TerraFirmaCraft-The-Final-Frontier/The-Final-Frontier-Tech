package tfctech.api.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import net.dries007.tfc.util.SimpleCraftMatrix;
import tfctech.registry.TechRegistries;

public class GlassworkingRecipe extends IForgeRegistryEntry.Impl<GlassworkingRecipe>
{
    public static GlassworkingRecipe get(SimpleCraftMatrix input)
    {
        return TechRegistries.GLASSWORKING.getValuesCollection().stream().filter(x -> x.matches(input)).findFirst().orElse(null);
    }

    private final SimpleCraftMatrix matrix;
    private final ItemStack output;

    public GlassworkingRecipe(ResourceLocation name, ItemStack output, String... pattern)
    {
        this.matrix = new SimpleCraftMatrix(false, pattern);
        this.output = output;
        setRegistryName(name);
    }

    public SimpleCraftMatrix getMatrix()
    {
        return matrix;
    }

    public ItemStack getOutput()
    {
        return output.copy();
    }

    private boolean matches(SimpleCraftMatrix other)
    {
        return other.matches(this.matrix);
    }
}
