package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.inventories.TileEntityItemConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.tooltips.Tooltip;

import java.util.List;
import java.util.stream.Collectors;

public class ItemConduitTooltip extends TileTooltip<TileEntityItemConduit> {
    @Override
    public void initTooltip() {
        addClass(TileEntityItemConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityItemConduit conduit, AdvancedInfoComponent c) {
        ItemStack[] stacks = conduit.getContents().stream().map(TileEntityItemConduit.PipeItem::getStack).toArray(ItemStack[]::new);
        c.drawItemList(stacks,0);
    }
}
