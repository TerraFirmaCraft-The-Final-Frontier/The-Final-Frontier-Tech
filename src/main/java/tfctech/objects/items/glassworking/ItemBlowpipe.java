package tfctech.objects.items.glassworking;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.dries007.tfc.api.capability.heat.ItemHeatHandler;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.util.calendar.CalendarTFC;
import tfctech.client.TechGuiHandler;
import tfctech.objects.fluids.TechFluids;
import tfctech.objects.items.ItemMiscTech;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemBlowpipe extends ItemMiscTech
{
    public ItemBlowpipe()
    {
        super(Size.LARGE, Weight.LIGHT);
        setMaxStackSize(1);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && !player.isSneaking())
        {
            IItemHeat cap = stack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
            if (cap instanceof BlowpipeCapability && ((BlowpipeCapability) cap).canWork())
            {
                TechGuiHandler.openGui(world, player.getPosition(), player, TechGuiHandler.Type.GLASSWORKING);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new BlowpipeCapability(stack, nbt);
    }

    public static class BlowpipeCapability extends ItemHeatHandler implements ICapabilityProvider, IFluidHandlerItem
    {
        private boolean hasGlass;
        private ItemStack stack;

        BlowpipeCapability(ItemStack stack, @Nullable NBTTagCompound nbt)
        {
            super(nbt, 0.35f, Float.MAX_VALUE);
            this.stack = stack;
        }

        public boolean canWork()
        {
            return hasGlass && getTemperature() >= TechFluids.GLASS_MELT_TEMPERATURE;
        }

        public boolean isSolidified()
        {
            return hasGlass && getTemperature() < TechFluids.GLASS_MELT_TEMPERATURE;
        }

        public void consume()
        {
            this.setTemperature(0);
            this.hasGlass = false;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY
                    || capability == CapabilityItemHeat.ITEM_HEAT_CAPABILITY;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            return hasCapability(capability, facing) ? (T) this : null;
        }

        @Nonnull
        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            float temp = getTemperature();
            nbt.setFloat("heat", temp);
            if (temp <= 0)
            {
                nbt.setLong("ticks", -1);
            }
            else
            {
                nbt.setLong("ticks", this.lastUpdateTick);
            }
            nbt.setBoolean("glass", hasGlass);
            return nbt;
        }

        @Override
        public void deserializeNBT(@Nullable NBTTagCompound nbt)
        {
            if (nbt != null)
            {
                temperature = nbt.getFloat("heat");
                lastUpdateTick = nbt.getLong("ticks");
                hasGlass = nbt.getBoolean("glass");
            }
            else
            {
                temperature = 0;
                lastUpdateTick = CalendarTFC.PLAYER_TIME.getTicks();
                hasGlass = false;
            }
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void addHeatInfo(@Nonnull ItemStack stack, @Nonnull List<String> tooltip)
        {
            super.addHeatInfo(stack, tooltip);
            if (hasGlass)
            {
                tooltip.add(new FluidStack(TechFluids.GLASS.get(), 1).getLocalizedName());
                if (getTemperature() < TechFluids.GLASS_MELT_TEMPERATURE)
                {
                    tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.tfctech.smeltery.solid"));
                }
                else
                {
                    tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.tfctech.smeltery.molten"));
                }
            }
        }

        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            return new FluidTankProperties[] {new FluidTankProperties(this.hasGlass ? new FluidStack(TechFluids.GLASS.get(), 250) : null, 250)};
        }

        @Override
        public int fill(FluidStack fluidStack, boolean doFill)
        {
            if (hasGlass || !fluidStack.getFluid().equals(TechFluids.GLASS.get()))
            {
                return 0;
            }
            else
            {
                if (doFill)
                {
                    hasGlass = true;
                    this.temperature = 1600f;
                    this.lastUpdateTick = CalendarTFC.PLAYER_TIME.getTicks();
                }
                return 250;
            }
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack fluidStack, boolean doDrain)
        {
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxAmount, boolean doDrain)
        {
            return null;
        }

        @Nonnull
        @Override
        public ItemStack getContainer()
        {
            return this.stack;
        }
    }
}
