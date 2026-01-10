package sunsetsatellite.signalindustries.tiles;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IFluidTransfer;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.interfaces.IAcceptsPosition;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;

public class TileEntityExternalIO extends TileEntityTieredMachineBase implements IAcceptsPosition {

    public TileEntity externalTile;
    public Direction externalTileSide;
    public CompoundTag externalTilePos;
    public static int range = 5;

    public TileEntityExternalIO() {
    }

    @Override
    public int getActiveFluidSlotForSide(Direction dir) {
        if (externalTile instanceof IFluidIO) {
            return ((IFluidIO) externalTile).getActiveFluidSlotForSide(dir);
        }
        return 0;
    }

    @Override
    public Connection getFluidIOForSide(Direction dir) {
        if (externalTile instanceof IFluidIO) {
            return ((IFluidIO) externalTile).getFluidIOForSide(dir);
        }
        return Connection.NONE;
    }

    @Override
    public void take(@NotNull FluidStack fluidStack, Direction dir, int slot) {
        if (externalTile instanceof IFluidTransfer) {
            ((IFluidTransfer) externalTile).take(fluidStack, dir, slot);
        }
    }

    @Override
    public void give(Direction dir, int slot, int otherSlot) {
        if (externalTile instanceof IFluidTransfer) {
            ((IFluidTransfer) externalTile).give(dir, slot, otherSlot);
        }
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        if (tag.containsKey("externalPosition")) {
            externalTilePos = tag.getCompound("externalPosition");
        }
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        if (externalTilePos != null) {
            tag.put("externalPosition", externalTilePos);
        }
        super.writeToNBT(tag);
    }

    @Override
    public void take(@NotNull FluidStack fluidStack, Direction dir) {
        if (externalTile instanceof IFluidTransfer) {
            ((IFluidTransfer) externalTile).take(fluidStack, dir);
        }
    }

    @Override
    public void give(Direction dir) {
        if (externalTile instanceof IFluidTransfer) {
            ((IFluidTransfer) externalTile).give(dir);
        }
    }

