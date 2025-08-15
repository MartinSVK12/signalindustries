package sunsetsatellite.signalindustries.recipes.entry;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RecipeEntryMachineRandomOutput extends RecipeEntrySI<RecipeExtendedSymbol[], WeightedRandomBag<WeightedRandomLootObject>, RecipeProperties> {

    public RecipeEntryMachineRandomOutput(RecipeExtendedSymbol[] input, WeightedRandomBag<WeightedRandomLootObject> output, RecipeProperties data) {
        super(input, output, data);
    }

    public RecipeEntryMachineRandomOutput() {}

    public boolean matches(RecipeExtendedSymbol[] symbols) {
        if(symbols.length == 0){
            return false;
        }
        //key is recipe input, value is inventory input
        HashMap<RecipeExtendedSymbol,RecipeExtendedSymbol> alreadyMatched = new HashMap<>();
        for (RecipeExtendedSymbol invInputSymbol : symbols) {
            for (RecipeExtendedSymbol recipeInputSymbol : getInput()) {
                if (recipeInputSymbol.matches(invInputSymbol) && !alreadyMatched.containsKey(recipeInputSymbol)) {
                    alreadyMatched.put(recipeInputSymbol, invInputSymbol);
                    break;
                }
            }
        }
        if(alreadyMatched.size() != getInput().length) return false;
        HashMap<List<ItemStack>,List<ItemStack>> alreadyMatchedResolved = new HashMap<>();
        alreadyMatched.forEach((recipeInputSymbol,invInputSymbol)->{
            alreadyMatchedResolved.put(recipeInputSymbol.asNormalSymbol().resolve(),invInputSymbol.asNormalSymbol().resolve());
        });

        return alreadyMatchedResolved.entrySet().stream().allMatch((e)->e.getKey().stream()
                        .anyMatch((s)->e.getValue().stream()
                                .anyMatch((s2)->s.stackSize <= s2.stackSize)));
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
            return namespace == parent.getParent();
        } else if (query.scope.getLeft() == SearchQuery.SearchScope.NAMESPACE_GROUP) {
            RecipeGroup<?> group;
            try {
                group = Registries.RECIPES.getGroupFromKey(query.scope.getRight());
            } catch (IllegalArgumentException e) {
                group = null;
            }
            return group == parent;
        }
        return false;
    }

    public boolean matchesRecipe(SearchQuery query) {
        if (query.query.getLeft() == SearchQuery.QueryType.NAME) {
            if (query.strict && getOutput().getEntries().stream().map(WeightedRandomLootObject::getDefinedItemStack).anyMatch(s -> s != null && s.getDisplayName().equalsIgnoreCase(query.query.getRight()))) {
                return true;
            } else return !query.strict && getOutput().getEntries().stream().map(WeightedRandomLootObject::getDefinedItemStack).anyMatch(s -> s != null && s.getDisplayName().toLowerCase().contains(query.query.getRight().toLowerCase()));
        } else if (query.query.getLeft() == SearchQuery.QueryType.GROUP && !Objects.equals(query.query.getRight(), "")) {
            List<ItemStack> groupStacks = new RecipeSymbol(query.query.getRight()).resolve();
            if (groupStacks == null) return false;
            return groupStacks.contains(getOutput().getEntries().stream().map(WeightedRandomLootObject::getDefinedItemStack).filter(Objects::nonNull).findFirst().orElse(null));
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
