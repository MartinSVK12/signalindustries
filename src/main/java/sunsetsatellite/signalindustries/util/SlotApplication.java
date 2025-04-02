package sunsetsatellite.signalindustries.util;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.interfaces.IApplicationItem;

public class SlotApplication extends Slot {

    public Tier tier;

    public SlotApplication(Container iinventory, int id, int x, int y, Tier tier) {
        super(iinventory, id, x, y);
        this.tier = tier;
    }

    public Container getInventory(){
        return this.container;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        if(itemstack != null && (itemstack.getItem() instanceof IApplicationItem)){
            return ((IApplicationItem<?>) itemstack.getItem()).getTier().ordinal() <= tier.ordinal();
        }
        return false;
    }
}
