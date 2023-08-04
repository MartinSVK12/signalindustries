package sunsetsatellite.signalindustries.inventories;


import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;

public class TileEntityConduit extends TileEntityFluidPipe {
    public String getInvName() {
        return "Signalum Conduit";
    }

    public TileEntityConduit(){
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }

    @Override
    public void updateEntity() {
        fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 1000;
        transferSpeed = 20 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
        super.updateEntity();
    }

}
