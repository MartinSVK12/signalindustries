package sunsetsatellite.signalindustries.invs;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryBackpack extends InventoryItemFluid {
    public InventoryBackpack(ItemStack container) {
        super(container);
        if(((ITiered) container.getItem()).getTier() == Tier.REINFORCED){
            contents = new ItemStack[27 * 2];
            fluidContents = new FluidStack[4];
            fluidCapacity = new int[4];
            Arrays.fill(fluidCapacity,4000);
        } else if (((ITiered) container.getItem()).getTier() == Tier.BASIC) {
            contents = new ItemStack[27];
            fluidContents = new FluidStack[2];
            fluidCapacity = new int[2];
            Arrays.fill(fluidCapacity,2000);
        }
        acceptedFluids.clear();
        for (int i = 0; i < fluidContents.length; i++) {
            acceptedFluids.add(new ArrayList<>());
            acceptedFluids.get(i).addAll(Fluid.fluidMap.values());
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.backpack";
    }
}
