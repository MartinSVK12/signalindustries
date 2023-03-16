package sunsetsatellite.signalindustries.tiles;

import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.blocks.BlockTiered;

public class TileEntityConduit extends TileEntityFluidPipe {
    public String getInvName() {
        return "Signalum Conduit";
    }
    @Override
    public void updateEntity() {
        fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 1000;
        transferSpeed = 20 * ((BlockContainerTiered)getBlockType()).tier.ordinal();
        super.updateEntity();
    }

}
