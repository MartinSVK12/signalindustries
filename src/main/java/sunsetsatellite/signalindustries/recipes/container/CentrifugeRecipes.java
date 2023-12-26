package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class CentrifugeRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "awakened_fragments",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) SignalIndustries.burntSignalumFlowing,250)),
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) SignalIndustries.burntSignalumFlowing,250)),
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) SignalIndustries.burntSignalumFlowing,250)),
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) SignalIndustries.burntSignalumFlowing,250)),
                        },
                        new ItemStack(SignalIndustries.awakenedSignalumFragment,1),
                        new RecipeProperties(400,240,Tier.REINFORCED,false)
                )
        );
    }
}
