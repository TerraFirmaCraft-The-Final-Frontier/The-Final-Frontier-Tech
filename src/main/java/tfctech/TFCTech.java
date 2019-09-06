package tfctech;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.dries007.tfc.api.util.TFCConstants;

//todo add required dependency TFC-Metallum

@SuppressWarnings("WeakerAccess")
@Mod(modid = TFCTech.MODID, name = TFCTech.NAME, version = TFCTech.VERSION, dependencies = "required-after:" + TFCConstants.MOD_ID)
public class TFCTech
{
    public static final String MODID = "tfctech";
    public static final String NAME = "TFCTech Unofficial";
    public static final String VERSION = "@VERSION@";

    private static Logger logger;

    public static Logger getLog()
    {
        return logger;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
