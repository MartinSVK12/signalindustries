package sunsetsatellite.signalindustries.inventories;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class TileEntityPump extends TileEntityTieredMachine implements IBoostable {

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
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        extractFluids();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null) {
            tier = block.tier;
        }
        if(currentBlock == null){
            Set<Integer> pumpableFluids = SIRecipes.PUMP.getAllRecipes().stream().map((R)->R.getOutput().getLiquid().id).collect(Collectors.toSet());
            for(int x1 = x-range; x < x+range; x++){
                for(int y1 = y-range; y1 < y+range; y1++){
                    for(int z1 = z-range; z < z+range; z++){
                        int id = worldObj.getBlockId(x1,y1,z1);
                        if(pumpableFluids.contains(id)){
                            currentBlock = new BlockInstance(Block.getBlock(id),new Vec3i(x1,y1,z1),null);
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
        if (!worldObj.isClientSide) {
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
            FluidStack stack = SIRecipes.PUMP.findFluidOutput(new ItemStack(currentBlock.block),tier);
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
        FluidStack stack = SIRecipes.PUMP.findFluidOutput(new ItemStack(currentBlock.block),tier);
        return stack != null && (fluidContents[1] == null || (fluidContents[1].isFluidEqual(stack) && (fluidContents[1].amount + stack.amount <= fluidCapacity[1])));
    }

}
