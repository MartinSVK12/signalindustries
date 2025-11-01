package sunsetsatellite.signalindustries.tiles.machines;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

import java.util.ArrayList;

public class TileEntityBonsaiPot extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityBonsaiPot(){
        itemContents = new ItemStack[4];
        fluidContents = new FluidStack[3];
        fluidCapacity = new int[3];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 2000;
        fluidCapacity[2] = 2000;
        acceptedFluids.clear();
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        acceptedFluids.get(1).add(Fluids.WATER);
        acceptedFluids.get(2).addAll(Fluid.fluidMap.values());
        acceptedFluids.get(2).remove(SIFluids.ENERGY);
        energySlot = 0;
        recipeGroup = SIRecipes.BONSAI_POT;
        itemInputs = new int[]{0};
        fluidInputs = new int[]{1};
        itemOutputs = new int[]{1,2,3};
        fluidOutputs = new int[]{2};
    }

    @Override
    public void tick() {
        super.tick();
        fluidCapacity[0] = 2000 * (tier.ordinal()+1);
        fluidCapacity[1] = 2000 * (tier.ordinal()+1);
        fluidCapacity[2] = 2000 * (tier.ordinal()+1);
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.bonsai";
    }
}
