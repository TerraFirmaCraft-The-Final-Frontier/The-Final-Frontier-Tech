package tfctech.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import tfctech.client.render.teisr.TEISRWireDrawBench;
import tfctech.client.render.tesr.TESRLatexExtractor;
import tfctech.client.render.tesr.TESRWireDrawBench;
import tfctech.objects.blocks.TechBlocks;
import tfctech.objects.blocks.devices.BlockWireDrawBench;
import tfctech.objects.items.TechItems;
import tfctech.objects.items.itemblocks.ItemBlockWireDrawBench;
import tfctech.objects.items.metal.ItemTechMetal;
import tfctech.objects.tileentities.TELatexExtractor;
import tfctech.objects.tileentities.TEWireDrawBench;

import static tfctech.TFCTech.MODID;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MODID)
public final class ClientRegisterEvents
{
    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    public static void registerModels(final ModelRegistryEvent event)
    {
        // ITEMS //

        //Fluid containers
        ModelLoader.setCustomModelResourceLocation(TechItems.FLUID_BOWL, 0, new ModelResourceLocation(TechItems.FLUID_BOWL.getRegistryName(), "inventory"));


        // Simple Items
        for (Item item : TechItems.getAllSimpleItems())
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));

        for (Block block : TechBlocks.getAllFluidBlocks())
            ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());


        // Item Blocks
        for (ItemBlock item : TechBlocks.getAllInventoryItemBlocks())
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            if (item instanceof ItemBlockWireDrawBench)
            {
                item.setTileEntityItemStackRenderer(new TEISRWireDrawBench());
            }
        }


        // Metals
        for (Item item : TechItems.getAllMetalItems())
        {
            ItemTechMetal metalItem = (ItemTechMetal) item;
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(MODID, "metal/" + metalItem.getType().name().toLowerCase()), "inventory"));
            if (((ItemTechMetal) item).getType() == ItemTechMetal.ItemType.WIRE)
            {
                for (int i = 1; i <= 4; i++)
                    ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(MODID, "metal/" + metalItem.getType().name().toLowerCase()), "inventory"));

            }
        }

        // Ignored states
        ModelLoader.setCustomStateMapper(TechBlocks.WIRE_DRAW_BENCH, new StateMap.Builder().ignore(BlockWireDrawBench.UPPER).ignore(BlockHorizontal.FACING).build());


        // TESRs //

        ClientRegistry.bindTileEntitySpecialRenderer(TELatexExtractor.class, new TESRLatexExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TEWireDrawBench.class, new TESRWireDrawBench());
    }

    @SubscribeEvent
    public static void registerItemColourHandlers(final ColorHandlerEvent.Item event)
    {
        final ItemColors itemColors = event.getItemColors();

        for (Item item : TechItems.getAllMetalItems())
        {
            itemColors.registerItemColorHandler(
                    (stack, tintIndex) -> ((ItemTechMetal) stack.getItem()).getMetal(stack).getColor(),
                    item);
        }
    }
}
