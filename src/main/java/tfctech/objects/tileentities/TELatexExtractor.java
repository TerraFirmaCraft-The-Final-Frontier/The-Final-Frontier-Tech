package tfctech.objects.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.Constants;
import net.dries007.tfc.objects.te.ITileFields;
import net.dries007.tfc.objects.te.TEBase;
import net.dries007.tfc.util.calendar.ICalendar;
import tfctech.TFCTech;

import static net.minecraftforge.fluids.Fluid.BUCKET_VOLUME;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TELatexExtractor extends TEBase implements ITickable, ITileFields
{
    private static final int MAX_FLUID = BUCKET_VOLUME;

    private int flowTicks = -1; //-1 No cut, 0 stopped, >= 1 still flowing
    private int fluid = 0;
    private boolean pot = false;
    private boolean base = false;

    public TELatexExtractor()
    {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        flowTicks = nbt.getInteger("flowTicks");
        fluid = nbt.getInteger("fluid");
        pot = nbt.getBoolean("pot");
        base = nbt.getBoolean("base");
        super.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("flowTicks", flowTicks);
        nbt.setInteger("fluid", fluid);
        nbt.setBoolean("pot", pot);
        nbt.setBoolean("base", base);
        return super.writeToNBT(nbt);
    }

    public boolean hasFluid()
    {
        return fluid > 0 && hasPot();
    }

    public boolean hasPot()
    {
        return pot;
    }

    public boolean hasBase()
    {
        return base;
    }

    public int getFluidAmount()
    {
        return Math.min(MAX_FLUID, fluid);
    }

    /**
     * the cut state to determine blockstate
     *
     * @return 0 - Not Cut, 1 - Not flowing, 2 - Flowing
     */
    public int cutState()
    {
        return flowTicks < 0 ? 0 : flowTicks > 0 ? 2 : 1;
    }

    @Nullable
    public IBlockState getBlockState()
    {
        if (!this.hasWorld())
        {
            return null;
        }
        return world.getBlockState(pos);
    }

    @Override
    public int getFieldCount()
    {
        return 4;
    }

    @Override
    public void setField(int index, int value)
    {
        if (index == 0)
        {
            this.flowTicks = value;
        }
        else if (index == 1)
        {
            this.fluid = value;
        }
        else if (index == 2)
        {
            this.pot = value > 0;
        }
        else if (index == 3)
        {
            this.base = value > 0;
        }
        else
        {
            TFCTech.getLog().warn("Invalid field ID {} in TELatexExtractor#setField", index);
        }
    }

    @Override
    public int getField(int index)
    {
        if (index == 0)
        {
            return this.flowTicks;
        }
        else if (index == 1)
        {
            return this.fluid;
        }
        else if (index == 2)
        {
            return this.pot ? 1 : 0;
        }
        else if (index == 3)
        {
            return this.base ? 1 : 0;
        }
        else
        {
            TFCTech.getLog().warn("Invalid field ID {} in TELatexExtractor#setField", index);
            return 0;
        }
    }

    public void makeCut()
    {
        if (flowTicks < 1 && hasPot() && hasBase())
        {
            flowTicks = ICalendar.TICKS_IN_DAY / 2 + Constants.RNG.nextInt(ICalendar.TICKS_IN_DAY * 2);
            //play sound
        }
    }

    public boolean isValidPot(ItemStack pot)
    {
        return pot.getItem() == Items.EGG; //todo change
    }

    public boolean isValidBase(ItemStack base)
    {
        return base.getItem() == Items.BOOK; //todo change
    }

    public void addPot(ItemStack stack)
    {
        if (!hasPot() && hasBase())
        {
            pot = true;
        }
    }

    public void addBase(ItemStack stack)
    {
        if (!hasBase())
        {
            base = true;
        }
    }

    public ItemStack removePot()
    {
        //todo create itemstack with contents
        flowTicks = 0;
        fluid = 0;
        return ItemStack.EMPTY;
    }

    public ItemStack removeBase()
    {
        if (!hasPot())
        {
            //todo remove base only when no pot is here
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void update()
    {
        if (!world.isRemote && flowTicks > 0)
        {
            flowTicks--;
            if (flowTicks % 20 == 0)
            {
                fluid++;
            }
        }
    }
}
