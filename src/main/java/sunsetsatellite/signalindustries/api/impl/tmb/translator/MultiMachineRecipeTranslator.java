package sunsetsatellite.signalindustries.api.impl.tmb.translator;

import sunsetsatellite.catalyst.fluids.api.impl.tmb.TMBFluidPlugin;
import sunsetsatellite.catalyst.fluids.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import turing.tmb.RecipeTranslator;
import turing.tmb.api.VanillaTypes;
import turing.tmb.api.ingredient.ITypedIngredient;

import java.util.Arrays;
import java.util.Objects;

public class MultiMachineRecipeTranslator extends RecipeTranslator<RecipeEntryMachineMultiOutput> {
    public MultiMachineRecipeTranslator(RecipeEntryMachineMultiOutput recipe) {
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
        if (ingredient.getType() == VanillaTypes.ITEM_STACK) {
            return Arrays.stream(recipe.getOutput())
                    .filter(RecipeOutputStack::isItem)
                    .map((S) -> S.stack)
                    .anyMatch((S) -> {
                        if (ingredient.getCastIngredient(VanillaTypes.ITEM_STACK) == null) return false;
                        return S.isItemEqual(Objects.requireNonNull(ingredient.getCastIngredient(VanillaTypes.ITEM_STACK)));
                    });
        } else if (ingredient.getType() == TMBFluidPlugin.FLUID_STACK) {
            return Arrays.stream(recipe.getOutput())
                    .filter(RecipeOutputStack::isFluid)
                    .map((S) -> S.fluid)
                    .anyMatch((S) -> {
                        if (ingredient.getCastIngredient(TMBFluidPlugin.FLUID_STACK) == null) return false;
                        return S.isFluidEqual(Objects.requireNonNull(ingredient.getCastIngredient(TMBFluidPlugin.FLUID_STACK)));
                    });
        }
        return false;
    }
}
