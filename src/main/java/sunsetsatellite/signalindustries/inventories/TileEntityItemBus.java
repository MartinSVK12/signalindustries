package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;

public class TileEntityItemBus extends TileEntityTieredContainer implements IMultiblockPart {

    public TileEntity connectedTo;

    public TileEntityItemBus(){
        itemContents = new ItemStack[9];
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        acceptedFluids.clear();
    }

    @Override
    public void tick() {
        super.tick();
        extractFluids();
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
