package sunsetsatellite.signalindustries.inventories;


import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;
import sunsetsatellite.signalindustries.recipes.legacy.CrystalChamberRecipes;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;

import java.util.ArrayList;

public class TileEntityCrystalChamber extends TileEntityTieredMachine implements IBoostable {

    //public CrystalChamberRecipes recipes = CrystalChamberRecipes.instance;

    public TileEntityCrystalChamber(){
        cost = 80;
        progressMaxTicks = 800;
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 4000;
        itemContents = new ItemStack[3];
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }
    @Override
    public String getInvName() {
        return "Crystal Chamber";
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        extractFluids();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null) {
            tier = block.tier;
            cost = 40 * (block.tier.ordinal()+1);
        }
        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(itemContents[0] == null){
            progressTicks = 0;
        } else if(canProcess()) {
            progressMaxTicks = 800 / speedMultiplier;
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
            progressMaxTicks = 800 / speedMultiplier;
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
            int size1 = this.itemContents[0].getData().getInteger("size");
            int sat1 = this.itemContents[0].getData().getInteger("saturation");
            int size2 = this.itemContents[2].getData().getInteger("size");
            int sat2 = this.itemContents[2].getData().getInteger("saturation");
            ItemStack stack = SIRecipes.CRYSTAL_CHAMBER.findOutput(
                    RecipeExtendedSymbol.arrayOf(itemContents[2],itemContents[0]),
                    tier);
            stack.getData().putInt("size",size1+size2);
            stack.getData().putInt("saturation",sat1+sat2);
            if(itemContents[1] == null){
                setInventorySlotContents(1, stack);
            } else if(itemContents[1].isItemEqual(stack)) {
                itemContents[1].stackSize++;
            }
            if(this.itemContents[0].getItem().hasContainerItem()) {
                this.itemContents[0] = new ItemStack(this.itemContents[0].getItem().getContainerItem());
            } else if(this.itemContents[2].getItem().hasContainerItem()){
                this.itemContents[2] = new ItemStack(this.itemContents[2].getItem().getContainerItem());
            } else {
                --this.itemContents[0].stackSize;
                --this.itemContents[2].stackSize;
            }
            if(this.itemContents[0].stackSize <= 0) {
                this.itemContents[0] = null;
            }
            if(this.itemContents[2].stackSize <= 0) {
                this.itemContents[2] = null;
            }
        }
    }

    private boolean canProcess() {
        if(itemContents[0] == null || itemContents[2] == null) {
            return false;
        } else {
            ItemStack c1 = this.itemContents[0];
            ItemStack c2 = this.itemContents[2];
            if(c1.getData().getInteger("size") + c2.getData().getInteger("size") > 8){
                return false;
            }
            ItemStack stack = SIRecipes.CRYSTAL_CHAMBER.findOutput(
                    RecipeExtendedSymbol.arrayOf(c2,c1),
                    tier);
            return stack != null && (itemContents[1] == null || (itemContents[1].isItemEqual(stack) && (itemContents[1].stackSize < getInventoryStackLimit() && itemContents[1].stackSize < itemContents[1].getMaxStackSize() || itemContents[1].stackSize < stack.getMaxStackSize())));
        }
    }

}
