package sunsetsatellite.signalindustries.containers;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.inventories.TileEntityFilter;

import java.util.List;

public class ContainerFilter extends Container {
    private final TileEntityFilter tile;

    public ContainerFilter(IInventory iinventory, TileEntityFilter tileEntity) {
        this.tile = tileEntity;
        int numberOfRows = 6;
        int i = (numberOfRows - 4) * 18;
        int l;
        int j1;
        for(l = 0; l < numberOfRows; ++l) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(tileEntity, j1 + l * 9, 8 + j1 * 18, 18 + l * 18));
            }
        }

        for(l = 0; l < 3; ++l) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(iinventory, j1 + l * 9 + 9, 8 + j1 * 18, 115 + l * 18 + i));
            }
        }

        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(iinventory, l, 8 + l * 18, 173 + i));
        }

    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return tile.canInteractWith(entityPlayer);
    }
}
