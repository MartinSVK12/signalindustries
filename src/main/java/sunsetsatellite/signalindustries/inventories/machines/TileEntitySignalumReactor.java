package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.client.Minecraft;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.interfaces.IStabilizable;
import sunsetsatellite.signalindustries.inventories.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.inventories.TileEntityIgnitor;
import sunsetsatellite.signalindustries.inventories.TileEntityItemBus;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTiered;
import sunsetsatellite.signalindustries.items.containers.ItemFuelCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntitySignalumReactor extends TileEntityTiered implements IMultiblock, IStabilizable, IActiveForm {
    public Multiblock multiblock;
    public List<TileEntityStabilizer> stabilizers = new ArrayList<>();
    public List<TileEntityIgnitor> ignitors = new ArrayList<>();
    public TileEntityEnergyConnector connector;
    public TileEntityItemBus input;
    public TileEntityItemBus output;
    public State state = State.INACTIVE;
    public int stabilityField = 0;
    public int maxStabilityField = 100;
    private boolean isValidMultiblock = false;
    private final TickTimer verifyTimer = new TickTimer(this,this::verifyIntegrity,40,true);

    private void verifyIntegrity() {
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            isValidMultiblock = multiblock.isValidAtSilent(worldObj,new BlockInstance(block,new Vec3i(x,y,z),this),Direction.getDirectionFromSide(worldObj.getBlockMetadata(x,y,z)));
        } else {
            isValidMultiblock = false;
        }
    }

    public TileEntitySignalumReactor(){
        multiblock = Multiblock.multiblocks.get("signalumReactor");
    }

    @Override
    public boolean isActive() {
        return state == State.RUNNING;
    }

    @Override
    public boolean isReady() {
        return state == State.IGNITING;
    }

    @Override
    public boolean isBurning() {
        return isActive();
    }

    @Override
    public boolean isDisabled() {
        //TODO:
        return false;
    }

    public enum State {
        INACTIVE,
        IGNITING,
        RUNNING,
    }

    @Override
    public void tick() {
        super.tick();
        stabilizers.clear();
        ignitors.clear();
        verifyTimer.tick();
        if(!isValidMultiblock){
            state = State.INACTIVE;
            return;
        }
        Direction dir = Direction.getDirectionFromSide(getMovedData());
        ArrayList<BlockInstance> tileEntities = multiblock.getTileEntities(worldObj,new Vec3i(x,y,z),Direction.Z_POS);
        for (BlockInstance tileEntity : tileEntities) {
            if(tileEntity.tile instanceof IMultiblockPart){
                if(tileEntity.tile instanceof TileEntityStabilizer){
                    stabilizers.add((TileEntityStabilizer) tileEntity.tile);
                }
                else if(tileEntity.tile instanceof TileEntityIgnitor){
                    ignitors.add((TileEntityIgnitor) tileEntity.tile);
                }
                else if(tileEntity.tile instanceof TileEntityItemBus){
                    if(tileEntity.block == SIBlocks.reinforcedItemInputBus){
                        input = (TileEntityItemBus) tileEntity.tile;
                    } else if (tileEntity.block == SIBlocks.reinforcedItemOutputBus) {
                        output = (TileEntityItemBus) tileEntity.tile;
                    }
                }
                else if(tileEntity.tile instanceof TileEntityEnergyConnector){
                    connector = (TileEntityEnergyConnector) tileEntity.tile;
                }
                ((IMultiblockPart) tileEntity.tile).connect(this);
            }
        }
        if(state == State.IGNITING && checkIfIgnitorsReady() && checkIfStabilizersReady()){
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
        Minecraft.getMinecraft(Minecraft.class).thePlayer.triggerAchievement(SIAchievements.REACTOR);
        Random random = new Random();
        if(random.nextFloat() <= 1.25){
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
            state = State.IGNITING;
        } else if (state == State.IGNITING || state == State.RUNNING) {
            state = State.INACTIVE;
        }
    }

    @Override
    public Multiblock getMultiblock() {
        return multiblock;
    }
}
