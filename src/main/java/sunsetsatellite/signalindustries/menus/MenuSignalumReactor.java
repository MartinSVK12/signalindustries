package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;

public class MenuSignalumReactor extends MenuFluid {
    public MenuSignalumReactor(ContainerInventory inv, TileEntityFluidItemContainer tile) {
        super(tile);
    }
}
