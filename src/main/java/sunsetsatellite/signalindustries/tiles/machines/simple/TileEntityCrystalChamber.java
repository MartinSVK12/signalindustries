package sunsetsatellite.signalindustries.tiles.machines.simple;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.items.tools.ItemSignalumCrystal;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

import java.util.ArrayList;

public class TileEntityCrystalChamber extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityCrystalChamber() {
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 4000;
        fluidCapacity[1] = 4000;
        itemContents = new ItemStack[3];
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        acceptedFluids.get(1).add(SIFluids.WORLD_RESIN);
        energySlot = 0;
        recipeGroup = SIRecipes.CRYSTAL_CHAMBER;
        itemInputs = new int[]{0, 2};
        itemOutputs = new int[]{1};
        fluidInputs = new int[]{1};
    }

    @Override
    public boolean canProcess() {
        ItemStack c1 = this.itemContents[itemInputs[0]];
        ItemStack c2 = this.itemContents[itemInputs[1]];
        if (c1 != null && c2 != null) {
            if ((c1.getData().getInteger("size") + c2.getData().getInteger("size")) > (8 * tier.ordinal())) {
                return false;
            }
        }
        return super.canProcess();
    }

    @Override
    public void processItem() {
        if (canProcess()) {
            if (this.itemContents[itemInputs[0]] != null && this.itemContents[itemInputs[0]].getItem() instanceof ItemSignalumCrystal &&
                    this.itemContents[itemInputs[1]] != null && this.itemContents[itemInputs[1]].getItem() instanceof ItemSignalumCrystal) {
                int size1 = this.itemContents[itemInputs[0]].getData().getInteger("size");
                int sat1 = this.itemContents[itemInputs[0]].getData().getInteger("saturation");
                int size2 = this.itemContents[itemInputs[1]].getData().getInteger("size");
                int sat2 = this.itemContents[itemInputs[1]].getData().getInteger("saturation");
                super.processItem();
                if (this.itemContents[itemOutputs[0]] != null) {
                    this.itemContents[itemOutputs[0]].getData().putInt("size", size1 + size2);
                    this.itemContents[itemOutputs[0]].getData().putInt("saturation", sat1 + sat2);
                }
            } else {
                super.processItem();
            }
        }
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.crystalChamber";
    }
}
