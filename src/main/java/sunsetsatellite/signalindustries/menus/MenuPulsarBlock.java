package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;

import java.util.ArrayList;
import java.util.List;

public class MenuPulsarBlock extends MenuMachine {
    public MenuPulsarBlock(ContainerInventory inv, TileEntityFluidItemContainer tile) {
        super(inv, tile);

        SlotFluid slot = new SlotFluid(tile, 0, 15,54); //116 35
        addFluidSlot(slot);

        this.addSlot(new Slot(tile, 0, 15, 10));

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

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        int lastDeviceSlot = tile.getContainerSize() - 1;
        if (slot.index <= lastDeviceSlot) {
            return getSlots(lastDeviceSlot+1, 36, true);
        }
        return new ArrayList<Integer>() {{add(0); add(2);}};
    }
}
