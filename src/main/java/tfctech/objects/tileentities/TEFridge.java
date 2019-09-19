package tfctech.objects.tileentities;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.objects.te.TEInventory;

public class TEFridge extends TEInventory implements ITickable
{
    //todo implement missing elements

    private float open = 0.0F;
    private int openingState = 0;
    public TEFridge()
    {
        super(8);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        open = nbt.getFloat("open");
        super.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("open", open);
        return super.writeToNBT(nbt);
    }

    public float getOpen()
    {
        return open;
    }

    public boolean isOpen()
    {
        return open >= 114F;
    }

    public boolean setOpening(boolean value)
    {
        if(openingState != 0)return false; //Already opening/closing
        if(value)
        {
            open = 0F;
            openingState = 1;
        }
        else
        {
            open = 114F;
            openingState = -1;
        }
        return true;
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

    @Override
    public void update()
    {
        if(openingState == 1)
        {
            //opening
            open+= 6F;
            if(open >= 114F)
            {
                open = 114F;
                openingState = 0;
            }
        }
        if(openingState == -1)
        {
            //opening
            open-= 6F;
            if(open <= 0F)
            {
                open = 0F;
                openingState = 0;
            }
        }
    }
}
