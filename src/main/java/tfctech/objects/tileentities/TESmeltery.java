package tfctech.objects.tileentities;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipeMetalMelting;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.fluids.capability.FluidHandlerSided;
import net.dries007.tfc.objects.fluids.capability.FluidTankCallback;
import net.dries007.tfc.objects.fluids.capability.IFluidHandlerSidedCallback;
import net.dries007.tfc.objects.fluids.capability.IFluidTankCallback;
import net.dries007.tfc.objects.te.TEInventory;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.dries007.tfc.util.fuel.FuelManager;
import tfctech.api.recipes.SmelteryRecipe;
import tfctech.objects.blocks.devices.BlockSmeltery;

import static net.dries007.tfc.objects.blocks.property.ILightableBlock.LIT;

@ParametersAreNonnullByDefault
public class TESmeltery extends TEInventory implements ITickable, ICalendarTickable, IFluidHandlerSidedCallback, IFluidTankCallback
{
    private int form = 5;
    private int burnTicks = 0;
    private long lastPlayerTick = 0;
    private float temp = 0, solidifyTemp = 0;
    private FluidTank tank = new FluidTankCallback(this, 0, 3000);

    public TESmeltery()
    {
        super(8);
    }

    @Override
    public void onLoad()
    {
        IBlockState state = world.getBlockState(pos).withProperty(BlockSmeltery.FORM, form);
        world.setBlockState(pos, state);
    }

    @Override
    public void update()
    {
        ICalendarTickable.super.update();

        if(!world.isRemote)
        {
            IBlockState state = world.getBlockState(pos);
            if(state.getValue(LIT))
            {
                if(--burnTicks <= 0)
                {
                    burnTicks = 0;
                    finishCooking();
                }
            }
            else
            {
                temp = CapabilityItemHeat.adjustTemp(temp, 0.2f, 1);
            }
        }
    }

    @Override
    public boolean canFill(FluidStack fluidStack, EnumFacing enumFacing)
    {
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing enumFacing)
    {
        return true;
    }

    @Override
    public void setAndUpdateFluidTank(int i)
    {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Override
    public void onCalendarUpdate(long l)
    {
        if (!world.isRemote && l > 0)
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getValue(LIT))
            {
                burnTicks -= l;
                if (burnTicks <= 0)
                {
                    long surplusTicks = burnTicks * -1;
                    burnTicks = 0;
                    finishCooking();
                    temp = CapabilityItemHeat.adjustTemp(temp, 0.2f, surplusTicks);
                }
            }
            else
            {
                temp = CapabilityItemHeat.adjustTemp(temp, 0.2f, l);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        lastPlayerTick = nbt.getLong("lastPlayerTick");
        burnTicks = nbt.getInteger("burnTicks");
        form = nbt.getInteger("form");
        temp = nbt.getFloat("temp");
        solidifyTemp = nbt.getFloat("solidifyTemp");
        tank.readFromNBT(nbt.getCompoundTag("tank"));
    }

    @Override
    public long getLastUpdateTick()
    {
        return lastPlayerTick;
    }

    @Override
    public void setLastUpdateTick(long l)
    {
        lastPlayerTick = l;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setLong("lastPlayerTick", lastPlayerTick);
        nbt.setInteger("burnTicks", burnTicks);
        nbt.setInteger("form", form);
        nbt.setFloat("temp", temp);
        nbt.setFloat("solidifyTemp", solidifyTemp);
        nbt.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(nbt);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        // Allow extraction of fluids via front facing
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            //noinspection unchecked
            return (T) new FluidHandlerSided(this, tank, facing);
        }
        return super.getCapability(capability, facing);
    }

    public boolean onIgnite()
    {
        if (hasFuel())
        {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, true));
            this.burnTicks = 500; //todo config
            for (int i = 4; i < 8; i++)
            {
                inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
            markDirty();
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        if(slot < 4)
        {
            return !FuelManager.isItemFuel(stack); // Accept anything non-fuel, destroy not recipe-able after firing
        }
        else
        {
            return FuelManager.isItemFuel(stack);
        }
    }

    private boolean hasFuel()
    {
        for(int i = 4; i < 8; i++)
        {
            if(!FuelManager.isItemFuel(inventory.getStackInSlot(i)))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return slot < 4 ? 64 : 1; //1 fuel
    }

    public boolean addWall()
    {
        if (form < 4)
        {
            form++;
            markDirty();
            return true;
        }
        return false;
    }

    private void finishCooking()
    {
        IBlockState state = world.getBlockState(pos);
        boolean first = false;
        if (this.form < 5)
        {
            this.form = 5;
            first = true;
        }
        world.setBlockState(pos, state.withProperty(LIT, false).withProperty(BlockSmeltery.FORM, this.form));
        if (first)
        {
            return;
        }
        if (tank.drain(1, false) == null)
        {
            List<ItemStack> input = new ArrayList<>();
            for (int i = 0; i < 4; i++)
            {
                input.add(inventory.extractItem(i, 64, false));
            }
            // Do Smeltery Recipes
            SmelteryRecipe recipe = SmelteryRecipe.get(input.toArray(new ItemStack[0]));
            if (recipe != null)
            {
                if (recipe.isFluid())
                {
                    temp = 1600f;
                    tank.fillInternal(recipe.getOutputFluid(input), true);
                    TerraFirmaCraft.getLog().warn("TANK1 = " + tank);
                    TerraFirmaCraft.getLog().warn("TANK2 = " + tank.drain(1, false));
                    TerraFirmaCraft.getLog().warn("FLUID = " + recipe.getOutputFluid(input));
                    solidifyTemp = recipe.getSolidifyTemp();
                }
                else
                {
                    ItemStack[] outputs = recipe.getOutputStack(input);
                    for (int i = 0; i < outputs.length && i < 4; i++)
                    {
                        inventory.insertItem(i, outputs[i], false);
                    }
                    temp = 0;
                    solidifyTemp = 0;
                }
            }
            else
            {
                // Do Heating recipes
                for (int i = 0; i < input.size(); i++)
                {
                    HeatRecipe heatRecipe = HeatRecipe.get(input.get(i), Metal.Tier.TIER_VI);
                    if (heatRecipe != null)
                    {
                        // Do molten fluid like crucible?
                        if (heatRecipe instanceof HeatRecipeMetalMelting)
                        {

                        }
                        else
                        {
                            inventory.insertItem(i, heatRecipe.getOutputStack(input.get(i)), false);
                        }
                    }
                }
            }
        }
        else
        {
            temp = 1600f;
        }
    }
}
