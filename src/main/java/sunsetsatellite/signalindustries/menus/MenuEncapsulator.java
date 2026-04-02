package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;

public class MenuEncapsulator extends MenuFluid {
    public MenuEncapsulator(ContainerInventory inv, TileEntityFluidItemContainer tile) {
        super(tile);

        SlotFluid slot = new SlotFluid(tile, 0, 8, 35);
        addFluidSlot(slot);

        addSlot(new Slot(tile, 0, 148, 10));
        addSlot(new Slot(tile, 1, 148, 60));
        addSlot(new Slot(tile, 2, 125, 60));

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(inv, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(inv, k, 8 + k * 18, 142));
        }
    }
}
