package sunsetsatellite.signalindustries.tiles;

import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityTiered extends TileEntityFluidItemContainer {
    public Tier tier = Tier.PROTOTYPE;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj != null && getBlockType() != null){
            tier = ((BlockContainerTiered)getBlockType()).tier;
        }
    }
}
