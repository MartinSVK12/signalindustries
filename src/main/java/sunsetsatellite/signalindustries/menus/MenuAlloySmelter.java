package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;

public class MenuAlloySmelter extends MenuMachine {
    public MenuAlloySmelter(ContainerInventory inv, TileEntityFluidItemContainer tile) {
        super(inv, tile);

        SlotFluid slot = new SlotFluid(tile, 0, 56,53); //116 35
        addFluidSlot(slot);

        this.addSlot(new Slot(tile, 0, 56, 17));
        this.addSlot(new Slot(tile, 1, 116, 35)); //56 53
        this.addSlot(new Slot(tile, 2, 28, 17));

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(inv, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(inv, k, 8 + k * 18, 142));
        }
    }
}
