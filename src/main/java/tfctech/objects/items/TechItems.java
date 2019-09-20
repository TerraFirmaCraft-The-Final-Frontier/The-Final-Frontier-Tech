package tfctech.objects.items;

import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;

import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.api.util.TFCConstants;
import net.dries007.tfc.objects.ToolMaterialsTFC;
import tfctech.objects.blocks.TechBlocks;
import tfctech.objects.items.ceramics.ItemFluidBowl;
import tfctech.objects.items.metal.ItemGroove;
import tfctech.objects.items.metal.ItemTechMetal;

import static net.dries007.tfc.objects.CreativeTabsTFC.*;
import static net.dries007.tfc.util.Helpers.getNull;
import static tfctech.TFCTech.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
@GameRegistry.ObjectHolder(MODID)
public final class TechItems
{
    @GameRegistry.ObjectHolder("metal/iron_groove")
    public static final ItemGroove IRON_GROOVE = getNull();
    @GameRegistry.ObjectHolder("metal/iron_bowl_mount")
    public static final ItemTechMetal IRON_BOWL_MOUNT = getNull();
    @GameRegistry.ObjectHolder("ceramics/fluid_bowl")
    public static final ItemFluidBowl FLUID_BOWL = getNull();
    @GameRegistry.ObjectHolder("latex/vulcanizing_agents")
    public static final Item VULCANIZING_AGENTS = getNull();
    @GameRegistry.ObjectHolder("latex/rubber_mix")
    public static final Item RUBBER_MIX = getNull();
    @GameRegistry.ObjectHolder("latex/rubber")
    public static final Item RUBBER = getNull();


    @GameRegistry.ObjectHolder("metal/iron_draw_plate")
    public static final ItemTechMetal IRON_DRAW_PLATE = getNull();
    @GameRegistry.ObjectHolder("metal/steel_draw_plate")
    public static final ItemTechMetal STEEL_DRAW_PLATE = getNull();
    @GameRegistry.ObjectHolder("metal/black_steel_draw_plate")
    public static final ItemTechMetal BLACK_STEEL_DRAW_PLATE = getNull();
    @GameRegistry.ObjectHolder("metal/iron_tongs")
    public static final ItemTechMetal IRON_TONGS = getNull();
    @GameRegistry.ObjectHolder("wiredraw/leather_belt")
    public static final Item LEATHER_BELT = getNull();
    @GameRegistry.ObjectHolder("wiredraw/winch")
    public static final Item WINCH = getNull();

    @GameRegistry.ObjectHolder("metal/copper_inductor")
    public static final ItemTechMetal COPPER_INDUCTOR = getNull();

    @GameRegistry.ObjectHolder("metal/tin_sleeve")
    public static final ItemTechMetal TIN_SLEEVE = getNull();
    @GameRegistry.ObjectHolder("metal/brass_sleeve")
    public static final ItemTechMetal BRASS_SLEEVE = getNull();
    @GameRegistry.ObjectHolder("metal/steel_sleeve")
    public static final ItemTechMetal STEEL_SLEEVE = getNull();

    private static ImmutableList<Item> allSimpleItems;
    public static ImmutableList<Item> getAllSimpleItems()
    {
        return allSimpleItems;
    }

    private static ImmutableList<Item> allMetalItems;

