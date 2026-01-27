package sunsetsatellite.signalindustries.tiles;

import com.b100.utils.ReflectUtils;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitBlock;
import sunsetsatellite.catalyst.core.util.conduit.IMultiConduit;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidPipe;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.interfaces.INamedTileEntity;
import sunsetsatellite.signalindustries.interfaces.ITiered;

import java.lang.reflect.Field;
import java.util.*;

//TODO: reimplement catalyst energy support
public class TileEntityMultiConduit extends TileEntityFluidContainer implements INamedTileEntity, IMultiConduit, /*IConduitTile,*/ IScreenActionListener, /*IEnergy, IEnergySource, IEnergySink,*/ ISupportsMultiparts {
    public IConduitBlock[] conduits = new IConduitBlock[4];
    public HashMap<Direction, Integer> conduitConnections = (HashMap<Direction, Integer>) Catalyst.mapOf(Direction.values(), Catalyst.arrayFill(new Integer[Direction.values().length], -1));

    //fluids
    public int maxRememberTicks = 100;
    public int rememberTicks = 0;
    public TileEntityFluidContainer[] lastPipes = new TileEntityFluidContainer[4];

   /* //catalyst energy
    public int energy = 0;
    public int capacity = 0;
    public IEnergy lastProvided;
    public IEnergy lastReceived;
    public TickTimer lastTransferMemory;
    public int maxReceive = 0;
    public int maxProvide = 0;*/

    //multipart
    public final HashMap<Direction, Multipart> parts = (HashMap<Direction, Multipart>) Catalyst.mapOf(Direction.values(), new Multipart[Direction.values().length]);

    public TileEntityMultiConduit() {
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        transferSpeed = 0;
        acceptedFluids.clear();
        fluidConnections.replaceAll((D, C) -> Connection.BOTH);
        //this.lastTransferMemory = new TickTimer(this,this::clearLastTransfers,10,true);
    }

    @Override
    public void tick() {
        worldObj.markBlockDirty(x, y, z);
        //lastTransferMemory.tick();
        if (Arrays.stream(conduits).allMatch(Objects::isNull) && !acceptedFluids.isEmpty()) {
            acceptedFluids.clear();
        }
        for (int i = 0; i < conduits.length; i++) {
            IConduitBlock conduit = conduits[i];
            if (conduit != null) {
                rememberTicks++;
                if (rememberTicks >= maxRememberTicks) {
                    rememberTicks = 0;
                    Arrays.fill(lastPipes, null);
                }
                HashMap<Direction, TileEntity> neighbors = new HashMap<>();
                for (Direction dir : Direction.values()) {
                    neighbors.put(dir, dir.getTileEntity(worldObj, this));
                }
                int finalI = i;
                neighbors.forEach((side, tile) -> {
                    if (tile instanceof TileEntityMultiConduit && !tile.equals(lastPipes[finalI])) {
                        TileEntityMultiConduit multiConduit = (TileEntityMultiConduit) tile;
                        if (multiConduit.fluidContents.length <= finalI || fluidContents.length <= finalI) {
                            return;
                        }
                        FluidStack intFluid = getFluidInSlot(finalI);
                        FluidStack extFluid = multiConduit.getFluidInSlot(finalI);
                        if (intFluid != null && extFluid == null) {
                            lastPipes[finalI] = (TileEntityMultiConduit) tile;
                            ((TileEntityMultiConduit) tile).lastPipes[finalI] = this;
                            give(side, finalI, finalI);
                        } else if (intFluid == null && extFluid != null) {
                            lastPipes[finalI] = (TileEntityMultiConduit) tile;
                            ((TileEntityMultiConduit) tile).lastPipes[finalI] = this;
                            take(extFluid, side, finalI);
                        } else if (intFluid != null) { //if both internal and external aren't null
                            lastPipes[finalI] = (TileEntityMultiConduit) tile;
                            ((TileEntityMultiConduit) tile).lastPipes[finalI] = this;
                            if (intFluid.amount < extFluid.amount) {
                                take(extFluid, side, finalI);
                            } else {
                                give(side, finalI, finalI);
                            }
                        }
                    } else if (tile instanceof TileEntityFluidPipe && !tile.equals(lastPipes[finalI])) {
                        TileEntityFluidPipe inv = (TileEntityFluidPipe) tile;
                        int activeSlot = conduitConnections.get(side);
                        if (activeSlot == -1) return;
                        FluidStack intFluid = getFluidInSlot(activeSlot);
                        FluidStack extFluid = inv.getFluidInSlot(0);
                        if (intFluid != null && extFluid == null) {
                            lastPipes[finalI] = (TileEntityFluidPipe) tile;
                            ((TileEntityFluidPipe) tile).last = this;
                            give(side, activeSlot, 0);
                        } else if (intFluid == null && extFluid != null) {
                            lastPipes[finalI] = (TileEntityFluidPipe) tile;
                            ((TileEntityFluidPipe) tile).last = this;
                            take(extFluid, side, activeSlot);
                        } else if (intFluid != null) { //if both internal and external aren't null
                            lastPipes[finalI] = (TileEntityFluidPipe) tile;
                            ((TileEntityFluidPipe) tile).last = this;
                            if (intFluid.amount < extFluid.amount) {
                                take(extFluid, side, activeSlot);
                            } else {
                                give(side, activeSlot, 0);
                            }
                        }
                    }
                });
            }
        }

        /*for (Direction dir : Direction.values()) {
            TileEntity facingTile = dir.getTileEntity(worldObj,this);
            if(facingTile instanceof IEnergySink && !facingTile.equals(lastReceived)){
                int provided = provide(dir,getMaxProvide(),true);
                if(provided <= 0){
                    continue;
                }
                int received = ((IEnergySink) facingTile).receive(dir.getOpposite(),provided,true);
                if(received > 0){
                    ((IEnergySink) facingTile).receive(dir.getOpposite(),provided,false);
                    provide(dir,received,false);
                    notifyOfProvide((IEnergy) facingTile);
                    ((IEnergy) facingTile).notifyOfReceive(this);
                }
            }
        }*/
    }

