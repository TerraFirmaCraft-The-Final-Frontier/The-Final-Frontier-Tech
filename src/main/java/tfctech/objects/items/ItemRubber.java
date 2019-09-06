package tfctech.objects.items;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.objects.items.ItemTFC;

public class ItemRubber extends ItemTFC
{
    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack itemStack)
    {
        return Size.SMALL;
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack itemStack)
    {
        return Weight.LIGHT;
    }
    //todo
}
