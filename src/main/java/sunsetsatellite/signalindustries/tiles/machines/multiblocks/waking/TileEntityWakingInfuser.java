package sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking;

import net.minecraft.core.block.Block;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityWakingInfuser extends TileEntityTieredMultiblock {
    @Override
    public void init(Block<?> block) {
        super.init(block);
        usesEnergy = true;
        usesItemInput = true;
        usesItemOutput = true;
        usesFluidInput = true;
        minimumFluidInputTier = Tier.REINFORCED;
        minimumEnergyTier = Tier.AWAKENED;
        minimumItemInputTier = Tier.REINFORCED;
        minimumItemOutputTier = Tier.REINFORCED;

        recipeGroup = SIRecipes.WAKING_INFUSER;

        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("wakingInfuser"));
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.wakingInfuser";
    }
}
