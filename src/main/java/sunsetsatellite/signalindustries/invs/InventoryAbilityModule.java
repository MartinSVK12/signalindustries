package sunsetsatellite.signalindustries.invs;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.util.InventorySerializer;

import java.util.ArrayList;


public class InventoryAbilityModule extends InventoryItemFluid {

    public InventoryAbilityModule(ItemStack armor) {
        super(armor);
        fluidCapacity = new int[0];
        fluidContents = new FluidStack[0];
        contents = new ItemStack[9];
        InventorySerializer.loadInvFromNBT(armor,this,9,0);
    }


    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.abilityModule";
    }
}
