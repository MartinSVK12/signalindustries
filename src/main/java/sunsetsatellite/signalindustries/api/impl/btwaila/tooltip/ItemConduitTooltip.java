package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class ItemConduitTooltip extends TileTooltip<TileEntityItemConduit> {
    @Override
    public void initTooltip() {
        addClass(TileEntityItemConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityItemConduit conduit, AdvancedInfoComponent c) {
        ItemStack[] stacks = conduit.getContents().stream().map(TileEntityItemConduit.PipeItem::getStack).toArray(ItemStack[]::new);
        c.drawItemList(stacks, 0);
    }
}
