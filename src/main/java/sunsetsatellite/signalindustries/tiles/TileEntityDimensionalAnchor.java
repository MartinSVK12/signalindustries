package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.BlockFluid;
import net.minecraft.src.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IMultiblock;
import sunsetsatellite.signalindustries.util.Multiblock;

import java.util.ArrayList;

public class TileEntityDimensionalAnchor extends TileEntityTieredMachine implements IMultiblock {

    public Multiblock multiblock;
    public TileEntityDimensionalAnchor(){
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 8000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        itemContents = new ItemStack[1];
        multiblock = Multiblock.multiblocks.get("dimensionalAnchor");
    }

    @Override
    public Multiblock getMultiblock() {
        return multiblock;
    }
}
