package sunsetsatellite.signalindustries.blocks;


import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.interfaces.ITiered;

public class BlockTiered extends Block implements ITiered {

    public Tier tier;

    public BlockTiered(int i, Tier tier, Material material) {
        super(i, material);
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