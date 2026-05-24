package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureManager;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.interfaces.IAttachment;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;

import java.util.List;

public abstract class ItemAttachment extends Item implements IAttachment, ICustomDescription {

    public final List<AttachmentPoint> attachmentPoints;

    public ItemAttachment(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints) {
        super(translationKey, namespaceId, id);
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

    public abstract void tick(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, int slot);

    public void loadTexture(String texturePath) {
        TextureManager t = Minecraft.getMinecraft().textureManager;
        t.bindTexture(t.loadTexture(texturePath));
    }
}
