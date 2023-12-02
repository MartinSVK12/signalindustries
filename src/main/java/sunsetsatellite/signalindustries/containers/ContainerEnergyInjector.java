package sunsetsatellite.signalindustries.containers;


import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.inventories.TileEntityTieredMachine;

public class ContainerEnergyInjector extends ContainerTiered {

    public ContainerEnergyInjector(IInventory iInventory, TileEntityTieredMachine tileEntity){
        super(iInventory, tileEntity);

        addFluidSlot(new SlotFluid(tileEntity, 0, 80, 53));
        addSlot(new Slot(tileEntity, 0, 80, 17));

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iInventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iInventory, k, 8 + k * 18, 142));
        }
    }

}
