package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;

public class TileEntityEnergyConnector extends TileEntityTieredContainer implements IMultiblockPart {

    public TileEntity connectedTo;

    public TileEntityEnergyConnector(){
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 16000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        connections.put(Direction.Y_POS, Connection.OUTPUT);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(isConnected()){
            if(connectedTo instanceof TileEntitySignalumReactor){
                if(((TileEntitySignalumReactor) connectedTo).state != TileEntitySignalumReactor.State.INACTIVE){
                    extractFluids();
                }
            }
        }
    }

    @Override
    public boolean isConnected() {
        return connectedTo != null;
    }

    @Override
    public TileEntity getConnectedTileEntity() {
        return connectedTo;
    }

    @Override
    public boolean connect(TileEntity tileEntity) {
        connectedTo = tileEntity;
        return true;
    }
}
