package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.BlockFluid;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.IFluidInventory;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.ArrayList;

public class InventoryPulsar extends ItemInventoryFluid {

    private ItemStack pulsar;

    public InventoryPulsar(ItemStack pulsar) {
        super(pulsar);
        this.pulsar = pulsar;
        fluidCapacity[0] = 16000;//acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);*/
    }


    public String getInvName() {
        return "The Pulsar";
    }


}
