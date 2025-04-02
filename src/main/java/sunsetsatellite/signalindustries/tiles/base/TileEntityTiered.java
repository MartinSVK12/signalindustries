package sunsetsatellite.signalindustries.tiles.base;

import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicTiered;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityTiered extends TileEntityWithName {
    public Tier tier = Tier.PROTOTYPE;

    @Override
    public void tick() {
        super.tick();
        if(worldObj != null && getBlock() != null){
            tier = Catalyst.blockLogic(getBlock(), BlockLogicTiered.class).tier;
        }
    }
}
