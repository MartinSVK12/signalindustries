package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;

public class MenuSITrommel extends MenuMachine {
    public MenuSITrommel(ContainerInventory inv, TileEntityFluidItemContainer tile) {
        super(inv, tile);

        this.addSlot(new Slot(tile, 0, 105, 30));
        this.addSlot(new Slot(tile, 1, 85, 50));
        this.addSlot(new Slot(tile, 2, 125, 50));
        this.addSlot(new Slot(tile, 3, 105, 70));

        this.addFluidSlot(new SlotFluid(tile, 0, 33, 30));

        this.addSlot(new Slot(tile, 4, 33, 70));

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inv, k + i * 9 + 9, 8 + k * 18, 110 + i * 18));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(inv, j, 8 + j * 18, 168));
        }
    }
}
