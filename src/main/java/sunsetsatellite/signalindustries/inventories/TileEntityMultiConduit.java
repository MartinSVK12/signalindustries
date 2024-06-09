package sunsetsatellite.signalindustries.inventories;

import com.b100.utils.ReflectUtils;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.IntTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.*;
import sunsetsatellite.catalyst.energy.api.IEnergy;
import sunsetsatellite.catalyst.energy.api.IEnergyItem;
import sunsetsatellite.catalyst.energy.api.IEnergySink;
import sunsetsatellite.catalyst.energy.api.IEnergySource;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.INamedTileEntity;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.lang.reflect.Field;
import java.util.*;

public class TileEntityMultiConduit extends TileEntityFluidContainer implements INamedTileEntity, IMultiConduit, IEnergy, IEnergySource, IEnergySink {
    public IConduit[] conduits = new IConduit[4];
    public HashMap<Direction, Integer> conduitConnections = (HashMap<Direction, Integer>) SignalIndustries.mapOf(Direction.values(),SignalIndustries.arrayFill(new Integer[Direction.values().length],-1));

    //fluids
    public int maxRememberTicks = 100;
    public int rememberTicks = 0;
    public TileEntityFluidContainer[] lastPipes = new TileEntityFluidContainer[4];

    //catalyst energy
    public int energy = 0;
    public int capacity = 0;
    public IEnergy lastProvided;
    public IEnergy lastReceived;
    public TickTimer lastTransferMemory;
    public int maxReceive = 0;
    public int maxProvide = 0;

    public TileEntityMultiConduit(){
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        transferSpeed = 0;
        acceptedFluids.clear();
        fluidConnections.replaceAll((D,C)->Connection.BOTH);
        this.lastTransferMemory = new TickTimer(this,this::clearLastTransfers,10,true);
    }

    @Override
    public void tick() {
        worldObj.markBlockDirty(x,y,z);
        lastTransferMemory.tick();
        if(Arrays.stream(conduits).allMatch(Objects::isNull) && !acceptedFluids.isEmpty()){
            acceptedFluids.clear();
        }
        for (int i = 0; i < conduits.length; i++) {
            IConduit conduit = conduits[i];
            if (conduit != null) {
               rememberTicks++;
               if(rememberTicks >= maxRememberTicks){
                   rememberTicks = 0;
                   Arrays.fill(lastPipes, null);
               }
                HashMap<Direction, TileEntity> neighbors = new HashMap<>();
                for (Direction dir : Direction.values()) {
                    neighbors.put(dir,dir.getTileEntity(worldObj,this));
                }
                int finalI = i;
                neighbors.forEach((side, tile) -> {
                    if(tile instanceof TileEntityMultiConduit && !tile.equals(lastPipes[finalI])){
                        TileEntityMultiConduit multiConduit = (TileEntityMultiConduit) tile;
                        if(multiConduit.fluidContents.length <= finalI || fluidContents.length <= finalI){
                            return;
                        }
                        FluidStack intFluid = getFluidInSlot(finalI);
                        FluidStack extFluid = multiConduit.getFluidInSlot(finalI);
                        if (intFluid != null && extFluid == null) {
                            lastPipes[finalI] = (TileEntityMultiConduit) tile;
                            ((TileEntityMultiConduit) tile).lastPipes[finalI] = this;
                            give(side,finalI,finalI);
                        } else if (intFluid == null && extFluid != null) {
                            lastPipes[finalI] = (TileEntityMultiConduit) tile;
                            ((TileEntityMultiConduit) tile).lastPipes[finalI] = this;
                            take(extFluid,side,finalI);
                        } else if (intFluid != null) { //if both internal and external aren't null
                            lastPipes[finalI] = (TileEntityMultiConduit) tile;
                            ((TileEntityMultiConduit) tile).lastPipes[finalI] = this;
                            if (intFluid.amount < extFluid.amount) {
                                take(extFluid,side,finalI);
                            } else {
                                give(side,finalI,finalI);
                            }
                        }
                    } else if (tile instanceof TileEntityFluidPipe && !tile.equals(lastPipes[finalI])) {
                        TileEntityFluidPipe inv = (TileEntityFluidPipe) tile;
                        int activeSlot = conduitConnections.get(side);
                        if(activeSlot == -1) return;
                        FluidStack intFluid = getFluidInSlot(activeSlot);
                        FluidStack extFluid = inv.getFluidInSlot(0);
                        if (intFluid != null && extFluid == null) {
                            lastPipes[finalI] = (TileEntityFluidPipe) tile;
                            ((TileEntityFluidPipe) tile).last = this;
                            give(side,activeSlot,0);
                        } else if (intFluid == null && extFluid != null) {
                            lastPipes[finalI] = (TileEntityFluidPipe) tile;
                            ((TileEntityFluidPipe) tile).last = this;
                            take(extFluid,side,activeSlot);
                        } else if (intFluid != null) { //if both internal and external aren't null
                            lastPipes[finalI] = (TileEntityFluidPipe) tile;
                            ((TileEntityFluidPipe) tile).last = this;
                            if (intFluid.amount < extFluid.amount) {
                                take(extFluid,side,activeSlot);
                            } else {
                                give(side,activeSlot,0);
                            }
                        }
                    }
                });
            }
        }

        for (Direction dir : Direction.values()) {
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
        }
    }

