package tfctech.objects.storage;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.dries007.tfc.api.capability.metal.IMetalItem;

/**
 * Energy storage for machines
 * Energy can only be input from outside and only be extracted internally
 */
@SuppressWarnings("WeakerAccess")
public class MachineEnergyContainer extends EnergyStorage implements INBTSerializable<NBTTagCompound>
{

    public MachineEnergyContainer(@Nullable NBTTagCompound nbt)
    {
        this(10000, 10000, 0);
        deserializeNBT(nbt);
    }

    public MachineEnergyContainer(int capacity, int maxReceive, int energy)
    {
        super(capacity, maxReceive, 0, energy);
    }

    /**
     * Tries to consume energy from storage.
     *
     * @param amount   the amount to consume
     * @param simulate if this is a simulation, the energy isn't really consumed
     * @return return true if the amount could be consumed
     */
    public boolean consumeEnergy(int amount, boolean simulate)
    {
        if (amount > energy)
            return false;
        if (!simulate)
            energy -= amount;
        return true;
    }

    /**
     * Use only in client!
     * this is here to update internal energy for GUI purposes
     *
     * @param amount the value to set the internal energy to
     */
    @SideOnly(Side.CLIENT)
    public void setEnergy(int amount)
    {
        energy = amount;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("energy", energy);
        return nbt;
    }

    @Override
    public void deserializeNBT(@Nullable NBTTagCompound nbt)
    {
        if (nbt != null)
        {
            energy = nbt.getInteger("energy");
        }
    }
}
