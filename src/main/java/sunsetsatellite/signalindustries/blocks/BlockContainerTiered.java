package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.BlockContainerRotatable;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import sunsetsatellite.signalindustries.Tiers;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;

public class BlockContainerTiered extends BlockContainerRotatable implements ICustomDescription {

    public Tiers tier;

    public BlockContainerTiered(int i, Tiers tier, Material material) {
        super(i, material);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder text = new StringBuilder();
        return text.append("Tier: ").append(tier.getColor()).append(tier.getRank()).toString();
    }

    @Override
    protected TileEntity getBlockEntity() {
        return null;
    }
}
