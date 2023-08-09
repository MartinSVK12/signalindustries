package sunsetsatellite.signalindustries.inventories;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;

public class InventoryHarness extends ItemInventoryFluid {

    private final ItemStack armor;

    public InventoryHarness(ItemStack armor) {
        super(armor);
        this.armor = armor;
        fluidCapacity[0] = 8000;//acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);*/
    }


    public String getInvName() {
        return "Signalum Prototype Harness";
    }


}
