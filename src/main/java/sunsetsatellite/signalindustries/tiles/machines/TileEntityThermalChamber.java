package sunsetsatellite.signalindustries.tiles.machines;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

import java.util.ArrayList;

public class TileEntityThermalChamber extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityThermalChamber() {
        itemContents = new ItemStack[2];
        fluidContents = new FluidStack[3];
        fluidCapacity = new int[3];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 1000;
        fluidCapacity[2] = 1000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        acceptedFluids.get(1).addAll(Fluid.fluidMap.values());
        acceptedFluids.get(1).remove(SIFluids.ENERGY);
        acceptedFluids.get(2).addAll(Fluid.fluidMap.values());
        acceptedFluids.get(2).remove(SIFluids.ENERGY);
        energySlot = 0;
        recipeGroup = SIRecipes.CRUSHER;
        itemInputs = new int[]{0};
        itemOutputs = new int[]{1};
        fluidInputs = new int[]{1};
        fluidOutputs = new int[]{2};
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
        fluidCapacity[2] = (int) (1000 * (Math.pow(2, tier.ordinal())));
        super.tick();
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.thermalChamber";
    }
}
