package tfctech.objects.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.te.TEInventory;
import net.dries007.tfc.util.Helpers;
import tfctech.client.TechSounds;
import tfctech.objects.items.metal.ItemTechMetal;

public class TEWireDrawBench extends TEInventory implements ITickable
{
    private int progress = 0;
    private boolean working = false;

    public TEWireDrawBench()
    {
        super(2);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        switch (slot)
        {
            case 0:
                return stack.getItem() instanceof ItemTechMetal && ((ItemTechMetal) stack.getItem()).getType() == ItemTechMetal.ItemType.DRAW_PLATE;
            case 1:
                return stack.getItem() instanceof ItemTechMetal
                        && ((ItemTechMetal) stack.getItem()).getType() == ItemTechMetal.ItemType.WIRE
                        && stack.getMetadata() > 0;
        }
        return false;
    }

    public boolean hasDrawPlate()
    {
        return inventory.getStackInSlot(0) != ItemStack.EMPTY;
    }

    public boolean hasWire()
    {
        return inventory.getStackInSlot(1) != ItemStack.EMPTY;
    }

    public boolean startWork()
    {
        if (canWork())
        {
            ItemTechMetal drawPlate = (ItemTechMetal) inventory.getStackInSlot(0).getItem();
            ItemTechMetal wire = (ItemTechMetal) inventory.getStackInSlot(1).getItem();
            if (wire.getMetal(inventory.getStackInSlot(1)).getTier().isAtLeast(drawPlate.getMetal(inventory.getStackInSlot(0)).getTier()))
            {
                world.playSound(null, pos, TechSounds.WIREDRAW_DRAWING, SoundCategory.BLOCKS, 1.0F, 1.0F);
                working = true;
            }
            else
            {
                //todo play sound can't turn wire
                //todo show status message couldn't work becuase low tier
            }
            return true;
        }
        return false;
    }

    public boolean insertWire(@Nonnull ItemStack stack)
    {
        if (!hasWire() && hasDrawPlate() && isItemValid(1, stack))
        {
            inventory.insertItem(1, stack.splitStack(1), false);
            return true;
        }
        return false;
    }

    public boolean insertDrawPlate(@Nonnull ItemStack stack)
    {
        if (!hasDrawPlate() && isItemValid(0, stack))
        {
            inventory.insertItem(0, stack.splitStack(1), false);
            return true;
        }
        return false;
    }

    @Nonnull
    public ItemStack extractWire()
    {
        if (!hasWire() || (progress > 0 && progress < 100))
        {
            return ItemStack.EMPTY;
        }
        progress = 0;
        return inventory.extractItem(1, 1, false);
    }

    @Nonnull
    public ItemStack extractDrawPlate()
    {
        if (!hasDrawPlate() || hasWire())
        {
            return ItemStack.EMPTY;
        }
        return inventory.extractItem(0, 1, false);
    }

    @Nullable
    public Metal getDrawPlateMetal()
    {
        ItemStack stack = inventory.getStackInSlot(0);
        if (stack.getItem() instanceof ItemTechMetal)
        {
            return ((ItemTechMetal) stack.getItem()).getMetal(stack);
        }
        return null;
    }

    public int getProgress()
    {
        return this.progress;
    }

    @Nullable
    public Metal getWireMetal()
    {
        ItemStack stack = inventory.getStackInSlot(1);
        if (stack.getItem() instanceof ItemTechMetal)
        {
            return ((ItemTechMetal) stack.getItem()).getMetal(stack);
        }
        return null;
    }

    public EnumFacing getRotation()
    {
        return world.getBlockState(pos).getValue(BlockHorizontal.FACING);
    }

    @Override
    public void update()
    {
        if (working)
        {
            if (++progress % 25 == 0)
            {
                working = false;
                if (progress >= 100)
                {
                    world.playSound(null, pos, TechSounds.WIREDRAW_TONGS_FALL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    Helpers.damageItem(inventory.getStackInSlot(0), 32);
                    ItemStack wire = inventory.getStackInSlot(1);
                    ItemStack workedWire = new ItemStack(wire.getItem(), 1, wire.getMetadata() - 1);
                    inventory.setStackInSlot(1, workedWire);
                    setAndUpdateSlots(1);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    private boolean canWork()
    {
        return hasDrawPlate() && hasWire() && !working && progress < 100;
    }
}
