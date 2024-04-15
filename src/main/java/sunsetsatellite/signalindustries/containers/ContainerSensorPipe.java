package sunsetsatellite.signalindustries.containers;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.inventories.TileEntityItemConduit;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityAssembler;

import java.util.List;

public class ContainerSensorPipe extends Container {

    public ContainerSensorPipe(IInventory iInventory){
        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iInventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iInventory, k, 8 + k * 18, 142));
        }
    }

    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id == 0) {
            return this.getSlots(0, 1, false);
        } else if (slot.id >= 1 && slot.id <= 4) {
            return this.getSlots(1, 4, false);
        } else if (slot.id >= 5 && slot.id <= 8) {
            return this.getSlots(5, 4, false);
        } else {
            if (action == InventoryAction.MOVE_SIMILAR) {
                if (slot.id >= 9 && slot.id <= 44) {
                    return this.getSlots(9, 36, false);
                }
            } else {
                if (slot.id >= 9 && slot.id <= 35) {
                    return this.getSlots(9, 27, false);
                }

                if (slot.id >= 36 && slot.id <= 44) {
                    return this.getSlots(36, 9, false);
                }
            }

            return null;
        }
    }

    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id >= 9 && slot.id <= 44) {
            if (target == 1) {
                return this.getSlots(1, 4, false);
            } else if (target == 2) {
                return this.getSlots(5, 4, false);
            } else {
                return slot.id < 36 ? this.getSlots(36, 9, false) : this.getSlots(9, 27, false);
            }
        } else {
            return slot.id == 0 ? this.getSlots(9, 36, true) : this.getSlots(9, 36, false);
        }
    }
    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }
}
