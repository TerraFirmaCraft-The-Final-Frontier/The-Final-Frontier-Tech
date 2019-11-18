package tfctech;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import net.dries007.tfc.api.util.TFCConstants;
import tfctech.client.TechGuiHandler;
import tfctech.network.PacketLatexUpdate;
import tfctech.network.PacketTileEntityUpdate;

@SuppressWarnings("WeakerAccess")
@Mod(modid = TFCTech.MODID, name = TFCTech.NAME, version = TFCTech.VERSION, dependencies = "required-after:" + TFCConstants.MOD_ID, certificateFingerprint = TFCTech.SIGNING_KEY)
public class TFCTech
{
    public static final String MODID = "tfctech";
    public static final String NAME = "TFCTech Unofficial";
    public static final String VERSION = "@VERSION@";
    public static final String SIGNING_KEY = "@FINGERPRINT@";

    @Mod.Instance
    private static TFCTech instance = null;

    public static SimpleNetworkWrapper getNetwork()
    {
        return instance.network;
    }

    private static Logger logger;

    public static Logger getLog()
    {
        return logger;
    }

    public static TFCTech getInstance()
    {
        return instance;
    }

    private SimpleNetworkWrapper network;

    @EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event)
    {
        logger.warn("Invalid fingerprint detected! This means this jar file has been modified externally and is not supported");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //Waila/Hwyla/TOP initialization (if present)
        FMLInterModComms.sendMessage("waila", "register", "tfctech.compat.waila.WailaPlugin.callbackRegister");
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "tfctech.compat.waila.TOPPlugin");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new TechGuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        int id = 0;
        network.registerMessage(new PacketLatexUpdate.Handler(), PacketLatexUpdate.class, ++id, Side.CLIENT);
        network.registerMessage(new PacketTileEntityUpdate.Handler(), PacketTileEntityUpdate.class, ++id, Side.CLIENT);
    }
}
