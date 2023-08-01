package sunsetsatellite.signalindustries.inventories;

import net.minecraft.src.BlockFluid;
import sunsetsatellite.fluidapi.FluidRegistry;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;

public class TileEntityFluidConduit extends TileEntityFluidPipe {
    public String getInvName() {
        return "Fluid Conduit";
    }

    public TileEntityFluidConduit(){
        acceptedFluids.get(0).clear();
        for (BlockFluid fluid : FluidRegistry.getAllFluids()) {
            if(fluid != SignalIndustries.energyFlowing) {
                acceptedFluids.get(0).add(fluid);
            }
        }
    }

    @Override
    public void updateEntity() {
        if(getBlockType() != null){
            fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 1000;
            transferSpeed = 20 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
            super.updateEntity();
        }
    }

}