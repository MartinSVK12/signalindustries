package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;


public class TileEntityFluidHatch extends TileEntityTieredContainer implements IMultiblockPart {

    public TileEntity connectedTo;

    public TileEntityFluidHatch(){
        itemContents = new ItemStack[0];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 8000;
        acceptedFluids.get(0).addAll(CatalystFluids.CONTAINERS.getAllFluids());
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
