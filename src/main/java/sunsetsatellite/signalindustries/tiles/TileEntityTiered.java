package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.Block;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.util.Tiers;

public class TileEntityTiered extends TileEntityFluidItemContainer {
    public Tiers tier = Tiers.PROTOTYPE;

    @Override
    public void updateEntity() {
        if(worldObj != null){
            tier = ((BlockContainerTiered)getBlockType()).tier;
        }
    }
}
