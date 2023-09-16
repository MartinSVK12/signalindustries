package sunsetsatellite.signalindustries.inventories;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.recipes.CrystalCutterRecipes;

import java.util.ArrayList;
import java.util.Map;

public class TileEntityCrystalCutter extends TileEntityTieredMachine implements IBoostable {

    public CrystalCutterRecipes recipes = CrystalCutterRecipes.getInstance();

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
    public void updateEntity() {
        super.updateEntity();
        worldObj.markBlocksDirty(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
        extractFluids();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null) {
            tier = block.tier;
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
            ArrayList<Object> list = new ArrayList<>();
            list.add(this.fluidContents[1]);
            list.add(this.itemContents[0]);
            ItemStack stack = recipes.getResult(list);
            Map.Entry<ArrayList<Object>, ItemStack> recipe = recipes.getValidRecipe(list);
            if(itemContents[1] == null){
                setInventorySlotContents(1, stack);
            } else if(itemContents[1].isItemEqual(stack)) {
                itemContents[1].stackSize++;
            }
            if(this.itemContents[0].getItem().hasContainerItem()) {
                this.itemContents[0] = new ItemStack(this.itemContents[0].getItem().getContainerItem());
            } else {
                this.itemContents[0].stackSize -= ((ItemStack)recipe.getKey().get(1)).stackSize;
                this.fluidContents[1].amount -= ((FluidStack)recipe.getKey().get(0)).amount;
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
            ArrayList<Object> list = new ArrayList<>();
            list.add(this.fluidContents[1]);
            list.add(this.itemContents[0]);
            ItemStack stack = recipes.getResult(list);
            return stack != null && (itemContents[1] == null || (itemContents[1].isItemEqual(stack) && (itemContents[1].stackSize < getInventoryStackLimit() && itemContents[1].stackSize < itemContents[1].getMaxStackSize() || itemContents[1].stackSize < stack.getMaxStackSize())));
        }
    }

}
