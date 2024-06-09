package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.catalyst.core.util.IMultiConduit;
import sunsetsatellite.signalindustries.inventories.TileEntityMultiConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.util.ProgressBarOptions;

public class MultiConduitTooltip extends SIBaseTooltip<TileEntityMultiConduit> {
    @Override
    public void initTooltip() {
        this.addClass(TileEntityMultiConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityMultiConduit tile, AdvancedInfoComponent c) {
        drawFluids(tile,c,false);
        if(tile.supports(ConduitCapability.CATALYST_ENERGY)){
            c.drawStringWithShadow(TextFormatting.WHITE + "Max Transfer: " + TextFormatting.LIGHT_GRAY + "IN: " + tile.maxReceive + TextFormatting.WHITE + " / " + TextFormatting.LIGHT_GRAY + "OUT: " + tile.maxProvide,0);
            c.drawProgressBarWithText(tile.energy,tile.capacity,new ProgressBarOptions(0,"Energy: ",true,true),0);
        }
    }
}
