package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.BlockFluid;
import net.minecraft.src.Item;
import sunsetsatellite.fluidapi.FluidAPI;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;

import java.util.Map;

public class TileEntityFluidConduit extends TileEntityFluidPipe {
    public String getInvName() {
        return "Fluid Conduit";
    }

    public TileEntityFluidConduit(){
        for (Map.Entry<Item, BlockFluid> entry : FluidAPI.fluidRegistry.fluids.entrySet()) {
            BlockFluid V = entry.getValue();
            if(V != SignalIndustries.energyFlowing) {
                acceptedFluids.get(0).add(V);
            }
        }
    }

    @Override
    public void updateEntity() {
        fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 1000;
        transferSpeed = 20 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
        super.updateEntity();
    }

}
