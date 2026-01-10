package sunsetsatellite.signalindustries.invs;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventorySorter;
import net.minecraft.core.player.inventory.container.Container;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.util.InventorySerializer;

import java.util.ArrayList;

public abstract class InventoryItemFluid implements Container, IFluidInventory {

    public ItemStack[] contents = new ItemStack[1];

    public FluidStack[] fluidContents = new FluidStack[1];
    public int[] fluidCapacity = new int[1];
    public ArrayList<ArrayList<Fluid>> acceptedFluids = new ArrayList<>(fluidContents.length);
    public int transferSpeed = 20;

    public final ItemStack container;

    public InventoryItemFluid(ItemStack container) {
        this.container = container;
    }

    public void save() {
        InventorySerializer.saveInvToNBT(container, this);
    }

    public void load() {
        InventorySerializer.loadInvFromNBT(container, this, getContainerSize(), getFluidInventorySize());
    }

    public @Nullable ItemStack getItem(int index) {
        return this.contents[index];
    }

    public @Nullable ItemStack removeItem(int index, int takeAmount) {
        if (this.contents[index] != null) {
            if (this.contents[index].stackSize <= takeAmount) {
                ItemStack itemstack = this.contents[index];
                this.contents[index] = null;
                this.setChanged();
                return itemstack;
            } else {
                ItemStack itemstack1 = this.contents[index].splitStack(takeAmount);
                if (this.contents[index].stackSize <= 0) {
                    this.contents[index] = null;
                }

                this.setChanged();
                return itemstack1;
            }
        } else {
            return null;
        }
    }

    public void setItem(int index, @Nullable ItemStack itemstack) {
        this.contents[index] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getMaxStackSize()) {
            itemstack.stackSize = this.getMaxStackSize();
        }

        this.setChanged();
    }

    public int getContainerSize() {
        return this.contents.length;
    }

    public int getMaxStackSize() {
        return 64;
    }

    public void setChanged() {
    }

    public boolean stillValid(Player entityplayer) {
        return true;
    }

    public void sortContainer() {
        InventorySorter.sortInventory(this.contents);
    }

    @Override
    public boolean canInsertFluid(int slot, FluidStack fluidStack) {
        if (getFluidInSlot(slot) != null) if (!getFluidInSlot(slot).isFluidEqual(fluidStack)) return false;
        return Math.min(fluidStack.amount, getRemainingCapacity(slot)) > 0;
    }

    @Override
    public FluidStack getFluidInSlot(int slot) {
        if (this.fluidContents.length == 0) return null;
        if (this.fluidContents[slot] == null || this.fluidContents[slot].fluid == null || this.fluidContents[slot].amount == 0) {
            this.fluidContents[slot] = null;
        }
        return fluidContents[slot];
    }


    @Override
    public int getFluidCapacityForSlot(int slot) {
        return fluidCapacity[slot];
    }

    @Override
    public ArrayList<Fluid> getAllowedFluidsForSlot(int slot) {
        return acceptedFluids.get(slot);
    }

    @Override
    public void setFluidInSlot(int slot, FluidStack fluid) {
        if (fluid == null || fluid.amount == 0 || fluid.fluid == null) {
            this.fluidContents[slot] = null;
            this.onFluidInventoryChanged();
            return;
        }
        if (acceptedFluids.get(slot).contains(fluid.fluid) || acceptedFluids.get(slot).isEmpty()) {
            this.fluidContents[slot] = fluid;
            this.onFluidInventoryChanged();
        }

    }

    @Override
    public FluidStack insertFluid(int slot, FluidStack fluidStack) {
        FluidStack stack = fluidContents[slot];
        FluidStack split = fluidStack.splitStack(Math.min(fluidStack.amount, getRemainingCapacity(slot)));
        if (stack != null && split.amount > 0) {
            fluidContents[slot].amount += split.amount;
        } else {
            fluidContents[slot] = split;
        }
        return fluidStack;
    }

    @Override
    public int getRemainingCapacity(int slot) {
        if (fluidContents[slot] == null) {
            return fluidCapacity[slot];
        }
        return fluidCapacity[slot] - fluidContents[slot].amount;
    }

    @Override
    public int getFluidInventorySize() {
        return fluidContents.length;
    }

    @Override
    public void onFluidInventoryChanged() {

    }

    @Override
    public int getTransferSpeed() {
        return transferSpeed;
    }
}
