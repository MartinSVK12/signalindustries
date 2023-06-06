package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.CrystalCutterRecipes;
import sunsetsatellite.signalindustries.recipes.InfuserRecipes;
import sunsetsatellite.signalindustries.recipes.MachineRecipesBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntityInfuser extends TileEntityTieredMachine implements IBoostable {

    public MachineRecipesBase<ArrayList<Object>, ItemStack> recipes = InfuserRecipes.instance;

    public TileEntityInfuser(){
        cost = 80;
        progressMaxTicks = 400;
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 4000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        itemContents = new ItemStack[3];
        //acceptedFluids.get(1).add((BlockFluid) Block.fluidWaterFlowing);
    }
    @Override
    public String getInvName() {
        return "Infuser";
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        extractFluids();
        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (itemContents[0] == null) {
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = 400 / speedMultiplier;
        }
        if (!worldObj.isMultiplayerAndNotHost) {
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
            ArrayList<Object> list = new ArrayList<>();
            list.add(this.fluidContents[1]);
            list.add(this.itemContents[0]);
            list.add(this.itemContents[1]);
            ItemStack stack = recipes.getResult(list);
            Map.Entry<ArrayList<Object>, ItemStack> recipe = ((InfuserRecipes)recipes).getValidRecipe(list);
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
                if(itemContents[0] != null && recipe.getKey().get(1) != null){
                    this.itemContents[0].stackSize -= ((ItemStack)recipe.getKey().get(1)).stackSize;
                }
                if(itemContents[1] != null && recipe.getKey().get(2) != null){
                    this.itemContents[1].stackSize -= ((ItemStack)recipe.getKey().get(2)).stackSize;
                }
                if(fluidContents[1] != null && recipe.getKey().get(0) != null){
                    this.fluidContents[1].amount -= ((FluidStack)recipe.getKey().get(0)).amount;
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
            ArrayList<Object> list = new ArrayList<>();
            list.add(this.fluidContents[1]);
            list.add(this.itemContents[0]);
            list.add(this.itemContents[1]);
            ItemStack stack = recipes.getResult(list);
            return stack != null && (itemContents[2] == null || (itemContents[2].isItemEqual(stack) && (itemContents[2].stackSize < getInventoryStackLimit() && itemContents[2].stackSize < itemContents[2].getMaxStackSize() || itemContents[2].stackSize < stack.getMaxStackSize())));
        }
    }


}
