package tfctech.objects.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.IFood;
import net.dries007.tfc.objects.te.TEInventory;
import net.dries007.tfc.util.Helpers;
import tfctech.TFCTech;
import tfctech.TechConfig;
import tfctech.network.PacketTileEntityUpdate;
import tfctech.objects.blocks.devices.BlockFridge;
import tfctech.objects.storage.MachineEnergyContainer;
import tfctech.registry.TechFoodTraits;

import static tfctech.objects.blocks.devices.BlockFridge.UPPER;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEFridge extends TEInventory implements ITickable
{
    private float open = 0.0F;
    private int openingState = 0;

    private float efficiency = 0.0F;
    private MachineEnergyContainer energyContainer;

    private int applyTrait = 0;

    public TEFridge()
    {
        super(8);
        energyContainer = new MachineEnergyContainer(TechConfig.DEVICES.fridgeEnergyCapacity, TechConfig.DEVICES.fridgeEnergyCapacity, 0);
    }

    public boolean isMainBlock()
    {
        if (!hasWorld())
        {
            return false;
        }
        return world.getBlockState(pos).getValue(UPPER);
    }

    public ItemStack insertItem(int slot, ItemStack stack)
    {
        ItemStack output = inventory.insertItem(slot, stack, false);
        setAndUpdateSlots(slot);
        return output;
    }

    public ItemStack extractItem(int slot)
    {
        ItemStack stack = inventory.extractItem(slot, 64, false);
        IFood cap = stack.getCapability(CapabilityFood.CAPABILITY, null);
        if (cap != null)
        {
            CapabilityFood.removeTrait(cap, TechFoodTraits.FROZEN);
            CapabilityFood.removeTrait(cap, TechFoodTraits.COLD);
        }
        setAndUpdateSlots(slot);
        return stack;
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        TFCTech.getNetwork().sendToAllTracking(new PacketTileEntityUpdate(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
        super.setAndUpdateSlots(slot);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        open = nbt.getFloat("open");
        openingState = nbt.getInteger("openingState");
        efficiency = nbt.getFloat("efficiency");
        energyContainer.deserializeNBT(nbt.getCompoundTag("energyContainer"));
        super.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("open", open);
        nbt.setInteger("openingState", openingState);
        nbt.setFloat("efficiency", efficiency);
        nbt.setTag("energyContainer", energyContainer.serializeNBT());
        return super.writeToNBT(nbt);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (!isMainBlock())
        {
            TEFridge te = Helpers.getTE(world, pos.up(), TEFridge.class);
            if (te != null)
            {
                return te.hasCapability(capability, facing);
            }
            else
            {
                return false;
            }
        }
        return (capability == CapabilityEnergy.ENERGY && facing == this.getRotation().getOpposite()) || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (!isMainBlock())
        {
            TEFridge te = Helpers.getTE(world, pos.up(), TEFridge.class);
            if (te != null)
            {
                return te.getCapability(capability, facing);
            }
            else
            {
                return null;
            }
        }
        return capability == CapabilityEnergy.ENERGY && facing == this.getRotation().getOpposite() ? (T) this.energyContainer : super.getCapability(capability, facing);
    }

    public ItemStack getSlot(int slot)
    {
        return inventory.extractItem(slot, 64, true);
    }

    public float getOpen()
    {
        return open;
    }

    public boolean isOpen()
    {
        return open >= 114F;
    }

    public boolean hasStack(int slot)
    {
        return inventory.extractItem(slot, 64, true) != ItemStack.EMPTY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    public EnumFacing getRotation()
    {
        return world.getBlockState(pos).getValue(BlockHorizontal.FACING);
    }

    public boolean isAnimating()
    {
        return openingState != 0;
    }

    @Nullable
    public Vec3d[] getItems()
    {
        if (!hasWorld())
        {
            return null;
        }
        return BlockFridge.getItems(this.getRotation());
    }

    public boolean setOpening(boolean value)
    {
        if (openingState != 0) return false; //Already opening/closing
        if (!world.isRemote)
        {
            if (value)
            {
                open = 0F;
                openingState = 1;
            }
            else
            {
                open = 114F;
                openingState = -1;
            }
            setAndUpdateSlots(0);
        }
        return true;
    }

    @Override
    public void update()
    {
        if (!isMainBlock()) return;
        if (openingState == 1)
        {
            //opening
            open += 6F;
            if (open >= 114F)
            {
                open = 114F;
                openingState = 0;
            }
        }
        if (openingState == -1)
        {
            //opening
            open -= 6F;
            if (open <= 0F)
            {
                open = 0F;
                openingState = 0;
            }
        }
        if (!world.isRemote)
        {
            int consumption = (int) Math.max(1.0D, TechConfig.DEVICES.fridgeEnergyConsumption * 10.0D);
            if (efficiency >= 75.0F)
            {
                consumption = (int) Math.max(1.0D, consumption / 4.0D);
            }
            boolean updateEfficiency = energyContainer.consumeEnergy(consumption, true);
            if (this.isOpen())
            {
                efficiency -= (100.0F / (TechConfig.DEVICES.fridgeLoseEfficiency * 6000.0F)); //5 Minutes to 0 default
                if (efficiency <= 0) efficiency = 0;
            }
            else if (updateEfficiency)
            {
                efficiency += (100.0F / (TechConfig.DEVICES.fridgeEfficiency * 36000.0F)); //30 Minutes to 100 default
                if (efficiency >= 100) efficiency = 100;
            }
            if (++applyTrait >= 100)
            {
                applyTrait = 0;
                if (efficiency >= 80.0F)
                {
                    for (int i = 0; i < inventory.getSlots(); i++)
                    {
                        IFood cap = inventory.getStackInSlot(i).getCapability(CapabilityFood.CAPABILITY, null);
                        if (cap != null)
                        {
                            CapabilityFood.removeTrait(cap, TechFoodTraits.COLD);
                            CapabilityFood.applyTrait(cap, TechFoodTraits.FROZEN);
                        }
                    }
                }
                else if (efficiency >= 30.0F)
                {
                    for (int i = 0; i < inventory.getSlots(); i++)
                    {
                        IFood cap = inventory.getStackInSlot(i).getCapability(CapabilityFood.CAPABILITY, null);
                        if (cap != null)
                        {
                            CapabilityFood.removeTrait(cap, TechFoodTraits.FROZEN);
                            CapabilityFood.applyTrait(cap, TechFoodTraits.COLD);
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < inventory.getSlots(); i++)
                    {
                        IFood cap = inventory.getStackInSlot(i).getCapability(CapabilityFood.CAPABILITY, null);
                        if (cap != null)
                        {
                            CapabilityFood.removeTrait(cap, TechFoodTraits.FROZEN);
                            CapabilityFood.removeTrait(cap, TechFoodTraits.COLD);
                        }
                    }
                }
            }
        }
    }
}
