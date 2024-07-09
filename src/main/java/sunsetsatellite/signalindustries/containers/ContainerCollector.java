package sunsetsatellite.signalindustries.containers;

import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.ContainerFluid;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityCollector;

public class ContainerCollector extends ContainerFluid {

    private final TileEntityCollector machine = ((TileEntityCollector) tile);

    public ContainerCollector(IInventory iInventory, TileEntityTieredContainer tile) {
        super(iInventory, tile);
        this.tile = tile;
        this.addFluidSlot(new SlotFluid(tile, 0, 80, 35));
        this.addSlot(new Slot(tile, 0, 80, 60));

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
