package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.FluidRegistry;
import sunsetsatellite.fluidapi.api.FluidStack;

public class TileEntityFluidHatch extends TileEntityTieredContainer{

    public TileEntityFluidHatch(){
        itemContents = new ItemStack[0];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 8000;
        acceptedFluids.get(0).addAll(FluidRegistry.getAllFluids());
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        extractFluids();
    }
}