    public boolean addConduit(IConduitBlock newConduit) {
        for (int i = 0; i < conduits.length; i++) {
            IConduitBlock conduit = conduits[i];
            if (newConduit.getConduitCapability() == ConduitCapability.RES_NETWORK && conduit != null && conduit.getConduitCapability() == ConduitCapability.RES_NETWORK) {
                return false;
            }
            if (conduit == null) {
                conduits[i] = newConduit;
                if (newConduit.getConduitCapability() == ConduitCapability.FLUID || newConduit.getConduitCapability() == ConduitCapability.SIGNALUM) {
                    fluidContents = Arrays.copyOf(fluidContents, fluidContents.length + 1);
                    fluidCapacity = Arrays.copyOf(fluidCapacity, fluidCapacity.length + 1);
                    acceptedFluids.add(new ArrayList<>());
                    if (newConduit instanceof ITiered) {
                        fluidCapacity[fluidCapacity.length - 1] = (int) Math.pow(2, ((ITiered) newConduit).getTier().ordinal()) * 1000;
                        int value = (int) Math.pow(2, ((ITiered) newConduit).getTier().ordinal()) * 20;
                        if (transferSpeed < value) {
                            transferSpeed = value;
                        }
                    }
                    if (newConduit.getConduitCapability() == ConduitCapability.FLUID) {
                        acceptedFluids.get(acceptedFluids.size() - 1).addAll(Fluid.fluidMap.values());
                        acceptedFluids.get(acceptedFluids.size() - 1).remove(SIFluids.ENERGY);
                    } else if (newConduit.getConduitCapability() == ConduitCapability.SIGNALUM) {
                        acceptedFluids.get(acceptedFluids.size() - 1).add(SIFluids.ENERGY);
                    }
                } else {
                   /* if(newConduit.getConduitCapability() == ConduitCapability.CATALYST_ENERGY){
                        if(newConduit instanceof ITiered){
                            Tier tier = ((ITiered) newConduit).getTier();
                            capacity += (int) Math.pow(2,tier.ordinal()) * 1024;
                            int transfer = 128 * (tier.ordinal()+1);
                            if(maxProvide < transfer && maxReceive < transfer){
                                maxReceive = transfer;
                                maxProvide = transfer;
                            }
                        }
                    }*/
                    fluidContents = Arrays.copyOf(fluidContents, fluidContents.length + 1);
                    fluidCapacity = Arrays.copyOf(fluidCapacity, fluidCapacity.length + 1);
                    acceptedFluids.add(new ArrayList<>());
                    fluidCapacity[fluidCapacity.length - 1] = 0;
                }
                return true;
            }
        }
        return false;
    }

