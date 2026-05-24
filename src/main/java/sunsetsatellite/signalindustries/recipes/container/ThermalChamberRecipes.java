package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.fluids.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class ThermalChamberRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineMultiOutput>> {

    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineMultiOutput> group) {
        group.register(
                "cobble_lava",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.COBBLE_STONE,1)),
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new FluidStack(Fluids.LAVA, 100)),
                        },
                        new RecipeProperties(40, 60, Tier.BASIC, false).addAuxData("mode","melting")
                )
        );
        group.register(
                "stone_lava",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.STONE,1)),
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new FluidStack(Fluids.LAVA, 100)),
                        },
                        new RecipeProperties(40, 60, Tier.BASIC, false).addAuxData("mode","melting")
                )
        );
        group.register(
                "ice",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER, 1000)),
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.ICE, 1)),
                        },
                        new RecipeProperties(40, 60, Tier.BASIC, false).addAuxData("mode","freezing")
                )
        );
    }
}
