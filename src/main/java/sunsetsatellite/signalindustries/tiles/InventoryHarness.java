package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.ItemStack;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;

public class InventoryHarness extends ItemInventoryFluid {

    private ItemStack armor;

    public InventoryHarness(ItemStack armor) {
        super(armor);
        this.armor = armor;
        fluidCapacity[0] = 8000;//acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);*/
    }


    public String getInvName() {
        return "Signalum Prototype Harness";
    }


}
