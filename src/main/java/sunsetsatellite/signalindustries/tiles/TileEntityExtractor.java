package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.fluidapi.util.Connection;
import sunsetsatellite.fluidapi.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.recipes.BasicExtractorRecipes;
import sunsetsatellite.signalindustries.recipes.ExtractorRecipes;
import sunsetsatellite.signalindustries.recipes.MachineRecipesBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntityExtractor extends TileEntityFluidItemContainer {

    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public int efficiency = 1;
    public int speedMultiplier = 1;
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
            speedMultiplier = block.tier.ordinal()+1;
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

    public int getProgressScaled(int paramInt) {
        return this.progressTicks * paramInt / progressMaxTicks;
    }

    public int getBurnTimeRemainingScaled(int paramInt) {
        if(this.fuelMaxBurnTicks == 0) {
            this.fuelMaxBurnTicks = 200 / speedMultiplier;;
        }
        return this.fuelBurnTicks * paramInt / this.fuelMaxBurnTicks;
    }

    public boolean fuel(){
        int burn = getItemBurnTime(itemContents[1]);
        if(burn > 0 && canProcess()){
            progressMaxTicks = 200 / speedMultiplier;;
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
                incrFluidAmount(0, stack.amount);
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
            return stack != null && (fluidContents[0] == null || (fluidContents[0].isFluidEqual(stack) && (fluidContents[0].amount < fluidCapacity[0])));
        }
    }

    private int getItemBurnTime(ItemStack stack) {
        return stack == null ? 0 : LookupFuelFurnace.fuelFurnace().getFuelYield(stack.getItem().itemID);
    }

    public boolean isBurning(){
        return fuelBurnTicks > 0;
    }

    public void extractFluids(){
        for (Map.Entry<Direction, Connection> e : connections.entrySet()) {
            Direction dir = e.getKey();
            Connection connection = e.getValue();
            TileEntity tile = dir.getTileEntity(worldObj,this);
            if (tile instanceof TileEntityFluidPipe) {
                pressurizePipes((TileEntityFluidPipe) tile, new ArrayList<>());
                moveFluids(dir, (TileEntityFluidPipe) tile, transferSpeed);
                ((TileEntityFluidPipe) tile).rememberTicks = 100;
            }
        }
    }

    public void pressurizePipes(TileEntityFluidPipe pipe, ArrayList<HashMap<String,Integer>> already){
        pipe.isPressurized = true;
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj,pipe);
            if (tile instanceof TileEntityFluidPipe) {
                for (HashMap<String, Integer> V2 : already) {
                    if (V2.get("x") == tile.xCoord && V2.get("y") == tile.yCoord && V2.get("z") == tile.zCoord) {
                        return;
                    }
                }
                HashMap<String,Integer> list = new HashMap<>();
                list.put("x",tile.xCoord);
                list.put("y",tile.yCoord);
                list.put("z",tile.zCoord);
                already.add(list);
                ((TileEntityFluidPipe) tile).isPressurized = true;
                pressurizePipes((TileEntityFluidPipe) tile,already);
            }
        }
    }

    public void unpressurizePipes(TileEntityFluidPipe pipe,ArrayList<HashMap<String,Integer>> already){
        pipe.isPressurized = false;
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj,pipe);
            if (tile instanceof TileEntityFluidPipe) {
                for (HashMap<String, Integer> V2 : already) {
                    if (V2.get("x") == tile.xCoord && V2.get("y") == tile.yCoord && V2.get("z") == tile.zCoord) {
                        return;
                    }
                }
                HashMap<String,Integer> list = new HashMap<>();
                list.put("x",tile.xCoord);
                list.put("y",tile.yCoord);
                list.put("z",tile.zCoord);
                already.add(list);
                ((TileEntityFluidPipe) tile).isPressurized = false;
                unpressurizePipes((TileEntityFluidPipe) tile,already);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        nBTTagCompound1.setShort("BurnTime", (short)this.fuelBurnTicks);
        nBTTagCompound1.setShort("ProcessTime", (short)this.progressTicks);
        nBTTagCompound1.setShort("MaxBurnTime", (short)this.fuelMaxBurnTicks);
        nBTTagCompound1.setInteger("MaxProcessTime",this.progressMaxTicks);
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        fuelBurnTicks = nBTTagCompound1.getShort("BurnTime");
        progressTicks = nBTTagCompound1.getShort("ProcessTime");
        progressMaxTicks = nBTTagCompound1.getInteger("MaxProcessTime");
        fuelMaxBurnTicks = nBTTagCompound1.getShort("MaxBurnTime");
    }



}
