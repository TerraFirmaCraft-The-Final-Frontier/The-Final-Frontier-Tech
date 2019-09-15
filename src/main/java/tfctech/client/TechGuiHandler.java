package tfctech.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import net.dries007.tfc.objects.container.ContainerCrucible;
import net.dries007.tfc.util.Helpers;
import tfctech.TFCTech;
import tfctech.client.gui.GuiElectricForge;
import tfctech.client.gui.GuiInductionCrucible;
import tfctech.objects.container.ContainerElectricForge;
import tfctech.objects.tileentities.TEElectricForge;
import tfctech.objects.tileentities.TEInductionCrucible;

import static tfctech.TFCTech.MODID;

public class TechGuiHandler implements IGuiHandler
{
    public static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(MODID, "textures/gui/elements.png");

    public static void openGui(World world, BlockPos pos, EntityPlayer player, Type type)
    {
        player.openGui(TFCTech.getInstance(), type.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Nullable
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        Type type = Type.valueOf(ID);
        switch (type)
        {
            case ELECTRIC_FORGE:
                TEElectricForge teElectricForge = Helpers.getTE(world, pos, TEElectricForge.class);
                return teElectricForge == null ? null : new ContainerElectricForge(player.inventory, teElectricForge);
            case INDUCTION_CRUCIBLE:
                TEInductionCrucible teInductionCrucible = Helpers.getTE(world, pos, TEInductionCrucible.class);
                return teInductionCrucible == null ? null : new ContainerCrucible(player.inventory, teInductionCrucible);
            default:
                return null;
        }

    }

    @Override
    @Nullable
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        Container container = getServerGuiElement(ID, player, world, x, y, z);
        Type type = Type.valueOf(ID);
        BlockPos pos = new BlockPos(x, y, z);
        switch (type)
        {
            case ELECTRIC_FORGE:
                return new GuiElectricForge(container, player.inventory, Helpers.getTE(world, pos, TEElectricForge.class));
            case INDUCTION_CRUCIBLE:
                return new GuiInductionCrucible(container, player.inventory, Helpers.getTE(world, pos, TEInductionCrucible.class));
            default:
                return null;
        }
    }

    public enum Type
    {
        ELECTRIC_FORGE,
        INDUCTION_CRUCIBLE;

        private static Type[] values = values();

        @Nonnull
        public static Type valueOf(int id)
        {
            while (id >= values.length) id -= values.length;
            while (id < 0) id += values.length;
            return values[id];
        }
    }
}
