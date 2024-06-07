package sunsetsatellite.signalindustries.inventories;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntityEnergyCell extends TileEntityFluidItemContainer implements IHasIOPreview {

    public IOPreview preview = IOPreview.NONE;

    public TileEntityEnergyCell(){
        fluidCapacity[0] = 8000;
        transferSpeed = 50;
        fluidConnections.replace(Direction.Y_POS, Connection.INPUT);
        fluidConnections.replace(Direction.Y_NEG, Connection.OUTPUT);
        acceptedFluids.get(0).add((BlockFluid) SIBlocks.energyFlowing);
    }

    //only for infinite tier energy cell, if true, the energy cell will act as an infinite source of energy, if false, it will act as a sink destroying any energy it gets.
    //does not do anything for any other tier
    public boolean isInfiniteSource = true;

    @Override
    public void tick() {
        if(getBlockType() != null){
            if(((BlockContainerTiered)getBlockType()).tier == Tier.INFINITE){
                for (Map.Entry<Direction, Connection> entry : fluidConnections.entrySet()) {
                    if(isInfiniteSource){
                        if(entry.getValue() == Connection.INPUT || entry.getValue() == Connection.BOTH){
                            entry.setValue(Connection.OUTPUT);
                        }
                    } else {
                        if(entry.getValue() == Connection.OUTPUT || entry.getValue() == Connection.BOTH){
                            entry.setValue(Connection.INPUT);
                        }
                    }
                }
                if(isInfiniteSource){
                    fluidCapacity[0] = Integer.MAX_VALUE;
                    transferSpeed = Integer.MAX_VALUE;
                    if(fluidContents[0] != null){
                        fluidContents[0].amount = Integer.MAX_VALUE;
                    } else {
                        fluidContents[0] = new FluidStack((BlockFluid) SIBlocks.energyFlowing,Integer.MAX_VALUE);
                    }
                } else {
                    fluidCapacity[0] = Integer.MAX_VALUE;
                    transferSpeed = Integer.MAX_VALUE;
                    if(fluidContents[0] != null){
                        fluidContents[0] = null;
                    }
                }

            } else {
                fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 8000;
                transferSpeed = 50 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
            }
            extractFluids();
            super.tick();
        }
    }

    public Tier getTier(){
        if(getBlockType() != null){
            return ((BlockContainerTiered)getBlockType()).tier;
        } else {
            return Tier.PROTOTYPE;
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
        for (Map.Entry<Direction, Connection> e : fluidConnections.entrySet()) {
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
                    if (V2.get("x") == tile.x && V2.get("y") == tile.y && V2.get("z") == tile.z) {
                        return;
                    }
                }
                HashMap<String,Integer> list = new HashMap<>();
                list.put("x",tile.x);
                list.put("y",tile.y);
                list.put("z",tile.z);
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
                    if (V2.get("x") == tile.x && V2.get("y") == tile.y && V2.get("z") == tile.z) {
                        return;
                    }
                }
                HashMap<String,Integer> list = new HashMap<>();
                list.put("x",tile.x);
                list.put("y",tile.y);
                list.put("z",tile.z);
                already.add(list);
                ((TileEntityFluidPipe) tile).isPressurized = false;
                unpressurizePipes((TileEntityFluidPipe) tile,already);
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }

    @Override
    public IOPreview getPreview() {
        return preview;
    }

    @Override
    public void setPreview(IOPreview preview) {
        this.preview = preview;
    }
}
