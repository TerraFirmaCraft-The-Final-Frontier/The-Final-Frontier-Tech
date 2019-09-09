package tfctech.objects.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.dries007.tfc.objects.te.TECrucible;
import tfctech.ModConfig;
import tfctech.TFCTech;
import tfctech.objects.storage.MachineEnergyContainer;

import static net.dries007.tfc.api.capability.heat.CapabilityItemHeat.MAX_TEMPERATURE;
import static net.minecraft.block.BlockHorizontal.FACING;

@ParametersAreNonnullByDefault
public class TEInductionCrucible extends TECrucible
{
    private MachineEnergyContainer energyContainer;

    public TEInductionCrucible()
    {
        super();
        energyContainer = new MachineEnergyContainer(ModConfig.DEVICES.inductionCrucibleEnergyCapacity, ModConfig.DEVICES.inductionCrucibleEnergyCapacity, 0);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && facing == world.getBlockState(pos).getValue(FACING)) || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityEnergy.ENERGY && facing == world.getBlockState(pos).getValue(FACING) ? (T) this.energyContainer : super.getCapability(capability, facing);
    }

    public int getEnergyCapacity()
    {
        return energyContainer.getMaxEnergyStored();
    }

    @Override
    public void update()
    {
        if (world.isRemote) return;
        ItemStack stack = inventory.getStackInSlot(SLOT_INPUT);
        IItemHeat cap = stack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
        int energyUsage = ModConfig.DEVICES.inductionCrucibleEnergyConsumption;
        if ((cap != null || this.getAlloy().removeAlloy(1, true) > 0) && energyContainer.consumeEnergy(energyUsage, false))
        {
            this.acceptHeat(MAX_TEMPERATURE);
        }
        super.update();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        energyContainer.deserializeNBT(nbt.getCompoundTag("energyContainer"));
        super.readFromNBT(nbt);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("energyContainer", energyContainer.serializeNBT());
        return super.writeToNBT(nbt);
    }

    @Override
    public int getFieldCount()
    {
        return 2;
    }

    @Override
    public void setField(int index, int value)
    {
        if (index == 0)
        {
            super.setField(index, value);
        }
        else if (index == 1)
        {
            this.energyContainer.setEnergy(value);
        }
        else
        {
            TFCTech.getLog().warn("Invalid field ID {} in TEElectricForge#setField", index);
        }
    }

    @Override
    public int getField(int index)
    {
        if (index == 0)
        {
            return super.getField(index);
        }
        else if (index == 1)
        {
            return this.energyContainer.getEnergyStored();
        }
        else
        {
            TFCTech.getLog().warn("Invalid field ID {} in TEElectricForge#setField", index);
            return 0;
        }
    }
}
