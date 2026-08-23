package sunsetsatellite.signalindustries.tiles.machines.simple;


import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;

public class TileEntityInfuser extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityInfuser() {
        itemContents = new ItemStack[2];
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 4000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        acceptedFluids.get(1).addAll(Fluid.fluidMap.values());
        acceptedFluids.get(1).remove(SIFluids.ENERGY);
        itemContents = new ItemStack[3];
        energySlot = 0;
        recipeGroup = SIRecipes.INFUSER;
        itemInputs = new int[]{0, 1};
        itemOutputs = new int[]{2};
        fluidInputs = new int[]{1};
    }

    @Override
    public void init(Block<?> block) {
        super.init(block);
        if (tier == Tier.REINFORCED) {
            fluidCapacity[1] = 16000;
        }
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.infuser";
    }

	@Override
	public void processItem() {
		super.processItem();
		if(hasOutput(0, SIBlocks.glowingObsidian)){
			doWithNearPlayers(8, (P)->P.triggerAchievement(SIAchievements.RELIC));
		}
	}
}
