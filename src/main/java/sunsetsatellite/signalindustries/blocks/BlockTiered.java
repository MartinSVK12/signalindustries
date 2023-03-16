package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import sunsetsatellite.signalindustries.Tiers;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;
import sunsetsatellite.signalindustries.interfaces.ITiered;

public class BlockTiered extends Block implements ITiered {

    public Tiers tier;

    public BlockTiered(int i, Tiers tier, Material material) {
        super(i, material);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank();
    }

}