package tfctech.compat.waila;

import java.util.function.Function;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import mcjty.theoneprobe.api.*;
import net.dries007.tfc.util.Helpers;
import tfctech.objects.blocks.devices.*;
import tfctech.objects.tileentities.*;

import static tfctech.TFCTech.MODID;

public final class TOPPlugin implements Function<ITheOneProbe, Void>, IProbeInfoProvider
{
    public static void init()
    {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "tfctech.compat.waila.TOPPlugin");
    }

    @Override
    public String getID()
    {
        return MODID + ":top_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData iProbeHitData)
    {
        Block b = iBlockState.getBlock();
        BlockPos pos = iProbeHitData.getPos();
        if (b instanceof BlockWireDrawBench)
        {
            BlockPos TEPos = pos;
            if (!iBlockState.getValue(BlockWireDrawBench.UPPER))
            {
                TEPos = TEPos.offset(iBlockState.getValue(BlockWireDrawBench.FACING));
            }
            TEWireDrawBench bench = Helpers.getTE(world, TEPos, TEWireDrawBench.class);
            if (bench != null)
            {
                if (bench.getProgress() > 0)
                {
                    IProbeInfo horizontalPane = iProbeInfo.horizontal(iProbeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                    horizontalPane.text((new TextComponentTranslation("waila.tfctech.wiredraw.progress", bench.getProgress())).getFormattedText());
                }
            }
        }
        if (b instanceof BlockFridge)
        {
            BlockPos TEPos = pos;
            if (!iBlockState.getValue(BlockWireDrawBench.UPPER))
            {
                TEPos = TEPos.up();
            }
            TEFridge fridge = Helpers.getTE(world, TEPos, TEFridge.class);
            if (fridge != null)
            {
                IProbeInfo horizontalPane = iProbeInfo.horizontal(iProbeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                horizontalPane.text((new TextComponentTranslation("waila.tfctech.fridge.efficiency", (int) fridge.getEfficiency())).getFormattedText());
                iProbeInfo.progress(fridge.getEnergyStored(), fridge.getEnergyCapacity(),
                        iProbeInfo.defaultProgressStyle()
                                .suffix("RF")
                                .filledColor(0xFF419099)
                                .alternateFilledColor(0xFF43748E)
                                .borderColor(0xFFFFFFFF)
                                .numberFormat(NumberFormat.COMPACT));
            }
        }
        if (b instanceof BlockElectricForge)
        {
            TEElectricForge forge = Helpers.getTE(world, pos, TEElectricForge.class);
            if (forge != null)
            {
                iProbeInfo.progress(forge.getEnergyStored(), forge.getEnergyCapacity(),
                        iProbeInfo.defaultProgressStyle()
                                .suffix("RF")
                                .filledColor(0xFF419099)
                                .alternateFilledColor(0xFF43748E)
                                .borderColor(0xFFFFFFFF)
                                .numberFormat(NumberFormat.COMPACT));
            }
        }
        if (b instanceof BlockInductionCrucible)
        {
            TEInductionCrucible crucible = Helpers.getTE(world, pos, TEInductionCrucible.class);
            if (crucible != null)
            {
                iProbeInfo.progress(crucible.getEnergyStored(), crucible.getEnergyCapacity(),
                        iProbeInfo.defaultProgressStyle()
                                .suffix("RF")
                                .filledColor(0xFF419099)
                                .alternateFilledColor(0xFF43748E)
                                .borderColor(0xFFFFFFFF)
                                .numberFormat(NumberFormat.COMPACT));
            }
        }
        if (b instanceof BlockLatexExtractor)
        {
            TELatexExtractor extractor = Helpers.getTE(world, pos, TELatexExtractor.class);
            if (extractor != null)
            {
                if (extractor.getFluidAmount() > 0)
                {
                    IProbeInfo horizontalPane = iProbeInfo.horizontal(iProbeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                    horizontalPane.text((new TextComponentTranslation("waila.tfctech.latex.quantity", extractor.getFluidAmount())).getFormattedText());
                }
            }
        }
    }

    @Nullable
    @Override
    public Void apply(ITheOneProbe iTheOneProbe)
    {
        iTheOneProbe.registerProvider(this);
        return null;
    }
}
