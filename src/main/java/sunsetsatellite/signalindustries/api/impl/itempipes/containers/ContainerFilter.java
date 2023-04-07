package sunsetsatellite.signalindustries.api.impl.itempipes.containers;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityFilter;

public class ContainerFilter extends Container {
    private final TileEntityFilter tile;
    private final int numberOfRows;

    public ContainerFilter(IInventory iinventory, TileEntityFilter tileEntity) {
        this.tile = tileEntity;
        this.numberOfRows = 6;
        int i = (this.numberOfRows - 4) * 18;
        int l;
        int j1;
        for(l = 0; l < this.numberOfRows; ++l) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(tileEntity, j1 + l * 9, 8 + j1 * 18, 18 + l * 18));
            }
        }

        for(l = 0; l < 3; ++l) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(iinventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(iinventory, l, 8 + l * 18, 161 + i));
        }

    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer) {
        return this.tile.canInteractWith(entityplayer);
    }

    public void quickMoveItems(int slotID, EntityPlayer player, boolean shift, boolean control) {
        Slot slot = this.inventorySlots.get(slotID);
        if (slot != null && slot.hasStack()) {
            ItemStack item = slot.getStack();
            ItemStack originalItem = item.copy();
            if (slotID < this.numberOfRows * 9) {
                this.onStackMergeShiftClick(item, this.numberOfRows * 9, this.inventorySlots.size(), true);
            } else {
                this.onStackMergeShiftClick(item, 0, this.numberOfRows * 9, false);
            }

            if (item.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (item.stackSize != originalItem.stackSize) {
                slot.onPickupFromSlot(item);
            }

        }
    }
}
