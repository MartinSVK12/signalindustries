package sunsetsatellite.signalindustries.api.impl.tmb;

import sunsetsatellite.catalyst.fluids.api.impl.tmb.TMBFluidPlugin;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import turing.tmb.RecipeTranslator;
import turing.tmb.api.VanillaTypes;
import turing.tmb.api.ingredient.ITypedIngredient;

import java.util.Arrays;

public class FluidMachineRecipeTranslator extends RecipeTranslator<RecipeEntryMachineFluid> {
    public FluidMachineRecipeTranslator(RecipeEntryMachineFluid recipe) {
        super(recipe);
    }

    @Override
    public boolean isValidInput(ITypedIngredient<?> ingredient) {
        if (ingredient.getType() == VanillaTypes.ITEM_STACK) {
            return Arrays.stream(recipe.getInput()).anyMatch(symbol -> symbol.matches(ingredient.getCastIngredient(VanillaTypes.ITEM_STACK)));
        } else if (ingredient.getType() == TMBFluidPlugin.FLUID_STACK) {
            return Arrays.stream(recipe.getInput()).anyMatch(symbol -> symbol.matchesFluid(ingredient.getCastIngredient(TMBFluidPlugin.FLUID_STACK)));
        }
        return false;
    }

    @Override
    public boolean isOutput(ITypedIngredient<?> ingredient) {
        if (ingredient.getType() == TMBFluidPlugin.FLUID_STACK) {
            return recipe.getOutput().isFluidEqual(ingredient.getCastIngredient(TMBFluidPlugin.FLUID_STACK));
        }
        return false;
    }
}
