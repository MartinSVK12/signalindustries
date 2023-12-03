package sunsetsatellite.signalindustries.containers;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.ContainerFluid;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.inventories.TileEntitySignalumDynamo;

public class ContainerSignalumDynamo extends ContainerFluid {

    public ContainerSignalumDynamo(IInventory iInventory, TileEntitySignalumDynamo tileEntity){
        super(iInventory,tileEntity);
        tile = tileEntity;


        addSlot(new Slot(tileEntity,0, 62 + 18,17 + 2 * 18));
        addSlot(new Slot(tileEntity,1, 62 + 18,17));
        addFluidSlot(new SlotFluid(tileEntity, 0, 9,56));


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

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer1) {
        return true;
    }
}
