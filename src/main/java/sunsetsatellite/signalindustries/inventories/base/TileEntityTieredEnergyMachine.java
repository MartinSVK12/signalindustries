package sunsetsatellite.signalindustries.inventories.base;

import com.mojang.nbt.CompoundTag;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.energy.api.IEnergy;

import java.util.HashMap;

public abstract class TileEntityTieredEnergyMachine extends TileEntityTieredMachineBase implements IEnergy {

    public int energy = 0;
    public int capacity = 0;
    public IEnergy lastProvided;
    public IEnergy lastReceived;
    public TickTimer lastTransferMemory;
    public HashMap<Direction, Connection> connections = new HashMap<>();

    public TileEntityTieredEnergyMachine(){
        this.lastTransferMemory = new TickTimer(this,this::clearLastTransfers,10,true);
        for (Direction dir : Direction.values()) {
            connections.put(dir, Connection.NONE);
        }
    }

    @Override
    public void tick() {
        lastTransferMemory.tick();
    }

    public void clearLastTransfers(){
        lastProvided = null;
        lastReceived = null;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public int getEnergy(Direction dir) {
        if(dir.getTileEntity(worldObj,this) instanceof IEnergy){
            return ((IEnergy)dir.getTileEntity(worldObj,this)).getEnergy();
        }
        return 0;
    }

    @Override
    public void writeToNBT(CompoundTag CompoundTag) {
        CompoundTag.putInt("energy",energy);
        CompoundTag.putInt("capacity",capacity);
        super.writeToNBT(CompoundTag);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        energy = tag.getInteger("energy");
        capacity = tag.getInteger("capacity");
        super.readFromNBT(tag);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getCapacity(Direction dir) {
        if(dir.getTileEntity(worldObj,this) instanceof IEnergy){
            return ((IEnergy)dir.getTileEntity(worldObj,this)).getCapacity();
        }
        return 0;
    }

    @Override
    public void setEnergy(int amount) {
        energy = amount;
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        } else if (this.energy < 0) {
            this.energy = 0;
        }
    }

    @Override
    public void modifyEnergy(int amount) {
        energy += amount;
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        } else if (this.energy < 0) {
            this.energy = 0;
        }
    }

    @Override
    public void setCapacity(int amount) {
        capacity = amount;
    }

    @Override
    public void notifyOfReceive(IEnergy notifier) {
        lastReceived = notifier;
    }

    @Override
    public void notifyOfProvide(IEnergy notifier) {
        lastProvided = notifier;
    }

    @Override
    public void setConnection(Direction dir, Connection connection) {
        connections.replace(dir,connection);
    }

    @Override
    public boolean canConnect(Direction dir, Connection connection) {
        if(connections.get(dir).equals(Connection.BOTH) && !connection.equals(Connection.NONE)){
            return true;
        }
        return connections.get(dir).equals(connection);
    }
}
