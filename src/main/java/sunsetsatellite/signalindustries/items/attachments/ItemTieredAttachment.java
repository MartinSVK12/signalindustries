package sunsetsatellite.signalindustries.items.attachments;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public abstract class ItemTieredAttachment extends ItemAttachment implements ITiered {
    public Tier tier;

    public ItemTieredAttachment(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(translationKey, namespaceId, id, attachmentPoints);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getTextColor() + tier.getRank()+"\n"+super.getDescription(stack);
    }

    @Override
    public Tier getTier() {
        return tier;
    }
}
