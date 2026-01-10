package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockLogicTiered extends BlockLogicFullyRotatable implements ITiered {

    public Tier tier;

    public BlockLogicTiered(Block<?> block, Material material, Tier tier) {
        super(block, material);
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
