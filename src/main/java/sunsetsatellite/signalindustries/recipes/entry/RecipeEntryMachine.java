package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RecipeEntryMachine extends RecipeEntrySI<RecipeExtendedSymbol[], ItemStack, RecipeProperties> {

    public RecipeEntryMachine(RecipeExtendedSymbol[] input, ItemStack output, RecipeProperties data) {
        super(input, output, data);
    }

    public RecipeEntryMachine() {}

    public boolean matches(RecipeExtendedSymbol[] symbols) {
        for (RecipeExtendedSymbol S : symbols) {
            if (Arrays.asList(getInput()).contains(S)) {
                RecipeExtendedSymbol inputSymbol = getInput()[Arrays.asList(getInput()).indexOf(S)];
                if(inputSymbol.hasFluid()){
                    FluidStack fluid = S.resolveFluids().get(0);
                    FluidStack inputFluid = inputSymbol.resolveFluids().get(0);
                    return fluid.amount >= inputFluid.amount;
                } else {
                    ItemStack stack = S.resolve().get(0);
                    ItemStack inputStack = inputSymbol.resolve().get(0);
                    return stack.stackSize >= inputStack.stackSize;
                }
            }
        }
        return false;
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
            RecipeGroup<?> group;
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
            if (query.strict && getOutput().getDisplayName().equalsIgnoreCase(query.query.getRight())) {
                return true;
            } else if (!query.strict && getOutput().getDisplayName().toLowerCase().contains(query.query.getRight().toLowerCase())) {
                return true;
            }
        } else if (query.query.getLeft() == SearchQuery.QueryType.GROUP && !Objects.equals(query.query.getRight(), "")) {
            List<ItemStack> groupStacks = new RecipeSymbol(query.query.getRight()).resolve();
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
                if(stacks == null) return false;
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
