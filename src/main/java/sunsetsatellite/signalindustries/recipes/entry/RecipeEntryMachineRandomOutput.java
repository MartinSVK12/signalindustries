package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.*;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.fluids.util.FluidInventoryWrapper;
import sunsetsatellite.signalindustries.recipes.adapter.RecipeMachineRandomOutputJsonAdapter;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeEntryMachineRandomOutput extends RecipeEntrySI<RecipeExtendedSymbol[], WeightedRandomBag<WeightedRandomLootObject>, RecipeProperties> implements HasJsonAdapter {

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

    @Override
    public void consumeMultiblockInputs(TileEntityTieredMultiblock multiblock) {
        if(multiblock.usesItemInput){
            List<ItemStack> recipeStacks = Catalyst.condenseItemList(
                    Arrays.stream(getInput())
                            .flatMap(symbol -> symbol.resolve().stream())
                            .filter(Objects::nonNull)
                            .map(ItemStack::copy).collect(Collectors.toList())
            );
            List<ItemStack> remainingRecipeStacks = recipeStacks.stream().map(ItemStack::copy).peek(I-> I.stackSize *= multiblock.parallel).collect(Collectors.toList());
            InventoryWrapper wrapper = new InventoryWrapper(multiblock.itemInput);
            for (ItemStack remainingRecipeStack : remainingRecipeStacks) {
                ItemStack stack = wrapper.removeUntil(remainingRecipeStack.itemID, remainingRecipeStack.getMetadata(), remainingRecipeStack.stackSize, remainingRecipeStack.getData(), false, false);
                if(stack.isStackEqual(remainingRecipeStack)){
                    if(stack.getItem().hasContainerItem() && !getData().consumeContainers){
                        wrapper.add(new ItemStack(stack.getItem().getContainerItem()));
                    }
                }
            }
        }
        if(multiblock.usesFluidInput){
            List<FluidStack> recipeStacks = Arrays.stream(getInput())
                    .flatMap(symbol -> symbol.resolveFluids().stream())
                    .filter(Objects::nonNull)
                    .map(FluidStack::copy)
                    .collect(Collectors.toList());
            List<FluidStack> remainingRecipeStacks = recipeStacks.stream().map(FluidStack::copy).peek(F->F.amount *= multiblock.parallel).collect(Collectors.toList());
            FluidInventoryWrapper wrapper = new FluidInventoryWrapper(multiblock.fluidInput);
            for (FluidStack remainingRecipeStack : remainingRecipeStacks) {
                wrapper.removeUntil(remainingRecipeStack.fluid.getFirstId(),remainingRecipeStack.amount,false);
            }
        }
    }

    @Override
    public boolean canMultiblockProcess(TileEntityTieredMultiblock multiblock) {
        for (WeightedRandomLootObject entry : getOutput().getEntries()) {
            ItemStack stack = entry.getDefinedItemStack();
            if(stack == null){
                return false;
            }
            stack.copy().stackSize = entry.isRandomYield() ? entry.getMaxYield() : entry.getFixedYield();
            if(!multiblock.areItemOutputsValid(stack)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void processMultiblockRecipe(TileEntityTieredMultiblock multiblock) {
        ItemStack stack = getOutput() == null ? null : getOutput().getRandom().getItemStack();
        if (stack != null) {
            multiblock.consumeInputs();
            if(multiblock.random.nextFloat() <= getData().chance){
                int multiplier = 1;
                multiplier *= multiblock.parallel;
                int outputAmountRemaining = stack.stackSize * multiplier;
                for (int i = 0; i < multiblock.itemOutput.itemContents.length; i++) {
                    ItemStack outputStack = multiblock.itemOutput.itemContents[i];
                    if (outputStack == null) {
                        int maxAmountInSlot = stack.getMaxStackSize();
                        if(maxAmountInSlot <= 0) continue;
                        int willTake = Math.min(outputAmountRemaining, maxAmountInSlot);
                        if(willTake <= 0) continue;
                        ItemStack copy = stack.copy();
                        copy.stackSize = willTake;
                        multiblock.itemOutput.setItem(i,copy);
                        outputAmountRemaining -= willTake;
                        if(outputAmountRemaining <= 0){
                            break;
                        }
                    } else if (outputStack.isItemEqual(stack)) {
                        int maxAmountInSlot = stack.getMaxStackSize() - outputStack.stackSize;
                        if(maxAmountInSlot <= 0) continue;
                        int willTake = Math.min(outputAmountRemaining, maxAmountInSlot);
                        if(willTake <= 0) continue;
                        outputStack.stackSize += willTake;
                        outputAmountRemaining -= willTake;
                        if(outputAmountRemaining <= 0){
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void consumeMachineInputs(TileEntityTieredMachineSimple machine) {
        //todo
    }

    @Override
    public boolean canMachineProcess(TileEntityTieredMachineSimple machine) {
        return false;
        //todo
    }

    @Override
    public void processMachineRecipe(TileEntityTieredMachineSimple machine) {
        //todo
    }

    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new RecipeMachineRandomOutputJsonAdapter();
    }
}
