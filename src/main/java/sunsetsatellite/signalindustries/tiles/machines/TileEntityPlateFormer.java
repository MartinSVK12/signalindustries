package sunsetsatellite.signalindustries.tiles.machines;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

public class TileEntityPlateFormer extends TileEntityTieredMachineSimple implements IBoostable {


    public TileEntityPlateFormer() {
        itemContents = new ItemStack[2];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        energySlot = 0;
        recipeGroup = SIRecipes.PLATE_FORMER;
        itemInputs = new int[]{0};
        itemOutputs = new int[]{1};
    }

    @Override
    public void tick() {
        super.tick();
        fluidCapacity[0] = (int) (2000 * (Math.pow(2, tier.ordinal())));
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.plateFormer";
    }
}
