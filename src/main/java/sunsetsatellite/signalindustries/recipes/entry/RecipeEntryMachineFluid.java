package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.tiles.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.tiles.TileEntityItemBus;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.*;
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

        return alreadyMatchedResolved.entrySet().stream()
                .allMatch((e)->e.getKey().stream()
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
            String stackName = I18n.getInstance().translateNameKey(getOutput().fluid.getName());
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
            for (int i = 0; i < multiblock.fluidInput.fluidContents.length; i++) {
                FluidStack inputStack = multiblock.fluidInput.getFluidInSlot(i);
                List<FluidStack> recipeStacks = Arrays.stream(getInput())
                        .flatMap(symbol -> symbol.resolveFluids().stream())
                        .filter(Objects::nonNull)
                        .map(FluidStack::copy)
                        .collect(Collectors.toList());
                List<FluidStack> remainingRecipeStacks = recipeStacks.stream().map(FluidStack::copy).collect(Collectors.toList());
                if(inputStack != null){
                    Optional<FluidStack> recipeStack = recipeStacks.stream().filter(stack -> stack.isFluidEqual(inputStack)).findFirst();
                    Optional<FluidStack> remainingRecipeStack = remainingRecipeStacks.stream().filter(stack -> stack.isFluidEqual(inputStack)).findFirst();
                    recipeStack.ifPresent(stack -> {
                        remainingRecipeStack.ifPresent(remainingStack -> {
                            if(remainingStack.amount > 0){
                                int willTake = Math.min(stack.amount * multiblock.parallel, inputStack.amount);
                                inputStack.amount -= willTake;
                                remainingStack.amount -= stack.amount;
                            }
                        });
                    });
                    //recipeStack.ifPresent(stack -> inputStack.amount -= stack.amount * parallel);
                    if (inputStack.amount <= 0) {
                        multiblock.fluidInput.setFluidInSlot(i, null);
                    }
                }
            }
        }
    }

    @Override
    public boolean canMultiblockProcess(TileEntityTieredMultiblock multiblock) {
        return multiblock.areFluidOutputsValid(getOutput());
    }

    @Override
    public void processMultiblockRecipe(TileEntityTieredMultiblock multiblock) {
        FluidStack fluidStack = getOutput() == null ? null : getOutput().copy();
        if (fluidStack != null) {
            multiblock.consumeInputs();
            if(multiblock.random.nextFloat() <= getData().chance) {
                int multiplier = 1;
                multiplier *=  multiblock.parallel;
                int outputAmountRemaining = fluidStack.amount * multiplier;
                for (int i = 0; i <  multiblock.fluidOutput.itemContents.length; i++) {
                    FluidStack outputStack =  multiblock.fluidOutput.fluidContents[i];
                    if (outputStack == null) {
                        int maxAmountInSlot = multiblock.fluidOutput.getFluidCapacityForSlot(i);
                        if(maxAmountInSlot <= 0) continue;
                        int willTake = Math.min(outputAmountRemaining, maxAmountInSlot);
                        if(willTake <= 0) continue;
                        FluidStack copy = fluidStack.copy();
                        copy.amount = willTake;
                        multiblock.fluidOutput.setFluidInSlot(i,copy);
                        outputAmountRemaining -= willTake;
                        if(outputAmountRemaining <= 0){
                            break;
                        }
                    } else if (outputStack.isFluidEqual(fluidStack)) {
                        int maxAmountInSlot = multiblock.fluidOutput.getFluidCapacityForSlot(i) - outputStack.amount;
                        if(maxAmountInSlot <= 0) continue;
                        int willTake = Math.min(outputAmountRemaining, maxAmountInSlot);
                        if(willTake <= 0) continue;
                        outputStack.amount += willTake;
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

}
