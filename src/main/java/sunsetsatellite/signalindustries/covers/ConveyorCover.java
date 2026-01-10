package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.items.covers.ItemCover;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ConveyorCover extends CoverBase {

    protected final String texture = "signalindustries:block/conveyor_cover";

    public int externalActiveSlot = 0;

    @Override
    public void openConfiguration(Player player, Direction dir) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        super.writeToNbt(tag);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        super.readFromNbt(tag);
    }

    @Override
    public void tick() {
        if (machine instanceof Container && machine instanceof TileEntity && machine instanceof IItemIO) {
            TileEntity tile = (TileEntity) machine;
            InventoryWrapper inv = new InventoryWrapper((Container) machine);
            IItemIO io = (IItemIO) machine;
            Connection con = io.getItemIOForSide(dir);
            int activeSlot = io.getActiveItemSlotForSide(dir);
            TileEntity otherTile = dir.getTileEntity(tile.worldObj, tile);
            if (activeSlot == -1 || con == Connection.NONE) return;
            if (otherTile instanceof Container && otherTile instanceof IItemIO) {
                InventoryWrapper otherInv = new InventoryWrapper((Container) otherTile);
                IItemIO otherIo = (IItemIO) otherTile;
                Connection otherCon = otherIo.getItemIOForSide(dir.getOpposite());
                int otherActiveSlot = otherIo.getActiveItemSlotForSide(dir.getOpposite());
                if (otherActiveSlot == -1 || otherCon == Connection.NONE) return;
                if (con == Connection.INPUT && otherCon == Connection.OUTPUT) {
                    Optional<ItemStack> stack = Optional.ofNullable(otherInv.remove(otherActiveSlot, false, false));
                    AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                    stack.ifPresent(S -> leftovers.set(Optional.ofNullable(inv.add(S))));
                    leftovers.get().ifPresent(S -> otherInv.add(otherActiveSlot, S));
                } else if (con == Connection.OUTPUT && otherCon == Connection.INPUT) {
                    Optional<ItemStack> stack = Optional.ofNullable(inv.remove(activeSlot, false, false));
                    AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                    stack.ifPresent(S -> leftovers.set(Optional.ofNullable(otherInv.add(otherActiveSlot, S))));
                    leftovers.get().ifPresent(S -> inv.add(activeSlot, S));
                }
            } else if (otherTile instanceof Container) {
                InventoryWrapper otherInv = new InventoryWrapper((Container) otherTile);
                if (con == Connection.INPUT) {
                    Optional<ItemStack> stack = Optional.ofNullable(otherInv.remove(externalActiveSlot, false, false));
                    AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                    stack.ifPresent(S -> leftovers.set(Optional.ofNullable(inv.add(activeSlot, S))));
                    leftovers.get().ifPresent(S -> otherInv.add(externalActiveSlot, S));
                } else if (con == Connection.OUTPUT) {
                    Optional<ItemStack> stack = Optional.ofNullable(inv.remove(activeSlot, false, false));
                    AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                    stack.ifPresent(S -> leftovers.set(Optional.ofNullable(otherInv.add(S))));
                    leftovers.get().ifPresent(S -> inv.add(activeSlot, S));
                }
            }
        }
    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.conveyorCover;
    }

    @Override
    public void onInstalled(Direction dir, IAcceptsCovers machine, Player player) {
        player.sendMessage("Cover installed!");
        super.onInstalled(dir, machine, player);
    }

    @Override
    public void onRemoved(Player player) {
        player.sendMessage("Cover removed!");
        super.onRemoved(player);
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {

    }
}
