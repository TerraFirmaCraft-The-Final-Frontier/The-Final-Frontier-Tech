package tfctech.objects.items.tools;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import net.dries007.tfc.api.capability.metal.IMetalItem;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.items.ItemTFC;

public class ItemDrawplate extends ItemTFC implements IMetalItem
{
    private static final Map<Metal, ItemDrawplate> TABLE = new HashMap<>();

    public static ItemDrawplate get(Metal metal)
    {
        return TABLE.get(metal);
    }

    private final Metal metal;

    public ItemDrawplate(Metal metal)
    {
        super();
        this.metal = metal;
        if (!TABLE.containsKey(metal))
        {
            TABLE.put(metal, this);
        }
        this.setNoRepair();
    }

    @Nullable
    @Override
    public Metal getMetal(ItemStack itemStack)
    {
        return metal;
    }

    @Override
    public int getSmeltAmount(ItemStack itemStack)
    {
        return 100; //todo change this once recipe is defined
    }

    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack itemStack)
    {
        return Size.NORMAL;
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack itemStack)
    {
        return Weight.MEDIUM;
    }
    //todo
}
