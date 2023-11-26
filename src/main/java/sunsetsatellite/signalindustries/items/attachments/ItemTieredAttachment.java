package sunsetsatellite.signalindustries.items.attachments;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemTieredAttachment extends ItemAttachment implements ITiered {
    public Tier tier;

    public ItemTieredAttachment(int i, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(i, attachmentPoints);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank()+"\n"+super.getDescription(stack);
    }

    @Override
    public Tier getTier() {
        return tier;
    }
}
