package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeGroup;

public interface MachineRecipesBase<T extends RecipeGroup<? extends RecipeEntryBase<?,?,?>>> {
 void addRecipes(T group);
}
