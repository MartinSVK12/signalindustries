package sunsetsatellite.signalindustries.invs;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryBlueprint extends InventoryItemFluid {
    public InventoryBlueprint(ItemStack container) {
        super(container);
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.blueprint";
    }
}
