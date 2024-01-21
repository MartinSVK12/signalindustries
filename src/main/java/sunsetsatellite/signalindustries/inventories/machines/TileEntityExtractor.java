package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.client.Minecraft;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.crafting.LookupFuelFurnace;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachine;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;

public class TileEntityExtractor extends TileEntityTieredMachine implements IBoostable {

    //public MachineRecipesBase<Integer, FluidStack> recipes = ExtractorRecipes.instance;

    public TileEntityExtractor(){
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }
    @Override
    public String getInvName() {
        return "Extractor";
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        extractFluids();
        for(int i = 0; i < itemContents.length; i++){
            if(itemContents[i] != null && itemContents[i].stackSize <= 0){
                itemContents[i] = null;
            }
        }
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            tier = block.tier;
            /*switch (block.tier){
                case PROTOTYPE:
                    recipes = ExtractorRecipes.instance;
                    break;
                case BASIC:
                    recipes = BasicExtractorRecipes.instance;
                    break;
                case REINFORCED:
                case AWAKENED:
                    break;
            }*/
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
            FluidStack stack = SIRecipes.EXTRACTOR.findFluidOutput(itemContents[0],tier);
            if(fluidContents[0] == null){
                setFluidInSlot(0, stack);
            } else if(getFluidInSlot(0).getLiquid() == stack.getLiquid()) {
                fluidContents[0].amount += stack.amount;
            }
            Minecraft.getMinecraft(this).thePlayer.triggerAchievement(SignalIndustriesAchievementPage.FROM_WITHIN);
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
            FluidStack stack = SIRecipes.EXTRACTOR.findFluidOutput(itemContents[0],tier);
            return stack != null && (fluidContents[0] == null || (fluidContents[0].isFluidEqual(stack) && (fluidContents[0].amount + stack.amount <= fluidCapacity[0])));
        }
    }

    private int getItemBurnTime(ItemStack stack) {
        return stack == null ? 0 : LookupFuelFurnace.instance.getFuelYield(stack.getItem().id);
    }

}
