package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.fluids.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class HeatPumpRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineMultiOutput>> {

    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineMultiOutput> group) {
        group.register(
                "melting",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.heatingCoil,1)),
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new FluidStack(Fluids.LAVA, 1000)),
                        },
                        new RecipeProperties(200, 120, Tier.BASIC, false).addAuxData("mode","melting")
                )
        );
        group.register(
                "freezing",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.coolingCoil,1)),
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.ICE, 1)),
                        },
                        new RecipeProperties(200, 120, Tier.BASIC, false).addAuxData("mode","freezing")
                )
        );
    }
}
