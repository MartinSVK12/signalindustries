package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;


public class TileEntityFluidHatch extends TileEntityTieredContainer {

    public TileEntityFluidHatch(){
        itemContents = new ItemStack[0];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 8000;
        acceptedFluids.get(0).addAll(CatalystFluids.FLUIDS.getAllFluids());
    }

    @Override
    public void tick() {
        super.tick();
        extractFluids();
    }
}
