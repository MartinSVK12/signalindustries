package sunsetsatellite.signalindustries.tiles.machines.simple;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

public class TileEntitySITrommel extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntitySITrommel() {
        itemContents = new ItemStack[5];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        energySlot = 0;
        recipeGroup = SIRecipes.TROMMEL;
        itemInputs = new int[]{0, 1, 2, 3};
        itemOutputs = new int[]{4};
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.trommel";
    }
}
