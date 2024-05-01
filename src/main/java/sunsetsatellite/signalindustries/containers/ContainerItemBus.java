package sunsetsatellite.signalindustries.containers;

import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.ContainerFluid;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.util.Tier;

public class ContainerItemBus extends ContainerFluid {

    public ContainerItemBus(IInventory iInventory, TileEntityTieredContainer tile) {
        super(iInventory, tile);
        this.tile = tile;


        if(tile.tier == Tier.BASIC){
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