    public boolean addConduit(IConduit newConduit){
        for (int i = 0; i < conduits.length; i++) {
            IConduit conduit = conduits[i];
            if(newConduit.getConduitCapability() == ConduitCapability.NETWORK && conduit != null && conduit.getConduitCapability() == ConduitCapability.NETWORK){
                return false;
            }
            if (conduit == null) {
                conduits[i] = newConduit;
                if(newConduit.getConduitCapability() == ConduitCapability.FLUID || newConduit.getConduitCapability() == ConduitCapability.SIGNALUM){
                    fluidContents = Arrays.copyOf(fluidContents,fluidContents.length + 1);
                    fluidCapacity = Arrays.copyOf(fluidCapacity, fluidCapacity.length + 1);
                    acceptedFluids.add(new ArrayList<>());
                    if(newConduit instanceof ITiered){
                        fluidCapacity[fluidCapacity.length-1] = (int) Math.pow(2,((ITiered)newConduit).getTier().ordinal()) * 1000;
                        int value = (int) Math.pow(2,((ITiered)newConduit).getTier().ordinal()) * 20;
                        if(transferSpeed < value)
                        {
                            transferSpeed = value;
                        }
                    }
                    if(newConduit.getConduitCapability() == ConduitCapability.FLUID){
                        acceptedFluids.get(acceptedFluids.size()-1).addAll(CatalystFluids.FLUIDS.getAllFluids());
                        acceptedFluids.get(acceptedFluids.size()-1).remove(SIBlocks.energyFlowing);
                    } else if (newConduit.getConduitCapability() == ConduitCapability.SIGNALUM) {
                        acceptedFluids.get(acceptedFluids.size()-1).add(SIBlocks.energyFlowing);
                    }
                } else {
                    if(newConduit.getConduitCapability() == ConduitCapability.CATALYST_ENERGY){
                        if(newConduit instanceof ITiered){
                            Tier tier = ((ITiered) newConduit).getTier();
                            capacity += (int) Math.pow(2,tier.ordinal()) * 1024;
                            int transfer = 128 * (tier.ordinal()+1);
                            if(maxProvide < transfer && maxReceive < transfer){
                                maxReceive = transfer;
                                maxProvide = transfer;
                            }
                        }
                    }
                    fluidContents = Arrays.copyOf(fluidContents,fluidContents.length + 1);
                    fluidCapacity = Arrays.copyOf(fluidCapacity, fluidCapacity.length + 1);
                    acceptedFluids.add(new ArrayList<>());
                    fluidCapacity[fluidCapacity.length-1] = 0;
                }
                return true;
            }
        }
        return false;
    }

    public int getAmountOfConduits(){
        int n = 0;
        for (IConduit conduit : conduits) {
            if (conduit != null) {
                n++;
            }
        }
        return n;
    }

    @Override
    public int getActiveFluidSlot(Direction dir) {
        return conduitConnections.get(dir);
    }

    @Override
    public Connection getConnection(Direction dir) {
        return Connection.BOTH;
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        ListTag conduitList = new ListTag();
        CompoundTag conduitConnectionsTag = new CompoundTag();
        for (IConduit conduit : conduits) {
            if(conduit == null) continue;
            Block block = (Block) conduit;
            conduitList.addTag(new IntTag(block.id));
        }
        for (Map.Entry<Direction, Integer> entry : conduitConnections.entrySet()) {
            Direction dir = entry.getKey();
            int n = entry.getValue();
            conduitConnectionsTag.putInt(String.valueOf(dir.ordinal()),n);
        }
        tag.putList("conduits", conduitList);
        tag.putCompound("conduitConnections",conduitConnectionsTag);

        tag.putInt("energy",energy);
        tag.putInt("capacity",capacity);

        tag.putInt("maxReceive",maxReceive);
        tag.putInt("maxProvide",maxProvide);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        ListTag conduitList = tag.getList("conduits");
        conduitList.forEach((C)->addConduit((IConduit) Block.getBlock((int) C.getValue())));

        maxReceive = tag.getInteger("maxReceive");
        maxProvide = tag.getInteger("maxProvide");

        energy = tag.getInteger("energy");
        capacity = tag.getInteger("capacity");

        CompoundTag connectionsTag = tag.getCompound("conduitConnections");
        for (Object con : connectionsTag.getValues()) {
            conduitConnections.replace(Direction.values()[Integer.parseInt(((IntTag)con).getTagName())],((IntTag) con).getValue());
        }
        super.readFromNBT(tag);
    }

    @Override
    public String getName() {
        Field field = ReflectUtils.getField(TileEntity.class,"classToNameMap");
        return ReflectUtils.getValue(field,null,String.class);
    }

    @Override
    public boolean supports(ConduitCapability capability) {
        for (IConduit conduit : conduits) {
            if(conduit == null) continue;
            if(conduit.getConduitCapability() == capability){
                return true;
            }
        }
        return false;
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
    }

    public BlockInstance toInstance(){
        return new BlockInstance(Block.blocksList[worldObj.getBlockId(x,y,z)],new Vec3i(x,y,z),this);
    }

    @Override
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
    }
}
