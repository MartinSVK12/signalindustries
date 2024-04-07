package sunsetsatellite.signalindustries.containers;


import net.minecraft.core.crafting.ICrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.ContainerFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityAlloySmelter;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityAssembler;

public class ContainerAssembler extends ContainerFluid {

    private final TileEntityAssembler machine = ((TileEntityAssembler) tile);

    public ContainerAssembler(IInventory iInventory, TileEntityFluidItemContainer tileEntity){
        super(iInventory, tileEntity);
        tile = tileEntity;

        SlotFluid slot = new SlotFluid(tileEntity, 0, 152,35); //116 35
        addFluidSlot(slot);


        this.addSlot(new Slot(tileEntity, 0, 120, 35));

        for(int l = 0; l < 3; l++)
        {
            for(int k1 = 0; k1 < 3; k1++)
            {
                addSlot(new Slot(machine.template, k1 + l * 3, 26 + k1 * 18, 17 + l * 18));
            }

        }

        for(int j = 0; j < 2; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(tileEntity, 1 + (i1 + j * 9), 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iInventory, i1 + j * 9 + 9, 8 + i1 * 18, 133 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iInventory, k, 8 + k * 18, 191));
        }
    }
}
