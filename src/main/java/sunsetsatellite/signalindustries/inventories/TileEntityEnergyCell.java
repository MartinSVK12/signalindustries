package sunsetsatellite.signalindustries.inventories;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.IPipePressurizer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntityEnergyCell extends TileEntityFluidItemContainer implements IPipePressurizer {
    public TileEntityEnergyCell(){
        fluidCapacity[0] = 8000;
        transferSpeed = 50;
        connections.replace(Direction.Y_POS, Connection.INPUT);
        connections.replace(Direction.Y_NEG, Connection.OUTPUT);
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }

    @Override
    public void updateEntity() {
        if(getBlockType() != null){
            if(((BlockContainerTiered)getBlockType()).tier == Tier.INFINITE){
                for (Map.Entry<Direction, Connection> entry : connections.entrySet()) {
                    if(entry.getValue() == Connection.INPUT || entry.getValue() == Connection.BOTH){
                        entry.setValue(Connection.OUTPUT);
                    }
                }
                fluidCapacity[0] = Integer.MAX_VALUE;
                transferSpeed = Integer.MAX_VALUE;
                if(fluidContents[0] != null){
                    fluidContents[0].amount = Integer.MAX_VALUE;
                } else {
                   fluidContents[0] = new FluidStack((BlockFluid) SignalIndustries.energyFlowing,Integer.MAX_VALUE);
                }
            } else {
                fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 8000;
                transferSpeed = 50 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
            }
            extractFluids();
            super.updateEntity();
        }
    }

    @Override
    public void writeToNBT(CompoundTag nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
    }

    @Override
    public void readFromNBT(CompoundTag nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
    }

    @Override
    public String getInvName() {
        return "Energy Cell";
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
