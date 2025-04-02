package sunsetsatellite.signalindustries.tiles.machines;



import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

import java.util.ArrayList;


public class TileEntityStoneworks extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityStoneworks(){
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[3];
        fluidCapacity = new int[3];
        acceptedFluids.clear();
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 2000;
        fluidCapacity[2] = 2000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        acceptedFluids.get(1).add(Fluids.WATER);
        acceptedFluids.get(2).add(Fluids.LAVA);
        energySlot = 0;
        recipeGroup = SIRecipes.STONEWORKS;
        fluidInputs = new int[]{1,2};
        itemOutputs = new int[]{0};
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
        return "container.signalindustries.stoneworks";
    }
}
