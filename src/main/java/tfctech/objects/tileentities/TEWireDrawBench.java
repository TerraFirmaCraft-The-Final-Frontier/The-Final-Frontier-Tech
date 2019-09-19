package tfctech.objects.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.te.TEInventory;
import net.dries007.tfc.util.Helpers;
import tfctech.api.recipes.WireDrawingRecipe;
import tfctech.client.TechSounds;
import tfctech.objects.items.metal.ItemTechMetal;
import tfctech.registry.TechRegistries;

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
                return stack.getItem() instanceof ItemTechMetal
                        && ((ItemTechMetal) stack.getItem()).getType() == ItemTechMetal.ItemType.DRAW_PLATE;
            case 1:
                WireDrawingRecipe recipe = TechRegistries.WIRE_DRAWING.getValuesCollection().stream()
                        .filter(x -> x.matches(stack)).findFirst().orElse(null);
                return recipe != null;
        }
        return false;
    }

    public boolean hasDrawPlate()
    {
        return inventory.getStackInSlot(0).getItem() instanceof ItemTechMetal
                && ((ItemTechMetal) inventory.getStackInSlot(0).getItem()).getType() == ItemTechMetal.ItemType.DRAW_PLATE;
    }

    public boolean hasWire()
    {
        WireDrawingRecipe recipe = TechRegistries.WIRE_DRAWING.getValuesCollection().stream()
                .filter(x -> x.matches(inventory.getStackInSlot(1))).findFirst().orElse(null);
        return recipe != null;
    }

    public boolean startWork(EntityPlayer player)
    {
        if (canWork())
        {
            if (progress == 0)
            {
                WireDrawingRecipe recipe = TechRegistries.WIRE_DRAWING.getValuesCollection().stream()
                        .filter(x -> x.matches(inventory.getStackInSlot(1))).findFirst().orElse(null);
                Metal.Tier workableTier = ((ItemTechMetal) inventory.getStackInSlot(0).getItem()).getMetal(inventory.getStackInSlot(0)).getTier();
                if (recipe == null)
                {
                    player.sendStatusMessage(new TextComponentTranslation("tooltip.tfctech.wiredraw.no_recipe"), true);
                    return false;
                }
                else if (!recipe.getTier().isAtMost(workableTier))
                {
                    world.playSound(null, pos, TechSounds.WIREDRAW_TONGS_FALL, SoundCategory.BLOCKS, 1.0F, 2.0F);
                    player.sendStatusMessage(new TextComponentTranslation("tooltip.tfctech.wiredraw.low_tier"), true);
                    return true;
                }
            }
            world.playSound(null, pos, TechSounds.WIREDRAW_DRAWING, SoundCategory.BLOCKS, 1.0F, 1.0F);
            working = true;
            return true;
        }
        return false;
    }

    public boolean insertWire(@Nonnull ItemStack stack)
    {
        if (!hasWire() && hasDrawPlate() && isItemValid(1, stack))
        {
            inventory.insertItem(1, stack.splitStack(1), false);
            setAndUpdateSlots(1);
            return true;
        }
        return false;
    }

    public boolean insertDrawPlate(@Nonnull ItemStack stack)
    {
        if (!hasDrawPlate() && isItemValid(0, stack))
        {
            inventory.insertItem(0, stack.splitStack(1), false);
            setAndUpdateSlots(0);
            return true;
        }
        return false;
    }

    @Nonnull
    public ItemStack extractItem(int slot)
    {
        if (slot < 0 || slot > 1 || (progress > 0 && progress < 100))
        {
            return ItemStack.EMPTY;
        }
        if (slot == 1)
        {
            progress = 0;
        }
        setAndUpdateSlots(slot);
        return inventory.extractItem(slot, 64, false);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        working = nbt.getBoolean("working");
        progress = nbt.getInteger("progress");
        super.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setBoolean("working", working);
        nbt.setInteger("progress", progress);
        return super.writeToNBT(nbt);
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
                    WireDrawingRecipe recipe = TechRegistries.WIRE_DRAWING.getValuesCollection().stream()
                            .filter(x -> x.matches(inventory.getStackInSlot(1))).findFirst().orElse(null);
                    if (recipe != null)
                    {
                        inventory.setStackInSlot(1, recipe.getOutput());
                        setAndUpdateSlots(1);
                    }
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