    @Override
    public FluidStack insertFluid(int slot, FluidStack fluidStack) {
        if (externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).insertFluid(slot, fluidStack);
        }
        return null;
    }

    @Override
    public int getRemainingCapacity(int slot) {
        if (externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getRemainingCapacity(slot);
        }
        return 0;
    }

    @Override
    public boolean canInsertFluid(int slot, FluidStack fluidStack) {
        if (externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).canInsertFluid(slot, fluidStack);
        }
        return false;
    }

    @Override
    public FluidStack getFluidInSlot(int slot) {
        if (externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getFluidInSlot(slot);
        }
        return null;
    }

    @Override
    public int getFluidCapacityForSlot(int slot) {
        if (externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getFluidCapacityForSlot(slot);
        }
        return 0;
    }

    @Override
    public ArrayList<Fluid> getAllowedFluidsForSlot(int slot) {
        if (externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getAllowedFluidsForSlot(slot);
        }
        return new ArrayList<>();
    }

    @Override
    public void setFluidInSlot(int slot, FluidStack fluid) {
        if (externalTile instanceof IFluidInventory) {
            ((IFluidInventory) externalTile).setFluidInSlot(slot, fluid);
        }
    }

    @Override
    public int getFluidInventorySize() {
        if (externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getFluidInventorySize();
        }
        return 0;
    }

    @Override
    public void onFluidInventoryChanged() {
        if (externalTile instanceof IFluidInventory) {
            ((IFluidInventory) externalTile).onFluidInventoryChanged();
        }
    }

    @Override
    public int getTransferSpeed() {
        if (externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getTransferSpeed();
        }
        return 0;
    }

    @Override
    public int getActiveItemSlotForSide(Direction dir, ItemStack stack) {
        if (externalTile instanceof Container) {
            if (activeItemSlots.get(dir) == -1) {
                if (itemConnections.get(dir) == Connection.INPUT) {
                    for (int i = 0; i < ((Container) externalTile).getContainerSize(); i++) {
                        ItemStack content = ((Container) externalTile).getItem(i);
                        if (content == null || content.isItemEqual(stack)) {
                            return i;
                        }
                    }
                } else if (itemConnections.get(dir) == Connection.OUTPUT) {
                    for (int i = 0; i < ((Container) externalTile).getContainerSize(); i++) {
                        ItemStack content = ((Container) externalTile).getItem(i);
                        if (content != null) {
                            return i;
                        }
                    }
                }
                return 0;
            } else {
                return activeItemSlots.get(dir);
            }
        }
        return 0;
    }

    /*@Override
    public void setFluidIOForSide(Direction dir, Connection con) {
        if(externalTile instanceof IFluidIO){
            ((IFluidIO) externalTile).setFluidIOForSide(dir, con);
        }
    }

    @Override
    public void cycleFluidIOForSide(Direction dir) {
        if(externalTile instanceof IFluidIO){
            ((IFluidIO) externalTile).cycleFluidIOForSide(dir);
        }
    }

    @Override
    public void cycleActiveFluidSlotForSide(Direction dir, boolean backwards) {
        if(externalTile instanceof IFluidIO){
            ((IFluidIO) externalTile).cycleActiveFluidSlotForSide(dir, backwards);
        }
    }

    @Override
    public void setActiveFluidSlotForSide(Direction dir, int slot) {
        if(externalTile instanceof IFluidIO){
            ((IFluidIO) externalTile).setActiveFluidSlotForSide(dir, slot);
        }
    }*/

    @Override
    public int getContainerSize() {
        if (externalTile instanceof Container) {
            return ((Container) externalTile).getContainerSize();
        }
        return 0;
    }

    @Override
    public @Nullable ItemStack getItem(int index) {
        if (externalTile instanceof Container) {
            return ((Container) externalTile).getItem(index);
        }
        return null;
    }

    @Override
    public @Nullable ItemStack removeItem(int index, int takeAmount) {
        if (externalTile instanceof Container) {
            return ((Container) externalTile).removeItem(index, takeAmount);
        }
        return null;
    }

    @Override
    public void setItem(int index, @Nullable ItemStack itemstack) {
        if (externalTile instanceof Container) {
            ((Container) externalTile).setItem(index, itemstack);
        }
    }

    @Override
    public int getMaxStackSize() {
        if (externalTile instanceof Container) {
            return ((Container) externalTile).getMaxStackSize();
        }
        return 0;
    }

    @Override
    public int getActiveItemSlotForSide(Direction dir) {
        if (externalTile instanceof IItemIO) {
            return ((IItemIO) externalTile).getActiveItemSlotForSide(dir);
        }
        return 0;
    }

    /*@Override
    public void setActiveItemSlotForSide(Direction dir, int slot) {
        if(externalTile instanceof IItemIO) {
            ((IItemIO) externalTile).setActiveItemSlotForSide(dir, slot);
        }
    }*/

    @Override
    public Connection getItemIOForSide(Direction dir) {
        if (externalTile instanceof IItemIO) {
            return ((IItemIO) externalTile).getItemIOForSide(dir);
        }
        return Connection.NONE;
    }

    /*@Override
    public void setItemIOForSide(Direction dir, Connection con) {
        if(externalTile instanceof IItemIO) {
            ((IItemIO) externalTile).setItemIOForSide(dir, con);
        }
    }

    @Override
    public void cycleItemIOForSide(Direction dir) {
        if(externalTile instanceof IItemIO) {
            ((IItemIO) externalTile).cycleItemIOForSide(dir);
        }
    }

    @Override
    public void cycleActiveItemSlotForSide(Direction dir, boolean backwards) {
        if(externalTile instanceof IItemIO) {
            ((IItemIO) externalTile).cycleActiveItemSlotForSide(dir, backwards);
        }
    }*/

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        if (externalTile == null) {
            if (tier == Tier.BASIC) {
                for (Direction dir : Direction.values()) {
                    TileEntity tile = dir.getTileEntity(worldObj, this);
                    if (tile instanceof Container || tile instanceof IFluidInventory) {
                        if (!(tile instanceof TileEntityExternalIO)) {
                            externalTile = tile;
                            externalTileSide = dir;
                            CompoundTag pos = new CompoundTag();
                            pos.putInt("x", externalTile.x);
                            pos.putInt("y", externalTile.y);
                            pos.putInt("z", externalTile.z);
                            pos.putInt("side", dir.getSideNumber());
                            pos.putInt("dim", externalTile.worldObj.dimension.id);
                            externalTilePos = pos;
                        }
                    }
                }
            } else if (tier == Tier.REINFORCED) {
                if (externalTilePos != null) {
                    if (externalTilePos.containsKey("x") && externalTilePos.containsKey("y") && externalTilePos.containsKey("z") && externalTilePos.containsKey("dim") && externalTilePos.containsKey("side")) {
                        int eX = externalTilePos.getInteger("x");
                        int eY = externalTilePos.getInteger("y");
                        int eZ = externalTilePos.getInteger("z");
                        int dim = externalTilePos.getInteger("dim");
                        Vec3i pos = new Vec3i(eX, eY, eZ);
                        Vec3f selfPos = new Vec3f(x, y, z);
                        if (pos.distanceTo(selfPos) < range && dim == worldObj.dimension.id) {
                            TileEntity tile = worldObj.getTileEntity(externalTilePos.getInteger("x"), externalTilePos.getInteger("y"), externalTilePos.getInteger("z"));
                            if (tile instanceof Container || tile instanceof IFluidInventory) {
                                if (!(tile instanceof TileEntityExternalIO)) {
                                    externalTile = tile;
                                    externalTileSide = Direction.getDirectionFromSide(externalTilePos.getInteger("side"));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (worldObj.getTileEntity(externalTile.x, externalTile.y, externalTile.z) != externalTile) {
                externalTile = null;
                externalTileSide = null;
                externalTilePos = null;
            }
        }
    }

    public Player getNearestPlayer() {
        int dist = -1;
        Player ret = null;
        for (Player p : worldObj.players) {
            int nd = (int) (Math.pow((int) p.x - x, 2) + Math.pow((int) p.y - y, 2) + Math.pow((int) p.z - z, 2));
            if (dist == -1 || nd < dist) {
                ret = p;
                dist = nd;
            }
        }
        return ret;
    }

    @Override
    public void receivePosition(int x, int y, int z, Side side, int dim) {
        if (tier == Tier.REINFORCED) {
            Vec3i pos = new Vec3i(x, y, z);
            Vec3f selfPos = new Vec3f(this.x, this.y, this.z);
            CompoundTag tag = new CompoundTag();
            tag.putInt("x", x);
            tag.putInt("y", y);
            tag.putInt("z", z);
            tag.putInt("side", side.getId());
            tag.putInt("dim", dim);
            externalTilePos = tag;
            TileEntity tile = worldObj.getTileEntity(x, y, z);
            if (pos.distanceTo(selfPos) < range) {
                if (dim == worldObj.dimension.id) {
                    if (tile instanceof Container || tile instanceof IFluidInventory) {
                        if (!(tile instanceof TileEntityExternalIO)) {
                            externalTile = tile;
                            externalTileSide = Direction.getDirectionFromSide(side.getId());
                            getNearestPlayer().sendMessage("Link established!");
                        }
                    } else {
                        getNearestPlayer().sendMessage("invalid block at position!");
                    }
                } else {
                    getNearestPlayer().sendMessage("Position outside this world!");
                }
            } else {
                getNearestPlayer().sendMessage("Position out of reach!");
            }
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.externalIo";
    }
}
