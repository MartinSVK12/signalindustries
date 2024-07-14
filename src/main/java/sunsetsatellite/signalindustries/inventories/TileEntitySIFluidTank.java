package sunsetsatellite.signalindustries.inventories;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Map;

public class TileEntitySIFluidTank extends TileEntityTieredContainer implements IHasIOPreview {

    //only for infinite tier energy cell, if true, the energy cell will act as an infinite source of energy, if false, it will act as a sink destroying any energy it gets.
    //does not do anything for any other tier
    public boolean isInfiniteSource = true;
    public Tier tier;

    public IOPreview preview = IOPreview.NONE;
    public TickTimer IOPreviewTimer = new TickTimer(this,this::disableIOPreview,20,false);

    @Override
    public void disableIOPreview() {
        preview = IOPreview.NONE;
    }

    @Override
    public void setTemporaryIOPreview(IOPreview preview, int ticks) {
        IOPreviewTimer.value = ticks;
        IOPreviewTimer.max = ticks;
        IOPreviewTimer.unpause();
        this.preview = preview;
    }

    public TileEntitySIFluidTank(){
        fluidCapacity[0] = 8000;
        transferSpeed = 50;
        fluidConnections.replace(Direction.Y_POS, Connection.INPUT);
        fluidConnections.replace(Direction.Y_NEG, Connection.OUTPUT);
        for (BlockFluid fluid : CatalystFluids.CONTAINERS.getAllFluids()) {
            if(fluid != SIBlocks.energyFlowing) {
                acceptedFluids.get(0).add(fluid);
            }
        }
    }

    @Override
    public void tick() {
        IOPreviewTimer.tick();
        if(getBlockType() != null){
            this.tier = ((BlockContainerTiered)getBlockType()).tier;
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
                    }
                } else {
                    fluidCapacity[0] = Integer.MAX_VALUE;
                    transferSpeed = Integer.MAX_VALUE;
                    if(fluidContents[0] != null){
                        fluidContents[0] = null;
                    }
                }
            } else {
                fluidCapacity[0] = (int) Math.pow(2, ((BlockContainerTiered) getBlockType()).tier.ordinal()) * 8000;
                transferSpeed = 50 * (((BlockContainerTiered) getBlockType()).tier.ordinal() + 1);
            }
            extractFluids();
            super.tick();
        }
    }

    @Override
    public String getInvName() {
        return "SI Fluid Tank";
    }

    public void extractFluids(){
        for (Map.Entry<Direction, Connection> e : fluidConnections.entrySet()) {
            Direction dir = e.getKey();
            Connection connection = e.getValue();
            TileEntity tile = dir.getTileEntity(worldObj,this);
            if (tile instanceof TileEntityFluidPipe) {
                moveFluids(dir, (TileEntityFluidPipe) tile);
                ((TileEntityFluidPipe) tile).rememberTicks = 100;
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
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
