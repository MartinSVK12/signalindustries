package sunsetsatellite.signalindustries.util;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.interfaces.IApplicationItem;

public class SlotApplication extends Slot {

    public Tier tier;

    public SlotApplication(IInventory iinventory, int id, int x, int y, Tier tier) {
        super(iinventory, id, x, y);
        this.tier = tier;
    }

    public IInventory getInventory(){
        return this.inventory;
    }

    @Override
    public boolean canPutStackInSlot(ItemStack itemstack) {
        if(itemstack != null && (itemstack.getItem() instanceof IApplicationItem)){
            return ((IApplicationItem<?>) itemstack.getItem()).getTier().ordinal() <= tier.ordinal();
        }
        return false;
    }

}
