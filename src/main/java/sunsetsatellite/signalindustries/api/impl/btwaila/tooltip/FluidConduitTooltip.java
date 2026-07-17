package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import sunsetsatellite.signalindustries.tiles.conduit.TileEntityConduit;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityFluidConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;

public class FluidConduitTooltip extends SIBaseTooltip<TileEntityFluidConduit> {
    @Override
    public void initTooltip() {
        addClass(TileEntityFluidConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityFluidConduit conduit, AdvancedInfoComponent c) {
        drawFluids(conduit, c, false);
    }
}
