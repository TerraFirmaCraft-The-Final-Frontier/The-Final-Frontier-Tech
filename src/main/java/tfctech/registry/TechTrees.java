package tfctech.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import net.dries007.tfc.api.registries.TFCRegistryEvent;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.api.util.TFCConstants;

import static net.dries007.tfc.types.DefaultTrees.GEN_TALL;
import static net.dries007.tfc.util.Helpers.getNull;
import static tfctech.TFCTech.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public final class TechTrees
{
    @GameRegistry.ObjectHolder(TFCConstants.MOD_ID + ":hevea")
    public static final Tree HEVEA = getNull();

    @SubscribeEvent
    public static void onPreRegisterTrees(TFCRegistryEvent.RegisterPreBlock<Tree> event)
    {
        event.getRegistry().registerAll(
                new Tree.Builder(new ResourceLocation(TFCConstants.MOD_ID, "hevea"), 20f, 300f, 5f, 31f, GEN_TALL).setRadius(0).setGrowthTime(10).setBurnInfo(762f, 2000).build()
        );
    }
}

