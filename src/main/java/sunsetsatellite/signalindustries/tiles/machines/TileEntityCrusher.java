package sunsetsatellite.signalindustries.tiles.machines;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

public class TileEntityCrusher extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityCrusher() {
        itemContents = new ItemStack[2];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        energySlot = 0;
        recipeGroup = SIRecipes.CRUSHER;
        itemInputs = new int[]{0};
        itemOutputs = new int[]{1};
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.crusher";
    }
}
