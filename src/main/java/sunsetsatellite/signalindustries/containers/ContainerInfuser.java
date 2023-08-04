package sunsetsatellite.signalindustries.containers;




import sunsetsatellite.fluidapi.api.ContainerFluid;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;

public class ContainerInfuser extends ContainerFluid {

    public ContainerInfuser(IInventory iInventory, TileEntityFluidItemContainer tileEntity){
        super(iInventory, tileEntity);
        tile = tileEntity;

        SlotFluid slot = new SlotFluid(tileEntity, 0, 9,56); //116 35
        addFluidSlot(slot);

        this.addSlot(new Slot(tileEntity, 0, 80, 18));
        this.addSlot(new Slot(tileEntity, 1, 80, 56));
        this.addSlot(new Slot(tileEntity,2,111,38));
        this.addFluidSlot(new SlotFluid(tileEntity, 1, 60, 37));

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
