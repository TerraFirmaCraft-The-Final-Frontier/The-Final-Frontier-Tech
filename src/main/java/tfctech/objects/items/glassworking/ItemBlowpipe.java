package tfctech.objects.items.glassworking;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Sets;
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
import net.dries007.tfc.objects.fluids.capability.FluidWhitelistHandler;
import net.dries007.tfc.util.calendar.CalendarTFC;
import tfctech.client.TechGuiHandler;
import tfctech.objects.fluids.TechFluids;
import tfctech.objects.items.ItemMiscTech;

@SuppressWarnings("WeakerAccess")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemBlowpipe extends ItemMiscTech
{
    public static final int BLOWPIPE_TANK = 250;

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
        private FluidWhitelistHandler tank;

        BlowpipeCapability(ItemStack stack, @Nullable NBTTagCompound nbt)
        {
            this.heatCapacity = 0.35f;
            this.meltTemp = 1599f;
            this.tank = new FluidWhitelistHandler(stack, BLOWPIPE_TANK, Sets.newHashSet(TechFluids.GLASS.get()));
            deserializeNBT(nbt);
        }

        public boolean canWork()
        {
            FluidStack fluidStack = getFluid();
            return fluidStack != null && getTemperature() + 273 >= fluidStack.getFluid().getTemperature();
        }

        public boolean isSolidified()
        {
            FluidStack fluidStack = getFluid();
            return fluidStack != null && getTemperature() + 273 < fluidStack.getFluid().getTemperature();
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
            FluidStack fluidStack = tank.drain(BLOWPIPE_TANK, false);
            if (fluidStack != null)
            {
                nbt.setTag("tank", fluidStack.writeToNBT(new NBTTagCompound()));
            }
            return nbt;
        }

        @Override
        public void deserializeNBT(@Nullable NBTTagCompound nbt)
        {
            if (nbt != null)
            {
                temperature = nbt.getFloat("heat");
                lastUpdateTick = nbt.getLong("ticks");
                tank.fill(FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("tank")), true);
            }
            else
            {
                temperature = 0;
                lastUpdateTick = CalendarTFC.PLAYER_TIME.getTicks();
            }
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void addHeatInfo(@Nonnull ItemStack stack, @Nonnull List<String> tooltip)
        {
            FluidStack fluid = tank.drain(BLOWPIPE_TANK, false);
            if (fluid != null)
            {
                String fluidDesc = TextFormatting.DARK_GREEN + fluid.getLocalizedName();
                if (isSolidified())
                {
                    fluidDesc += I18n.format("tfc.tooltip.solid");
                }
                else if (canWork())
                {
                    fluidDesc += I18n.format("tfc.tooltip.liquid");
                }
                tooltip.add(fluidDesc);
            }
            super.addHeatInfo(stack, tooltip);
        }

        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            return new FluidTankProperties[] {new FluidTankProperties(tank.drain(BLOWPIPE_TANK, false), BLOWPIPE_TANK)};
        }

        @Override
        public int fill(FluidStack fluidStack, boolean doFill)
        {
            int value = tank.fill(fluidStack, doFill);
            if (doFill && value > 0)
            {
                this.temperature = fluidStack.getFluid().getTemperature(); // giving 273 heat so player has time to craft.
                this.lastUpdateTick = CalendarTFC.PLAYER_TIME.getTicks();
            }
            return value;
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

        public void consumeFluid()
        {
            tank.drain(BLOWPIPE_TANK, true);
            this.setTemperature(0);
        }

        @Nullable
        public FluidStack getFluid()
        {
            return tank.drain(BLOWPIPE_TANK, false);
        }

        @Nonnull
        @Override
        public ItemStack getContainer()
        {
            return this.tank.getContainer();
        }
    }
}
