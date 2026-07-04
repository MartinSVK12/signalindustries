package sunsetsatellite.signalindustries.tiles.machines.multiblocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.ITileEntityInit;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.interfaces.IStabilizable;
import sunsetsatellite.signalindustries.items.tools.ItemFuelCell;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.tiles.TileEntityIgnitor;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityItemBus;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTiered;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntitySignalumReactor extends TileEntityTiered implements IMultiblock, IStabilizable, IActiveForm, ITileEntityInit {
    public MultiblockInstance multiblock;
    public List<TileEntityStabilizer> stabilizers = new ArrayList<>();
    public List<TileEntityIgnitor> ignitors = new ArrayList<>();
    public TileEntityEnergyConnector connector;
    public TileEntityItemBus input;
    public TileEntityItemBus output;
    public State state = State.INACTIVE;
    public int stabilityField = 0;
    public int maxStabilityField = 100;

    public TileEntitySignalumReactor() {
        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("signalumReactor"));
    }

    @Override
    public void init(Block<?> block) {
        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("signalumReactor"));
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
        if (multiblock == null) {
            return;
        }
        super.tick();
        stabilizers.clear();
        ignitors.clear();
        if (!multiblock.isValid()) {
            state = State.INACTIVE;
            return;
        }
        Direction dir = Direction.getDirectionFromSide(getBlockMeta());
        ArrayList<BlockInstance> tileEntities = multiblock.data.getTileEntities(worldObj, new Vec3i(x, y, z), dir);
        for (BlockInstance tileEntity : tileEntities) {
            if (tileEntity.tile instanceof IMultiblockPart) {
                if (tileEntity.tile instanceof TileEntityStabilizer) {
                    stabilizers.add((TileEntityStabilizer) tileEntity.tile);
                } else if (tileEntity.tile instanceof TileEntityIgnitor) {
                    ignitors.add((TileEntityIgnitor) tileEntity.tile);
                } else if (tileEntity.tile instanceof TileEntityItemBus) {
                    if (tileEntity.block == SIBlocks.reinforcedItemInputBus) {
                        input = (TileEntityItemBus) tileEntity.tile;
                    } else if (tileEntity.block == SIBlocks.reinforcedItemOutputBus) {
                        output = (TileEntityItemBus) tileEntity.tile;
                    }
                } else if (tileEntity.tile instanceof TileEntityEnergyConnector) {
                    connector = (TileEntityEnergyConnector) tileEntity.tile;
                }
                ((IMultiblockPart) tileEntity.tile).connect(this);
            }
        }
        if (state == State.IGNITING && checkIfIgnitorsReady() && checkIfStabilizersReady()) {
            state = State.RUNNING;
        }
        //TODO: state machine maybe?
        if (state == State.RUNNING && checkIfStabilizersReady() && getFuel() > 0) {
            depleteRandomFuelCell();
        } else if (state == State.RUNNING) {
            state = State.INACTIVE;
        }
        if (state == State.RUNNING) {
            ItemStack[] itemContents = input.itemContents;
            for (int i = 0; i < itemContents.length; i++) {
                ItemStack stack = itemContents[i];
                if (stack != null && stack.getItem() instanceof ItemFuelCell) {
                    if (stack.getData().getInteger("fuel") <= 0) {
                        if (output.itemContents[i] == null) {
                            output.itemContents[i] = stack;
                            input.itemContents[i] = null;
                        }
                    }
                }
            }

            if (getFuel() <= 0) {
                state = State.INACTIVE;
            }
            for (TileEntityIgnitor ignitor : ignitors) {
                if (ignitor.isEmpty()) {
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

    public void depleteRandomFuelCell() {
        //Minecraft.getMinecraft(Minecraft.class).//thePlayer.triggerAchievement(SIAchievements.REACTOR);
        Random random = new Random();
        if (random.nextFloat() <= 1.25) {
            //TODO: actually pick a random cell
            for (ItemStack stack : input.itemContents) {
                if (stack != null && stack.getItem() instanceof ItemFuelCell) {
                    int fuel = stack.getData().getInteger("fuel");
                    int depleted = stack.getData().getInteger("depleted");
                    if (fuel >= 0) {
                        stack.getData().putInt("fuel", fuel - 1);
                        stack.getData().putInt("depleted", depleted + 1);
                    }
                }
            }
        }
    }

    public boolean checkIfIgnitorsReady() {
        for (TileEntityIgnitor ignitor : ignitors) {
            if (!ignitor.isReady()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkIfStabilizersReady() {
        boolean ready = true;
        for (TileEntityStabilizer stabilizer : stabilizers) {
            if (!stabilizer.canProcess()) {
                ready = false;
            }
        }
        return ready;
    }

    public int getFuel() {
        if (input == null) return 0;
        int fuel = 0;
        for (ItemStack stack : input.itemContents) {
            if (stack != null && stack.getItem() instanceof ItemFuelCell) {
                fuel += stack.getData().getInteger("fuel");
            }
        }
        return fuel;
    }

    public int getDepletedFuel() {
        if (input == null) return 0;
        int depleted = 0;
        for (ItemStack stack : input.itemContents) {
            if (stack != null && stack.getItem() instanceof ItemFuelCell) {
                depleted += stack.getData().getInteger("depleted");
            }
        }
        return depleted;
    }

    public void start() {
        if (getFuel() > 0 && state == State.INACTIVE) {
            state = State.IGNITING;
        } else if (state == State.IGNITING || state == State.RUNNING) {
            state = State.INACTIVE;
        }
    }

    @Override
    public MultiblockInstance getMultiblock() {
        return multiblock;
    }

    public Vec3i getPosition() {
        return new Vec3i(x, y, z);
    }
}
