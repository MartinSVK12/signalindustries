package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.fluidapi.util.Connection;
import sunsetsatellite.fluidapi.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.AlloySmelterRecipes;
import sunsetsatellite.signalindustries.recipes.CrusherRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntityAlloySmelter extends TileEntityFluidItemContainer {

    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public int efficiency = 1;
    public int speedMultiplier = 1;
    public int cost = 40;

    public TileEntityAlloySmelter(){
        itemContents = new ItemStack[3];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }
    @Override
    public String getInvName() {
        return "Alloy Smelter";
    }

    @Override
    public void updateEntity() {
        worldObj.markBlocksDirty(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
        extractFluids();
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

    public int getProgressScaled(int paramInt) {
        return this.progressTicks * paramInt / progressMaxTicks;
    }

    public int getBurnTimeRemainingScaled(int paramInt) {
        if(this.fuelMaxBurnTicks == 0) {
            this.fuelMaxBurnTicks = 200;
        }
        return this.fuelBurnTicks * paramInt / this.fuelMaxBurnTicks;
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
            ItemStack stack = AlloySmelterRecipes.getInstance().getResult(new Item[]{this.itemContents[2].getItem(), this.itemContents[0].getItem()});
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
            ItemStack stack = AlloySmelterRecipes.getInstance().getResult(new Item[]{this.itemContents[2].getItem(), this.itemContents[0].getItem()});
            return stack != null && (itemContents[1] == null || (itemContents[1].isItemEqual(stack) && (itemContents[1].stackSize < getInventoryStackLimit() && itemContents[1].stackSize < itemContents[1].getMaxStackSize() || itemContents[1].stackSize < stack.getMaxStackSize())));
        }
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
        NBTTagCompound connectionsTag = new NBTTagCompound();
        for (Map.Entry<Direction, Connection> entry : connections.entrySet()) {
            Direction dir = entry.getKey();
            Connection con = entry.getValue();
            connectionsTag.setInteger(String.valueOf(dir.ordinal()),con.ordinal());
        }
        nBTTagCompound1.setCompoundTag("fluidConnections",connectionsTag);
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        fuelBurnTicks = nBTTagCompound1.getShort("BurnTime");
        progressTicks = nBTTagCompound1.getShort("ProcessTime");
        progressMaxTicks = nBTTagCompound1.getInteger("MaxProcessTime");
        fuelMaxBurnTicks = nBTTagCompound1.getShort("MaxBurnTime");
        NBTTagCompound connectionsTag = nBTTagCompound1.getCompoundTag("fluidConnections");
        for (Object con : connectionsTag.func_28110_c()) {
            connections.replace(Direction.values()[Integer.parseInt(((NBTTagInt)con).getKey())],Connection.values()[((NBTTagInt)con).intValue]);
        }
    }



}
