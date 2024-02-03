package sunsetsatellite.signalindustries.inventories;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IFluidTransfer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;

import java.util.ArrayList;

public class TileEntityExternalIO extends TileEntityTieredMachineBase {

    public TileEntity externalTile;
    public Direction externalTileSide;

    public TileEntityExternalIO(){
    }

    @Override
    public int getSizeInventory() {
        if(externalTile instanceof IInventory){
            return ((IInventory) externalTile).getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i1) {
        if(externalTile instanceof IInventory) {
            return ((IInventory) externalTile).getStackInSlot(i1);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i1, int i2) {
        if(externalTile instanceof IInventory) {
            return ((IInventory) externalTile).decrStackSize(i1, i2);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i1, ItemStack itemStack2) {
        if(externalTile instanceof IInventory){
            ((IInventory) externalTile).setInventorySlotContents(i1, itemStack2);
        }
    }

    @Override
    public String getInvName() {
        if(externalTile instanceof IInventory){
            return ((IInventory) externalTile).getInvName();
        }
        return "External I/O";
    }

    @Override
    public void readFromNBT(CompoundTag CompoundTag1) {
        if(externalTile != null) {
            externalTile.readFromNBT(CompoundTag1);
        }
        super.readFromNBT(CompoundTag1);
    }

    @Override
    public void writeToNBT(CompoundTag CompoundTag1) {
        if(externalTile != null) {
            externalTile.writeToNBT(CompoundTag1);
        }
        super.writeToNBT(CompoundTag1);
    }

    @Override
    public int getInventoryStackLimit() {
        if(externalTile instanceof IInventory) {
            return ((IInventory) externalTile).getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer1) {
        if(externalTile instanceof IInventory) {
            return ((IInventory) externalTile).canInteractWith(entityPlayer1);
        }
        return false;
    }

    @Override
    public void take(@NotNull FluidStack fluidStack, Direction dir) {
        if(externalTile instanceof IFluidTransfer){
            ((IFluidTransfer) externalTile).take(fluidStack, dir);
        }
    }

    @Override
    public void give(Direction dir) {
        if(externalTile instanceof IFluidTransfer) {
            ((IFluidTransfer) externalTile).give(dir);
        }
    }

    @Override
    public FluidStack insertFluid(int slot, FluidStack fluidStack) {
        if(externalTile instanceof IFluidInventory){
            return ((IFluidInventory) externalTile).insertFluid(slot, fluidStack);
        }
        return null;
    }

    @Override
    public int getRemainingCapacity(int slot) {
        if(externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getRemainingCapacity(slot);
        }
        return 0;
    }

    @Override
    public boolean canInsertFluid(int slot, FluidStack fluidStack) {
        if(externalTile instanceof IFluidInventory){
            return ((IFluidInventory) externalTile).canInsertFluid(slot, fluidStack);
        }
        return false;
    }

    @Override
    public Connection getConnection(Direction dir) {
        if(externalTile instanceof IFluidTransfer) {
            return ((IFluidTransfer) externalTile).getConnection(dir);
        }
        return Connection.NONE;
    }

    @Override
    public FluidStack getFluidInSlot(int slot) {
        if(externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getFluidInSlot(slot);
        }
        return null;
    }

    @Override
    public int getFluidCapacityForSlot(int slot) {
        if(externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getFluidCapacityForSlot(slot);
        }
        return 0;
    }

    @Override
    public ArrayList<BlockFluid> getAllowedFluidsForSlot(int slot) {
        if(externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getAllowedFluidsForSlot(slot);
        }
        return new ArrayList<>();
    }

    @Override
    public void setFluidInSlot(int slot, FluidStack fluid) {
        if(externalTile instanceof IFluidInventory) {
            ((IFluidInventory) externalTile).setFluidInSlot(slot, fluid);
        }
    }

    @Override
    public int getFluidInventorySize() {
        if(externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getFluidInventorySize();
        }
        return 0;
    }

    @Override
    public void onFluidInventoryChanged() {
        if(externalTile instanceof IFluidInventory) {
            ((IFluidInventory) externalTile).onFluidInventoryChanged();
        }
    }

    @Override
    public int getTransferSpeed() {
        if(externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getTransferSpeed();
        }
        return 0;
    }

    @Override
    public int getActiveFluidSlot(Direction dir) {
        if(externalTile instanceof IFluidInventory) {
            return ((IFluidInventory) externalTile).getActiveFluidSlot(dir);
        }
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        if(externalTile == null){
            for (Direction dir : Direction.values()) {
                TileEntity tile = dir.getTileEntity(worldObj,this);
                if(tile instanceof IInventory || tile instanceof IFluidInventory){
                    if(!(tile instanceof TileEntityExternalIO)){
                        externalTile = tile;
                        externalTileSide = dir;
                    }
                }
            }
        } else {
            if(worldObj.getBlockTileEntity(externalTile.x,externalTile.y,externalTile.z) != externalTile){
                externalTile = null;
                externalTileSide = null;
            }
        }
    }

}
