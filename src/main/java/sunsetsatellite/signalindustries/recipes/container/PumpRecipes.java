package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.data.registry.recipe.RecipeGroup;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class PumpRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineFluid>> {
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineFluid> group) {
        group.register("water", new RecipeEntryMachineFluid(
                new RecipeExtendedSymbol[]{
                        new RecipeExtendedSymbol(new FluidStack(Fluids.WATER))
                },
                new FluidStack(Fluids.WATER, 1000),
                new RecipeProperties(100, 0, Tier.PROTOTYPE, false)
        ));
        group.register("lava", new RecipeEntryMachineFluid(
                new RecipeExtendedSymbol[]{
                        new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA))
                },
                new FluidStack(Fluids.LAVA, 1000),
                new RecipeProperties(200, 5, Tier.BASIC, false)
        ));
        group.register("world_resin", new RecipeEntryMachineFluid(
                new RecipeExtendedSymbol[]{
                        new RecipeExtendedSymbol(SIBlocks.eternalTreeLog.getDefaultStack())
                },
                new FluidStack(SIFluids.WORLD_RESIN, 100),
                new RecipeProperties(400, 20, Tier.REINFORCED, false)
        ));
    }
}
