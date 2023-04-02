package sunsetsatellite.signalindustries.items;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.util.Tiers;
import sunsetsatellite.signalindustries.interfaces.ITiered;

public class ItemTiered extends Item implements ITiered {
    public Tiers tier;

    public ItemTiered(int i, Tiers tier) {
        super(i);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank();
    }
}
