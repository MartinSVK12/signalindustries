package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.Dimension;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;
import sunsetsatellite.signalindustries.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class GreenhouseRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineMultiOutput>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineMultiOutput> group) {
        group.register(
            "wheat",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.SEEDS_WHEAT, 1))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Items.WHEAT, 1)),
                                new RecipeOutputStack(new ItemStack(Items.SEEDS_WHEAT, 1)).randomYield(1,4)
                        },
                        new RecipeProperties(600, 100, Tier.BASIC, false)
                )
        );
    }
}
