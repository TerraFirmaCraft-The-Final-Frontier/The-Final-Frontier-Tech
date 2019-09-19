package tfctech.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;

import net.dries007.tfc.api.recipes.WeldingRecipe;
import net.dries007.tfc.api.recipes.anvil.AnvilRecipe;
import net.dries007.tfc.api.recipes.barrel.BarrelRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipeSimple;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.api.util.TFCConstants;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.dries007.tfc.objects.items.metal.ItemMetal;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.forge.ForgeRule;
import tfctech.api.recipes.WireDrawingRecipe;
import tfctech.objects.fluids.TechFluids;
import tfctech.objects.items.TechItems;
import tfctech.objects.items.metal.ItemTechMetal;

import static tfctech.TFCTech.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public final class TechRecipes
{
    @SubscribeEvent
    public static void onRegisterBarrelRecipeEvent(RegistryEvent.Register<BarrelRecipe> event)
    {
        event.getRegistry().registerAll(
                new BarrelRecipe(IIngredient.of(TechFluids.LATEX.get(), 100), IIngredient.of(new ItemStack(TechItems.VULCANIZING_AGENTS)), null, new ItemStack(TechItems.RUBBER_MIX, 6), 8 * ICalendar.TICKS_IN_HOUR).setRegistryName("rubber_mix")
        );
    }

    @SubscribeEvent
    public static void onRegisterHeatRecipeEvent(RegistryEvent.Register<HeatRecipe> event)
    {
        event.getRegistry().registerAll(
                new HeatRecipeSimple(IIngredient.of(new ItemStack(TechItems.RUBBER_MIX)), new ItemStack(TechItems.RUBBER), 600f, Metal.Tier.TIER_I).setRegistryName("rubber")
        );
    }

    @SubscribeEvent
    public static void onRegisterAnvilRecipeEvent(RegistryEvent.Register<AnvilRecipe> event)
    {
        IForgeRegistry<AnvilRecipe> r = event.getRegistry();
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "iron_groove"), IIngredient.of(ItemTechMetal.get(Metal.WROUGHT_IRON, ItemTechMetal.ItemType.STRIP)), new ItemStack(TechItems.IRON_GROOVE), Metal.Tier.TIER_III, ForgeRule.HIT_LAST, ForgeRule.BEND_SECOND_LAST, ForgeRule.BEND_THIRD_LAST));
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "iron_bowl_mount"), IIngredient.of(ItemMetal.get(Metal.WROUGHT_IRON, Metal.ItemType.INGOT)), new ItemStack(TechItems.IRON_BOWL_MOUNT), Metal.Tier.TIER_III, ForgeRule.BEND_LAST, ForgeRule.DRAW_SECOND_LAST, ForgeRule.BEND_NOT_LAST));

        r.register(new AnvilRecipe(new ResourceLocation(MODID, "iron_tongs"), IIngredient.of(ItemMetal.get(Metal.WROUGHT_IRON, Metal.ItemType.INGOT)), new ItemStack(TechItems.IRON_TONGS), Metal.Tier.TIER_III, ForgeRule.HIT_LAST, ForgeRule.DRAW_SECOND_LAST, ForgeRule.BEND_THIRD_LAST));
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "iron_draw_plate"), IIngredient.of(ItemMetal.get(Metal.WROUGHT_IRON, Metal.ItemType.INGOT)), new ItemStack(TechItems.IRON_DRAW_PLATE), Metal.Tier.TIER_III, ForgeRule.PUNCH_LAST, ForgeRule.PUNCH_SECOND_LAST, ForgeRule.HIT_ANY));
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "steel_draw_plate"), IIngredient.of(ItemMetal.get(TFCRegistries.METALS.getValue(new ResourceLocation(TFCConstants.MOD_ID, "steel")), Metal.ItemType.INGOT)), new ItemStack(TechItems.STEEL_DRAW_PLATE), Metal.Tier.TIER_III, ForgeRule.PUNCH_LAST, ForgeRule.PUNCH_SECOND_LAST, ForgeRule.HIT_ANY));
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "black_steel_draw_plate"), IIngredient.of(ItemMetal.get(TFCRegistries.METALS.getValue(new ResourceLocation(TFCConstants.MOD_ID, "black_steel")), Metal.ItemType.INGOT)), new ItemStack(TechItems.BLACK_STEEL_DRAW_PLATE), Metal.Tier.TIER_III, ForgeRule.PUNCH_LAST, ForgeRule.PUNCH_SECOND_LAST, ForgeRule.HIT_ANY));

        //Register all wires
        for (Metal metal : TFCRegistries.METALS.getValuesCollection())
        {
            if (ReflectionHelper.getPrivateValue(Metal.class, metal, "usable").equals(false))
                continue;
            IIngredient<ItemStack> ingredient = IIngredient.of(new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.STRIP)));
            ItemStack output = new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.WIRE), 1, 4); //using meta as stage selector
            if (!output.isEmpty())
            {
                //noinspection ConstantConditions
                r.register(new AnvilRecipe(new ResourceLocation(MODID, (metal.getRegistryName().getPath()).toLowerCase() + "_wire"), ingredient, output, metal.getTier(), ForgeRule.DRAW_LAST, ForgeRule.DRAW_NOT_LAST));
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterWireDrawingRecipeEvent(RegistryEvent.Register<WireDrawingRecipe> event)
    {
        IForgeRegistry<WireDrawingRecipe> r = event.getRegistry();
        //Register all wires
        for (Metal metal : TFCRegistries.METALS.getValuesCollection())
        {
            if (ReflectionHelper.getPrivateValue(Metal.class, metal, "usable").equals(false))
                continue;
            for (int i = 4; i > 0; i--)
            {
                IIngredient<ItemStack> ingredient = IIngredient.of(new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.WIRE), 1, i));
                ItemStack output = new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.WIRE), 1, i - 1);
                Metal.Tier tier = metal.getTier();
                int color = metal.getColor();
                if (!output.isEmpty())
                {
                    //noinspection ConstantConditions
                    r.register(new WireDrawingRecipe(new ResourceLocation(MODID, (metal.getRegistryName().getPath()).toLowerCase() + "_wire_" + i), ingredient, tier, output, color));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterWeldingRecipeEvent(RegistryEvent.Register<WeldingRecipe> event)
    {
        IForgeRegistry<WeldingRecipe> r = event.getRegistry();
        // Register all gear (rackwheel + sleeve) welding recipes
        // Tier I-II = Tin sleeve
        // Tier III-IV = Brass sleeve
        // Tier V-VI = Steel sleeve
        for (Metal metal : TFCRegistries.METALS.getValuesCollection())
        {
            if (ReflectionHelper.getPrivateValue(Metal.class, metal, "usable").equals(false))
                continue;
            IIngredient<ItemStack> ingredient1 = IIngredient.of(new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.RACKWHEEL)));
            IIngredient<ItemStack> ingredient2;
            if (metal.getTier().isAtMost(Metal.Tier.TIER_II))
            {
                ingredient2 = IIngredient.of(new ItemStack(TechItems.TIN_SLEEVE));
            }
            else if (metal.getTier().isAtMost(Metal.Tier.TIER_IV))
            {
                ingredient2 = IIngredient.of(new ItemStack(TechItems.BRASS_SLEEVE));
            }
            else
            {
                ingredient2 = IIngredient.of(new ItemStack(TechItems.STEEL_SLEEVE));
            }
            ItemStack output = new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.GEAR));
            if (!output.isEmpty())
            {
                //noinspection ConstantConditions
                r.register(new WeldingRecipe(new ResourceLocation(MODID, (metal.getRegistryName().getPath()).toLowerCase() + "_gear"), ingredient1, ingredient2, output, metal.getTier()));
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCraftingRecipeEvent(RegistryEvent.Register<IRecipe> event)
    {
        IForgeRegistry<IRecipe> r = event.getRegistry();
        //Register all strips
        List<Item> allChisels = new ArrayList<>();
        for (Metal metal : TFCRegistries.METALS.getValuesCollection())
        {
            if (!metal.isToolMetal())
                continue;
            allChisels.add(ItemMetal.get(metal, Metal.ItemType.CHISEL));
        }
        Ingredient chisel = Ingredient.fromItems(allChisels.toArray(new Item[0]));
        for (Metal metal : TFCRegistries.METALS.getValuesCollection())
        {
            if (ReflectionHelper.getPrivateValue(Metal.class, metal, "usable").equals(false))
                continue;
            Ingredient ingredient = Ingredient.fromStacks(new ItemStack(ItemMetal.get(metal, Metal.ItemType.SHEET)));
            ItemStack output = new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.STRIP), 4);
            if (!output.isEmpty())
            {
                NonNullList<Ingredient> list = NonNullList.create();
                list.add(chisel);
                list.add(ingredient);
                //noinspection ConstantConditions
                r.register(new ShapedRecipes("strip", 2, 1, list, output).setRegistryName(MODID, metal.getRegistryName().getPath().toLowerCase() + "_strip"));
            }

            ingredient = Ingredient.fromStacks(new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.RACKWHEEL_PIECE)));
            output = new ItemStack(ItemTechMetal.get(metal, ItemTechMetal.ItemType.RACKWHEEL));
            if (!output.isEmpty())
            {
                NonNullList<Ingredient> list = NonNullList.create();
                list.add(ingredient);
                list.add(ingredient);
                list.add(ingredient);
                list.add(ingredient);
                //noinspection ConstantConditions
                r.register(new ShapedRecipes("rackwheel", 2, 2, list, output).setRegistryName(MODID, metal.getRegistryName().getPath().toLowerCase() + "_rackwheel"));
            }
        }
    }
}
