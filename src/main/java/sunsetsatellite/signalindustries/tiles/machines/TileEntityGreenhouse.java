package sunsetsatellite.signalindustries.tiles.machines;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

public class TileEntityGreenhouse extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityGreenhouse(){
        itemContents = new ItemStack[3];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        energySlot = 0;
        recipeGroup = SIRecipes.ALLOY_SMELTER;
        itemInputs = new int[]{0,2};
        itemOutputs = new int[]{1};
    }

    @Override
    public void tick() {
        super.tick();
        fluidCapacity[0] = 2000 * (tier.ordinal()+1);
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.greenhouse";
    }
}
