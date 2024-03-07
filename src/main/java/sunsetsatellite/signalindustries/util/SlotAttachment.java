package sunsetsatellite.signalindustries.util;


import net.minecraft.client.Minecraft;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.interfaces.IAttachable;
import sunsetsatellite.signalindustries.interfaces.IAttachment;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.attachments.ItemWingsAttachment;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;

public class SlotAttachment extends Slot implements IAttachable {

    public AttachmentPoint attachmentPoint;
    public Tier tier;

    public SlotAttachment(IInventory iinventory, int id, int x, int y, AttachmentPoint attachmentPoint, Tier tier) {
        super(iinventory, id, x, y);
        this.attachmentPoint = attachmentPoint;
        this.tier = tier;
    }

    public IInventory getInventory(){
        return this.inventory;
    }

    @Override
    public boolean canPutStackInSlot(ItemStack itemstack) {
        if(itemstack != null && itemstack.getItem() instanceof IAttachment){
            if(itemstack.getItem() instanceof ITiered && ((ITiered) itemstack.getItem()).getTier().ordinal() > tier.ordinal() && attachmentPoint != AttachmentPoint.CORE_MODULE){
                return false;
            }
            if(attachmentPoint == AttachmentPoint.ANY || ((IAttachment) itemstack.getItem()).getAttachmentPoints().contains(AttachmentPoint.ANY)){
                return true;
            }
            return ((IAttachment) itemstack.getItem()).getAttachmentPoints().contains(attachmentPoint);
        }
        return false;
    }

    @Override
    public void putStack(ItemStack itemstack) {
        super.putStack(itemstack);
        if(itemstack.getItem() instanceof ItemWingsAttachment){
            Minecraft.getMinecraft(this).thePlayer.triggerAchievement(SignalIndustriesAchievementPage.WINGS);
        }
    }

    @Override
    public AttachmentPoint getAttachmentPoint() {
        return attachmentPoint;
    }
}
