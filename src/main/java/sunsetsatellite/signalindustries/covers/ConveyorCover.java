package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IItemIO;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.items.covers.ItemCover;

public class ConveyorCover extends CoverBase {

    protected final IconCoordinate texture = TextureRegistry.getTexture("signalindustries:block/conveyor_cover");

    public int externalActiveSlot = -1;

    @Override
    public void openConfiguration(EntityPlayer player) {

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
        if(machine instanceof IInventory && machine instanceof TileEntity && machine instanceof IItemIO){
            TileEntity tile = (TileEntity) machine;
            IInventory inv = (IInventory) machine;
            IItemIO io = (IItemIO) machine;
            Connection con = io.getItemIOForSide(dir);
            int activeSlot = io.getActiveItemSlotForSide(dir);
            if(activeSlot == -1 || con == Connection.NONE) return;
            TileEntity otherTile = dir.getTileEntity(tile.worldObj, tile);
            if(otherTile instanceof IInventory && otherTile instanceof IItemIO){
                IInventory otherInv = (IInventory) otherTile;
                IItemIO otherIo = (IItemIO) otherTile;
                Connection otherCon = otherIo.getItemIOForSide(dir.getOpposite());
                int otherActiveSlot = otherIo.getActiveItemSlotForSide(dir.getOpposite());
                if(otherActiveSlot == -1 || otherCon == Connection.NONE) return;
                ItemStack tileStack = inv.getStackInSlot(activeSlot);
                ItemStack otherTileStack = otherInv.getStackInSlot(otherActiveSlot);
                if(con == Connection.INPUT && otherCon == Connection.OUTPUT){
                    if(tileStack == null || (tileStack.isItemEqual(otherTileStack) && tileStack.stackSize+otherTileStack.stackSize <= tileStack.getMaxStackSize())){
                        if(tileStack == null){
                            inv.setInventorySlotContents(activeSlot,otherTileStack);
                            otherInv.setInventorySlotContents(otherActiveSlot,null);
                        } else {
                            tileStack.stackSize += otherTileStack.stackSize;
                            otherInv.setInventorySlotContents(otherActiveSlot,null);
                        }
                    } else if (tileStack.isItemEqual(otherTileStack)) {
                        int remainder = Math.min(tileStack.getMaxStackSize() - tileStack.stackSize, otherTileStack.stackSize);
                        if(remainder <= 0){
                            return;
                        }
                        otherTileStack.stackSize -= remainder;
                        tileStack.stackSize += remainder;
                    }
                } else if (con == Connection.OUTPUT && otherCon == Connection.INPUT) {
                    if(tileStack != null){
                        if(otherTileStack == null || (otherTileStack.isItemEqual(tileStack) && tileStack.stackSize+otherTileStack.stackSize <= otherTileStack.getMaxStackSize())){
                            if(otherTileStack == null){
                                otherInv.setInventorySlotContents(otherActiveSlot,tileStack);
                                inv.setInventorySlotContents(activeSlot,null);
                            } else {
                                otherTileStack.stackSize += tileStack.stackSize;
                                inv.setInventorySlotContents(activeSlot,null);
                            }
                        } else if (otherTileStack.isItemEqual(tileStack)) {
                            int remainder = Math.min(tileStack.getMaxStackSize() - tileStack.stackSize, otherTileStack.stackSize);
                            if(remainder <= 0){
                                return;
                            }
                            otherTileStack.stackSize += remainder;
                            tileStack.stackSize -= remainder;
                        }
                    }
                }
            } else if (otherTile instanceof IInventory) {
                IInventory otherInv = (IInventory) otherTile;
                int otherActiveSlot = externalActiveSlot;
                ItemStack tileStack = inv.getStackInSlot(activeSlot);
                if(otherActiveSlot == -1){
                    for (int i = 0; i < otherInv.getSizeInventory(); i++) {
                        ItemStack stack = otherInv.getStackInSlot(i);
                        if(con == Connection.INPUT){
                            if(stack != null && tileStack != null && stack.isItemEqual(tileStack)){
                                if(tileStack.stackSize+stack.stackSize <= tileStack.getMaxStackSize()){
                                    otherActiveSlot = i;
                                    break;
                                }
                            }
                        } else if (con == Connection.OUTPUT) {
                            if(stack == null && tileStack != null){
                                otherActiveSlot = i;
                                break;
                            } else if (tileStack != null && stack.isItemEqual(tileStack)) {
                                if(tileStack.stackSize+stack.stackSize <= stack.getMaxStackSize()){
                                    otherActiveSlot = i;
                                    break;
                                }
                            }
                        }
                    }
                }
                if(otherActiveSlot == -1) return;
                ItemStack otherTileStack = otherInv.getStackInSlot(otherActiveSlot);
                if(con == Connection.INPUT){
                    if(tileStack == null || (tileStack.isItemEqual(otherTileStack) && tileStack.stackSize+otherTileStack.stackSize <= tileStack.getMaxStackSize())){
                        if(tileStack == null){
                            inv.setInventorySlotContents(activeSlot,otherTileStack);
                            otherInv.setInventorySlotContents(otherActiveSlot,null);
                        } else {
                            tileStack.stackSize += otherTileStack.stackSize;
                            otherInv.setInventorySlotContents(otherActiveSlot,null);
                        }
                    } else if (tileStack.isItemEqual(otherTileStack)) {
                        int remainder = Math.min(tileStack.getMaxStackSize() - tileStack.stackSize, otherTileStack.stackSize);
                        if(remainder <= 0){
                            return;
                        }
                        otherTileStack.stackSize -= remainder;
                        tileStack.stackSize += remainder;
                    }
                } else if (con == Connection.OUTPUT) {
                    if(tileStack != null){
                        if(otherTileStack == null || (otherTileStack.isItemEqual(tileStack) && tileStack.stackSize+otherTileStack.stackSize <= otherTileStack.getMaxStackSize())){
                            if(otherTileStack == null){
                                otherInv.setInventorySlotContents(otherActiveSlot,tileStack);
                                inv.setInventorySlotContents(activeSlot,null);
                            } else {
                                otherTileStack.stackSize += tileStack.stackSize;
                                inv.setInventorySlotContents(activeSlot,null);
                            }
                        } else if (otherTileStack.isItemEqual(tileStack)) {
                            int remainder = Math.min(tileStack.getMaxStackSize() - tileStack.stackSize, otherTileStack.stackSize);
                            if(remainder <= 0){
                                return;
                            }
                            otherTileStack.stackSize += remainder;
                            tileStack.stackSize -= remainder;
                        }
                    }
                }
            }
        }
    }

    @Override
    public IconCoordinate getTexture() {
        return texture;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.conveyorCover;
    }

    @Override
    public void onInstalled(Direction dir, IAcceptsCovers machine, EntityPlayer player) {
        player.sendMessage("Cover installed!");
        super.onInstalled(dir, machine, player);
    }

    @Override
    public void onRemoved(EntityPlayer player) {
        player.sendMessage("Cover removed!");
        super.onRemoved(player);
    }
}
