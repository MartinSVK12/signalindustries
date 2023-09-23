package sunsetsatellite.signalindustries.inventories;

import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityTiered extends TileEntityWithName {
    public Tier tier = Tier.PROTOTYPE;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj != null && getBlockType() != null){
            tier = ((BlockContainerTiered)getBlockType()).tier;
        }
    }
}
