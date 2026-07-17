package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityConduit;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class ConduitTooltip extends SIBaseTooltip<TileEntityConduit> {
    @Override
    public void initTooltip() {
        addClass(TileEntityConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityConduit conduit, AdvancedInfoComponent c) {
        drawFluids(conduit, c, false);
    }
}
