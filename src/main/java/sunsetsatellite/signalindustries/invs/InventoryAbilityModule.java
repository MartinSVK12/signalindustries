package sunsetsatellite.signalindustries.invs;


import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.util.InventorySerializer;


public class InventoryAbilityModule extends InventoryItemFluid {

    public InventoryAbilityModule(ItemStack armor) {
        super(armor);
        fluidCapacity = new int[0];
        fluidContents = new FluidStack[0];
        contents = new ItemStack[9];
        InventorySerializer.loadInvFromNBT(armor, this, 9, 0);
    }


    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.abilityModule";
    }
}
