package tfctech.client;

import java.awt.*;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.api.capability.IMoldHandler;
import net.dries007.tfc.api.types.Metal;
import tfctech.client.render.teisr.TEISRTechDevices;
import tfctech.client.render.tesr.TESRFridge;
import tfctech.client.render.tesr.TESRLatexExtractor;
import tfctech.client.render.tesr.TESRWireDrawBench;
import tfctech.objects.blocks.TechBlocks;
import tfctech.objects.items.TechItems;
import tfctech.objects.items.metal.ItemGear;
import tfctech.objects.items.metal.ItemTechMetal;
import tfctech.objects.tileentities.TEFridge;
import tfctech.objects.tileentities.TELatexExtractor;
import tfctech.objects.tileentities.TEWireDrawBench;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
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
        }

        //TEISR item blocks
        for (ItemBlock item : TechBlocks.getAllTEISRBlocks())
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            item.setTileEntityItemStackRenderer(new TEISRTechDevices());
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

        // Molds
        for (Item item : TechItems.getAllCeramicMoldItems())
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));

        // Ignored states
        ModelLoader.setCustomStateMapper(TechBlocks.WIRE_DRAW_BENCH, new IStateMapper()
        {
            @Override
            @Nonnull
            public Map<IBlockState, ModelResourceLocation> putStateModelLocations(@Nonnull Block blockIn)
            {
                return Collections.emptyMap();
            }
        });
        ModelLoader.setCustomStateMapper(TechBlocks.FRIDGE, new IStateMapper()
        {
            @Override
            @Nonnull
            public Map<IBlockState, ModelResourceLocation> putStateModelLocations(@Nonnull Block blockIn)
            {
                return Collections.emptyMap();
            }
        });


        // TESRs //

        ClientRegistry.bindTileEntitySpecialRenderer(TELatexExtractor.class, new TESRLatexExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TEWireDrawBench.class, new TESRWireDrawBench());
        ClientRegistry.bindTileEntitySpecialRenderer(TEFridge.class, new TESRFridge());
    }

    @SubscribeEvent
    public static void registerItemColourHandlers(final ColorHandlerEvent.Item event)
    {
        final ItemColors itemColors = event.getItemColors();

        for (Item item : TechItems.getAllMetalItems())
        {
            itemColors.registerItemColorHandler(
                (stack, tintIndex) -> {
                    if (tintIndex == 1 && stack.getItem() instanceof ItemGear)
                    {
                        return (new Color(((ItemGear) stack.getItem()).getSleeveMetal().getColor())).brighter().getRGB();
                    }
                    return (new Color(((ItemTechMetal) stack.getItem()).getMetal(stack).getColor())).brighter().getRGB();
                },
                item);
        }

        for (Item item : TechItems.getAllCeramicMoldItems())
        {
            itemColors.registerItemColorHandler(
                (stack, tintIndex) -> {
                    if (tintIndex == 1)
                    {
                        IFluidHandler capFluidHandler = stack.getCapability(FLUID_HANDLER_CAPABILITY, null);
                        if (capFluidHandler instanceof IMoldHandler)
                        {
                            Metal metal = ((IMoldHandler) capFluidHandler).getMetal();
                            if (metal != null)
                            {
                                return (new Color(metal.getColor())).brighter().getRGB();
                            }
                        }
                        return 0xFF000000;
                    }
                    return -1;
                },
                item);
        }
    }
}
