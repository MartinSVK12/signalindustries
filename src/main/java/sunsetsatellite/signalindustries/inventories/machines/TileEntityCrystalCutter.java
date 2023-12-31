package sunsetsatellite.signalindustries.inventories.machines;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachine;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;

import java.util.ArrayList;

public class TileEntityCrystalCutter extends TileEntityTieredMachine implements IBoostable {

    //public CrystalCutterRecipes recipes = CrystalCutterRecipes.getInstance();

    public int recipeSelector = 0;

    public TileEntityCrystalCutter(){
        cost = 80;
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 1000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        acceptedFluids.get(1).add((BlockFluid) Block.fluidWaterFlowing);
    }
    @Override
    public String getInvName() {
        return "Crystal Cutter";
    }

    @Override
    public void readFromNBT(CompoundTag nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        recipeSelector = nBTTagCompound1.getInteger("SelectedRecipe");
    }

    @Override
    public void writeToNBT(CompoundTag nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        nBTTagCompound1.putInt("SelectedRecipe",recipeSelector);
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        extractFluids();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null) {
            tier = block.tier;
            switch (block.tier) {
                case PROTOTYPE:
                    fluidCapacity[0] = 2000;
                    fluidCapacity[1] = 1000;
                    break;
                case BASIC:
                    fluidCapacity[0] = 4000;
                    fluidCapacity[1] = 4000;
                    break;
                case REINFORCED:
                case AWAKENED:
                    break;
            }
        }
        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(itemContents[0] == null){
            progressTicks = 0;
        } else if(canProcess()) {
            progressMaxTicks = 200 / speedMultiplier;
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
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if(burn > 0 && canProcess() && fluidContents[0].amount >= cost){
            progressMaxTicks = 200 / speedMultiplier;//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
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
            ItemStack stack = SIRecipes.CRYSTAL_CUTTER.findOutput(
                    RecipeExtendedSymbol.arrayOf(fluidContents[1],itemContents[0]),
                    tier,
                    recipeSelector);
            RecipeEntryMachine recipe = SIRecipes.CRYSTAL_CUTTER.findRecipe(
                    RecipeExtendedSymbol.arrayOf(fluidContents[1],itemContents[0]),
                    tier,
                    recipeSelector);
            if(itemContents[1] == null){
                setInventorySlotContents(1, stack);
            } else if(itemContents[1].isItemEqual(stack)) {
                itemContents[1].stackSize++;
            }
            if(this.itemContents[0].getItem().hasContainerItem()) {
                this.itemContents[0] = new ItemStack(this.itemContents[0].getItem().getContainerItem());
            } else {
                this.itemContents[0].stackSize -= recipe.getInput()[1].resolve().get(0).stackSize;
                this.fluidContents[1].amount -= recipe.getInput()[0].resolveFluids().get(0).amount;
            }
            if(this.itemContents[0].stackSize <= 0) {
                this.itemContents[0] = null;
            }
            if(this.fluidContents[1].amount <= 0) {
                this.fluidContents[1] = null;
            }
        }
    }

    private boolean canProcess() {
        if(itemContents[0] == null || fluidContents[1] == null) {
            return false;
        } else {
            ItemStack stack = SIRecipes.CRYSTAL_CUTTER.findOutput(
                    RecipeExtendedSymbol.arrayOf(fluidContents[1],itemContents[0]),
                    tier,
                    recipeSelector);
            return stack != null && (itemContents[1] == null || (itemContents[1].isItemEqual(stack) && (itemContents[1].stackSize < getInventoryStackLimit() && itemContents[1].stackSize < itemContents[1].getMaxStackSize() || itemContents[1].stackSize < stack.getMaxStackSize())));
        }
    }

}
