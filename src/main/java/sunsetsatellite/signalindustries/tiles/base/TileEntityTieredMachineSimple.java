package sunsetsatellite.signalindustries.tiles.base;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.*;

public abstract class TileEntityTieredMachineSimple extends TileEntityTieredMachineBase implements IFluidIO, IItemIO {

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
        Block<?> block = getBlock();
        if(block != null){
            setCurrentRecipe();
            if(!disabled) work();
        }
    }

    public void work(){
        if(EnvironmentHelper.isClientWorld()) return;
        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(areAllInputsNull()){
            progressTicks = 0;
        } else if(canProcess()) {
            progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);
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
            this.setChanged();
        }
    }

    public boolean areAllInputsNull(){
        boolean itemsNull = Arrays.stream(itemInputs).allMatch(slot -> itemContents[slot] == null);
        boolean fluidsNull = Arrays.stream(fluidInputs).allMatch(slot -> fluidContents[slot] == null);
        return itemsNull && fluidsNull;
    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[energySlot]);
        if(burn > 0 && canProcess() && currentRecipe != null && fuelBurnTicks <= 0){
            if(fluidContents[energySlot].amount >= currentRecipe.getData().cost){
                progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);
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
                        int multiplier = 1;
                        float fraction = Float.parseFloat("0."+(String.valueOf(yield).split("\\.")[1]));
                        if(fraction <= 0) fraction = 1;
                        if(yield > 1 && random.nextFloat() <= fraction){
                            multiplier = (int) Math.ceil(yield);
                        }
                        if (itemContents[itemOutputs[0]] == null) {
                            stack.stackSize *= multiplier;
                            setItem(itemOutputs[0], stack);
                        } else if (itemContents[itemOutputs[0]].isItemEqual(stack)) {
                            itemContents[itemOutputs[0]].stackSize += (stack.stackSize * multiplier);
                        }
                    }
                }
            } else if (currentRecipe instanceof RecipeEntryMachineFluid) {
                RecipeEntryMachineFluid recipe = ((RecipeEntryMachineFluid) currentRecipe);
                FluidStack fluidStack = recipe.getOutput() == null ? null : recipe.getOutput().copy();
                if (fluidStack != null) {
                    consumeInputs();
                    if(random.nextFloat() <= recipe.getData().chance) {
                        int multiplier = 1;
                        float fraction = Float.parseFloat("0."+(String.valueOf(yield).split("\\.")[1]));
                        if(fraction <= 0) fraction = 1;
                        if(yield > 1 && random.nextFloat() <= fraction){
                            multiplier = (int) Math.ceil(yield);
                        }
                        if (fluidContents[fluidOutputs[0]] == null) {
                            fluidStack.amount *= multiplier;
                            setFluidInSlot(fluidOutputs[0], fluidStack);
                        } else if (fluidContents[fluidOutputs[0]].isFluidEqual(fluidStack)) {
                            fluidContents[fluidOutputs[0]].amount += (fluidStack.amount * multiplier);
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
                ItemStack inputStack = getItem(itemInput);
                if(inputStack != null && inputStack.getItem().hasContainerItem() && !recipe.getData().consumeContainers){
                    setItem(itemInput, new ItemStack(inputStack.getItem().getContainerItem()));
                } else if (inputStack != null) {
                    Optional<ItemStack> recipeStack = Arrays.stream(recipe.getInput())
                            .flatMap(symbol -> symbol.resolve().stream())
                            .filter(Objects::nonNull)
                            .filter(stack -> stack.isItemEqual(inputStack))
                            .findFirst();
                    if(inputStack.getItem().hasContainerItem() && !recipe.getData().consumeContainers){
                        setItem(itemInput, inputStack.getItem().getContainerItem().getDefaultStack());
                    } else {
                        recipeStack.ifPresent(stack -> inputStack.stackSize -= stack.stackSize);
                        if (inputStack.stackSize <= 0) {
                            setItem(itemInput, null);
                        }
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
                ItemStack inputStack = getItem(itemInput);
                if(inputStack != null && inputStack.getItem().hasContainerItem() && !recipe.getData().consumeContainers){
                    setItem(itemInput, new ItemStack(inputStack.getItem().getContainerItem()));
                } else if (inputStack != null) {
                    Optional<ItemStack> recipeStack = Arrays.stream(recipe.getInput())
                            .flatMap(symbol -> symbol.resolve().stream())
                            .filter(Objects::nonNull)
                            .filter(stack -> stack.isItemEqual(inputStack))
                            .findFirst();
                    if(inputStack.getItem().hasContainerItem() && !recipe.getData().consumeContainers){
                        setItem(itemInput, inputStack.getItem().getContainerItem().getDefaultStack());
                    } else {
                        recipeStack.ifPresent(stack -> inputStack.stackSize -= stack.stackSize);
                        if (inputStack.stackSize <= 0) {
                            setItem(itemInput, null);
                        }
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
            ItemStack outputStack = getItem(itemOutput);
            if (outputStack != null) {
                if (outputStack.isItemEqual(stack)) {
                    if(yield > 1){
                        int n = outputStack.stackSize+(stack.stackSize*((int) Math.ceil(yield)));
                        if ((n > getMaxStackSize() || n > outputStack.getMaxStackSize()) && n > stack.getMaxStackSize()) {
                            return false;
                        }
                    } else {
                        int n = outputStack.stackSize+stack.stackSize;
                        if ((n > getMaxStackSize() || n > outputStack.getMaxStackSize()) && n > stack.getMaxStackSize()) {
                            return false;
                        }
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
                    if(yield > 1){
                        if (stack.amount*Math.ceil(yield) > getRemainingCapacity(fluidOutput)) {
                            return false;
                        }
                    } else {
                        if (stack.amount > getRemainingCapacity(fluidOutput)) {
                            return false;
                        }
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
            if (getItem(id) != null) {
                symbols.add(new RecipeExtendedSymbol(getItem(id)));
            }
        });
        Arrays.stream(fluidInputs).forEach((id)->{
            if (getFluidInSlot(id) != null) {
                symbols.add(new RecipeExtendedSymbol(getFluidInSlot(id)));
            }
        });
        currentRecipe = recipeGroup.findRecipe(symbols.toArray(new RecipeExtendedSymbol[0]),tier,recipeId);
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        tag.putInt("RecipeId",recipeId);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        recipeId = tag.getInteger("RecipeId");
    }

    @Override
    public void setActiveFluidSlotForSide(Direction dir, int slot) {
        activeFluidSlots.replace(dir,slot);
    }

    @Override
    public void setFluidIOForSide(Direction dir, Connection con) {
        fluidConnections.put(dir,con);
    }



    @Override
    public void setActiveItemSlotForSide(Direction dir, int slot) {
        activeItemSlots.replace(dir,slot);
    }

    @Override
    public void setItemIOForSide(Direction dir, Connection con) {
        itemConnections.put(dir,con);
    }

    @Override
    public void cycleItemIOForSide(Direction dir) {
        switch (itemConnections.get(dir)) {
            case NONE:
                itemConnections.replace(dir, Connection.INPUT);
                break;
            case INPUT:
                itemConnections.replace(dir, Connection.OUTPUT);
                break;
            case OUTPUT:
                itemConnections.replace(dir, Connection.BOTH);
                break;
            case BOTH:
                itemConnections.replace(dir, Connection.NONE);
                break;
        }
    }

    @Override
    public void cycleActiveItemSlotForSide(Direction dir, boolean backwards) {
        int i = activeItemSlots.get(dir);
        if(!backwards){
            if(i < getContainerSize()-1){
                activeItemSlots.replace(dir,i+1);
            } else {
                activeItemSlots.replace(dir,0);
            }
        } else {
            if(i > -1){
                activeItemSlots.replace(dir,i-1);
            } else {
                activeItemSlots.replace(dir,getContainerSize()-1);
            }
        }
    }

    @Override
    public void cycleFluidIOForSide(Direction dir) {
        switch (fluidConnections.get(dir)) {
            case NONE:
                fluidConnections.replace(dir, Connection.INPUT);
                break;
            case INPUT:
                fluidConnections.replace(dir, Connection.OUTPUT);
                break;
            case OUTPUT:
                fluidConnections.replace(dir, Connection.BOTH);
                break;
            case BOTH:
                fluidConnections.replace(dir, Connection.NONE);
                break;
        }
    }

    @Override
    public void cycleActiveFluidSlotForSide(Direction dir, boolean backwards) {
        int i = activeFluidSlots.get(dir);
        if(!backwards){
            if(i < getContainerSize()-1){
                activeFluidSlots.replace(dir,i+1);
            } else {
                activeFluidSlots.replace(dir,0);
            }
        } else {
            if(i > -1){
                activeFluidSlots.replace(dir,i-1);
            } else {
                activeFluidSlots.replace(dir,getContainerSize()-1);
            }
        }
    }
}
