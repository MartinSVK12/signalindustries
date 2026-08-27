package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryTrommel;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class SITrommelRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineRandomOutput>> {

    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineRandomOutput> group) {
        for (RecipeEntryTrommel trommelRecipe : Registries.RECIPES.getAllTrommelRecipes()) {
            String recipeKey = Registries.RECIPES.deconstructKey(trommelRecipe.toString())[2];
            RecipeSymbol input = trommelRecipe.getInput();
            group.register(
                    recipeKey+"_basic",
                    new RecipeEntryMachineRandomOutput(
                            new RecipeExtendedSymbol[]{
                                    new RecipeExtendedSymbol(input)
                            },
                            trommelRecipe.getOutput(),
                            new RecipeProperties(50, 40, Tier.BASIC, true).setChance(0.5f)
                    )
            );
			group.register(
				recipeKey+"_reinforced",
				new RecipeEntryMachineRandomOutput(
					new RecipeExtendedSymbol[]{
						new RecipeExtendedSymbol(input)
					},
					trommelRecipe.getOutput(),
					new RecipeProperties(50, 40, Tier.REINFORCED, true).setChance(0.75f)
				)
			);
			group.register(
				recipeKey+"_awakened",
				new RecipeEntryMachineRandomOutput(
					new RecipeExtendedSymbol[]{
						new RecipeExtendedSymbol(input)
					},
					trommelRecipe.getOutput(),
					new RecipeProperties(50, 40, Tier.AWAKENED, false).setChance(1f)
				)
			);
        }
    }
}
