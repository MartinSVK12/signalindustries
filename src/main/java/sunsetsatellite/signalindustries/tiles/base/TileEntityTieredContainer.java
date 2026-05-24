package sunsetsatellite.signalindustries.tiles.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.ITileEntityInit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Objects;

public abstract class TileEntityTieredContainer extends TileEntityCoverable implements ITiered, ITileEntityInit {
    public Tier tier = Tier.PROTOTYPE;

    @Override
    public void init(Block<?> block) {
        tier = Objects.requireNonNull(Catalyst.blockLogic(getBlock(), ITiered.class)).getTier();
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "";
    }

    public Tier getTier() {
        return tier;
    }
}
