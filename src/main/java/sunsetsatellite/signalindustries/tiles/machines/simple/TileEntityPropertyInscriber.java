package sunsetsatellite.signalindustries.tiles.machines.simple;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.items.tools.ItemSignalumCrystal;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

import java.util.ArrayList;

public class TileEntityPropertyInscriber extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityPropertyInscriber() {
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 4000;
        itemContents = new ItemStack[4];
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        energySlot = 0;
        recipeGroup = SIRecipes.PROPERTY_INSCRIBER;
        itemInputs = new int[]{0, 1, 2};
        itemOutputs = new int[]{3};
    }

    @Override
    public boolean canProcess() {
        return super.canProcess();
    }

    @Override
    public void processItem() {
		super.processItem();
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.propertyInscriber";
    }
}
