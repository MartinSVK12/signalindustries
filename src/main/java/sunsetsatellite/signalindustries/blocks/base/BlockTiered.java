package sunsetsatellite.signalindustries.blocks.base;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockTiered extends Block implements ITiered {

    public Tier tier;

    public BlockTiered(String key, int i, Tier tier, Material material) {
        super(key, i, material);
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