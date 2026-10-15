package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryBlastFurnace;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Arrays;

// I really did not want to remake how multiblocks work just for this
public class InductionSmelterRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>> {

    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        for (RecipeEntryFurnace furnaceRecipe : Registries.RECIPES.getAllFurnaceRecipes()) {
            String recipeKey = Registries.RECIPES.deconstructKey(furnaceRecipe.toString())[2];
            RecipeSymbol input = furnaceRecipe.getInput();
            group.register(
                    recipeKey,
                    new RecipeEntryMachine(
                            new RecipeExtendedSymbol[]{
                                    new RecipeExtendedSymbol(input)
                            },
                            furnaceRecipe.getOutput(),
                            new RecipeProperties(100, 40, Tier.BASIC, false)
                    )
            );
        }
    }

	public void addRecipesBlast(RecipeGroup<RecipeEntryMachine> group) {
		for (RecipeEntryBlastFurnace furnaceRecipe : Registries.RECIPES.getAllBlastFurnaceRecipes()) {
			String recipeKey = Registries.RECIPES.deconstructKey(furnaceRecipe.toString())[2];
			RecipeSymbol[] input = furnaceRecipe.getInput();
			group.register(
				recipeKey,
				new RecipeEntryMachine(
					Arrays.stream(input).map(RecipeExtendedSymbol::new).toArray(RecipeExtendedSymbol[]::new),
					furnaceRecipe.getOutput(),
					new RecipeProperties(100, 80, Tier.BASIC, false)
				)
			);
		}
	}
}
