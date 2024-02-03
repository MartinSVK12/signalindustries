package sunsetsatellite.signalindustries.blocks.base;


import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public abstract class BlockContainerTiered extends BlockTileEntityRotatable implements ITiered {

    public Tier tier;

    public BlockContainerTiered(String key, int i, Tier tier, Material material) {
        super(key, i, material);
        this.tier = tier;
    }

    @Override
    public Tier getTier(){
        return tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank();
    }
}
