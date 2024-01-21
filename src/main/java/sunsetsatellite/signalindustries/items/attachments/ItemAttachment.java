package sunsetsatellite.signalindustries.items.attachments;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.interfaces.IAttachment;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;

import java.util.List;

public class ItemAttachment extends Item implements IAttachment, ICustomDescription {

    List<AttachmentPoint> attachmentPoints;

    public ItemAttachment(String name, int id, List<AttachmentPoint> attachmentPoints) {
        super(name, id);
        this.attachmentPoints = attachmentPoints;
    }

    /*public ItemAttachment(int i, List<AttachmentPoint> attachmentPoints) {
        super(i);
        this.attachmentPoints = attachmentPoints;
    }*/

    @Override
    public List<AttachmentPoint> getAttachmentPoints() {
        return attachmentPoints;
    }

    @Override
    public void activate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {
    }

    @Override
    public void openSettings(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {
    }

    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder s = new StringBuilder(TextFormatting.YELLOW + "Attachment" + TextFormatting.WHITE);
        for (AttachmentPoint attachmentPoint : attachmentPoints) {
            s.append("\n").append("- ").append(attachmentPoint);
        }
        return s.toString();
    }

    public void tick(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world, int slot){};
}
