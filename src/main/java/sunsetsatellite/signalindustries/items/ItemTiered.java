package sunsetsatellite.signalindustries.items;



import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.interfaces.ITiered;

public class ItemTiered extends Item implements ITiered {
    public Tier tier;

    public ItemTiered(int i, Tier tier) {
        super(i);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank();
    }

    @Override
    public Tier getTier() {
        return tier;
    }
}
