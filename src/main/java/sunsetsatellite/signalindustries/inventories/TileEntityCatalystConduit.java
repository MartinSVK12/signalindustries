package sunsetsatellite.signalindustries.inventories;

import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityCatalystConduit extends TileEntityEnergyConductor {

    public TileEntityCatalystConduit() {
        for (Direction dir : Direction.values()){
            setConnection(dir, Connection.BOTH);
        }
    }

    @Override
    public void tick() {
        if(getBlockType() != null){
            Tier tier = ((ITiered) getBlockType()).getTier();
            setCapacity((int) Math.pow(2,tier.ordinal()) * 1024);
            setTransfer(128 * (tier.ordinal()+1));
        }
        super.tick();
    }
}
