package sunsetsatellite.signalindustries.inventories.machines.multiblocks.basic;

import net.minecraft.core.block.Block;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityInductionSmelter extends TileEntityTieredMultiblock {

    @Override
    public void init(Block block) {
        usesEnergy = true;
        usesItemInput = true;
        usesItemOutput = true;
        minimumEnergyTier = Tier.BASIC;
        minimumItemInputTier = Tier.BASIC;
        minimumItemOutputTier = Tier.BASIC;

        recipeGroup = SIRecipes.INDUCTION_SMELTER;

        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("basicInductionSmelter"));

        baseParallel = 16;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
