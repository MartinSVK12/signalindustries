package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import sunsetsatellite.signalindustries.Tiers;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;

public class BlockTiered extends Block implements ICustomDescription {

    public Tiers tier;

    public BlockTiered(int i, Tiers tier, Material material) {
        super(i, material);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder text = new StringBuilder();
        return text.append("Tier: ").append(tier.getColor()).append(tier.getRank()).toString();
    }

}