package sunsetsatellite.signalindustries.inventories.base;

import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Map;

public class TileEntityTieredContainer extends TileEntityCoverable {
    public Tier tier = Tier.PROTOTYPE;

    @Override
    public void tick() {
        super.tick();
        if(worldObj != null && getBlockType() != null){
            tier = ((BlockContainerTiered)getBlockType()).tier;
        }
    }

    public void extractFluids(){
        for (Map.Entry<Direction, Connection> e : fluidConnections.entrySet()) {
            Direction dir = e.getKey();
            TileEntity tile = dir.getTileEntity(worldObj,this);
            if (tile instanceof TileEntityFluidPipe) {
                moveFluids(dir, (TileEntityFluidPipe) tile);
                ((TileEntityFluidPipe) tile).rememberTicks = 100;
            }
        }
    }

    public Tier getTier() {
        return tier;
    }
}
