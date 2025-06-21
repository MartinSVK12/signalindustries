package sunsetsatellite.signalindustries.tiles.base;

import com.mojang.nbt.tags.CompoundTag;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.network.Network;
import sunsetsatellite.catalyst.core.util.network.NetworkComponentTile;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.energy.simple.api.IEnergyContainer;
import sunsetsatellite.catalyst.energy.simple.impl.TileEntityEnergyConductor;

public abstract class TileEntityTieredEnergyMachine extends TileEntityTieredMachineBase implements IEnergyContainer, NetworkComponentTile {

    @Override
    public void writeToNBT(CompoundTag tag) {
        tag.putLong("energy",energy);
        tag.putLong("capacity",capacity);
        tag.putLong("maxReceive",maxReceive);
        tag.putLong("maxProvide",maxProvide);
        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        energy = tag.getLong("energy");
        capacity = tag.getLong("capacity");
        maxReceive = tag.getLong("maxReceive");
        maxProvide = tag.getLong("maxProvide");
        super.readFromNBT(tag);
    }

    protected long energy = 0;
    protected long capacity = 0;

    protected long maxReceive = 0;
    protected long maxProvide = 0;

    //IEnergyContainer
    @Override
    public long getEnergy() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long getMaxReceive() {
        return maxReceive;
    }

    @Override
    public long getMaxProvide() {
        return maxProvide;
    }

    @Override
    public long internalChangeEnergy(long difference) {
        energy += difference;
        return difference;
    }

    //NetworkComponent
    public Network energyNet;

    @Override
    public NetworkType getType() {
        return NetworkType.CATALYST_ENERGY;
    }

    @Override
    public Vec3i getPosition() {
        return new Vec3i(x,y,z);
    }

    @Override
    public boolean isConnected(Direction direction) {
        return direction.getTileEntity(worldObj,this) instanceof TileEntityEnergyConductor;
    }

    @Override
    public void networkChanged(Network network) {
        this.energyNet = network;
    }

    @Override
    public void removedFromNetwork(Network network) {
        this.energyNet = null;
    }
}
