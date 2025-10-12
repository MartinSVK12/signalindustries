package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.fluids.util.FluidInventoryWrapper;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeEntryMachineMultiOutput extends RecipeEntrySI<RecipeExtendedSymbol[], RecipeOutputStack[], RecipeProperties>{

    public RecipeEntryMachineMultiOutput(RecipeExtendedSymbol[] input, RecipeOutputStack[] output, RecipeProperties data) {
        super(input, output, data);
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean matchesRecipe(SearchQuery query) {
        if (query.query.getLeft() == SearchQuery.QueryType.NAME) {
            if(!query.strict) {
                return Arrays.stream(getOutput()).anyMatch((O)->{
                    if(O.isItem()){
                        return O.stack.getDisplayName().toLowerCase().contains(query.query.getRight().toLowerCase());
                    } else if (O.isFluid()) {
                        return O.fluid.fluid.getName().toLowerCase().contains(query.query.getRight().toLowerCase());
                    }
                    return false;
                });
            } else {
                return Arrays.stream(getOutput()).anyMatch((O)->{
                    if(O.isItem()){
                        return O.stack.getDisplayName().equalsIgnoreCase(query.query.getRight());
                    } else if (O.isFluid()) {
                        return O.fluid.fluid.getName().equalsIgnoreCase(query.query.getRight());
                    }
                    return false;
                });
            }

        } else if (query.query.getLeft() == SearchQuery.QueryType.GROUP && !Objects.equals(query.query.getRight(), "")) {
            List<ItemStack> groupStacks = new RecipeSymbol(query.query.getRight()).resolve();
            if (groupStacks == null) return false;
            return Arrays.stream(getOutput()).anyMatch((O)->{
                if(O.isItem()){
                    return groupStacks.stream().anyMatch((S)->S.isItemEqual(O.stack));
                } else if (O.isFluid()) {
                    return groupStacks.stream().anyMatch((S)->S.isItemEqual(O.fluid.toItemStack()));
                }
                return false;
            });
        }
        return false;
    }

    @Override
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
        for (RecipeOutputStack output : getOutput()) {
            if(output.isItem()){
                ItemStack stack = output.stack.copy();
                stack.stackSize = output.randomAmount ? output.amountMax : stack.stackSize;
                if(!multiblock.areItemOutputsValid(stack)){
                    return false;
                }
            } else if (output.isFluid()){
                FluidStack stack = output.fluid.copy();
                stack.amount = output.randomAmount ? output.amountMax : stack.amount;
                if(!multiblock.areFluidOutputsValid(stack)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void processMultiblockRecipe(TileEntityTieredMultiblock multiblock) {
        multiblock.consumeInputs();
        if(multiblock.random.nextFloat() <= getData().chance) {
            for (RecipeOutputStack outputStack : getOutput()) {
                if (outputStack.isItem()) {
                    ItemStack stack = getOutput() == null ? null : outputStack.stack.copy();
                    if(stack != null && outputStack.randomAmount){
                        stack.stackSize = Catalyst.random(multiblock.random, outputStack.amountMin,outputStack.amountMax + 1);
                    }
                    if (stack != null) {
                        if (multiblock.random.nextFloat() <= outputStack.chance) {
                            int multiplier = 1;
                            multiplier *= multiblock.parallel;
                            int outputAmountRemaining = stack.stackSize * multiplier;
                            for (int i = 0; i < multiblock.itemOutput.itemContents.length; i++) {
                                ItemStack busStack = multiblock.itemOutput.itemContents[i];
                                if (busStack == null) {
                                    int maxAmountInSlot = stack.getMaxStackSize();
                                    if (maxAmountInSlot <= 0) continue;
                                    int willTake = Math.min(outputAmountRemaining, maxAmountInSlot);
                                    if (willTake <= 0) continue;
                                    ItemStack copy = stack.copy();
                                    copy.stackSize = willTake;
                                    multiblock.itemOutput.setItem(i, copy);
                                    outputAmountRemaining -= willTake;
                                    if (outputAmountRemaining <= 0) {
                                        break;
                                    }
                                } else if (busStack.isItemEqual(stack)) {
                                    int maxAmountInSlot = stack.getMaxStackSize() - busStack.stackSize;
                                    if (maxAmountInSlot <= 0) continue;
                                    int willTake = Math.min(outputAmountRemaining, maxAmountInSlot);
                                    if (willTake <= 0) continue;
                                    busStack.stackSize += willTake;
                                    outputAmountRemaining -= willTake;
                                    if (outputAmountRemaining <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else if (outputStack.isFluid()) {
                    FluidStack fluidStack = getOutput() == null ? null : outputStack.fluid.copy();
                    if(fluidStack != null && outputStack.randomAmount){
                        fluidStack.amount =  Catalyst.random(multiblock.random, outputStack.amountMin,outputStack.amountMax + 1);
                    }
                    if (fluidStack != null) {
                        if(multiblock.random.nextFloat() <= outputStack.chance) {
                            int multiplier = 1;
                            multiplier *= multiblock.parallel;
                            int outputAmountRemaining = fluidStack.amount * multiplier;
                            for (int i = 0; i < multiblock.fluidOutput.itemContents.length; i++) {
                                FluidStack hatchStack = multiblock.fluidOutput.fluidContents[i];
                                if (hatchStack == null) {
                                    int maxAmountInSlot = multiblock.fluidOutput.getFluidInSlot(i).amount;
                                    if (maxAmountInSlot <= 0) continue;
                                    int willTake = Math.min(outputAmountRemaining, maxAmountInSlot);
                                    if (willTake <= 0) continue;
                                    FluidStack copy = fluidStack.copy();
                                    copy.amount = willTake;
                                    multiblock.fluidOutput.setFluidInSlot(i, copy);
                                    outputAmountRemaining -= willTake;
                                    if (outputAmountRemaining <= 0) {
                                        break;
                                    }
                                } else if (hatchStack.isFluidEqual(fluidStack)) {
                                    int maxAmountInSlot = multiblock.fluidOutput.getFluidCapacityForSlot(i) - hatchStack.amount;
                                    if (maxAmountInSlot <= 0) continue;
                                    int willTake = Math.min(outputAmountRemaining, maxAmountInSlot);
                                    if (willTake <= 0) continue;
                                    hatchStack.amount += willTake;
                                    outputAmountRemaining -= willTake;
                                    if (outputAmountRemaining <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
