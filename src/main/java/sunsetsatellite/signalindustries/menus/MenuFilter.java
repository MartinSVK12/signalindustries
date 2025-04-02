package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;

public class MenuFilter extends MenuMachine {
    public MenuFilter(ContainerInventory inv, TileEntityFluidItemContainer tile) {
        super(inv, tile);
        int numberOfRows = 6;
        int i = (numberOfRows - 4) * 18;
        int l;
        int j1;
        for(l = 0; l < numberOfRows; ++l) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(tile, j1 + l * 9, 8 + j1 * 18, 18 + l * 18));
            }
        }

        for(l = 0; l < 3; ++l) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inv, j1 + l * 9 + 9, 8 + j1 * 18, 115 + l * 18 + i));
            }
        }

        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inv, l, 8 + l * 18, 173 + i));
        }
    }
}
