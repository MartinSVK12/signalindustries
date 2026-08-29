package sunsetsatellite.signalindustries.api.impl.tmb.translator;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.api.impl.tmb.TMBFluidPlugin;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;
import turing.tmb.RecipeTranslator;
import turing.tmb.api.VanillaTypes;
import turing.tmb.api.ingredient.IIngredientTypeWithSubtypes;
import turing.tmb.api.ingredient.ITypedIngredient;

import java.util.Arrays;

public class RandomMachineRecipeTranslator extends RecipeTranslator<RecipeEntryMachineRandomOutput> {
	public RandomMachineRecipeTranslator(RecipeEntryMachineRandomOutput recipe) {
		super(recipe);
	}

	@Override
	public <I> boolean isValidInput(ITypedIngredient<I> ingredient) {
		if (ingredient.getType() == VanillaTypes.ITEM_STACK) {
			return Arrays.stream(recipe.getInput()).anyMatch(symbol -> symbol.matches(ingredient.getCastIngredient(VanillaTypes.ITEM_STACK)));
		} else if (ingredient.getType() == TMBFluidPlugin.FLUID_STACK) {
			return Arrays.stream(recipe.getInput()).anyMatch(symbol -> symbol.matchesFluid(ingredient.getCastIngredient(TMBFluidPlugin.FLUID_STACK)));
		}
		return false;
	}

	@Override
	public <I> boolean isOutput(ITypedIngredient<I> ingredient) {
		if (ingredient.getType() == VanillaTypes.ITEM_STACK) {
			return recipe.getOutput().getEntries().stream().anyMatch((w) -> w.getDefinedItemStack().isItemEqual(ingredient.getCastIngredient(VanillaTypes.ITEM_STACK)));
		}
		if (ingredient.getType() instanceof IIngredientTypeWithSubtypes<?, I> type && type.getIngredientBaseClass() == ItemStack.class) {
			return recipe.getOutput().getEntries().stream().anyMatch((w) -> w.getDefinedItemStack().isItemEqual(ingredient.getBaseIngredient((IIngredientTypeWithSubtypes<ItemStack, I>) type)));
		}
		return false;
	}
}
