package sunsetsatellite.signalindustries.invs;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;

import java.util.ArrayList;

public class InventoryDrill extends InventoryItemFluid {
    public InventoryDrill(ItemStack container) {
        super(container);
        contents = new ItemStack[1];
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        acceptedFluids.clear();
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.drill";
    }
}
