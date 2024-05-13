package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
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
        if(symbols.length == 0){
            return false;
        }
        if(symbols.length != getInput().length){
            return false;
        }
        for (RecipeExtendedSymbol subSymbol : symbols) {
            if (Arrays.stream(getInput()).anyMatch((S)->S.matches(subSymbol))) {
                RecipeExtendedSymbol inputSymbol = Arrays.stream(getInput()).filter((S) -> S.matches(subSymbol)).findFirst().orElse(null);
                if(inputSymbol == null) return false;
                if(inputSymbol.hasFluid()){
                    List<FluidStack> fluid = subSymbol.resolveFluids();
                    List<FluidStack> inputFluid = inputSymbol.resolveFluids();
                    List<Pair<FluidStack, FluidStack>> pairs = SignalIndustries.zip(fluid, inputFluid);
                    if (pairs.stream().anyMatch(pair -> pair.getLeft().amount < pair.getRight().amount)) {
                        return false;
                    }

                } else {
                    List<ItemStack> stack = subSymbol.resolve();
                    List<ItemStack> inputStack = inputSymbol.resolve();
                    List<Pair<ItemStack, ItemStack>> pairs = SignalIndustries.zip(stack, inputStack);
                    if (pairs.stream().anyMatch(pair -> pair.getLeft().stackSize < pair.getRight().stackSize)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
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
