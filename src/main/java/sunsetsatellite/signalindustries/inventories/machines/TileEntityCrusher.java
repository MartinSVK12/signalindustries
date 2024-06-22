package sunsetsatellite.signalindustries.inventories.machines;

import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.recipes.SIRecipes;

public class TileEntityCrusher extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityCrusher() {
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add(SIBlocks.energyFlowing);
        energySlot = 0;
        recipeGroup = SIRecipes.CRUSHER;
        itemInputs = new int[]{0};
        itemOutputs = new int[]{1};
    }

    @Override
    public String getInvName() {
        return "Crusher";
    }
}
