package sunsetsatellite.signalindustries.inventories;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.IFluidIO;
import sunsetsatellite.sunsetutils.util.IItemIO;

public abstract class TileEntityTieredMachine extends TileEntityTieredContainer implements IFluidIO, IItemIO {
    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public int efficiency = 1;
    public int speedMultiplier = 1;
    public int cost = 0;

    //TODO: Generify code for all machines
    public boolean isBurning(){
        return fuelBurnTicks > 0;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            speedMultiplier = block.tier.ordinal()+1;
            for(Direction dir : Direction.values()){
                TileEntity tile = dir.getTileEntity(worldObj,this);
                if(tile instanceof TileEntityBooster){
                    if(((TileEntityBooster) tile).isBurning()){
                        int meta = tile.getBlockMetadata();
                        if(Direction.getDirectionFromSide(meta).getOpposite() == dir){
                            speedMultiplier = block.tier.ordinal()+2;
                        }
                    }
                }
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

    public int getProgressScaled(int paramInt) {
        return this.progressTicks * paramInt / progressMaxTicks;
    }

    public int getBurnTimeRemainingScaled(int paramInt) {
        if(this.fuelMaxBurnTicks == 0) {
            this.fuelMaxBurnTicks = 200;
        }
        return this.fuelBurnTicks * paramInt / this.fuelMaxBurnTicks;
    }

    @Override
    public int getActiveFluidSlotForSide(Direction dir) {
        return activeFluidSlots.get(dir);
    }

    @Override
    public Connection getFluidIOForSide(Direction dir) {
        return connections.get(dir);
    }

    @Override
    public int getActiveItemSlotForSide(Direction dir) {
        return activeItemSlots.get(dir);
    }

    @Override
    public Connection getItemIOForSide(Direction dir) {
        return itemConnections.get(dir);
    }
}
