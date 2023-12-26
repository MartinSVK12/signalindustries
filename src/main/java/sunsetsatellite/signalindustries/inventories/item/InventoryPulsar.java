package sunsetsatellite.signalindustries.inventories.item;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.impl.ItemInventoryFluid;


public class InventoryPulsar extends ItemInventoryFluid {

    private final ItemStack pulsar;

    public InventoryPulsar(ItemStack pulsar) {
        super(pulsar);
        this.pulsar = pulsar;
        fluidCapacity[0] = 16000;
    }

    public String getInvName() {
        return "The Pulsar";
    }


}
