package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;

public class TileEntityBus extends TileEntityTieredContainer{

    public TileEntityBus(){
        itemContents = new ItemStack[9];
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        acceptedFluids.clear();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        extractFluids();
    }
}
