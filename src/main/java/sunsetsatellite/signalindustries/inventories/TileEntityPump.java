package sunsetsatellite.signalindustries.inventories;



import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.recipes.MachineRecipesBase;
import sunsetsatellite.signalindustries.recipes.PumpRecipes;
import sunsetsatellite.sunsetutils.util.BlockInstance;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.ArrayList;
import java.util.Set;

public class TileEntityPump extends TileEntityTieredMachine implements IBoostable {

    public MachineRecipesBase<Integer, FluidStack> recipes = PumpRecipes.instance;
    public BlockInstance currentBlock = null;
    public int range = 3;
    public TileEntityPump(){
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 2000;
        progressMaxTicks = 600;
        cost = 10;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(1).add((BlockFluid) Block.fluidWaterFlowing);
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }
    @Override
    public String getInvName() {
        return "Pump";
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        extractFluids();
        if(currentBlock == null){
            Set<Integer> pumpableFluids = recipes.getRecipeList().keySet();
            for(int x = xCoord-range; x < xCoord+range; x++){
                for(int y = yCoord-range; y < yCoord+range; y++){
                    for(int z = zCoord-range; z < zCoord+range; z++){
                        int id = worldObj.getBlockId(x,y,z);
                        if(pumpableFluids.contains(id)){
                            currentBlock = new BlockInstance(Block.getBlock(id),new Vec3i(x,y,z),null);
                        }
                    }
                }
            }
        }
        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (fluidContents[0] == null) {
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = 600 / speedMultiplier;
        }
        if (!worldObj.isMultiplayerAndNotHost) {
            if (progressTicks == 0 && canProcess()) {
                update = fuel();
            } else if(progressTicks > 0 && fuelBurnTicks <= 0 && canProcess()){
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
            progressMaxTicks = 600 / speedMultiplier;//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
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
            FluidStack stack = recipes.getResult(currentBlock.block.blockID);
            if(fluidContents[1] == null){
                setFluidInSlot(1, stack);
            } else if(getFluidInSlot(1).getLiquid() == stack.getLiquid()) {
                fluidContents[1].amount += stack.amount;
            }
            worldObj.setBlockWithNotify(currentBlock.pos.x,currentBlock.pos.y,currentBlock.pos.z,0);
            currentBlock = null;
        }
    }

    private boolean canProcess() {
        if(currentBlock == null){
            return false;
        }
        FluidStack stack = recipes.getResult(currentBlock.block.blockID);
        return stack != null && (fluidContents[1] == null || (fluidContents[1].isFluidEqual(stack) && (fluidContents[1].amount + stack.amount <= fluidCapacity[1])));
    }

}
