package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.interfaces.ITiered;

public abstract class BlockContainerTiered extends BlockContainerRotatable implements ITiered {

    public Tier tier;

    public BlockContainerTiered(int i, Tier tier, Material material) {
        super(i, material);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank();
    }
}
