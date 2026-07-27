package sunsetsatellite.signalindustries.gui.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;

public class MenuSignalumReactor extends MenuMachine {
    public MenuSignalumReactor(ContainerInventory inv, TileEntityFluidItemContainer tile) {
        super(inv, tile);
		initialized = true;
    }
}
