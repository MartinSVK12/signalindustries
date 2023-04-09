package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerAlloySmelter;
import sunsetsatellite.signalindustries.gui.GuiAlloySmelter;
import sunsetsatellite.signalindustries.tiles.TileEntityAlloySmelter;
import sunsetsatellite.signalindustries.util.Tiers;
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
