package sunsetsatellite.signalindustries.util;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.interfaces.IHasAbility;

public class SlotAbility extends Slot {

    public Mode mode;

    public SlotAbility(IInventory iinventory, int id, int x, int y, Mode mode) {
        super(iinventory, id, x, y);
        this.mode = mode;
    }

    public IInventory getInventory(){
        return this.inventory;
    }

    @Override
    public boolean canPutStackInSlot(ItemStack itemstack) {
        if(itemstack != null && itemstack.getItem() instanceof IHasAbility){
            if(mode != Mode.AWAKENED && ((IHasAbility) itemstack.getItem()).getAbility().mode == Mode.AWAKENED){
                return false;
            }
            return true;
        }
        return false;
    }

}
