package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.util.Tier;

public class MenuItemBus extends MenuMachine {
    public MenuItemBus(ContainerInventory inv, TileEntityTieredContainer tile) {
        super(inv, tile);

        if (tile.tier == Tier.BASIC) {
            for (int i = 0; i < 2; ++i) {
                for (int l = 0; l < 2; ++l) {
                    this.addSlot(new Slot(tile, l + i * 2, 71 + l * 18, 26 + i * 18));
                }
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                for (int l = 0; l < 3; ++l) {
                    this.addSlot(new Slot(tile, l + i * 3, 62 + l * 18, 17 + i * 18));
                }
            }
        }


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
