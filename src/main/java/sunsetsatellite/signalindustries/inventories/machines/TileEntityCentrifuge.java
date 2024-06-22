package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.recipes.SIRecipes;

import java.util.ArrayList;
import java.util.Arrays;


public class TileEntityCentrifuge extends TileEntityTieredMachineSimple implements IBoostable {
    public TileEntityCentrifuge(){
        itemContents = new ItemStack[2];
        fluidContents = new FluidStack[5];
        fluidCapacity = new int[5];
        Arrays.fill(fluidCapacity,8000);
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIBlocks.burntSignalumFlowing);
        acceptedFluids.get(1).add(SIBlocks.burntSignalumFlowing);
        acceptedFluids.get(2).add(SIBlocks.burntSignalumFlowing);
        acceptedFluids.get(3).add(SIBlocks.burntSignalumFlowing);

        acceptedFluids.get(4).add(SIBlocks.energyFlowing);

        energySlot = 4;
        itemOutputs = new int[]{0};
        fluidInputs = new int[]{0,1,2,3};
        recipeGroup = SIRecipes.CENTRIFUGE;

    }
    @Override
    public String getInvName() {
        return "Separation Centrifuge";
    }

    /*@Override
    public void tick() {
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        extractFluids();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null) {
            tier = block.tier;
            speedMultiplier = 1;
        }
        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(canProcess()) {
            progressMaxTicks = 400 / speedMultiplier;
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

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[4]);
        if(burn > 0 && canProcess() && fluidContents[4].amount >= cost){
            progressMaxTicks = 400 / speedMultiplier;//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[4].amount -= cost;
            if(fluidContents[4].amount == 0) {
                fluidContents[4] = null;
            }
            return true;
        }
        return false;
    }

    public void processItem(){
        if(canProcess()){
            ItemStack stack = SIRecipes.CENTRIFUGE.findOutput(RecipeExtendedSymbol.arrayOf(fluidContents[0],fluidContents[1],fluidContents[2],fluidContents[3]),tier);
            RecipeEntryMachine recipe = SIRecipes.CENTRIFUGE.findRecipe(RecipeExtendedSymbol.arrayOf(fluidContents[0],fluidContents[1],fluidContents[2],fluidContents[3]),tier);
            Random random = new Random();
            if(random.nextFloat() < 0.25){
                if(itemContents[0] == null){
                    setInventorySlotContents(0, stack);
                } else if(itemContents[0].isItemEqual(stack)) {
                    itemContents[0].stackSize++;
                }
            }
            if(fluidContents[0] != null && recipe.getInput()[0] != null) fluidContents[0].amount -= recipe.getInput()[0].resolveFluids().get(0).amount;
            if(fluidContents[1] != null && recipe.getInput()[1] != null) fluidContents[1].amount -= recipe.getInput()[1].resolveFluids().get(0).amount;
            if(fluidContents[2] != null && recipe.getInput()[2] != null) fluidContents[2].amount -= recipe.getInput()[2].resolveFluids().get(0).amount;
            if(fluidContents[3] != null && recipe.getInput()[3] != null) fluidContents[3].amount -= recipe.getInput()[3].resolveFluids().get(0).amount;
            if(fluidContents[0] != null && fluidContents[0].amount <= 0) fluidContents[0] = null;
            if(fluidContents[1] != null && fluidContents[1].amount <= 0) fluidContents[1] = null;
            if(fluidContents[2] != null && fluidContents[2].amount <= 0) fluidContents[2] = null;
            if(fluidContents[3] != null && fluidContents[3].amount <= 0) fluidContents[3] = null;
        }
    }

    private boolean canProcess() {
        ItemStack stack = SIRecipes.CENTRIFUGE.findOutput(RecipeExtendedSymbol.arrayOf(fluidContents[0],fluidContents[1],fluidContents[2],fluidContents[3]),tier);
        return stack != null && (itemContents[0] == null || (itemContents[0].isItemEqual(stack) && (itemContents[0].stackSize < getInventoryStackLimit() && itemContents[0].stackSize < itemContents[0].getMaxStackSize() || itemContents[0].stackSize < stack.getMaxStackSize())));
    }*/
}
