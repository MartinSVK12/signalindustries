package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;


import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.signalindustries.tiles.TileEntityMultiConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;

public class MultiConduitTooltip extends SIBaseTooltip<TileEntityMultiConduit> {
    @Override
    public void initTooltip() {
        this.addClass(TileEntityMultiConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityMultiConduit tile, AdvancedInfoComponent c) {
        drawFluids(tile, c, false, 4);
        if (tile.supports(ConduitCapability.CATALYST_ENERGY)) {
            /*c.drawStringWithShadow(TextFormatting.WHITE + "Max Transfer: " + TextFormatting.LIGHT_GRAY + "IN: " + tile.maxReceive + TextFormatting.WHITE + " / " + TextFormatting.LIGHT_GRAY + "OUT: " + tile.maxProvide,0);
            c.drawProgressBarWithText(tile.energy,tile.capacity,new ProgressBarOptions(0,"Energy: ",true,true),0);*/
        }
    }
}
