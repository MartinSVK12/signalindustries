package sunsetsatellite.signalindustries.containers;

import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.fluidapi.api.ContainerFluid;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.signalindustries.inventories.TileEntityTieredContainer;

public class ContainerBus extends ContainerFluid {

    public ContainerBus(IInventory iInventory, TileEntityTieredContainer tile) {
        super(iInventory, tile);
        this.tile = tile;
        this.addFluidSlot(new SlotFluid(tile, 0, 80, 35));
        this.addSlot(new Slot(tile, 0, 80, 60));

        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 3; ++l) {
                this.addSlot(new Slot(tile, l + i * 3, 62 + l * 18, 17 + i * 18));
            }
        }

        int k;
        for(k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(iInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(iInventory, k, 8 + k * 18, 142));
        }
    }


}
