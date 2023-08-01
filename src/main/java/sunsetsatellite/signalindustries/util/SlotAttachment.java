package sunsetsatellite.signalindustries.util;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import sunsetsatellite.signalindustries.interfaces.IAttachable;
import sunsetsatellite.signalindustries.interfaces.IAttachment;

public class SlotAttachment extends Slot implements IAttachable {

    public AttachmentPoint attachmentPoint;

    public SlotAttachment(IInventory iinventory, int id, int x, int y, AttachmentPoint attachmentPoint) {
        super(iinventory, id, x, y);
        this.attachmentPoint = attachmentPoint;
    }

    public IInventory getInventory(){
        return this.inventory;
    }

    @Override
    public boolean canPutStackInSlot(ItemStack itemstack) {
        if(itemstack != null && itemstack.getItem() instanceof IAttachment){
            if(attachmentPoint == AttachmentPoint.ANY || ((IAttachment) itemstack.getItem()).getAttachmentPoints().contains(AttachmentPoint.ANY)){
                return true;
            }
            return ((IAttachment) itemstack.getItem()).getAttachmentPoints().contains(attachmentPoint);
        }
        return false;
    }

    @Override
    public AttachmentPoint getAttachmentPoint() {
        return attachmentPoint;
    }
}