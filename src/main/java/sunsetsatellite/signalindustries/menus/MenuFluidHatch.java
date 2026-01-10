package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredContainer;

public class MenuFluidHatch extends MenuMachine {
    public MenuFluidHatch(ContainerInventory inv, TileEntityTieredContainer tile) {
        super(inv, tile);

        this.addFluidSlot(new SlotFluid(tile, 0, 80, 35));

        int k;
        for (k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
        }
    }
}