    public int getAmountOfConduits() {
        int n = 0;
        for (IConduitBlock conduit : conduits) {
            if (conduit != null) {
                n++;
            }
        }
        return n;
    }

    @Override
    public int getActiveFluidSlotForSide(Direction dir) {
        return conduitConnections.get(dir);
    }

    @Override
    public Connection getFluidIOForSide(Direction dir) {
        return Connection.BOTH;
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        ListTag conduitList = new ListTag();
        CompoundTag conduitConnectionsTag = new CompoundTag();
        for (IConduitBlock conduit : conduits) {
            if (conduit == null) continue;
            BlockLogic block = (BlockLogic) conduit;
            conduitList.addTag(new IntTag(block.id()));
        }
        for (Map.Entry<Direction, Integer> entry : conduitConnections.entrySet()) {
            Direction dir = entry.getKey();
            int n = entry.getValue();
            conduitConnectionsTag.putInt(String.valueOf(dir.ordinal()), n);
        }
        tag.putList("conduits", conduitList);
        tag.putCompound("conduitConnections", conduitConnectionsTag);

        /*tag.putInt("energy",energy);
        tag.putInt("capacity",capacity);

        tag.putInt("maxReceive",maxReceive);
        tag.putInt("maxProvide",maxProvide);*/

        CompoundTag coversNbt = new CompoundTag();

        for (Map.Entry<Direction, Multipart> entry : parts.entrySet()) {
            if (entry.getValue() == null) continue;
            CompoundTag partNbt = new CompoundTag();
            entry.getValue().writeToNbt(partNbt);
            coversNbt.putCompound(String.valueOf(entry.getKey().ordinal()), partNbt);
        }

        tag.putCompound("Parts", coversNbt);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        ListTag conduitList = tag.getList("conduits");
        conduitList.forEach((C) -> addConduit((IConduitBlock) Blocks.getBlock((int) C.getValue()).getLogic()));

        /*maxReceive = tag.getInteger("maxReceive");
        maxProvide = tag.getInteger("maxProvide");

        energy = tag.getInteger("energy");
        capacity = tag.getInteger("capacity");*/

        CompoundTag connectionsTag = tag.getCompound("conduitConnections");
        for (Object con : connectionsTag.getValues()) {
            conduitConnections.replace(Direction.values()[Integer.parseInt(((IntTag) con).getTagName())], ((IntTag) con).getValue());
        }

        CompoundTag coversNbt = tag.getCompound("Parts");

        for (Map.Entry<String, Tag<?>> entry : coversNbt.getValue().entrySet()) {
            Direction dir = Direction.values()[Integer.parseInt(entry.getKey())];
            CompoundTag partTag = (CompoundTag) entry.getValue();
            parts.put(dir, new Multipart(partTag));
        }

        super.readFromNBT(tag);
    }

    @Override
    public String getName() {
        Field field = ReflectUtils.getField(TileEntity.class, "classToNameMap");
        return ReflectUtils.getValue(field, null, String.class);
    }

    @Override
    public boolean supports(ConduitCapability capability) {
        for (IConduitBlock conduit : conduits) {
            if (conduit == null) continue;
            if (conduit.getConduitCapability() == capability) {
                return true;
            }
        }
        return false;
    }

    /*public void clearLastTransfers(){
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
        if (this.energy+amount > this.capacity) {
            this.energy = this.capacity;
        } else if (this.energy+amount < 0) {
            this.energy = 0;
        } else {
            energy += amount;
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
    }

    @Override
    public boolean canConnect(Direction dir, Connection connection) {
        return true;
    }*/

