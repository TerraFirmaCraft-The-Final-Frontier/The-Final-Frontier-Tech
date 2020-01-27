package tfctech.objects.tileentities;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.dries007.tfc.objects.items.ItemFireStarter;
import net.dries007.tfc.objects.te.TEInventory;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.dries007.tfc.util.fuel.FuelManager;
import tfctech.objects.blocks.devices.BlockSmeltery;

import static net.dries007.tfc.objects.blocks.property.ILightableBlock.LIT;

public class TESmeltery extends TEInventory implements ICalendarTickable
{
    private int form = 1;
    private int burnTicks = 0;
    private long lastPlayerTick = 0;

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
                    if(this.form < 5)
                    {
                        this.form = 5;
                    }
                    world.setBlockState(pos, state.withProperty(LIT, false));
                    //todo check recipe for molten stuff, else do heating recipe
                    for(int i = 0; i < 4; i++)
                    {
                        // todo heating recipe
                    }
                }
            }
        }
    }

    @Override
    public void onCalendarUpdate(long l)
    {

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
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        lastPlayerTick = nbt.getLong("lastPlayerTick");
        burnTicks = nbt.getInteger("burnTicks");
        form = nbt.getInteger("form");
    }

    public boolean itemClick(IBlockState state, ItemStack stack)
    {
        if(OreDictionaryHelper.doesStackMatchOre(stack, "fireClay") && stack.getCount() >= 4 && this.form < 4)
        {
            stack.shrink(4);
            this.form++;
            world.setBlockState(pos, state.withProperty(BlockSmeltery.FORM, form));
            return true;
        }
        else if(this.form >= 4 && !state.getValue(LIT))
        {
            if(hasFuel() && ItemFireStarter.onIgnition(stack))
            {
                world.setBlockState(pos, state.withProperty(LIT, true));
                this.burnTicks = 500; //todo config
                return true;
            }
            else if(stack.getItem() == Items.STICK) // todo change to blowpipe / implement on blowpipe
            {

            }
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
}
