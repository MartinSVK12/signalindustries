package sunsetsatellite.signalindustries.invs;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIFluids;

import java.util.ArrayList;


public class InventoryHarness extends InventoryItemFluid {

    private final ItemStack armor;

    public InventoryHarness(ItemStack armor) {
        super(armor);
        this.armor = armor;
        fluidCapacity[0] = 8000;
        acceptedFluids.clear();
        acceptedFluids.add(new ArrayList<>());
        acceptedFluids.get(0).add(SIFluids.ENERGY);
    }


    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.harness";
    }
}
