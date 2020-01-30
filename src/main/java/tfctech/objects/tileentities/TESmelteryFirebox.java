package tfctech.objects.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.objects.te.ITileFields;
import net.dries007.tfc.objects.te.TEInventory;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.dries007.tfc.util.fuel.Fuel;
import net.dries007.tfc.util.fuel.FuelManager;

import static net.dries007.tfc.objects.blocks.property.ILightableBlock.LIT;

@SuppressWarnings("WeakerAccess")
public class TESmelteryFirebox extends TEInventory implements ITickable, ICalendarTickable, ITileFields
{
    private float temperature;
    private float burnTemperature;
    private int burnTicks;
    private long lastPlayerTick;

    private int reload;

    public TESmelteryFirebox()
    {
        super(4);
        temperature = 0;
        burnTemperature = 0;
        burnTicks = 0;
        lastPlayerTick = CalendarTFC.PLAYER_TIME.getTicks();
        reload = 0;
    }

    @Override
    public int getFieldCount()
    {
        return 1;
    }

    @Override
    public void setField(int index, int value)
    {
        temperature = value;
    }

    @Override
    public int getField(int i)
    {
        return (int) temperature;
    }

    @Override
    public void update()
    {
        ICalendarTickable.super.update();
        if (!world.isRemote)
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getValue(LIT))
            {
                if (--burnTicks <= 0)
                {
                    consumeFuel();
                }
            }
            else
            {
                burnTemperature = 0;
                burnTicks = 0;
            }
            if (temperature > 0 || burnTemperature > 0)
            {
                // Update temperature
                if (temperature != burnTemperature)
                {
                    float delta = (float) ConfigTFC.GENERAL.temperatureModifierHeating;
                    temperature = CapabilityItemHeat.adjustTempTowards(temperature, burnTemperature, delta, delta);
                }
            }
        }
        if (reload++ >= 20)
        {
            reload = 0;
            BlockPos cauldronPos = pos.up().offset(world.getBlockState(pos).getValue(BlockHorizontal.FACING).getOpposite());
            TESmelteryCauldron teCauldron = Helpers.getTE(world, cauldronPos, TESmelteryCauldron.class);
            if (teCauldron != null)
            {
                teCauldron.setFireboxPos(pos);
            }
        }
    }

    public float getTemperature()
    {
        return temperature;
    }

    public void setTemperature(float temperature)
    {
        this.temperature = temperature;
    }

    public boolean onIgnite()
    {
        IBlockState state = world.getBlockState(pos);
        if (!state.getValue(LIT))
        {
            consumeFuel();
            return state.getValue(LIT);
        }
        return false;
    }

    @Override
    public void onCalendarUpdate(long deltaPlayerTicks)
    {
        IBlockState state = world.getBlockState(pos);
        if (!state.getValue(LIT))
        {
            return;
        }
        while (deltaPlayerTicks > 0)
        {
            if (burnTicks > deltaPlayerTicks)
            {
                burnTicks -= deltaPlayerTicks;
                float delta = (float) ConfigTFC.GENERAL.temperatureModifierHeating * deltaPlayerTicks;
                temperature = CapabilityItemHeat.adjustTempTowards(temperature, burnTemperature, delta, delta);
                deltaPlayerTicks = 0;
            }
            else
            {
                deltaPlayerTicks -= burnTicks;
                float delta = (float) ConfigTFC.GENERAL.temperatureModifierHeating * burnTicks;
                temperature = CapabilityItemHeat.adjustTempTowards(temperature, burnTemperature, delta, delta);
                consumeFuel();
            }
        }
    }

    @Override
    public long getLastUpdateTick()
    {
        return lastPlayerTick;
    }

    @Override
    public void setLastUpdateTick(long ticks)
    {
        lastPlayerTick = ticks;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return FuelManager.isItemFuel(stack);
    }

    @Override
    public void deserializeNBT(@Nullable NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        if (nbt != null)
        {
            temperature = nbt.getFloat("temperature");
            burnTemperature = nbt.getFloat("burnTemperature");
            burnTicks = nbt.getInteger("burnTicks");
            lastPlayerTick = nbt.getLong("lastPlayerTick");
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = super.serializeNBT();
        nbt.setFloat("temperature", temperature);
        nbt.setInteger("burnTicks", burnTicks);
        nbt.setFloat("burnTemperature", burnTemperature);
        nbt.setLong("lastPlayerTick", lastPlayerTick);
        return nbt;
    }

    private void consumeFuel()
    {
        burnTicks = 0;
        IBlockState state = world.getBlockState(pos);
        for (int i = 0; i < 4; i++)
        {
            ItemStack stack = inventory.extractItem(i, 1, true);
            if (!stack.isEmpty())
            {
                Fuel fuel = FuelManager.getFuel(stack);
                burnTicks = fuel.getAmount();
                burnTemperature = fuel.getTemperature();
                world.setBlockState(pos, state.withProperty(LIT, true));
                break;
            }
        }
        // Didn't find a fuel to consume
        if (burnTicks <= 0)
        {
            world.setBlockState(pos, state.withProperty(LIT, false));
            burnTicks = 0;
            burnTemperature = 0;
        }
    }
}
