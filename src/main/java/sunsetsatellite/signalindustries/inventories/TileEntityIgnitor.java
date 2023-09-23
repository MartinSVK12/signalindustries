package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.fx.EntityFlameFX;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.TickTimer;

import java.util.*;

public class TileEntityIgnitor extends TileEntityFluidItemContainer {
    public boolean isActivated = false;
    private final TickTimer timer = new TickTimer(this,"work",5,true);

    public TileEntityIgnitor(){
        itemContents = new ItemStack[0];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        Arrays.fill(fluidCapacity,1000);
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        for (Direction dir : Direction.values()) {
            connections.put(dir, Connection.INPUT);
            activeFluidSlots.put(dir,0);
        }
        transferSpeed = 10;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        timer.tick();
        spreadFluids(Direction.Y_POS);
        worldObj.markBlocksDirty(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
        extractFluids();
        if(getFluidInSlot(0) != null && getFluidInSlot(0).amount <= 0) fluidContents[0] = null;
        if(isActivated && getFluidInSlot(0) != null && getFluidInSlot(0).amount >= 5){
            Random random = new Random();
            if(random.nextFloat() < 0.25){
                float xd = random.nextFloat() / 10 - 0.05f;
                float yd = random.nextFloat() / 10 - 0.05f;
                float zd = random.nextFloat() / 10 - 0.05f;
                SignalIndustries.spawnParticle(new EntityFlameFX(worldObj,xCoord+0.5,yCoord+0.5,zCoord,xd,yd,zd, EntityFlameFX.Type.ORANGE));
                SignalIndustries.spawnParticle(new EntityFlameFX(worldObj,xCoord+0.5,yCoord+0.5,zCoord+1,xd,yd,zd, EntityFlameFX.Type.ORANGE));
                SignalIndustries.spawnParticle(new EntityFlameFX(worldObj,xCoord,yCoord+0.5,zCoord+0.5,xd,yd,zd, EntityFlameFX.Type.ORANGE));
                SignalIndustries.spawnParticle(new EntityFlameFX(worldObj,xCoord+1,yCoord+0.5,zCoord+0.5,xd,yd,zd, EntityFlameFX.Type.ORANGE));
            }
        }
    }

    public void work(){
        if(isActivated && getFluidInSlot(0) != null && getFluidInSlot(0).amount >= 5){
            getFluidInSlot(0).amount-=5;
        }
    }

    public void spreadFluids(Direction dir) {
        for (Direction direction : Direction.values()) {
            connections.put(dir, Connection.BOTH);
        }
        if(getFluidInSlot(0) != null){
            this.give(dir);
        }
        /*if(tile.getFluidInSlot(0) == null && getFluidInSlot(0) != null){
            this.give(dir);
        } else if (tile.getFluidInSlot(0) != null && getFluidInSlot(0) == null) {
            this.take(tile.getFluidInSlot(0), dir);
        } else {
            if(tile.getFluidInSlot(0) == null || getFluidInSlot(0) == null) return;
            if(tile.getFluidInSlot(0).amount < getFluidInSlot(0).amount){
                this.give(dir);
            } else {
                this.take(tile.getFluidInSlot(0), dir);
            }
        }*/
        for (Direction direction : Direction.values()) {
            connections.put(dir, Connection.INPUT);
        }
    }

    public boolean isBurning(){
        return fluidContents[0] != null && fluidContents[0].liquid == SignalIndustries.energyFlowing && fluidContents[0].amount > 0 && isActivated;
    }

    public boolean isReady(){
        return fluidContents[0] != null && fluidContents[0].liquid == SignalIndustries.energyFlowing && fluidContents[0].amount >= fluidCapacity[0];
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
}
