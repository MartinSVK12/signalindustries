package sunsetsatellite.signalindustries.inventories.item;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.impl.ItemInventoryFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.Arrays;


public class InventoryBackpack extends ItemInventoryFluid {

    private final ItemStack backpack;

    public InventoryBackpack(ItemStack backpack) {
        super(backpack);
        this.backpack = backpack;
        if(backpack.getItem() != null && ((ITiered)backpack.getItem()).getTier() == Tier.REINFORCED){
            contents = new ItemStack[27 * 2];
            fluidContents = new FluidStack[4];
            fluidCapacity = new int[4];
            Arrays.fill(fluidCapacity,4000);
        } else if (backpack.getItem() != null && ((ITiered) backpack.getItem()).getTier() == Tier.BASIC) {
            contents = new ItemStack[27];
            fluidContents = new FluidStack[2];
            fluidCapacity = new int[2];
            Arrays.fill(fluidCapacity,2000);
        }
        acceptedFluids.clear();
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        for (BlockFluid fluid : CatalystFluids.CONTAINERS.getAllFluids()) {
            if(fluid != SIBlocks.energyFlowing) {
                acceptedFluids.get(0).add(fluid);
            }
        }
    }

    public String getInvName() {
        return "Backpack";
    }


}
