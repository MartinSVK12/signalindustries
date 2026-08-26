package sunsetsatellite.signalindustries.tiles.machines.multiblocks;

import net.minecraft.core.block.Block;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityGigaTrommel extends TileEntityTieredMultiblock {
    @Override
    public void init(Block<?> block) {
        super.init(block);
        usesEnergy = true;
        usesItemInput = true;
        usesItemOutput = true;
        minimumEnergyTier = Tier.AWAKENED;
        minimumItemInputTier = Tier.AWAKENED;
        minimumItemOutputTier = Tier.AWAKENED;

        recipeGroup = SIRecipes.TROMMEL;

        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("gigaTrommel"));

		baseParallel = 256;
		baseSpeedMultiplier = 5;
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.gigaTrommel";
    }
}