    public static ImmutableList<Item> getAllMetalItems()
    {
        return allMetalItems;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> r = event.getRegistry();
        ImmutableList.Builder<Item> simpleItems = ImmutableList.builder();

        simpleItems.add(register(r, "latex/vulcanizing_agents", new Item(), CT_MISC));
        simpleItems.add(register(r, "latex/rubber_mix", new Item(), CT_MISC));
        simpleItems.add(register(r, "latex/rubber", new Item(), CT_MISC));

        simpleItems.add(register(r, "wiredraw/leather_belt", new Item(), CT_MISC));
        simpleItems.add(register(r, "wiredraw/winch", new Item(), CT_MISC));

        allSimpleItems = simpleItems.build();

        ImmutableList.Builder<Item> metalItems = ImmutableList.builder();

        metalItems.add(register(r, "metal/iron_groove", ItemTechMetal.ItemType.create(Metal.WROUGHT_IRON, ItemTechMetal.ItemType.GROOVE), CT_METAL));
        metalItems.add(register(r, "metal/iron_bowl_mount", ItemTechMetal.ItemType.create(Metal.WROUGHT_IRON, ItemTechMetal.ItemType.BOWL_MOUNT), CT_METAL));

        metalItems.add(register(r, "metal/iron_draw_plate", ItemTechMetal.ItemType.create(Metal.WROUGHT_IRON, ItemTechMetal.ItemType.DRAW_PLATE).setMaxDamage(ToolMaterialsTFC.WROUGHT_IRON.getMaxUses()).setMaxStackSize(1), CT_METAL));
        metalItems.add(register(r, "metal/steel_draw_plate", ItemTechMetal.ItemType.create(TFCRegistries.METALS.getValue(new ResourceLocation(TFCConstants.MOD_ID, "steel")), ItemTechMetal.ItemType.DRAW_PLATE).setMaxDamage(ToolMaterialsTFC.STEEL.getMaxUses()).setMaxStackSize(1), CT_METAL));
        metalItems.add(register(r, "metal/black_steel_draw_plate", ItemTechMetal.ItemType.create(TFCRegistries.METALS.getValue(new ResourceLocation(TFCConstants.MOD_ID, "black_steel")), ItemTechMetal.ItemType.DRAW_PLATE).setMaxDamage(ToolMaterialsTFC.BLACK_STEEL.getMaxUses()).setMaxStackSize(1), CT_METAL));
        metalItems.add(register(r, "metal/iron_tongs", ItemTechMetal.ItemType.create(Metal.WROUGHT_IRON, ItemTechMetal.ItemType.TONGS).setMaxStackSize(1), CT_MISC));

        metalItems.add(register(r, "metal/copper_inductor", ItemTechMetal.ItemType.create(TFCRegistries.METALS.getValue(new ResourceLocation(TFCConstants.MOD_ID, "copper")), ItemTechMetal.ItemType.INDUCTOR), CT_METAL));

        metalItems.add(register(r, "metal/tin_sleeve", ItemTechMetal.ItemType.create(TFCRegistries.METALS.getValue(new ResourceLocation(TFCConstants.MOD_ID, "tin")), ItemTechMetal.ItemType.SLEEVE), CT_METAL));
        metalItems.add(register(r, "metal/brass_sleeve", ItemTechMetal.ItemType.create(TFCRegistries.METALS.getValue(new ResourceLocation(TFCConstants.MOD_ID, "brass")), ItemTechMetal.ItemType.SLEEVE), CT_METAL));
        metalItems.add(register(r, "metal/steel_sleeve", ItemTechMetal.ItemType.create(TFCRegistries.METALS.getValue(new ResourceLocation(TFCConstants.MOD_ID, "steel")), ItemTechMetal.ItemType.SLEEVE), CT_METAL));

        for (Metal metal : TFCRegistries.METALS.getValuesCollection())
        {
            if (ReflectionHelper.getPrivateValue(Metal.class, metal, "usable").equals(false))
                continue;
            //noinspection ConstantConditions
            metalItems.add(register(r, "metal/" + metal.getRegistryName().getPath().toLowerCase() + "_strip", ItemTechMetal.ItemType.create(metal, ItemTechMetal.ItemType.STRIP), CT_METAL));
            metalItems.add(register(r, "metal/" + metal.getRegistryName().getPath().toLowerCase() + "_rackwheel_piece", ItemTechMetal.ItemType.create(metal, ItemTechMetal.ItemType.RACKWHEEL_PIECE), CT_METAL));
            metalItems.add(register(r, "metal/" + metal.getRegistryName().getPath().toLowerCase() + "_rackwheel", ItemTechMetal.ItemType.create(metal, ItemTechMetal.ItemType.RACKWHEEL), CT_METAL));
            metalItems.add(register(r, "metal/" + metal.getRegistryName().getPath().toLowerCase() + "_gear", ItemTechMetal.ItemType.create(metal, ItemTechMetal.ItemType.GEAR), CT_METAL));
            metalItems.add(register(r, "metal/" + metal.getRegistryName().getPath().toLowerCase() + "_wire", ItemTechMetal.ItemType.create(metal, ItemTechMetal.ItemType.WIRE), CT_METAL));
        }

        allMetalItems = metalItems.build();

        register(r, "ceramics/fluid_bowl", new ItemFluidBowl(), CT_POTTERY);

        //todo ceramic molds for glass
        TechBlocks.getAllInventoryItemBlocks().forEach(x -> registerItemBlock(r, x));
        TechBlocks.getAllTEISRBlocks().forEach(x -> registerItemBlock(r, x));
    }

    private static <T extends Block> ItemBlock createItemBlock(T block, Function<T, ItemBlock> producer)
    {
        ItemBlock itemBlock = producer.apply(block);
        //noinspection ConstantConditions
        itemBlock.setRegistryName(block.getRegistryName());
        return itemBlock;
    }

    @SuppressWarnings("ConstantConditions")
    private static void registerItemBlock(IForgeRegistry<Item> r, ItemBlock item)
    {
        item.setRegistryName(item.getBlock().getRegistryName());
        item.setCreativeTab(item.getBlock().getCreativeTab());
        r.register(item);
    }

    private static <T extends Item> T register(IForgeRegistry<Item> r, String name, T item, CreativeTabs ct)
    {
        item.setRegistryName(MODID, name);
        item.setTranslationKey(MODID + "." + name.replace('/', '.'));
        item.setCreativeTab(ct);
        r.register(item);
        return item;
    }
}
