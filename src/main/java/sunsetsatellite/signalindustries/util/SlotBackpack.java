package sunsetsatellite.signalindustries.util;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;

public class SlotBackpack extends Slot {
    public SlotBackpack(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        if (itemstack.getItem() instanceof ItemBackpackAttachment) {
            return false;
        }
        return super.mayPlace(itemstack);
    }
}
