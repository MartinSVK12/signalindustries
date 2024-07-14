package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.recipes.SIRecipes;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TileEntityPump extends TileEntityTieredMachineBase implements IBoostable {

    public BlockInstance currentBlock = null;
    public RecipeEntrySI<?,?, RecipeProperties> currentRecipe;
    public TickTimer pumpTimer = new TickTimer(this,this::findFluid,20,true);
    public int range = 3;


    public TileEntityPump(){
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 2000;
        progressMaxTicks = 600;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(1).addAll(CatalystFluids.CONTAINERS.getAllFluids().stream().filter((F)->F.id != SIBlocks.energyFlowing.id).collect(Collectors.toList()));
        acceptedFluids.get(0).add(SIBlocks.energyFlowing);
    }
    @Override
    public String getInvName() {
        return "Pump";
    }

    public void findFluid(){
        if(currentBlock == null || currentRecipe == null){
            Set<Integer> pumpableFluids = new HashSet<>();
            for (RecipeEntryMachineFluid recipe : SIRecipes.PUMP.getAllRecipes()) {
                for (RecipeExtendedSymbol symbol : recipe.getInput()) {
                    for (ItemStack stack : symbol.resolve()) {
                        pumpableFluids.add(stack.itemID);
                    }
                }
            }
            for (int pumpX = x-range; pumpX < x+range; pumpX++) {
                for (int pumpY = y-1; pumpY > y-range-1; pumpY--) {
                    for (int pumpZ = z-range; pumpZ < z + range; pumpZ++) {
                        Block block = worldObj.getBlock(pumpX,pumpY,pumpZ);
                        if(block instanceof BlockFluid){
                            if(pumpableFluids.contains(block.id)){
                                currentBlock = new BlockInstance(block,new Vec3i(pumpX,pumpY,pumpZ),null);
                                currentRecipe = SIRecipes.PUMP.findRecipe(RecipeExtendedSymbol.arrayOf(new FluidStack((BlockFluid) block)),tier);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        pumpTimer.tick();
        extractFluids();

        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (fluidContents[0] == null) {
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);
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
        if(burn > 0 && canProcess() && fluidContents[0].amount >= currentRecipe.getData().cost){
            progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= currentRecipe.getData().cost;
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
        if(currentBlock == null || currentRecipe == null){
            return false;
        }
        FluidStack stack = SIRecipes.PUMP.findFluidOutput(new ItemStack(currentBlock.block),tier);
        return stack != null && (fluidContents[1] == null || (fluidContents[1].isFluidEqual(stack) && (fluidContents[1].amount + stack.amount <= fluidCapacity[1])));
    }

}
