package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachine;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;

public class TileEntityPlateFormer extends TileEntityTieredMachine implements IBoostable {

    //public PlateFormerRecipes recipes = PlateFormerRecipes.getInstance();

    public TileEntityPlateFormer(){
        cost = 20;
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }
    @Override
    public String getInvName() {
        return "Plate Former";
    }

    @Override
    public void tick() {
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        extractFluids();
        boolean update = false;
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null) {
            tier = block.tier;
        }
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
            ItemStack stack = SIRecipes.PLATE_FORMER.findOutput(itemContents[0],tier);//recipes.getResult(this.itemContents[0].getItem().id);
            if(itemContents[1] == null){
                setInventorySlotContents(1, stack);
            } else if(itemContents[1].isItemEqual(stack)) {
                itemContents[1].stackSize += stack.stackSize;
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
            ItemStack stack = SIRecipes.PLATE_FORMER.findOutput(itemContents[0],tier);
            return stack != null && (itemContents[1] == null || (itemContents[1].isItemEqual(stack) && (itemContents[1].stackSize < getInventoryStackLimit() && itemContents[1].stackSize < itemContents[1].getMaxStackSize() || itemContents[1].stackSize < stack.getMaxStackSize())));
        }
    }

}
