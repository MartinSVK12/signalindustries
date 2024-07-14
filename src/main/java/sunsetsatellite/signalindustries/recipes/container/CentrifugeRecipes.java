package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class CentrifugeRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "awakened_fragments",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(SIBlocks.burntSignalumFlowing,250)),
                                new RecipeExtendedSymbol(new FluidStack(SIBlocks.burntSignalumFlowing,250)),
                                new RecipeExtendedSymbol(new FluidStack(SIBlocks.burntSignalumFlowing,250)),
                                new RecipeExtendedSymbol(new FluidStack(SIBlocks.burntSignalumFlowing,250)),
                        },
                        new ItemStack(SIItems.awakenedSignalumFragment,1),
                        new RecipeProperties(400,240,Tier.REINFORCED,false).setChance(0.25f)
                )
        );
    }
}
