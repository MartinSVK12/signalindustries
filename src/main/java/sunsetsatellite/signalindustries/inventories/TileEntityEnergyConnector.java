package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;

public class TileEntityEnergyConnector extends TileEntityTieredContainer{

    public TileEntityEnergyConnector(){
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 16000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        extractFluids();
    }
}
