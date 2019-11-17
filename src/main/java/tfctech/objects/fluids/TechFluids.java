package tfctech.objects.fluids;

import javax.annotation.Nonnull;

import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import net.dries007.tfc.objects.fluids.properties.FluidWrapper;

import static net.dries007.tfc.api.util.TFCConstants.MOD_ID;

@SuppressWarnings("WeakerAccess")
public final class TechFluids
{
    private static final ResourceLocation LAVA_STILL = new ResourceLocation(MOD_ID, "blocks/lava_still");
    private static final ResourceLocation LAVA_FLOW = new ResourceLocation(MOD_ID, "blocks/lava_flow");
    private static final HashBiMap<Fluid, FluidWrapper> WRAPPERS = HashBiMap.create();

    public static FluidWrapper LATEX;

    public static void registerFluids()
    {
        LATEX = registerFluid(new Fluid("latex", LAVA_STILL, LAVA_FLOW, 0xFFF8F8F8));
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
