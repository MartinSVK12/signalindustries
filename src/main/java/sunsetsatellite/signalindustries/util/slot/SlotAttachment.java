package sunsetsatellite.signalindustries.util.slot;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.interfaces.IAttachable;
import sunsetsatellite.signalindustries.interfaces.IAttachment;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.attachments.ItemWingsAttachment;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class SlotAttachment extends Slot implements IAttachable {

    public AttachmentPoint attachmentPoint;
    public Tier tier;

    public SlotAttachment(Container inv, int id, int x, int y, AttachmentPoint attachmentPoint, Tier tier) {
        super(inv, id, x, y);
        this.attachmentPoint = attachmentPoint;
        this.tier = tier;
    }

    public Container getInventory() {
        return this.container;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        if (getInventory().locked(index)) return false;
        if (itemstack != null && itemstack.getItem() instanceof IAttachment) {
            if (itemstack.getItem() instanceof ITiered && ((ITiered) itemstack.getItem()).getTier().ordinal() > tier.ordinal() && attachmentPoint != AttachmentPoint.CORE_MODULE) {
                return false;
            }
            if (attachmentPoint == AttachmentPoint.ANY || ((IAttachment) itemstack.getItem()).getAttachmentPoints().contains(AttachmentPoint.ANY)) {
                return true;
            }
            return ((IAttachment) itemstack.getItem()).getAttachmentPoints().contains(attachmentPoint);
        }
        return false;
    }

    @Override
    public void set(ItemStack itemstack) {
        super.set(itemstack);
        if(itemstack != null && itemstack.getItem() instanceof ItemWingsAttachment && !EnvironmentHelper.isMultiplayerServer()){
            Minecraft.getMinecraft().thePlayer.triggerAchievement(SIAchievements.WINGS);
        }
    }

    @Override
    public AttachmentPoint getAttachmentPoint() {
        return attachmentPoint;
    }
}
