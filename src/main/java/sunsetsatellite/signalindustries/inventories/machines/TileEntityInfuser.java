package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;

import java.util.ArrayList;

public class TileEntityInfuser extends TileEntityTieredMachineSimple implements IBoostable {

   // public MachineRecipesBase<ArrayList<Object>, ItemStack> recipes = InfuserRecipes.instance;

    public TileEntityInfuser(){
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 4000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        itemContents = new ItemStack[3];
        energySlot = 0;
        recipeGroup = SIRecipes.INFUSER;
        itemInputs = new int[]{0,1};
        itemOutputs = new int[]{2};
        fluidInputs = new int[]{1};
    }
    @Override
    public String getInvName() {
        return "Infuser";
    }

    /*@Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        extractFluids();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null) {
            tier = block.tier;
        }
        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (itemContents[0] == null) {
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = 400 / speedMultiplier;
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
            this.onInventoryChanged();
        }

    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if(burn > 0 && canProcess() && fluidContents[0].amount >= cost){
            progressMaxTicks = 400 / speedMultiplier;//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= cost;
            if(fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    public void processItem(){
        if(canProcess()){
            ItemStack stack = SIRecipes.INFUSER.findOutput(
                    RecipeExtendedSymbol.arrayOf(fluidContents[1],itemContents[0],itemContents[1]),
                    tier);
            RecipeEntryMachine recipe = SIRecipes.INFUSER.findRecipe(  RecipeExtendedSymbol.arrayOf(fluidContents[1],itemContents[0],itemContents[1]),tier);
            if(itemContents[2] == null){
                setInventorySlotContents(2, stack);
            } else if(itemContents[2].isItemEqual(stack)) {
                itemContents[2].stackSize++;
            }
            if(itemContents[0] != null && this.itemContents[0].getItem().hasContainerItem()) {
                this.itemContents[0] = new ItemStack(this.itemContents[0].getItem().getContainerItem());
            } else if(itemContents[1] != null && this.itemContents[1].getItem().hasContainerItem()) {
                this.itemContents[1] = new ItemStack(this.itemContents[1].getItem().getContainerItem());
            } else {
                if(itemContents[0] != null && recipe.getInput()[1] != null){
                    this.itemContents[0].stackSize -= recipe.getInput()[1].resolve().get(0).stackSize;
                }
                if(itemContents[1] != null && recipe.getInput()[2] != null){
                    this.itemContents[1].stackSize -= recipe.getInput()[2].resolve().get(0).stackSize;
                }
                if(fluidContents[1] != null && recipe.getInput()[0] != null){
                    this.fluidContents[1].amount -=  recipe.getInput()[0].resolveFluids().get(0).amount;
                }
            }
            if(itemContents[0] != null && this.itemContents[0].stackSize <= 0) {
                this.itemContents[0] = null;
            }
            if(itemContents[1] != null && this.itemContents[1].stackSize <= 0) {
                this.itemContents[1] = null;
            }
            if(fluidContents[1] != null && this.fluidContents[1].amount <= 0) {
                this.fluidContents[1] = null;
            }

        }
    }

    private boolean canProcess() {
        if(itemContents[0] == null) {
            return false;
        } else {
            ItemStack stack = SIRecipes.INFUSER.findOutput(
                    RecipeExtendedSymbol.arrayOf(fluidContents[1],itemContents[0],itemContents[1]),
                    tier);
            return stack != null && (itemContents[2] == null || (itemContents[2].isItemEqual(stack) && (itemContents[2].stackSize < getInventoryStackLimit() && itemContents[2].stackSize < itemContents[2].getMaxStackSize() || itemContents[2].stackSize < stack.getMaxStackSize())));
        }
    }*/


}
