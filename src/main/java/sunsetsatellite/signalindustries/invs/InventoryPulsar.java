package sunsetsatellite.signalindustries.invs;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;

import java.util.ArrayList;

public class InventoryPulsar extends InventoryItemFluid {
    public InventoryPulsar(ItemStack container) {
        super(container);
        contents = new ItemStack[1];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 16000;
        acceptedFluids.clear();
        acceptedFluids.add(new ArrayList<>());
        acceptedFluids.get(0).add(SIFluids.ENERGY);
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.pulsar";
    }
}
