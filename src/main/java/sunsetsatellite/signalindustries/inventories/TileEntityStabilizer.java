package sunsetsatellite.signalindustries.inventories;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityColorParticleFX;
import sunsetsatellite.signalindustries.interfaces.IStabilizable;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TileEntityStabilizer extends TileEntityFluidItemContainer {

    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public int efficiency = 1;
    public int speedMultiplier = 1;
    public int cost = 160;
    //public MachineRecipesBase<ArrayList<Object>, ItemStack> recipes = InfuserRecipes.instance;
    public Random random = new Random();
    public TileEntity connectedTo;

    public TileEntityStabilizer(){
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 4000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        itemContents = new ItemStack[1];
        //acceptedFluids.get(1).add((BlockFluid) Block.fluidWaterFlowing);
    }
    @Override
    public String getInvName() {
        return "Dilithium Stabilizer";
    }

    @Override
    public void updateEntity() {
        worldObj.markBlocksDirty(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
        extractFluids();
        boolean update = false;

        if(!worldObj.isClientSide){

            if(fuelBurnTicks > 0){
                fuelBurnTicks--;
            }
            /*if (progressTicks >= 0 && canProcess()){
                update = fuel();
            }*/
            if(isBurning() && canProcess()){
                if(progressTicks > 0){
                    /*SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,xCoord+random.nextFloat(),yCoord+random.nextFloat(),zCoord+random.nextFloat(),0,0,0,1.0f,1.0f,0.0f,1.0f));
                    SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,xCoord+random.nextFloat(),yCoord+random.nextFloat(),zCoord+random.nextFloat(),0,0,0,1.0f,1.0f,0.0f,1.0f));*/
                    if(connectedTo instanceof IStabilizable && ((IStabilizable) connectedTo).isActive()){

                        progressTicks--;
                        Vec3i pos = new Vec3i(xCoord,yCoord,zCoord);
                        Vec3i connectedPos = new Vec3i(connectedTo.xCoord, connectedTo.yCoord, connectedTo.zCoord);
                        if(pos.x > connectedPos.x){
                            int temp = pos.x;
                            pos.x = connectedPos.x;
                            connectedPos.x = temp;
                        }
                        if(pos.z > connectedPos.z){
                            int temp = pos.z;
                            pos.z = connectedPos.z;
                            connectedPos.z = temp;
                        }
                        for (float i = pos.x; i <= connectedPos.x; i+=0.1f) {
                            for (float k = pos.z; k <= connectedPos.z; k+=0.1f) {
                                for (float l = 0; l < 4; l++) {
                                    SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,i+0.5,yCoord+0.5,k+0.5,0,0,0,1.0f,1.0f,0.0f,1.0f,6));
                                }
                            }
                        }
                    }
                }
                if(progressTicks <= 0){
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if(canProcess()){
                if(connectedTo instanceof IStabilizable && ((IStabilizable) connectedTo).isActive()) {
                    fuel();
                    if(fuelBurnTicks > 0){
                        fuelBurnTicks++;
                    }
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
            this.fuelMaxBurnTicks = 400;
        }
        return this.fuelBurnTicks * paramInt / this.fuelMaxBurnTicks;
    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if(burn > 0 && canProcess() && fluidContents[0].amount >= cost){
            progressMaxTicks = 200 * speedMultiplier;//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
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
        if(canProcess() && progressTicks <= 0){
            progressMaxTicks = 200 * speedMultiplier;
            progressTicks = progressMaxTicks;
            itemContents[0].stackSize -= 1;
            if(itemContents[0].stackSize <= 0){
                itemContents[0] = null;
            }
        }
    }

    public boolean canProcess() {
        return ((itemContents[0] != null && itemContents[0].getItem() == SignalIndustries.dilithiumShard) || progressTicks > 0) && connectedTo instanceof IStabilizable;

        /*if(itemContents[0] == null) {
            return false;
        } else {
            ArrayList<Object> list = new ArrayList<>();
            list.add(this.fluidContents[1]);
            list.add(this.itemContents[0]);
            list.add(this.itemContents[1]);
            ItemStack stack = recipes.getResult(list);
            return stack != null && (itemContents[2] == null || (itemContents[2].isItemEqual(stack) && (itemContents[2].stackSize < getInventoryStackLimit() && itemContents[2].stackSize < itemContents[2].getMaxStackSize() || itemContents[2].stackSize < stack.getMaxStackSize())));
        }*/
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
                moveFluids(dir, (TileEntityFluidPipe) tile);
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
    public void writeToNBT(CompoundTag nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        nBTTagCompound1.putShort("BurnTime", (short)this.fuelBurnTicks);
        nBTTagCompound1.putShort("ProcessTime", (short)this.progressTicks);
        nBTTagCompound1.putShort("MaxBurnTime", (short)this.fuelMaxBurnTicks);
        nBTTagCompound1.putInt("MaxProcessTime",this.progressMaxTicks);
    }

    @Override
    public void readFromNBT(CompoundTag nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        fuelBurnTicks = nBTTagCompound1.getShort("BurnTime");
        progressTicks = nBTTagCompound1.getShort("ProcessTime");
        progressMaxTicks = nBTTagCompound1.getInteger("MaxProcessTime");
        fuelMaxBurnTicks = nBTTagCompound1.getShort("MaxBurnTime");

    }



}
