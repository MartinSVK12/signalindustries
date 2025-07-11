package sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking;

import net.minecraft.core.block.Block;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityWakingCrusher extends TileEntityTieredMultiblock {
    @Override
    public void init(Block<?> block) {
        super.init(block);
        usesEnergy = true;
        usesItemInput = true;
        usesItemOutput = true;
        minimumEnergyTier = Tier.AWAKENED;
        minimumItemInputTier = Tier.REINFORCED;
        minimumItemOutputTier = Tier.REINFORCED;

        recipeGroup = SIRecipes.WAKING_CRUSHER;

        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("wakingCrusher"));
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.wakingCrusher";
    }
}
