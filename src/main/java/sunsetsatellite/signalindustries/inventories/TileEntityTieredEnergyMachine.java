package sunsetsatellite.signalindustries.inventories;

import com.mojang.nbt.CompoundTag;
import sunsetsatellite.energyapi.api.IEnergy;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.TickTimer;

import java.util.HashMap;

public abstract class TileEntityTieredEnergyMachine extends TileEntityTieredMachine implements IEnergy {

    public int energy = 0;
    public int capacity = 0;
    public IEnergy lastProvided;
    public IEnergy lastReceived;
    public TickTimer lastTransferMemory;
    public HashMap<Direction, Connection> connections = new HashMap<>();

    public TileEntityTieredEnergyMachine(){
        this.lastTransferMemory = new TickTimer(this,"clearLastTransfers",10,true);
        for (Direction dir : Direction.values()) {
            connections.put(dir, Connection.NONE);
        }
    }

    @Override
    public void updateEntity() {
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
    public void readFromNBT(CompoundTag CompoundTag) {
        energy = CompoundTag.getInteger("energy");
        capacity = CompoundTag.getInteger("capacity");
        super.readFromNBT(CompoundTag);
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