    public BlockInstance toInstance() {
        return new BlockInstance(Blocks.blocksList[worldObj.getBlockId(x, y, z)], new Vec3i(x, y, z), this);
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (button == 0) {
            if (id > 5 && id < 12) {
                Direction dir = Direction.values()[id - 6];
                Integer currentValue = conduitConnections.get(dir);
                if (currentValue < getAmountOfConduits() - 1) {
                    conduitConnections.replace(dir, currentValue + 1);
                }
            }
        } else if (button == 1) {
            if (id > 5 && id < 12) {
                Direction dir = Direction.values()[Math.min(6, Math.max(0, id - 6))];
                Integer currentValue = conduitConnections.get(dir);
                if (currentValue > -1) {
                    conduitConnections.put(dir, currentValue - 1);
                }
            }
        }
    }

    /*@Override
    public int receive(Direction dir, int amount, boolean test) {
        if(canConnect(dir, Connection.INPUT)){
            int received = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, amount));
            if(!test){
                energy += received;
            }
            return received;
        }
        return 0;
    }

    @Override
    public int getMaxReceive() {
        return maxReceive;
    }

    @Override
    public int getMaxReceive(Direction dir) {
        if(dir.getTileEntity(worldObj,this) instanceof IEnergySink){
            return ((IEnergySink)dir.getTileEntity(worldObj,this)).getMaxReceive();
        }
        return 0;
    }

    @Override
    public void setMaxReceive(int amount) {
        maxReceive = amount;
    }

    @Override
    public int provide(Direction dir, int amount, boolean test) {
        if(canConnect(dir, Connection.OUTPUT)){
            int provided = Math.min(this.energy, Math.min(this.maxProvide, amount));
            if(!test){
                energy -= provided;
            }
            return provided;
        }
        return 0;
    }

    @Override
    public int provide(ItemStack stack, int amount, boolean test){
        if(stack.getItem() instanceof IEnergyItem){
            int provided = Math.min(this.energy, Math.min(this.maxProvide, amount));
            int received = ((IEnergyItem) stack.getItem()).receive(stack,amount,true);
            int actual = Math.min(provided,received);
            if(!test){
                energy -= actual;
                ((IEnergyItem) stack.getItem()).receive(stack,actual,false);
            }
            return actual;
        }
        return 0;
    }

    @Override
    public int receive(ItemStack stack, int amount, boolean test){
        if(stack.getItem() instanceof IEnergyItem){
            int received = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, amount));
            int provided = ((IEnergyItem) stack.getItem()).provide(stack,amount,true);
            int actual = Math.min(provided,received);
            if(!test){
                energy += actual;
                ((IEnergyItem) stack.getItem()).provide(stack,actual,false);
            }
            return actual;
        }
        return 0;
    }

    @Override
    public int getMaxProvide() {
        return maxProvide;
    }

    @Override
    public int getMaxProvide(Direction dir) {
        if(dir.getTileEntity(worldObj,this) instanceof IEnergySource){
            return ((IEnergySource)dir.getTileEntity(worldObj,this)).getMaxProvide();
        }
        return 0;
    }

    @Override
    public void setMaxProvide(int amount) {
        maxProvide = amount;
    }

    public void setTransfer(int amount){
        maxProvide = amount;
        maxReceive = amount;
    }*/

    @Override
    public HashMap<Direction, Multipart> getParts() {
        return parts;
    }
/*
    @Override
    public ConduitCapability getConduitCapability() {
        if(supports(ConduitCapability.RES_NETWORK)) return ConduitCapability.RES_NETWORK;
        return null;
    }

    @Override
    public boolean isConnected(Direction direction) {
        if(worldObj == null) return false;
        return direction.getTileEntity(worldObj,this) instanceof TileEntityMultiConduit;
    }

    @Override
    public void networkChanged(Network network) {

    }

    @Override
    public void removedFromNetwork(Network network) {

    }

    @Override
    public NetworkType getType() {
        if(supports(ConduitCapability.RES_NETWORK)) return NetworkType.RES_NETWORK;
        return null;
    }*/
}
