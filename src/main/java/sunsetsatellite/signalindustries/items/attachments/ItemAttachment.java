package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.command.ChatColor;
import sunsetsatellite.signalindustries.interfaces.IAttachment;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;
import sunsetsatellite.signalindustries.util.AttachmentPoint;

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
        StringBuilder s = new StringBuilder(ChatColor.yellow + "Attachment" + ChatColor.white);
        for (AttachmentPoint attachmentPoint : attachmentPoints) {
            s.append("\n").append("- ").append(attachmentPoint);
        }
        return s.toString();
    }
}
