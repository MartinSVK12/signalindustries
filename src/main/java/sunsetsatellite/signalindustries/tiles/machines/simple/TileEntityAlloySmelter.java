package sunsetsatellite.signalindustries.tiles.machines.simple;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

public class TileEntityAlloySmelter extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityAlloySmelter() {
        itemContents = new ItemStack[3];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        energySlot = 0;
        recipeGroup = SIRecipes.ALLOY_SMELTER;
        itemInputs = new int[]{0, 2};
        itemOutputs = new int[]{1};
    }

    @Override
    public void tick() {
        super.tick();
        fluidCapacity[0] = 2000 * (tier.ordinal() + 1);
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.alloySmelter";
    }

	@Override
	public void processItem() {
		super.processItem();
		if(hasOutput(0, SIItems.reinforcedCrystalAlloyIngot)){
			doWithNearPlayers(8, (P)->P.triggerAchievement(SIAchievements.KNIGHTS_ALLOY));
		}
	}
}
