package sunsetsatellite.signalindustries.tiles.machines;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

import java.util.ArrayList;

public class TileEntityCrystalCutter extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityCrystalCutter() {
        itemContents = new ItemStack[2];
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 1000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        acceptedFluids.get(1).add(Fluids.WATER);
        energySlot = 0;
        recipeGroup = SIRecipes.CRYSTAL_CUTTER;
        itemInputs = new int[]{0};
        itemOutputs = new int[]{1};
        fluidInputs = new int[]{1};
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
    }

    @Override
    public void tick() {
        fluidCapacity[1] = (int) (1000 * (Math.pow(2, tier.ordinal())));
        super.tick();
    }

    @Override
    public void processItem() {
        super.processItem();
        if (itemContents[itemOutputs[0]].getItem().equals(SIItems.signalumCrystalEmpty)) {
            if (fluidContents[energySlot] != null && fluidContents[energySlot].amount + 1000 <= fluidCapacity[energySlot]) {
                fluidContents[energySlot].amount += 1000;
            } else if (fluidContents[energySlot] == null) {
                fluidContents[energySlot] = new FluidStack(SIFluids.ENERGY, 1000);
            }
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.crystalCutter";
    }
}
