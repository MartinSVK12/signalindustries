package sunsetsatellite.signalindustries.inventories.base;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IFluidIO;
import sunsetsatellite.catalyst.core.util.IItemIO;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.*;

public class TileEntityTieredMachineSimple extends TileEntityTieredMachineBase implements IFluidIO, IItemIO {

    public RecipeGroupSI<?> recipeGroup;
    public RecipeEntrySI<?,?, RecipeProperties> currentRecipe;
    public int recipeId = 0;
    public int[] itemInputs = new int[0];
    public int[] itemOutputs = new int[0];
    public int[] fluidInputs = new int[0];
    public int[] fluidOutputs = new int[0];
    public int energySlot;
    public Random random = new Random();

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        extractFluids();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            setCurrentRecipe();
            work();
        }
    }



    public void work(){
        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(areAllInputsNull()){
            progressTicks = 0;
        } else if(canProcess()) {
            progressMaxTicks = currentRecipe.getData().ticks / speedMultiplier;
        }
        if(!worldObj.isClientSide){
            if (progressTicks == 0 && canProcess()){
                update = fuel();
            }
            if(isBurning() && canProcess()){
                progressTicks++;
                if(progressTicks >= progressMaxTicks){
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if(canProcess()){
                fuel();
                if(fuelBurnTicks > 0){
                    fuelBurnTicks++;
                }
            }
        }

        if(update) {
            this.onInventoryChanged();
        }
    }

    public boolean areAllInputsNull(){
        boolean itemsNull = Arrays.stream(itemInputs).allMatch(slot -> itemContents[slot] == null);
        boolean fluidsNull = Arrays.stream(fluidInputs).allMatch(slot -> fluidContents[slot] == null);
        return itemsNull && fluidsNull;
    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[energySlot]);
        if(burn > 0 && canProcess() && currentRecipe != null){
            if(fluidContents[energySlot].amount >= currentRecipe.getData().cost){
                progressMaxTicks = currentRecipe.getData().ticks / speedMultiplier;
                fuelMaxBurnTicks = fuelBurnTicks = burn;
                fluidContents[energySlot].amount -= currentRecipe.getData().cost;
                if (fluidContents[energySlot].amount == 0) {
                    fluidContents[energySlot] = null;
                }
                return true;
            }
        }
        return false;
    }

    public void processItem(){
        if(canProcess()){
            if(currentRecipe instanceof RecipeEntryMachine){
                RecipeEntryMachine recipe = ((RecipeEntryMachine) currentRecipe);
                ItemStack stack = recipe.getOutput() == null ? null : recipe.getOutput().copy();
                if (stack != null) {
                    consumeInputs();
                    if(random.nextFloat() <= recipe.getData().chance){
                        if (itemContents[itemOutputs[0]] == null) {
                            setInventorySlotContents(itemOutputs[0], stack);
                        } else if (itemContents[itemOutputs[0]].isItemEqual(stack)) {
                            itemContents[itemOutputs[0]].stackSize += stack.stackSize;
                        }
                    }
                }
            } else if (currentRecipe instanceof RecipeEntryMachineFluid) {
                RecipeEntryMachineFluid recipe = ((RecipeEntryMachineFluid) currentRecipe);
                FluidStack fluidStack = recipe.getOutput() == null ? null : recipe.getOutput().copy();
                if (fluidStack != null) {
                    consumeInputs();
                    if(random.nextFloat() <= recipe.getData().chance) {
                        if (fluidContents[fluidOutputs[0]] == null) {
                            setFluidInSlot(fluidOutputs[0], fluidStack);
                        } else if (fluidContents[fluidOutputs[0]].isFluidEqual(fluidStack)) {
                            fluidContents[fluidOutputs[0]].amount += fluidStack.amount;
                        }
                    }
                }
            }
        }
    }

    public void consumeInputs(){
        if(currentRecipe instanceof RecipeEntryMachine) {
            RecipeEntryMachine recipe = ((RecipeEntryMachine) currentRecipe);
            for (int itemInput : itemInputs) {
                ItemStack inputStack = getStackInSlot(itemInput);
                if(inputStack != null && inputStack.getItem().hasContainerItem()){
                    setInventorySlotContents(itemInput, new ItemStack(inputStack.getItem().getContainerItem()));
                } else if (inputStack != null) {
                    Optional<ItemStack> recipeStack = Arrays.stream(recipe.getInput())
                            .flatMap(symbol -> symbol.resolve().stream())
                            .filter(Objects::nonNull)
                            .filter(stack -> stack.isItemEqual(inputStack))
                            .findFirst();
                    recipeStack.ifPresent(stack -> inputStack.stackSize -= stack.stackSize);
                    if (inputStack.stackSize <= 0) {
                        setInventorySlotContents(itemInput, null);
                    }
                }
            }
            for (int fluidInput : fluidInputs) {
                FluidStack inputStack = getFluidInSlot(fluidInput);
                if(inputStack != null){
                    Optional<FluidStack> recipeStack = Arrays.stream(recipe.getInput())
                            .flatMap(symbol -> symbol.resolveFluids().stream())
                            .filter(Objects::nonNull)
                            .filter(stack -> stack.isFluidEqual(inputStack))
                            .findFirst();
                    recipeStack.ifPresent(stack -> inputStack.amount -= stack.amount);
                    if (inputStack.amount <= 0) {
                        setFluidInSlot(fluidInput, null);
                    }
                }
            }
        } else if (currentRecipe instanceof RecipeEntryMachineFluid) {
            RecipeEntryMachineFluid recipe = ((RecipeEntryMachineFluid) currentRecipe);
            for (int itemInput : itemInputs) {
                ItemStack inputStack = getStackInSlot(itemInput);
                if(inputStack != null && inputStack.getItem().hasContainerItem()){
                    setInventorySlotContents(itemInput, new ItemStack(inputStack.getItem().getContainerItem()));
                } else if (inputStack != null) {
                    Optional<ItemStack> recipeStack = Arrays.stream(recipe.getInput())
                            .flatMap(symbol -> symbol.resolve().stream())
                            .filter(Objects::nonNull)
                            .filter(stack -> stack.isItemEqual(inputStack))
                            .findFirst();
                    recipeStack.ifPresent(stack -> inputStack.stackSize -= stack.stackSize);
                    if (inputStack.stackSize <= 0) {
                        setInventorySlotContents(itemInput, null);
                    }
                }
            }
            for (int fluidInput : fluidInputs) {
                FluidStack inputStack = getFluidInSlot(fluidInput);
                if(inputStack != null){
                    Optional<FluidStack> recipeStack = Arrays.stream(recipe.getInput())
                            .flatMap(symbol -> symbol.resolveFluids().stream())
                            .filter(Objects::nonNull)
                            .filter(stack -> stack.isFluidEqual(inputStack))
                            .findFirst();
                    recipeStack.ifPresent(stack -> inputStack.amount -= stack.amount);
                    if (inputStack.amount <= 0) {
                        setFluidInSlot(fluidInput, null);
                    }
                }
            }
        }
    }

    public boolean canProcess(){
        if(currentRecipe instanceof RecipeEntryMachine){
            RecipeEntryMachine recipe = ((RecipeEntryMachine) currentRecipe);
            ItemStack stack = recipe.getOutput();
            if (stack == null) {
                return false;
            }
            return areItemOutputsValid(stack);
        } else if (currentRecipe instanceof RecipeEntryMachineFluid) {
            RecipeEntryMachineFluid recipe = ((RecipeEntryMachineFluid) currentRecipe);
            FluidStack fluidStack = recipe.getOutput();
            if(fluidStack == null){
                return false;
            }
            return areFluidOutputsValid(fluidStack);
        }
        return false;
    }

    public boolean areItemOutputsValid(ItemStack stack){
        for (int itemOutput : itemOutputs) {
            ItemStack outputStack = getStackInSlot(itemOutput);
            if (outputStack != null) {
                if (outputStack.isItemEqual(stack)) {
                    if ((outputStack.stackSize >= getInventoryStackLimit() || outputStack.stackSize >= outputStack.getMaxStackSize()) && outputStack.stackSize >= stack.getMaxStackSize()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean areFluidOutputsValid(FluidStack stack){
        for (int fluidOutput : fluidOutputs) {
            FluidStack outputStack = getFluidInSlot(fluidOutput);
            if (outputStack != null) {
                if (outputStack.isFluidEqual(stack)) {
                    if (stack.amount > getRemainingCapacity(fluidOutput)) {
                        return false;
                    }
                    return false;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void setCurrentRecipe(){
        ArrayList<RecipeExtendedSymbol> symbols = new ArrayList<>();
        Arrays.stream(itemInputs).forEach((id)->{
            if (getStackInSlot(id) != null) {
                symbols.add(new RecipeExtendedSymbol(getStackInSlot(id)));
            }
        });
        Arrays.stream(fluidInputs).forEach((id)->{
            if (getFluidInSlot(id) != null) {
                symbols.add(new RecipeExtendedSymbol(getFluidInSlot(id)));
            }
        });
        currentRecipe = recipeGroup.findRecipe(symbols.toArray(new RecipeExtendedSymbol[0]),tier,recipeId);
    }

}
