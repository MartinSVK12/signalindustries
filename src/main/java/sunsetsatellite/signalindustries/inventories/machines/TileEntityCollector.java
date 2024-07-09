package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.SIRecipes;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.util.MeteorLocation;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TileEntityCollector extends TileEntityTieredMachineBase implements IBoostable {

    public RecipeGroupSI<?> recipeGroup;
    public RecipeEntrySI<?,?, RecipeProperties> currentRecipe;
    public int recipeId = 0;
    public boolean enhanced = false;

    public int[] itemInputs = new int[]{0};
    public int[] fluidOutputs = new int[]{0};
    public Random random = new Random();


    public TileEntityCollector(){
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 4000;
        acceptedFluids.get(0).add(SIBlocks.energyFlowing);
        fuelMaxBurnTicks = 1;
        recipeGroup = SIRecipes.COLLECTOR;
    }

    public void work(){
        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(areAllInputsNull()){
            progressTicks = 0;
            fuelBurnTicks = 0;
        } else if(canProcess()) {
            progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);
            fuelBurnTicks = 1;
        }
        if(!worldObj.isClientSide){
            if(isBurning() && canProcess()){
                progressTicks++;
                if(progressTicks >= progressMaxTicks){
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            }
        }

        if(update) {
            this.onInventoryChanged();
        }
    }

    public void processItem(){
        if(canProcess()){
            if (currentRecipe instanceof RecipeEntryMachineFluid) {
                RecipeEntryMachineFluid recipe = ((RecipeEntryMachineFluid) currentRecipe);
                FluidStack fluidStack = recipe.getOutput() == null ? null : recipe.getOutput().copy();
                if (fluidStack != null) {
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

    public boolean canProcess(){
    if (currentRecipe instanceof RecipeEntryMachineFluid) {
        if(!worldObj.canExistingBlockSeeTheSky(x,y,z)) return false;
        RecipeEntryMachineFluid recipe = ((RecipeEntryMachineFluid) currentRecipe);
        FluidStack fluidStack = recipe.getOutput();
        if(fluidStack == null){
            return false;
        }
            return areFluidOutputsValid(fluidStack);
        }
        return false;
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
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean areAllInputsNull(){
        return Arrays.stream(itemInputs).allMatch(slot -> itemContents[slot] == null);
    }

    public void setCurrentRecipe(){
        ArrayList<RecipeExtendedSymbol> symbols = new ArrayList<>();
        Arrays.stream(itemInputs).forEach((id)->{
            if (getStackInSlot(id) != null) {
                symbols.add(new RecipeExtendedSymbol(getStackInSlot(id)));
            }
        });
        currentRecipe = recipeGroup.findRecipe(symbols.toArray(new RecipeExtendedSymbol[0]),tier,recipeId);
    }

    @Override
    public void tick() {
        super.tick();
        extractFluids();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            setCurrentRecipe();
            if(!disabled) work();
            if(!enhanced){
                double distance = 24;
                for (MeteorLocation meteorLocation : SignalIndustries.meteorLocations) {
                    ChunkCoordinates location = meteorLocation.location;
                    if(location.getSqDistanceTo(x, y, z) < distance && meteorLocation.type == MeteorLocation.Type.SIGNALUM){
                        enhanced = true;
                    }
                }
            }
        }
    }

    @Override
    public void applyModifiers() {
        super.applyModifiers();
        yield = enhanced ? 2 : 1;
    }

    @Override
    public String getInvName() {
        return "Signalum Collector";
    }
}
