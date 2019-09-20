package tfctech.registry;

import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.IFoodTrait;

public final class TechFoodTraits
{
    public static final IFoodTrait.Impl COLD = new IFoodTrait.Impl("cold", 0.25f);
    public static final IFoodTrait.Impl FROZEN = new IFoodTrait.Impl("frozen", 0.1f);

    public static void preInit()
    {
        CapabilityFood.getTraits().put("cold", COLD);
        CapabilityFood.getTraits().put("frozen", FROZEN);
    }
}
