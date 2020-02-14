package tfctech.objects.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import net.dries007.tfc.Constants;
import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.objects.recipes.RecipeUtils;
import tfctech.objects.items.ceramics.ItemTechMold;
import tfctech.objects.items.glassworking.ItemGlassMolder;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;

@SuppressWarnings("WeakerAccess")
public class UnmoldGlassRecipe extends ShapelessOreRecipe
{
    /* This is return chance, not break chance */
    private float chance;

    public UnmoldGlassRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result, float chance)
    {
        super(group, input, result);
        this.chance = chance;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv)
    {
        // Return empty molds
        for (int slot = 0; slot < inv.getSizeInventory(); slot++)
        {
            ItemStack stack = inv.getStackInSlot(slot);
            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof ItemTechMold)
                {
                    // No need to check for the mold, as it has already been checked earlier
                    EntityPlayer player = ForgeHooks.getCraftingPlayer();
                    if (!player.world.isRemote)
                    {
                        if (Constants.RNG.nextFloat() <= chance)
                        {
                            // This can't use the remaining items, because vanilla doesn't sync them on crafting, thus it gives a desync error
                            // To fix: ContainerWorkbench#onCraftMatrixChanged needs to call Container#detectAndSendChanges
                            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(stack.getItem()));
                        }
                        else
                        {
                            player.world.playSound(null, player.getPosition(), TFCSounds.CERAMIC_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        }
                    }
                }
            }
        }
        return super.getRemainingItems(inv);
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack moldStack = null;
        for (int slot = 0; slot < inv.getSizeInventory(); slot++)
        {
            ItemStack stack = inv.getStackInSlot(slot);
            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof ItemGlassMolder)
                {
                    ItemGlassMolder tmp = ((ItemGlassMolder) stack.getItem());
                    if (moldStack == null)
                    {
                        moldStack = stack;
                    }
                    else
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    return ItemStack.EMPTY;
                }
            }
        }
        if (moldStack != null)
        {
            IFluidHandler moldCap = moldStack.getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null);
            if (moldCap instanceof ItemGlassMolder.GlassMolderCapability)
            {
                ItemGlassMolder.GlassMolderCapability cap = (ItemGlassMolder.GlassMolderCapability) moldCap;
                if (cap.isSolidified())
                {
                    return getRecipeOutput();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world)
    {
        boolean foundMold = false;
        for (int slot = 0; slot < inv.getSizeInventory(); slot++)
        {
            ItemStack stack = inv.getStackInSlot(slot);
            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof ItemGlassMolder)
                {
                    ItemGlassMolder moldItem = ((ItemGlassMolder) stack.getItem());
                    IFluidHandlerItem cap = stack.getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null);

                    if (cap instanceof ItemGlassMolder.GlassMolderCapability)
                    {
                        ItemGlassMolder.GlassMolderCapability moldHandler = (ItemGlassMolder.GlassMolderCapability) cap;
                        if (moldHandler.isSolidified())
                        {
                            if (!foundMold)
                            {
                                foundMold = true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }
        return foundMold;
    }

    @Override
    @Nonnull
    public String getGroup()
    {
        return group == null ? "" : group.toString();
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return true;
    }

    @SuppressWarnings("unused")
    public static class Factory implements IRecipeFactory
    {
        @Override
        public IRecipe parse(final JsonContext context, final JsonObject json)
        {
            final NonNullList<Ingredient> ingredients = RecipeUtils.parseShapeless(context, json);
            final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
            final String group = JsonUtils.getString(json, "group", "");

            //Chance of getting the mold back
            float chance = 0;
            if (JsonUtils.hasField(json, "chance"))
            {
                chance = JsonUtils.getFloat(json, "chance");
            }
            return new UnmoldGlassRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, result, chance);
        }
    }
}