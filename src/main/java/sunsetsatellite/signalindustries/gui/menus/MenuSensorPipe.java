package sunsetsatellite.signalindustries.gui.menus;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jspecify.annotations.NonNull;

public class MenuSensorPipe extends MenuAbstract {
    public MenuSensorPipe(ContainerInventory inv, TileEntity tile) {

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(inv, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(inv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public IntList getMoveSlots(@NonNull InventoryAction inventoryAction, @NonNull Slot slot, int i, Player player) {
        return IntList.of();
    }

    @Override
    public IntList getTargetSlots(@NonNull InventoryAction inventoryAction, @NonNull Slot slot, int i, Player player) {
        return IntList.of();
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}
