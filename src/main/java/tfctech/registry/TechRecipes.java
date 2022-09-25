package tfctech.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.recipes.WeldingRecipe;
import net.dries007.tfc.api.recipes.anvil.AnvilRecipe;
import net.dries007.tfc.api.recipes.barrel.BarrelRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipeSimple;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipe;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipeSimple;
import net.dries007.tfc.api.recipes.knapping.KnappingType;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.dries007.tfc.objects.items.ItemsTFC;
import net.dries007.tfc.objects.items.metal.ItemMetal;
import net.dries007.tfc.objects.recipes.ShapelessDamageRecipe;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.forge.ForgeRule;
import tfctech.TechConfig;
import tfctech.api.recipes.GlassworkingRecipe;
import tfctech.api.recipes.SmelteryRecipe;
import tfctech.api.recipes.WireDrawingRecipe;
import tfctech.objects.fluids.TechFluids;
import tfctech.objects.items.TechItems;
import tfctech.objects.items.glassworking.ItemBlowpipe;
import tfctech.objects.items.metal.ItemTechMetal;

import static tfctech.TFCTech.MODID;

@SuppressWarnings({"ConstantConditions", "unused"})
@Mod.EventBusSubscriber(modid = MODID)
public final class TechRecipes
{

    @SubscribeEvent
    public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
    {
        IForgeRegistryModifiable<IRecipe> modRegistry = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
        if (TechConfig.TWEAKS.removeGlassRecipes)
        {
            modRegistry.remove(new ResourceLocation("minecraft:glass_pane"));

            List<ItemStack> removeList = new ArrayList<>();
            FurnaceRecipes.instance().getSmeltingList().keySet().forEach(stack -> {
                ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
                if (OreDictionaryHelper.doesStackMatchOre(result, "blockGlass"))
                {
                    removeList.add(result);
                }
            });
            removeList.forEach(stack -> FurnaceRecipes.instance().getSmeltingList().remove(stack));
        }
    }

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
            new HeatRecipeSimple(IIngredient.of(new ItemStack(TechItems.RUBBER_MIX)), new ItemStack(TechItems.RUBBER), 600f, Metal.Tier.TIER_I).setRegistryName("rubber"),
            new HeatRecipeSimple(IIngredient.of(new ItemStack(TechItems.ASH_POT)), new ItemStack(TechItems.POTASH_POT), 500f, Metal.Tier.TIER_I).setRegistryName("potash_pot"),
            new HeatRecipeSimple(IIngredient.of("rockFlux"), new ItemStack(TechItems.LIME, 2), 600f, Metal.Tier.TIER_I).setRegistryName("lime"),
            new HeatRecipeSimple(IIngredient.of(new ItemStack(TechItems.UNFIRED_MOLD_PANE)), new ItemStack(TechItems.MOLD_PANE), 1599f, Metal.Tier.TIER_I).setRegistryName("fired_mold_pane"),
            new HeatRecipeSimple(IIngredient.of(new ItemStack(TechItems.UNFIRED_MOLD_BLOCK)), new ItemStack(TechItems.MOLD_BLOCK), 1599f, Metal.Tier.TIER_I).setRegistryName("fired_mold_block")
        );


        IForgeRegistryModifiable<HeatRecipe> modRegistry = (IForgeRegistryModifiable<HeatRecipe>) event.getRegistry();
        if (TechConfig.TWEAKS.removeGlassRecipes)
        {
            modRegistry.remove(new ResourceLocation(TerraFirmaCraft.MOD_ID, "glass"));
        }

    }

    @SubscribeEvent
    public static void onRegisterAnvilRecipeEvent(RegistryEvent.Register<AnvilRecipe> event)
    {
        IForgeRegistry<AnvilRecipe> r = event.getRegistry();
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "iron_bowl_mount"), IIngredient.of(ItemMetal.get(Metal.WROUGHT_IRON, Metal.ItemType.INGOT)), new ItemStack(TechItems.IRON_BOWL_MOUNT), Metal.Tier.TIER_III, null, ForgeRule.BEND_LAST, ForgeRule.DRAW_SECOND_LAST, ForgeRule.BEND_NOT_LAST));

        r.register(new AnvilRecipe(new ResourceLocation(MODID, "iron_tongs"), IIngredient.of(ItemMetal.get(Metal.WROUGHT_IRON, Metal.ItemType.INGOT)), new ItemStack(TechItems.IRON_TONGS), Metal.Tier.TIER_III, null, ForgeRule.HIT_LAST, ForgeRule.DRAW_SECOND_LAST, ForgeRule.BEND_THIRD_LAST));
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "iron_draw_plate"), IIngredient.of(ItemMetal.get(Metal.WROUGHT_IRON, Metal.ItemType.INGOT)), new ItemStack(TechItems.IRON_DRAW_PLATE), Metal.Tier.TIER_III, null, ForgeRule.PUNCH_LAST, ForgeRule.PUNCH_SECOND_LAST, ForgeRule.HIT_ANY));
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "steel_draw_plate"), IIngredient.of(ItemMetal.get(TFCRegistries.METALS.getValue(new ResourceLocation(TerraFirmaCraft.MOD_ID, "steel")), Metal.ItemType.INGOT)), new ItemStack(TechItems.STEEL_DRAW_PLATE), Metal.Tier.TIER_IV, null, ForgeRule.PUNCH_LAST, ForgeRule.PUNCH_SECOND_LAST, ForgeRule.HIT_ANY));
        r.register(new AnvilRecipe(new ResourceLocation(MODID, "black_steel_draw_plate"), IIngredient.of(ItemMetal.get(TFCRegistries.METALS.getValue(new ResourceLocation(TerraFirmaCraft.MOD_ID, "black_steel")), Metal.ItemType.INGOT)), new ItemStack(TechItems.BLACK_STEEL_DRAW_PLATE), Metal.Tier.TIER_V, null, ForgeRule.PUNCH_LAST, ForgeRule.PUNCH_SECOND_LAST, ForgeRule.HIT_ANY));
    }

    @SubscribeEvent
    public static void onRegisterKnappingRecipeEvent(RegistryEvent.Register<KnappingRecipe> event)
    {
        IForgeRegistry<KnappingRecipe> r = event.getRegistry();

        r.registerAll(
            new KnappingRecipeSimple(KnappingType.CLAY, true, new ItemStack(TechItems.UNFIRED_MOLD_PANE), "XXXXX", "X   X", "X   X", "X   X", "XXXXX").setRegistryName("clay_mold_pane"),
            new KnappingRecipeSimple(KnappingType.CLAY, true, new ItemStack(TechItems.UNFIRED_MOLD_BLOCK), "X   X", "X   X", "X   X", "X   X", " XXX ").setRegistryName("clay_mold_block")
        );
    }

    @SubscribeEvent
    public static void onRegisterGlassworkingRecipeEvent(RegistryEvent.Register<GlassworkingRecipe> event)
    {
        IForgeRegistry<GlassworkingRecipe> r = event.getRegistry();

        r.registerAll(
            new GlassworkingRecipe(new ItemStack(Items.GLASS_BOTTLE),
                " X X ", " X X ", "X   X", "X   X", " XXX ").setRegistryName(MODID, "glass_bottle")
        );
    }

    @SubscribeEvent
    public static void onRegisterSmelteryRecipeEvent(RegistryEvent.Register<SmelteryRecipe> event)
    {
        IForgeRegistry<SmelteryRecipe> r = event.getRegistry();
        r.registerAll(


            new SmelteryRecipe.Builder()
                .addInput(IIngredient.of("dustPotash")).addInput(IIngredient.of("sandSilica")).addInput(IIngredient.of("dustLime"))
                .setOutput(new FluidStack(TechFluids.GLASS.get(), 1000), 800).build()
                .setRegistryName(new ResourceLocation(MODID, "glass")),

            new SmelteryRecipe.Builder()
                .addInput(IIngredient.of("blockGlass"))
                .setOutput(new FluidStack(TechFluids.GLASS.get(), 1000), 800).build()
                .setRegistryName(new ResourceLocation(MODID, "glass_block")),

            new SmelteryRecipe.Builder()
                .addInput(IIngredient.of("paneGlass"))
                .setOutput(new FluidStack(TechFluids.GLASS.get(), 375), 800).build()
                .setRegistryName(new ResourceLocation(MODID, "glass_pane")),

            new SmelteryRecipe.Builder()
                .addInput(IIngredient.of(Items.GLASS_BOTTLE))
                .setOutput(new FluidStack(TechFluids.GLASS.get(), 250), 800).build()
                .setRegistryName(new ResourceLocation(MODID, "glass_bottle"))
				
        );
    }
}
