package sunsetsatellite.signalindustries.tiles;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.recipes.BasicExtractorRecipes;
import sunsetsatellite.signalindustries.recipes.ExtractorRecipes;
import sunsetsatellite.signalindustries.recipes.MachineRecipesBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntityExtractor extends TileEntityTieredMachine implements IBoostable {

    public MachineRecipesBase<Integer, FluidStack> recipes = ExtractorRecipes.instance;

    public TileEntityExtractor(){
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }
    @Override
    public String getInvName() {
        return "Extractor";
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        worldObj.markBlocksDirty(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
        extractFluids();
        for(int i = 0; i < itemContents.length; i++){
            if(itemContents[i] != null && itemContents[i].stackSize <= 0){
                itemContents[i] = null;
            }
        }
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            switch (block.tier){
                case PROTOTYPE:
                    recipes = ExtractorRecipes.instance;
                    break;
                case BASIC:
                    recipes = BasicExtractorRecipes.instance;
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
        if(!worldObj.isMultiplayerAndNotHost){
            if (progressTicks == 0 && canProcess() && fuelBurnTicks < 2){
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
                if(fuelBurnTicks < 2){
                    fuel();
                }
            }
        }

        if(update) {
            this.onInventoryChanged();
        }
    }

    public boolean fuel(){
        int burn = getItemBurnTime(itemContents[1]);
        if(burn > 0 && canProcess()){
            progressMaxTicks = 200 / speedMultiplier;
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            if(itemContents[1].getItem().hasContainerItem()) {
                itemContents[1] = new ItemStack(itemContents[1].getItem().getContainerItem());
            } else {
                itemContents[1].stackSize--;
                if(itemContents[1].stackSize == 0) {
                    itemContents[1] = null;
                }
            }
            return true;
        }
        return false;
    }

    public void processItem(){
        if(canProcess()){
            FluidStack stack = recipes.getResult(this.itemContents[0].getItem().itemID);
            if(fluidContents[0] == null){
                setFluidInSlot(0, stack);
            } else if(getFluidInSlot(0).getLiquid() == stack.getLiquid()) {
                fluidContents[0].amount += stack.amount;
            }
            if(this.itemContents[0].getItem().hasContainerItem()) {
                this.itemContents[0] = new ItemStack(this.itemContents[0].getItem().getContainerItem());
            } else {
                --this.itemContents[0].stackSize;
            }
            if(this.itemContents[0].stackSize <= 0) {
                this.itemContents[0] = null;
            }
        }
    }

    private boolean canProcess() {
        if(itemContents[0] == null) {
            return false;
        } else {
            FluidStack stack = recipes.getResult(itemContents[0].itemID);
            return stack != null && (fluidContents[0] == null || (fluidContents[0].isFluidEqual(stack) && (fluidContents[0].amount + stack.amount <= fluidCapacity[0])));
        }
    }

    private int getItemBurnTime(ItemStack stack) {
        return stack == null ? 0 : LookupFuelFurnace.fuelFurnace().getFuelYield(stack.getItem().itemID);
    }

}
