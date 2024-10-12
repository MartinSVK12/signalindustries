package sunsetsatellite.signalindustries.inventories.machines;

import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.interfaces.IStabilizable;
import sunsetsatellite.signalindustries.inventories.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.inventories.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.inventories.TileEntityItemBus;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;

import java.util.ArrayList;

public class TileEntityWarpGate extends TileEntityTieredMachineBase implements IMultiblock, IStabilizable {

    public Multiblock multiblock;
    public TileEntityItemBus itemInput;
    public TileEntityItemBus itemOutput;
    public TileEntityFluidHatch fluidInput;
    public TileEntityFluidHatch fluidOutput;
    public TileEntityEnergyConnector energy;
    private boolean isValidMultiblock = false;
    private final TickTimer verifyTimer = new TickTimer(this,this::verifyIntegrity,20,true);

    public TileEntityWarpGate(){
        multiblock = Multiblock.multiblocks.get("warpGate");
    }

    @Override
    public void tick() {
        super.tick();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        itemInput = null;
        itemOutput = null;
        energy = null;
        verifyTimer.tick();
        if(isValidMultiblock) {
            Direction dir = Direction.getDirectionFromSide(getMovedData());
            ArrayList<BlockInstance> tileEntities = multiblock.getTileEntities(worldObj, new Vec3i(x, y, z), dir);
            for (BlockInstance tileEntity : tileEntities) {
                if (tileEntity.tile instanceof IMultiblockPart) {
                    if (tileEntity.tile instanceof TileEntityItemBus && tileEntity.block == SIBlocks.reinforcedItemInputBus) {
                        itemInput = (TileEntityItemBus) tileEntity.tile;
                    } else if (tileEntity.tile instanceof TileEntityItemBus && tileEntity.block == SIBlocks.reinforcedItemOutputBus) {
                        itemOutput = (TileEntityItemBus) tileEntity.tile;
                    } else if (tileEntity.tile instanceof TileEntityFluidHatch && tileEntity.block == SIBlocks.basicFluidInputHatch) {
                        fluidInput = (TileEntityFluidHatch) tileEntity.tile;
                    } else if (tileEntity.tile instanceof TileEntityFluidHatch && tileEntity.block == SIBlocks.basicFluidOutputHatch) {
                        fluidOutput = (TileEntityFluidHatch) tileEntity.tile;
                    } else if (tileEntity.tile instanceof TileEntityEnergyConnector && tileEntity.block == SIBlocks.awakenedEnergyConnector) {
                        energy = (TileEntityEnergyConnector) tileEntity.tile;
                    }
                    ((IMultiblockPart) tileEntity.tile).connect(this);
                }
            }
            if (block != null && itemInput != null && itemOutput != null && fluidInput != null && fluidOutput != null && energy != null) {
                //work
            }
            ArrayList<BlockInstance> blocks = multiblock.getBlocks(new Vec3i(x, y, z), dir);
            for (BlockInstance blockInstance : blocks) {
                if(blockInstance.block == SIBlocks.reinforcedCasing2 || blockInstance.block == SIBlocks.awakenedSocketCasing || blockInstance.block == SIBlocks.awakenedCasing2) {
                    worldObj.setBlockMetadata(blockInstance.pos.x, blockInstance.pos.y, blockInstance.pos.z, isBurning() ? 1 : 0);
                }
            }
        }
    }

    public void verifyIntegrity(){
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            isValidMultiblock = multiblock.isValidAtSilent(worldObj,new BlockInstance(block,new Vec3i(x,y,z),this), Direction.getDirectionFromSide(worldObj.getBlockMetadata(x,y,z)));
        } else {
            isValidMultiblock = false;
        }
    }

    @Override
    public Multiblock getMultiblock() {
        return multiblock;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }
}
