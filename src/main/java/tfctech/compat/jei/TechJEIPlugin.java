package tfctech.compat.jei;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.dries007.tfc.compat.jei.wrappers.SimpleRecipeWrapper;
import tfctech.compat.jei.categories.WireDrawingCategory;
import tfctech.objects.blocks.TechBlocks;
import tfctech.registry.TechRegistries;

import static tfctech.TFCTech.MODID;

@JEIPlugin
public class TechJEIPlugin implements IModPlugin
{
    private static final String WIRE_DRAWING_UID = MODID + ".wire_drawing";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        //Add new JEI recipe categories
        registry.addRecipeCategories(new WireDrawingCategory(registry.getJeiHelpers().getGuiHelper(), WIRE_DRAWING_UID));
    }

    @Override
    public void register(IModRegistry registry)
    {
        List<SimpleRecipeWrapper> wireList = TechRegistries.WIRE_DRAWING.getValuesCollection()
                .stream()
                .filter(x -> x.getIngredients().size() == 2) //Only shows recipes which have a wire drawing plate (so, it can be obtained)
                .map(SimpleRecipeWrapper::new)
                .collect(Collectors.toList());

        registry.addRecipes(wireList, WIRE_DRAWING_UID);
        registry.addRecipeCatalyst(new ItemStack(TechBlocks.WIRE_DRAW_BENCH), WIRE_DRAWING_UID);
    }
}
