package tfctech.objects.fluids;

import javax.annotation.Nonnull;

import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.objects.fluids.properties.FluidWrapper;

public final class TechFluids
{
    private static final ResourceLocation MOLTEN_STILL = new ResourceLocation(TerraFirmaCraft.MOD_ID, "blocks/fluids/molten_still");
    private static final ResourceLocation MOLTEN_FLOWING = new ResourceLocation(TerraFirmaCraft.MOD_ID, "blocks/fluids/molten_flowing");
    private static final HashBiMap<Fluid, FluidWrapper> WRAPPERS = HashBiMap.create();

    public static FluidWrapper LATEX;
    public static FluidWrapper GLASS;

    public static void registerFluids()
    {
        LATEX = registerFluid(new Fluid("latex", MOLTEN_STILL, MOLTEN_FLOWING, 0xFFF8F8F8));
        GLASS = registerFluid(new Fluid("glass", MOLTEN_STILL, MOLTEN_FLOWING, 0xFFED97B5));

        setFluidTemperatures();
    }

    private static void setFluidTemperatures()
    {
        GLASS.get().setTemperature(1073); // Kelvin
    }

    private static FluidWrapper registerFluid(@Nonnull Fluid newFluid)
    {
        boolean isDefault = FluidRegistry.registerFluid(newFluid);
        if (!isDefault)
        {
            // Fluid was already registered with this name, default to that fluid
            newFluid = FluidRegistry.getFluid(newFluid.getName());
        }
        FluidRegistry.addBucketForFluid(newFluid);
        FluidWrapper properties = new FluidWrapper(newFluid, isDefault);
        WRAPPERS.put(newFluid, properties);
        return properties;
    }
}
