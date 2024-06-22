package sunsetsatellite.signalindustries.inventories;


import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;

public class TileEntityConduit extends TileEntityFluidPipe {
    public String getInvName() {
        return "Signalum Conduit";
    }

    public TileEntityConduit(){
        acceptedFluids.get(0).clear();
        acceptedFluids.get(0).add(SIBlocks.energyFlowing);
    }

    @Override
    public void tick() {
        if(fluidContents[0] != null && fluidContents[0].amount < 0){
            fluidContents[0] = null;
        }
        if(getBlockType() != null){
            fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 1000;
            transferSpeed = 20 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
        }
        super.tick();
    }

    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }
}
