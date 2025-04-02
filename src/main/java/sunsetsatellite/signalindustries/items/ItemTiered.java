package sunsetsatellite.signalindustries.items;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemTiered extends Item implements ITiered {
    public Tier tier;

    public ItemTiered(String translationKey, String namespaceId, int id, Tier tier) {
        super(translationKey, namespaceId, id);
        this.tier = tier;
    }


    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getTextColor() + tier.getRank();
    }

    @Override
    public Tier getTier() {
        return tier;
    }
}
