package sunsetsatellite.signalindustries.containers;

import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.inventories.TileEntityCentrifuge;
import sunsetsatellite.signalindustries.inventories.TileEntityTieredContainer;

public class ContainerCentrifuge extends ContainerTiered{

    private final TileEntityCentrifuge machine = ((TileEntityCentrifuge) tile);
    public ContainerCentrifuge(IInventory iInventory, TileEntityTieredContainer tileEntityTieredMachine) {
        super(iInventory, tileEntityTieredMachine);
        this.addFluidSlot(new SlotFluid(machine, 0, 105, 30));
        this.addFluidSlot(new SlotFluid(machine, 1, 85, 50));
        this.addFluidSlot(new SlotFluid(machine, 2, 125, 50));
        this.addFluidSlot(new SlotFluid(machine, 3, 105, 70));

        this.addFluidSlot(new SlotFluid(machine, 4, 33,30));

        this.addSlot(new Slot(machine, 0, 33, 70));

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(iInventory, k + i * 9 + 9, 8 + k * 18, 110 + i * 18));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(iInventory, j, 8 + j * 18, 168));
        }
    }

}
