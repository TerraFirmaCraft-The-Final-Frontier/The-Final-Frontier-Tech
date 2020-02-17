package tfctech.compat.jei;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.compat.jei.wrappers.SimpleRecipeWrapper;
import tfctech.client.gui.GuiGlassworking;
import tfctech.client.gui.GuiSmelteryCauldron;
import tfctech.compat.jei.categories.GlassworkingCategory;
import tfctech.compat.jei.categories.SmelteryCategory;
import tfctech.compat.jei.categories.WireDrawingCategory;
import tfctech.compat.jei.wrappers.GlassworkingRecipeWrapper;
import tfctech.compat.jei.wrappers.SmelteryRecipeWrapper;
import tfctech.objects.blocks.TechBlocks;
import tfctech.objects.items.glassworking.ItemBlowpipe;
import tfctech.registry.TechRegistries;

import static tfctech.TFCTech.MODID;

@JEIPlugin
public class TechJEIPlugin implements IModPlugin
{
    private static final String WIRE_DRAWING_UID = MODID + ".wire_drawing";
    private static final String SMELTERY_UID = MODID + ".smeltery";
    private static final String GLASSWORKING_UID = MODID + ".glassworking";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        //Add new JEI recipe categories
        registry.addRecipeCategories(new WireDrawingCategory(registry.getJeiHelpers().getGuiHelper(), WIRE_DRAWING_UID));
        registry.addRecipeCategories(new SmelteryCategory(registry.getJeiHelpers().getGuiHelper(), SMELTERY_UID));
        registry.addRecipeCategories(new GlassworkingCategory(registry.getJeiHelpers().getGuiHelper(), GLASSWORKING_UID));
    }

    @Override
    public void register(IModRegistry registry)
    {
        // Wire drawing
        List<SimpleRecipeWrapper> wireList = TechRegistries.WIRE_DRAWING.getValuesCollection()
            .stream()
            .filter(x -> x.getIngredients().size() == 2) //Only shows recipes which have a wire drawing plate (so, it can be obtained)
            .map(SimpleRecipeWrapper::new)
            .collect(Collectors.toList());

        registry.addRecipes(wireList, WIRE_DRAWING_UID);
        registry.addRecipeCatalyst(new ItemStack(TechBlocks.WIRE_DRAW_BENCH), WIRE_DRAWING_UID);

        // Glassworking (blowpipe)
        List<GlassworkingRecipeWrapper> glassList = TechRegistries.GLASSWORKING.getValuesCollection()
            .stream()
            .map(x -> new GlassworkingRecipeWrapper(x, registry.getJeiHelpers().getGuiHelper()))
            .collect(Collectors.toList());

        registry.addRecipes(glassList, GLASSWORKING_UID);
        TFCRegistries.METALS.getValuesCollection().forEach(metal -> {
            ItemBlowpipe blowpipe = ItemBlowpipe.get(metal);
            if (blowpipe != null)
            {
                registry.addRecipeCatalyst(new ItemStack(blowpipe), GLASSWORKING_UID);
            }
        });

        // Smeltery
        List<SmelteryRecipeWrapper> smelteryList = TechRegistries.SMELTERY.getValuesCollection()
            .stream()
            .map(SmelteryRecipeWrapper::new)
            .collect(Collectors.toList());

        registry.addRecipes(smelteryList, SMELTERY_UID);
        registry.addRecipeCatalyst(new ItemStack(TechBlocks.SMELTERY_CAULDRON), SMELTERY_UID);

        // Click areas
        registry.addRecipeClickArea(GuiSmelteryCauldron.class, 52, 58, 72, 15, SMELTERY_UID);
        registry.addRecipeClickArea(GuiGlassworking.class, 132, 27, 9, 14, GLASSWORKING_UID);

    }
}
