package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.BlockContainerRotatable;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import sunsetsatellite.signalindustries.Tiers;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;
import sunsetsatellite.signalindustries.interfaces.ITiered;

public abstract class BlockContainerTiered extends BlockContainerRotatable implements ITiered {

    public Tiers tier;

    public BlockContainerTiered(int i, Tiers tier, Material material) {
        super(i, material);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank();
    }
}
