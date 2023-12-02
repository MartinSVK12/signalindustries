package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RecipeEntryMachineFluid extends RecipeEntrySI<RecipeExtendedSymbol[], FluidStack, RecipeProperties> {

    public RecipeEntryMachineFluid(RecipeExtendedSymbol[] input, FluidStack output, RecipeProperties data) {
        super(input, output, data);
    }

    public RecipeEntryMachineFluid() {}

    public boolean matches(RecipeExtendedSymbol[] symbols) {
        return Arrays.equals(symbols,getInput());
    }

    public boolean matchesQuery(SearchQuery query) {
        switch (query.mode) {
            case ALL: {
                if ((matchesRecipe(query) || matchesUsage(query)) && matchesScope(query)) return true;
                break;
            }
            case RECIPE: {
                if (matchesRecipe(query) && matchesScope(query)) return true;
                break;
            }
            case USAGE: {
                if (matchesUsage(query) && matchesScope(query)) return true;
                break;
            }
        }
        return false;
    }

    public boolean matchesScope(SearchQuery query) {
        if (query.scope.getLeft() == SearchQuery.SearchScope.NONE) return true;
        if (query.scope.getLeft() == SearchQuery.SearchScope.NAMESPACE) {
            RecipeNamespace namespace = Registries.RECIPES.getItem(query.scope.getRight());
            if (namespace == parent.getParent()) {
                return true;
            }
        } else if (query.scope.getLeft() == SearchQuery.SearchScope.NAMESPACE_GROUP) {
            RecipeGroup group;
            try {
                group = Registries.RECIPES.getGroupFromKey(query.scope.getRight());
            } catch (IllegalArgumentException e) {
                group = null;
            }
            if (group == parent) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesRecipe(SearchQuery query) {
        if (query.query.getLeft() == SearchQuery.QueryType.NAME) {
            String stackName = I18n.getInstance().translateNameKey(getOutput().getFluidName());
            if (query.strict && stackName.equalsIgnoreCase(query.query.getRight())) {
                return true;
            } else if (!query.strict && stackName.toLowerCase().contains(query.query.getRight().toLowerCase())) {
                return true;
            }
        } else if (query.query.getLeft() == SearchQuery.QueryType.GROUP && !Objects.equals(query.query.getRight(), "")) {
            List<FluidStack> groupStacks = new RecipeExtendedSymbol(query.query.getRight()).resolveFluids();
            if (groupStacks == null) return false;
            return groupStacks.contains(getOutput());
        }
        return false;
    }

    public boolean matchesUsage(SearchQuery query) {
        RecipeExtendedSymbol[] symbols = getInput();
        for (RecipeExtendedSymbol symbol : symbols) {
            if (symbol == null) continue;
            List<ItemStack> stacks = symbol.resolve();
            if (query.query.getLeft() == SearchQuery.QueryType.NAME) {
                for (ItemStack stack : stacks) {
                    if (query.strict && stack.getDisplayName().equalsIgnoreCase(query.query.getRight())) {
                        return true;
                    } else if (!query.strict && stack.getDisplayName().toLowerCase().contains(query.query.getRight().toLowerCase())) {
                        return true;
                    }
                }
            } else if (query.query.getLeft() == SearchQuery.QueryType.GROUP && !Objects.equals(query.query.getRight(), "")) {
                List<ItemStack> groupStacks = new RecipeExtendedSymbol(query.query.getRight()).resolve();
                if (groupStacks == null) return false;
                if (stacks.stream().anyMatch((groupStacks::contains))) {
                    return true;
                }
            }

        }
        return false;
    }

}
