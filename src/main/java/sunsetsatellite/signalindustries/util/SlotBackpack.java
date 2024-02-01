package sunsetsatellite.signalindustries.util;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.item.InventoryBackpack;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;

public class SlotBackpack extends Slot {
    public SlotBackpack(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean canPutStackInSlot(ItemStack itemstack) {
        if(itemstack.getItem() instanceof ItemBackpackAttachment){
            return false;
        }
        return super.canPutStackInSlot(itemstack);
    }
}
