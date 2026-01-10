package sunsetsatellite.signalindustries.tiles.machines;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.*;
import java.util.stream.Collectors;

public class TileEntityPump extends TileEntityTieredMachineBase implements IBoostable {

    public BlockInstance currentBlock = null;
    public Fluid currentFluid = null;
    public RecipeEntrySI<?,?, RecipeProperties> currentRecipe;
    public TickTimer pumpTimer = new TickTimer(this,this::findFluid,20,true);
    public int range = 3;
    public Random rand = new Random();


    public TileEntityPump(){
        fluidContents = new FluidStack[2];
        itemContents = new ItemStack[0];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 2000;
        progressMaxTicks = 600;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(1).addAll(Fluid.fluidMap.values().stream().filter((F)->F != SIFluids.ENERGY).collect(Collectors.toList()));
        acceptedFluids.get(0).add(SIFluids.ENERGY);
    }

    public void findFluid(){
        if(worldObj == null) return;
        if(currentBlock == null || currentRecipe == null || currentFluid == null){
            Set<Fluid> pumpableFluids = new HashSet<>();
            for (RecipeEntryMachineFluid recipe : SIRecipes.PUMP.getAllRecipes()) {
                pumpableFluids.add(recipe.getOutput().fluid);
            }
            for (int pumpX = x-range; pumpX < x+range; pumpX++) {
                for (int pumpY = y-1; pumpY > y-range-1; pumpY--) {
                    for (int pumpZ = z-range; pumpZ < z + range; pumpZ++) {
                        Block<?> block = worldObj.getBlock(pumpX,pumpY,pumpZ);
                        int metadata = worldObj.getBlockMetadata(pumpX, pumpY, pumpZ);
                        BlockLogicFluid logic = Catalyst.blockLogic(block, BlockLogicFluid.class);
                        if(block != null && block == SIBlocks.eternalTreeLog && metadata == 1){
                            currentBlock = new BlockInstance(block,new Vec3i(pumpX,pumpY,pumpZ),metadata,null);
                            currentFluid = SIFluids.WORLD_RESIN;
                            currentRecipe = SIRecipes.PUMP.getItem("world_resin");
                            return;
                        }
                        if(block != null && logic != null){
                            List<Fluid> fluids = pumpableFluids.stream().filter((F) -> F.blocks.contains(block)).collect(Collectors.toList());
                            if(!fluids.isEmpty()){
                                currentBlock = new BlockInstance(block,new Vec3i(pumpX,pumpY,pumpZ),null);
                                currentFluid = fluids.get(0);
                                currentRecipe = SIRecipes.PUMP.findRecipe(RecipeExtendedSymbol.arrayOf(new FluidStack(fluids.get(0),1000)),tier);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void init(Block<?> block) {
        super.init(block);
        range = 3 * (tier.ordinal()+1);
    }

    @Override
    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
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
            this.setChanged();
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
        if(canProcess() && worldObj != null){
            FluidStack stack = SIRecipes.PUMP.findFluidOutput(new FluidStack(currentFluid),tier);
            if(currentBlock.block == SIBlocks.eternalTreeLog && currentBlock.meta == 1){
                stack = SIRecipes.PUMP.getItem("world_resin").getOutput().copy();
            }
            if(getFluidInSlot(1) == null){
                setFluidInSlot(1, stack);
            } else if(getFluidInSlot(1) != null && getFluidInSlot(1).fluid == stack.fluid) {
                fluidContents[1].amount += stack.amount;
            }
            if(currentBlock.block == SIBlocks.eternalTreeLog && currentBlock.meta == 1){
                if(rand.nextInt(8) == 0){
                    worldObj.setBlockMetadataWithNotify(currentBlock.pos.x,currentBlock.pos.y,currentBlock.pos.z,0);
                }
            } else {
                worldObj.setBlockWithNotify(currentBlock.pos.x,currentBlock.pos.y,currentBlock.pos.z,0);
            }
            currentBlock = null;
            currentFluid = null;
        }
    }

    private boolean canProcess() {
        if(currentBlock == null || currentRecipe == null || currentFluid == null){
            return false;
        }
        if(currentBlock.block == SIBlocks.eternalTreeLog && currentBlock.meta == 1){
            FluidStack stack = SIRecipes.PUMP.getItem("world_resin").getOutput();
            return stack != null && (fluidContents[1] == null || (fluidContents[1].isFluidEqual(stack) && (fluidContents[1].amount + stack.amount <= fluidCapacity[1])));
        }
        FluidStack stack = SIRecipes.PUMP.findFluidOutput(new FluidStack(currentFluid),tier);
        return stack != null && (fluidContents[1] == null || (fluidContents[1].isFluidEqual(stack) && (fluidContents[1].amount + stack.amount <= fluidCapacity[1])));
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.pump";
    }
}
