package tfctech.objects.items;

import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import tfctech.objects.blocks.ModBlocks;

import static tfctech.TFCTech.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
@GameRegistry.ObjectHolder(MODID)
public final class ModItems
{

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

        allSimpleItems = simpleItems.build();

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
}
