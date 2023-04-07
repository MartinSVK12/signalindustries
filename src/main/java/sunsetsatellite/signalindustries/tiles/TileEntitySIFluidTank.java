package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.FluidAPI;
import sunsetsatellite.fluidapi.api.IPipePressurizer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntitySIFluidTank extends TileEntityFluidItemContainer implements IPipePressurizer {
    public TileEntitySIFluidTank(){
        fluidCapacity[0] = 8000;
        transferSpeed = 50;
        connections.replace(Direction.Y_POS, Connection.INPUT);
        connections.replace(Direction.Y_NEG, Connection.OUTPUT);
        for (Map.Entry<Item, BlockFluid> entry : FluidAPI.fluidRegistry.fluids.entrySet()) {
            BlockFluid V = entry.getValue();
            if(V != SignalIndustries.energyFlowing) {
                acceptedFluids.get(0).add(V);
            }
        }
    }

    @Override
    public void updateEntity() {
        fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 8000;
        transferSpeed = 50 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
        extractFluids();
        super.updateEntity();
    }

    @Override
    public String getInvName() {
        return "SI Fluid Tank";
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
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
    }

}
