package sunsetsatellite.signalindustries.tiles.machines.simple;


import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

import java.util.ArrayList;
import java.util.Arrays;


public class TileEntityCentrifuge extends TileEntityTieredMachineSimple implements IBoostable {
    public TileEntityCentrifuge() {
        itemContents = new ItemStack[2];
        fluidContents = new FluidStack[5];
        fluidCapacity = new int[5];
        Arrays.fill(fluidCapacity, 8000);
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.BURNT_ENERGY);
        acceptedFluids.get(1).add(SIFluids.BURNT_ENERGY);
        acceptedFluids.get(2).add(SIFluids.BURNT_ENERGY);
        acceptedFluids.get(3).add(SIFluids.BURNT_ENERGY);

        acceptedFluids.get(4).add(SIFluids.ENERGY);

        energySlot = 4;
        itemOutputs = new int[]{0};
        fluidInputs = new int[]{0, 1, 2, 3};
        recipeGroup = SIRecipes.CENTRIFUGE;

    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.centrifuge";
    }
}
