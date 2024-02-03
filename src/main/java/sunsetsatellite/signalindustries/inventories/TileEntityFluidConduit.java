package sunsetsatellite.signalindustries.inventories;


import net.minecraft.core.block.BlockFluid;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;

public class TileEntityFluidConduit extends TileEntityFluidPipe {
    public String getInvName() {
        return "Fluid Conduit";
    }

    public TileEntityFluidConduit(){
        acceptedFluids.get(0).clear();
        for (BlockFluid fluid : CatalystFluids.FLUIDS.getAllFluids()) {
            if(fluid != SignalIndustries.energyFlowing) {
                acceptedFluids.get(0).add(fluid);
            }
        }
    }

    @Override
    public void tick() {
        if(fluidContents[0] != null && fluidContents[0].amount < 0){
            fluidContents[0] = null;
        }
        if(getBlockType() != null){
            fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 1000;
            transferSpeed = 20 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
            super.tick();
        }
    }

}
