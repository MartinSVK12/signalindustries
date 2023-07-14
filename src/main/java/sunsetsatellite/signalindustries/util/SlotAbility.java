package sunsetsatellite.signalindustries.util;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import sunsetsatellite.signalindustries.interfaces.IAttachable;
import sunsetsatellite.signalindustries.interfaces.IAttachment;
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
            if(mode == Mode.AWAKENED || ((IHasAbility) itemstack.getItem()).getAbility().mode == Mode.NORMAL){
                return true;
            }
            return ((IHasAbility) itemstack.getItem()).getAbility().mode == mode;
        }
        return false;
    }

}
