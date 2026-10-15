package sunsetsatellite.signalindustries.tiles.machines.multiblocks;

import net.minecraft.core.block.Block;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Arrays;

public class TileEntityInductionSmelter extends TileEntityTieredMultiblock {

    @Override
    public void init(Block<?> block) {
        super.init(block);
        usesEnergy = true;
        usesItemInput = true;
        usesItemOutput = true;
        if(block.getLogic() instanceof ITiered){
            Tier tier = ((ITiered) block.getLogic()).getTier();
            switch (tier) {
                case BASIC:
                    minimumEnergyTier = Tier.BASIC;
                    minimumItemInputTier = Tier.BASIC;
                    minimumItemOutputTier = Tier.BASIC;

                    recipeGroup = SIRecipes.INDUCTION_SMELTER;

                    multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("basicInductionSmelter"));

                    baseParallel = 16;
                    break;
                case REINFORCED:
                    minimumEnergyTier = Tier.REINFORCED;
                    minimumItemInputTier = Tier.REINFORCED;
                    minimumItemOutputTier = Tier.REINFORCED;

                    recipeGroup = SIRecipes.INDUCTION_SMELTER;

                    multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("reinforcedInductionSmelter"));

                    baseParallel = 32;
                    break;
            }
        }

    }

    @Override
    public void tick() {
		if(itemInput != null){
			if (Arrays.stream(itemInput.itemContents).anyMatch(item -> item != null && item.getItem().equals(SIItems.heatingCoil))) {
				recipeGroup = SIRecipes.INDUCTION_SMELTER_BLAST;
			} else {
				recipeGroup = SIRecipes.INDUCTION_SMELTER;
			}
		}
        super.tick();
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.inductionSmelter";
    }
}
