package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.interfaces.IStabilizable;
import sunsetsatellite.signalindustries.items.ItemFuelCell;
import sunsetsatellite.sunsetutils.util.BlockInstance;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3i;
import sunsetsatellite.sunsetutils.util.multiblocks.IMultiblock;
import sunsetsatellite.sunsetutils.util.multiblocks.Multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntitySignalumReactor extends TileEntityTiered implements IMultiblock, IStabilizable {
    public Multiblock multiblock;
    public List<TileEntityStabilizer> stabilizers = new ArrayList<>();
    public List<TileEntityIgnitor> ignitors = new ArrayList<>();
    public TileEntityEnergyConnector connector;
    public TileEntityItemBus input;
    public TileEntityItemBus output;
    public State state = State.INACTIVE;

    public TileEntitySignalumReactor(){
        multiblock = Multiblock.multiblocks.get("signalumReactor");
    }

    @Override
    public boolean isActive() {
        return state == State.RUNNING;
    }

    @Override
    public boolean isReady() {
        return state == State.STARTING;
    }

    public enum State {
        INACTIVE,
        STARTING,
        RUNNING,
    }

    @Override
    public void updateEntity() {
        stabilizers.clear();
        ignitors.clear();
        Direction dir = Direction.getDirectionFromSide(getBlockMetadata());
        ArrayList<BlockInstance> tileEntities = multiblock.getTileEntities(worldObj,new Vec3i(xCoord,yCoord,zCoord),Direction.Z_POS);
        for (BlockInstance tileEntity : tileEntities) {
            if(tileEntity.tile instanceof IMultiblockPart){
                if(tileEntity.tile instanceof TileEntityStabilizer){
                    stabilizers.add((TileEntityStabilizer) tileEntity.tile);
                }
                else if(tileEntity.tile instanceof TileEntityIgnitor){
                    ignitors.add((TileEntityIgnitor) tileEntity.tile);
                }
                else if(tileEntity.tile instanceof TileEntityItemBus){
                    if(tileEntity.block == SignalIndustries.reinforcedItemInputBus){
                        input = (TileEntityItemBus) tileEntity.tile;
                    } else if (tileEntity.block == SignalIndustries.reinforcedItemOutputBus) {
                        output = (TileEntityItemBus) tileEntity.tile;
                    }
                }
                else if(tileEntity.tile instanceof TileEntityEnergyConnector){
                    connector = (TileEntityEnergyConnector) tileEntity.tile;
                }
                ((IMultiblockPart) tileEntity.tile).connect(this);
            }
        }
        if(state == State.STARTING && checkIfIgnitorsReady() && checkIfStabilizersReady()){
            state = State.RUNNING;
        }
        //TODO: state machine maybe?
        if(state == State.RUNNING && checkIfStabilizersReady() && getFuel() > 0){
            depleteRandomFuelCell();
        } else if (state == State.RUNNING) {
            state = State.INACTIVE;
        }
        if(state == State.RUNNING){
            ItemStack[] itemContents = input.itemContents;
            for (int i = 0; i < itemContents.length; i++) {
                ItemStack stack = itemContents[i];
                if (stack != null && stack.getItem() instanceof ItemFuelCell) {
                    if (stack.getData().getInteger("fuel") <= 0) {
                        if(output.itemContents[i] == null){
                            output.itemContents[i] = stack;
                            input.itemContents[i] = null;
                        }
                    }
                }
            }

            if(getFuel() <= 0){
                state = State.INACTIVE;
            }
            for (TileEntityIgnitor ignitor : ignitors) {
                if(ignitor.isEmpty()){
                    state = State.INACTIVE;
                    break;
                }
                ignitor.isActivated = true;
            }
        } else {
            for (TileEntityIgnitor ignitor : ignitors) {
                ignitor.isActivated = false;
            }
        }
    }

    public void depleteRandomFuelCell(){
        Random random = new Random();
        if(random.nextFloat() <= 0.25){
            //TODO: actually pick a random cell
            for (ItemStack stack : input.itemContents) {
                if (stack != null && stack.getItem() instanceof ItemFuelCell) {
                    int fuel = stack.getData().getInteger("fuel");
                    int depleted = stack.getData().getInteger("depleted");
                    if(fuel >= 0){
                        stack.getData().putInt("fuel",fuel-1);
                        stack.getData().putInt("depleted",depleted+1);
                    }
                }
            }
        }
    }

    public boolean checkIfIgnitorsReady(){
        for (TileEntityIgnitor ignitor : ignitors) {
            if(!ignitor.isReady()){
                return false;
            }
        }
        return true;
    }

    public boolean checkIfStabilizersReady(){
        boolean ready = true;
        for (TileEntityStabilizer stabilizer : stabilizers) {
            if (!stabilizer.canProcess()) {
                ready = false;
            }
        }
        return ready;
    }

    public int getFuel(){
        if(input == null) return 0;
        int fuel = 0;
        for (ItemStack stack : input.itemContents) {
            if(stack != null && stack.getItem() instanceof ItemFuelCell){
                fuel += stack.getData().getInteger("fuel");
            }
        }
        return fuel;
    }

    public int getDepletedFuel(){
        if(input == null) return 0;
        int depleted = 0;
        for (ItemStack stack : input.itemContents) {
            if(stack != null && stack.getItem() instanceof ItemFuelCell){
                depleted += stack.getData().getInteger("depleted");
            }
        }
        return depleted;
    }

    public void start() {
        if(getFuel() > 0 && state == State.INACTIVE){
            state = State.STARTING;
        } else if (state == State.STARTING || state == State.RUNNING) {
            state = State.INACTIVE;
        }
    }

    @Override
    public Multiblock getMultiblock() {
        return multiblock;
    }
}
