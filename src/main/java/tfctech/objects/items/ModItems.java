package tfctech.objects.items;

import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import tfctech.objects.blocks.ModBlocks;
import tfctech.objects.items.tools.ItemFluidBowl;
import tfctech.objects.items.tools.ItemGroove;

import static net.dries007.tfc.objects.CreativeTabsTFC.CT_MISC;
import static net.dries007.tfc.objects.CreativeTabsTFC.CT_POTTERY;
import static net.dries007.tfc.util.Helpers.getNull;
import static tfctech.TFCTech.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
@GameRegistry.ObjectHolder(MODID)
public final class ModItems
{
    @GameRegistry.ObjectHolder("tools/fluid_bowl")
    public static final ItemFluidBowl FLUID_BOWL = getNull();

    @GameRegistry.ObjectHolder("tools/bowl_mount")
    public static final Item BOWL_MOUNT = getNull();

    @GameRegistry.ObjectHolder("tools/draw_plate")
    public static final Item DRAW_PLATE = getNull();

    @GameRegistry.ObjectHolder("tools/groove")
    public static final ItemGroove GROOVE = getNull();

    public static final Item RUBBER_BALL = getNull();

    private static ImmutableList<Item> allSimpleItems;

    public static ImmutableList<Item> getAllSimpleItems()
    {
        return allSimpleItems;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> r = event.getRegistry();
        ImmutableList.Builder<Item> simpleItems = ImmutableList.builder();

        simpleItems.add(register(r, "tools/groove", new ItemGroove(), CT_MISC));
        simpleItems.add(register(r, "tools/bowl_mount", new Item(), CT_MISC));
        simpleItems.add(register(r, "tools/draw_plate", new Item(), CT_MISC));
        simpleItems.add(register(r, "rubber_ball", new Item(), CT_MISC));
        simpleItems.add(register(r, "rubber", new Item(), CT_MISC));

        allSimpleItems = simpleItems.build();

        register(r, "tools/fluid_bowl", new ItemFluidBowl(), CT_POTTERY);

        //todo register items
        //todo ceramic molds for glass
        ModBlocks.getAllInventoryItemBlocks().forEach(x -> registerItemBlock(r, x));
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
