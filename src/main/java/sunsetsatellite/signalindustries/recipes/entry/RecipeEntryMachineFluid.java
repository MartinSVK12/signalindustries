package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipeEntryMachineFluid extends RecipeEntrySI<RecipeExtendedSymbol[], FluidStack, RecipeProperties> {

    public RecipeEntryMachineFluid(RecipeExtendedSymbol[] input, FluidStack output, RecipeProperties data) {
        super(input, output, data);
    }

    public RecipeEntryMachineFluid() {}

    public boolean matches(RecipeExtendedSymbol[] symbols) {
        if(symbols.length == 0){
            return false;
        }
        if(symbols.length != getInput().length){
            return false;
        }
        List<FluidStack> recipeFluids = Arrays.stream(getInput()).flatMap((S) -> {
            if(S.hasFluid()) {
                return S.resolveFluids().stream();
            } else if (!Objects.equals(S.getItemGroup(), "")){
                return S.resolve().stream().filter((I)-> I.itemID < 16384 && Block.getBlock(I.itemID) instanceof BlockFluid).map((I)->new FluidStack((BlockFluid) Block.getBlock(I.itemID)));
            }
            return null;
        }).collect(Collectors.toList());
        List<ItemStack> recipeStacks = Arrays.stream(getInput()).flatMap((S) -> S.resolve().stream()).collect(Collectors.toList());
        for (RecipeExtendedSymbol symbol : symbols) {
            if(symbol.hasFluid()){
                List<FluidStack> fluids = symbol.resolveFluids();
                if(fluids == null) return false;
                if(fluids.isEmpty()) return false;
                for (FluidStack fluid : fluids) {
                    if(SignalIndustries.listContains(recipeFluids,fluid,FluidStack::areFluidsEqual)){
                        FluidStack recipeFluid = recipeFluids.get(fluids.indexOf(fluid));
                        if(fluid.amount < recipeFluid.amount){
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                List<ItemStack> stacks = symbol.resolve();
                if(stacks == null) return false;
                if(stacks.isEmpty()) return false;
                for (ItemStack stack : stacks) {
                    if(SignalIndustries.listContains(recipeStacks,stack,ItemStack::isItemEqual)){
                        ItemStack recipeStack = recipeStacks.get(stacks.indexOf(stack));
                        if(stack.stackSize < recipeStack.stackSize){
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
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
            String stackName = I18n.getInstance().translateNameKey(getOutput().getFluidName());
            if (query.strict && stackName.equalsIgnoreCase(query.query.getRight())) {
                return true;
            } else return !query.strict && stackName.toLowerCase().contains(query.query.getRight().toLowerCase());
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
