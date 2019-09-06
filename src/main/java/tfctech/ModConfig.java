package tfctech;

import net.minecraftforge.common.config.Config;

import static tfctech.TFCTech.MODID;

@SuppressWarnings("WeakerAccess")
@Config(modid = MODID, category = "")
@Config.LangKey("config." + MODID)
public final class ModConfig
{
    @Config.Comment("General configuration")
    @Config.LangKey("config." + MODID + ".general")
    public static General GENERAL = new General();

    public static class General
    {
        //todo add config for devices and recipes here
    }
}
