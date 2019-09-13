package tfctech.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import tfctech.client.render.TESRLatexExtractor;
import tfctech.objects.blocks.ModBlocks;
import tfctech.objects.items.ModItems;
import tfctech.objects.tileentities.TELatexExtractor;

import static tfctech.TFCTech.MODID;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MODID)
public final class ClientRegisterEvents
{
    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    public static void registerModels(ModelRegistryEvent event)
    {
        // ITEMS //

        //Fluid containers
        ModelLoader.setCustomModelResourceLocation(ModItems.FLUID_BOWL, 0, new ModelResourceLocation(ModItems.FLUID_BOWL.getRegistryName(), "inventory"));


        // Simple Items
        for (Item item : ModItems.getAllSimpleItems())
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));

        for (Block block : ModBlocks.getAllFluidBlocks())
            ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());


        // Item Blocks
        for (ItemBlock item : ModBlocks.getAllInventoryItemBlocks())
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));

        // TESRs //

        ClientRegistry.bindTileEntitySpecialRenderer(TELatexExtractor.class, new TESRLatexExtractor());
    }
}
