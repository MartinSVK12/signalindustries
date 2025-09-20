package sunsetsatellite.signalindustries.tiles.machines.multiblocks;

import net.minecraft.core.block.Block;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityLaserDrill extends TileEntityTieredMultiblock {
    @Override
    public void init(Block<?> block) {
        super.init(block);
        usesEnergy = true;
        usesItemOutput = true;
        usesFluidInput = true;
        minimumEnergyTier = Tier.REINFORCED;
        minimumItemOutputTier = Tier.REINFORCED;
        minimumFluidInputTier = Tier.REINFORCED;

        recipeGroup = SIRecipes.LASER_DRILL;

        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("laserDrill"));
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.laserDrill";
    }

    @Override
    public boolean canProcess() {
        boolean previous = super.canProcess();
        if(!previous) return false;

        if(currentRecipe instanceof RecipeEntryMachineRandomOutput){
            RecipeEntryMachineRandomOutput recipe = ((RecipeEntryMachineRandomOutput) currentRecipe);
            return worldObj != null && recipe.getData().allowedDimensions.contains(worldObj.dimension);
        }

        return false;
    }
}
