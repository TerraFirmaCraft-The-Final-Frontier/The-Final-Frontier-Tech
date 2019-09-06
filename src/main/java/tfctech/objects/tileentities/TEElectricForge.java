package tfctech.objects.tileentities;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.dries007.tfc.api.capability.metal.IMetalItem;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.objects.te.TEInventory;

import static tfctech.objects.blocks.devices.BlockElectricForge.LIT;

@SuppressWarnings("WeakerAccess")
public class TEElectricForge extends TEInventory implements ITickable, IEnergyStorage
{
    public static final int SLOT_INPUT_MIN = 0;
    public static final int SLOT_INPUT_MAX = 8;
    public static final int SLOT_EXTRA_MIN = 9;
    public static final int SLOT_EXTRA_MAX = 12;
    public static final int ENERGY_CAPACITY = 10000;
    private HeatRecipe[] cachedRecipes = new HeatRecipe[9];
    private float targetTemperature = 0.0F;
    private int storedEnergy = 0;
    private int litTime = 0; //visual only

    public TEElectricForge()
    {
        super(13);
    }

    @Override
    public void update()
    {
        if (world.isRemote) return;
        IBlockState state = world.getBlockState(pos);
        boolean isLit = state.getValue(LIT);
        int energyUsage = (int) (5 * targetTemperature / 100);
        for (int i = SLOT_INPUT_MIN; i <= SLOT_INPUT_MAX; i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            IItemHeat cap = stack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
            float modifier = stack.getItem() instanceof IMetalItem ? ((IMetalItem) stack.getItem()).getSmeltAmount(stack) / 100.0F : 1.0F;
            if (cap != null)
            {
                // Update temperature of item
                float itemTemp = cap.getTemperature();
                int energy = (int) (energyUsage * modifier);
                if (targetTemperature > itemTemp && extractEnergy(energy, true) == energy)
                {
                    CapabilityItemHeat.addTemp(cap);
                    extractEnergy(energy, false);
                    litTime = 15;
                    if (!isLit)
                    {
                        isLit = true;
                        state = state.withProperty(LIT, true);
                        world.setBlockState(pos, state, 2);
                    }
                }
                handleInputMelting(stack, i);
            }
        }
        if (--litTime <= 0)
        {
            litTime = 0;
            if (isLit)
            {
                state = state.withProperty(LIT, false);
                world.setBlockState(pos, state, 2);
            }
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        int energyReceived = Math.min(ENERGY_CAPACITY - storedEnergy, maxReceive);
        if (!simulate)
            storedEnergy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        int energyExtracted = Math.min(storedEnergy, maxExtract);
        if (!simulate)
            storedEnergy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored()
    {
        return storedEnergy;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return ENERGY_CAPACITY;
    }

    @Override
    public boolean canExtract()
    {
        return false;
    }

    @Override
    public boolean canReceive()
    {
        return true;
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        this.markDirty();
        updateCachedRecipes();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        targetTemperature = nbt.getFloat("targetTemperature");
        storedEnergy = nbt.getInteger("storedEnergy");
        super.readFromNBT(nbt);

        updateCachedRecipes();
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("targetTemperature", targetTemperature);
        nbt.setInteger("storedEnergy", storedEnergy);
        return super.writeToNBT(nbt);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        if (slot <= SLOT_INPUT_MAX)
        {
            return stack.hasCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
        }
        else
        {
            return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null) && stack.hasCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
        }
    }

    public boolean isLit()
    {
        return litTime > 0;
    }

    private void handleInputMelting(ItemStack stack, int index)
    {
        HeatRecipe recipe = cachedRecipes[index];
        IItemHeat cap = stack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);

        if (recipe != null && cap != null && recipe.isValidTemperature(cap.getTemperature()))
        {
            // Handle possible metal output
            FluidStack fluidStack = recipe.getOutputFluid(stack);
            float itemTemperature = cap.getTemperature();
            if (fluidStack != null)
            {
                // Loop through all input slots
                for (int i = SLOT_EXTRA_MIN; i <= SLOT_EXTRA_MAX; i++)
                {
                    // While the fluid is still waiting
                    if (fluidStack.amount <= 0)
                    {
                        break;
                    }
                    // Try an output slot
                    ItemStack output = inventory.getStackInSlot(i);
                    // Fill the fluid
                    IFluidHandler fluidHandler = output.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                    if (fluidHandler != null)
                    {
                        int amountFilled = fluidHandler.fill(fluidStack.copy(), true);
                        if (amountFilled > 0)
                        {
                            fluidStack.amount -= amountFilled;

                            // If the fluid was filled, make sure to make it the same temperature
                            IItemHeat heatHandler = output.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
                            if (heatHandler != null)
                            {
                                heatHandler.setTemperature(itemTemperature);
                            }
                        }
                    }
                }
            }

            // Handle possible item output
            inventory.setStackInSlot(index, recipe.getOutputStack(stack));
        }
    }

    private void updateCachedRecipes()
    {
        for (int i = SLOT_INPUT_MIN; i <= SLOT_INPUT_MAX; ++i)
        {
            this.cachedRecipes[i] = null;
            ItemStack inputStack = this.inventory.getStackInSlot(i);
            if (!inputStack.isEmpty())
            {
                this.cachedRecipes[i] = HeatRecipe.get(inputStack);
            }
        }
    }
}
