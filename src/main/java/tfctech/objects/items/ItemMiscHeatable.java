package tfctech.objects.items;

import net.dries007.tfc.api.capability.heat.ItemHeatHandler;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.objects.items.ItemMisc;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class ItemMiscHeatable extends ItemMisc {

    private float heatCapacity, meltTemp;

    public ItemMiscHeatable(Size size, Weight weight, float heatCapacity, float meltTemp) {
        super(size, weight);
        this.heatCapacity = heatCapacity;
        this.meltTemp = meltTemp;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ItemHeatHandler(nbt, heatCapacity, meltTemp);
    }
}
