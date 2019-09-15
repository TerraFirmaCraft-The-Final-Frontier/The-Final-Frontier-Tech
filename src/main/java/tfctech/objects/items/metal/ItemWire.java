package tfctech.objects.items.metal;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.api.types.Metal;

public class ItemWire extends ItemTechMetal
{
    public ItemWire(Metal metal, ItemType type)
    {
        super(metal, type);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (stack.getMetadata() > 0)
        {
            String stage = (new TextComponentTranslation("tooltip.tfctech.metalitem.wire.stage", 5 - stack.getMetadata())).getFormattedText();
            tooltip.add(stage);
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
