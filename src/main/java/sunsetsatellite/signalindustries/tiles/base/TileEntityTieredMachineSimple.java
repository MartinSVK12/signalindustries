package sunsetsatellite.signalindustries.tiles.base;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.fluids.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.entry.*;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.*;

public abstract class TileEntityTieredMachineSimple extends TileEntityTieredMachineBase implements IFluidIO, IItemIO {

    public RecipeGroupSI<?> recipeGroup;
    public RecipeEntrySI<?, ?, RecipeProperties> currentRecipe;
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
		if(worldObj == null) return;
        worldObj.markBlockDirty(tilePos);
        extractFluids();
        Block<?> block = getBlock();
        if (block != null) {
            setCurrentRecipe();
            if (!disabled) work();
        }
    }

    public void work() {
        if (EnvironmentHelper.isClientWorld()) return;
        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (areAllInputsNull()) {
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = getTieredProgressDuration(currentRecipe.getData().ticks); //(int) (currentRecipe.getData().ticks / speedMultiplier);
        }
        if (!worldObj.isClientSide) {
            if (progressTicks == 0 && canProcess()) {
                update = fuel();
            }
            if (isBurning() && canProcess()) {
                progressTicks++;
                if (progressTicks >= progressMaxTicks) {
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if (canProcess()) {
                fuel();
                if (fuelBurnTicks > 0) {
                    fuelBurnTicks++;
                }
            }
        }

        if (update) {
            this.setChanged();
        }
    }

    public boolean areAllInputsNull() {
        boolean itemsNull = Arrays.stream(itemInputs).allMatch(slot -> itemContents[slot] == null);
        boolean fluidsNull = Arrays.stream(fluidInputs).allMatch(slot -> fluidContents[slot] == null);
        return itemsNull && fluidsNull;
    }

    public boolean fuel() {
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[energySlot]);
        if (burn > 0 && canProcess() && currentRecipe != null && fuelBurnTicks <= 0) {
            if (fluidContents[energySlot].amount >= currentRecipe.getData().cost) {
                progressMaxTicks = getTieredProgressDuration(currentRecipe.getData().ticks);
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

    //TODO: move code to recipe entry classes
    public void processItem() {
        if (canProcess()) {
            if (currentRecipe instanceof RecipeEntryMachine recipe) {
				ItemStack stack = recipe.getOutput() == null ? null : recipe.getOutput().copy();
                if (stack != null) {
                    consumeInputs();
                    if (random.nextFloat() <= recipe.getData().chance) {
                        int multiplier = 1;
                        float fraction = Float.parseFloat("0." + (String.valueOf(yield).split("\\.")[1]));
                        if (fraction <= 0) fraction = 1;
                        if (yield > 1 && random.nextFloat() <= fraction) {
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
            } else if (currentRecipe instanceof RecipeEntryMachineFluid recipe) {
				FluidStack fluidStack = recipe.getOutput() == null ? null : recipe.getOutput().copy();
                if (fluidStack != null) {
                    consumeInputs();
                    if (random.nextFloat() <= recipe.getData().chance) {
                        int multiplier = 1;
                        float fraction = Float.parseFloat("0." + (String.valueOf(yield).split("\\.")[1]));
                        if (fraction <= 0) fraction = 1;
                        if (yield > 1 && random.nextFloat() <= fraction) {
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
            } else if (currentRecipe instanceof RecipeEntryMachineRandomOutput) {
                currentRecipe.processMachineRecipe(this);
            } else if (currentRecipe instanceof RecipeEntryMachineMultiOutput) {
                currentRecipe.processMachineRecipe(this);
            }
        }
    }

    //TODO: move code to recipe entry classes
    public void consumeInputs() {
        if (currentRecipe instanceof RecipeEntryMachine recipe) {
			for (int itemInput : itemInputs) {
                ItemStack inputStack = getItem(itemInput);
                if (inputStack != null && inputStack.getItem().hasContainerItem() && !recipe.getData().consumeContainers) {
                    setItem(itemInput, new ItemStack(inputStack.getItem().getContainerItem()));
                } else if (inputStack != null) {
                    Optional<ItemStack> recipeStack = Arrays.stream(recipe.getInput())
                            .flatMap(symbol -> symbol.resolve().stream())
                            .filter(Objects::nonNull)
                            .filter(stack -> stack.isItemEqual(inputStack))
                            .findFirst();
                    if (inputStack.getItem().hasContainerItem() && !recipe.getData().consumeContainers) {
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
                if (inputStack != null) {
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
        } else if (currentRecipe instanceof RecipeEntryMachineFluid recipe) {
			for (int itemInput : itemInputs) {
                ItemStack inputStack = getItem(itemInput);
                if (inputStack != null && inputStack.getItem().hasContainerItem() && !recipe.getData().consumeContainers) {
                    setItem(itemInput, new ItemStack(inputStack.getItem().getContainerItem()));
                } else if (inputStack != null) {
                    Optional<ItemStack> recipeStack = Arrays.stream(recipe.getInput())
                            .flatMap(symbol -> symbol.resolve().stream())
                            .filter(Objects::nonNull)
                            .filter(stack -> stack.isItemEqual(inputStack))
                            .findFirst();
                    if (inputStack.getItem().hasContainerItem() && !recipe.getData().consumeContainers) {
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
                if (inputStack != null) {
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
        } else if (currentRecipe instanceof RecipeEntryMachineRandomOutput) {
            currentRecipe.consumeMachineInputs(this);
        } else if (currentRecipe instanceof RecipeEntryMachineMultiOutput) {
            currentRecipe.consumeMachineInputs(this);
        }
    }

    //TODO: move code to recipe entry classes
    public boolean canProcess() {
        if (currentRecipe instanceof RecipeEntryMachine recipe) {
			ItemStack stack = recipe.getOutput();
            if (stack == null) {
                return false;
            }
            return areItemOutputsValid(stack);
        } else if (currentRecipe instanceof RecipeEntryMachineFluid recipe) {
			FluidStack fluidStack = recipe.getOutput();
            if (fluidStack == null) {
                return false;
            }
            return areFluidOutputsValid(fluidStack);
        } else if (currentRecipe instanceof RecipeEntryMachineRandomOutput) {
            return currentRecipe.canMachineProcess(this);
        } else if (currentRecipe instanceof RecipeEntryMachineMultiOutput) {
            return currentRecipe.canMachineProcess(this);
        }
        return false;
    }

    public boolean areRecipeOutputsValid(RecipeOutputStack[] stacks) {
        Set<Integer> occupiedItemSlots = new HashSet<>();
        Set<Integer> occupiedFluidSlots = new HashSet<>();
        for (RecipeOutputStack output : stacks) {
            if (output.isItem()) {
                ItemStack stack = output.stack.copy();
                stack.stackSize = output.randomAmount ? output.amountMax : stack.stackSize;
                boolean valid = false;
                for (int itemOutput : itemOutputs) {
                    if (occupiedItemSlots.contains(itemOutput)) continue;
                    ItemStack outputStack = getItem(itemOutput);
                    if (outputStack != null && outputStack.isItemEqual(stack)) {
                        if (yield > 1) {
                            int n = outputStack.stackSize + (stack.stackSize * ((int) Math.ceil(yield)));
                            if ((n <= getMaxStackSize() && n <= outputStack.getMaxStackSize()) || n <= stack.getMaxStackSize()) {
                                occupiedItemSlots.add(itemOutput);
                                valid = true;
                                break;
                            }
                        } else {
                            int n = outputStack.stackSize + stack.stackSize;
                            if ((n <= getMaxStackSize() && n <= outputStack.getMaxStackSize()) || n <= stack.getMaxStackSize()) {
                                occupiedItemSlots.add(itemOutput);
                                valid = true;
                                break;
                            }
                        }
                    } else if (outputStack == null) {
                        occupiedItemSlots.add(itemOutput);
                        valid = true;
                        break;
                    }
                }
                if (!valid) return false;
            } else if (output.isFluid()) {
                FluidStack stack = output.fluid.copy();
                stack.amount = output.randomAmount ? output.amountMax : stack.amount;
                boolean valid = false;
                for (int fluidOutput : fluidOutputs) {
                    if (occupiedFluidSlots.contains(fluidOutput)) continue;
                    FluidStack outputStack = getFluidInSlot(fluidOutput);
                    if (outputStack != null) {
                        if (outputStack.isFluidEqual(stack)) {
                            if (yield > 1) {
                                if (!(stack.amount * Math.ceil(yield) > getRemainingCapacity(fluidOutput))) {
                                    occupiedFluidSlots.add(fluidOutput);
                                    valid = true;
                                    break;
                                }
                            } else {
                                if (stack.amount <= getRemainingCapacity(fluidOutput)) {
                                    occupiedFluidSlots.add(fluidOutput);
                                    valid = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        occupiedFluidSlots.add(fluidOutput);
                        valid = true;
                        break;
                    }
                }
                if (!valid) return false;
            }
        }
        return true;
    }

    public boolean areItemOutputsValid(ItemStack stack) {
        for (int itemOutput : itemOutputs) {
            ItemStack outputStack = getItem(itemOutput);
            if (outputStack != null) {
                if (outputStack.isItemEqual(stack)) {
                    if (yield > 1) {
                        int n = outputStack.stackSize + (stack.stackSize * ((int) Math.ceil(yield)));
                        if ((n > getMaxStackSize() || n > outputStack.getMaxStackSize()) && n > stack.getMaxStackSize()) {
                            return false;
                        }
                    } else {
                        int n = outputStack.stackSize + stack.stackSize;
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

    public boolean areFluidOutputsValid(FluidStack stack) {
        for (int fluidOutput : fluidOutputs) {
            FluidStack outputStack = getFluidInSlot(fluidOutput);
            if (outputStack != null) {
                if (outputStack.isFluidEqual(stack)) {
                    if (yield > 1) {
                        if (stack.amount * Math.ceil(yield) > getRemainingCapacity(fluidOutput)) {
                            return false;
                        }
                    } else {
                        if (stack.amount > getRemainingCapacity(fluidOutput)) {
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

    public void setCurrentRecipe() {
        ArrayList<RecipeExtendedSymbol> symbols = new ArrayList<>();
        Arrays.stream(itemInputs).forEach((id) -> {
            if (getItem(id) != null) {
                symbols.add(new RecipeExtendedSymbol(getItem(id)));
            }
        });
        Arrays.stream(fluidInputs).forEach((id) -> {
            if (getFluidInSlot(id) != null) {
                symbols.add(new RecipeExtendedSymbol(getFluidInSlot(id)));
            }
        });
        currentRecipe = recipeGroup.findRecipe(symbols.toArray(new RecipeExtendedSymbol[0]), tier, recipeId);
    }

    @Override
    public void writeAdditionalData(@NonNull CompoundTag tag) {
        super.writeToNBT(tag);
        tag.putInt("RecipeId", recipeId);
        /*if(currentRecipe != null) {
            tag.putString("CurrentRecipe",currentRecipe.toString());
        }*/
    }

    @Override
    public void readAdditionalData(@NonNull CompoundTag tag) {
        super.readFromNBT(tag);
        recipeId = tag.getInteger("RecipeId");
        /*if(currentRecipe == null && tag.containsKey("CurrentRecipe")) {
            //todo: why does this not work
            try {
                currentRecipe = (RecipeEntrySI<?, ?, RecipeProperties>) Registries.RECIPES.getRecipeFromKey(tag.getString("CurrentRecipe")).recipe;
            } catch (IllegalArgumentException ignored) {}
        }*/
    }

	@Override
	public void sort() {

	}
}
