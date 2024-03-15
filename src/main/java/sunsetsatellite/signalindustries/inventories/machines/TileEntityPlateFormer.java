package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.core.block.BlockFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;

public class TileEntityPlateFormer extends TileEntityTieredMachineSimple implements IBoostable {


    public TileEntityPlateFormer(){
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        energySlot = 0;
        recipeGroup = SIRecipes.PLATE_FORMER;
        itemInputs = new int[]{0};
        itemOutputs = new int[]{1};
    }

    @Override
    public void tick() {
        super.tick();
        fluidCapacity[0] = 2000 * (2 * tier.ordinal());
    }

    @Override
    public String getInvName() {
        return "Plate Former";
    }

}
