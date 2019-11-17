package tfctech.objects.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.Heat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.dries007.tfc.objects.te.TECrucible;
import tfctech.TFCTech;
import tfctech.TechConfig;
import tfctech.client.TechSounds;
import tfctech.client.audio.IMachineSoundEffect;
import tfctech.client.audio.MachineSound;
import tfctech.objects.blocks.devices.BlockElectricForge;
import tfctech.objects.storage.MachineEnergyContainer;

import static net.minecraft.block.BlockHorizontal.FACING;
import static tfctech.objects.blocks.devices.BlockInductionCrucible.LIT;

@ParametersAreNonnullByDefault
public class TEInductionCrucible extends TECrucible implements IMachineSoundEffect
{
    private MachineEnergyContainer energyContainer;
    private int litTime = 0; //Client "effects" only

    public TEInductionCrucible()
    {
        super();
        energyContainer = new MachineEnergyContainer(TechConfig.DEVICES.inductionCrucibleEnergyCapacity, TechConfig.DEVICES.inductionCrucibleEnergyCapacity, 0);
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
        if (world.isRemote)
        {
            if (isPlaying())
            {
                MachineSound sound = new MachineSound(this);
                // Play sound on client side
                if (!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(sound))
                {
                    Minecraft.getMinecraft().getSoundHandler().playSound(sound);
                }
            }
            return;
        }
        IBlockState state = world.getBlockState(pos);
        boolean isLit = state.getValue(LIT);
        ItemStack stack = inventory.getStackInSlot(SLOT_INPUT);
        IItemHeat cap = stack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
        int energyUsage = TechConfig.DEVICES.inductionCrucibleEnergyConsumption;
        if ((cap != null || this.getAlloy().removeAlloy(1, true) > 0) && energyContainer.consumeEnergy(energyUsage, false))
        {
            this.acceptHeat(Heat.maxVisibleTemperature());
            litTime = 15;
            if (!isLit)
            {
                isLit = true;
                state = state.withProperty(BlockElectricForge.LIT, true);
                world.setBlockState(pos, state, 2);
            }
        }
        if (--litTime <= 0)
        {
            litTime = 0;
            if (isLit)
            {
                state = state.withProperty(BlockElectricForge.LIT, false);
                world.setBlockState(pos, state, 2);
            }
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

    @Override
    public SoundEvent getSoundEvent()
    {
        return TechSounds.INDUCTION_WORK;
    }

    @Override
    public boolean isPlaying()
    {
        return !this.isInvalid() && this.hasWorld() && world.getBlockState(pos).getValue(LIT);
    }

    public int getEnergyStored()
    {
        return energyContainer.getEnergyStored();
    }
}
