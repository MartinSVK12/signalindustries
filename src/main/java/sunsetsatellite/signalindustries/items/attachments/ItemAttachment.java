package sunsetsatellite.signalindustries.items.attachments;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.signalindustries.interfaces.IAttachment;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.sunsetutils.util.ICustomDescription;

import java.util.List;

public class ItemAttachment extends Item implements IAttachment, ICustomDescription {

    List<AttachmentPoint> attachmentPoints;

    public ItemAttachment(int i, List<AttachmentPoint> attachmentPoints) {
        super(i);
        this.attachmentPoints = attachmentPoints;
    }

    @Override
    public List<AttachmentPoint> getAttachmentPoints() {
        return attachmentPoints;
    }

    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder s = new StringBuilder(TextFormatting.YELLOW + "Attachment" + TextFormatting.WHITE);
        for (AttachmentPoint attachmentPoint : attachmentPoints) {
            s.append("\n").append("- ").append(attachmentPoint);
        }
        return s.toString();
    }
}
