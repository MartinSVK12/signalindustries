package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;

public abstract class RecipeEntrySI<I,O,D> extends RecipeEntryBase<I,O,D> {

    public RecipeEntrySI(I input, O output, D data) {
        super(input, output, data);
    }

    public RecipeEntrySI() {
    }

    public abstract boolean matches(RecipeExtendedSymbol[] symbols);

    public abstract boolean matchesQuery(SearchQuery query);

    public abstract boolean matchesScope(SearchQuery query);

    public abstract boolean matchesRecipe(SearchQuery query);

    public abstract boolean matchesUsage(SearchQuery query);
}
