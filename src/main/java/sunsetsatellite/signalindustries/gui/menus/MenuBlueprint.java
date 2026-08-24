package sunsetsatellite.signalindustries.gui.menus;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.signalindustries.invs.InventoryBlueprint;

public class MenuBlueprint extends MenuFluid {
    public MenuBlueprint(ContainerInventory playerInv, int slotIndex, boolean isArmor) {
        super(new InventoryBlueprint(playerInv.getItem(slotIndex)));
    }

    @Override
    public IntList getTargetSlots(@NonNull InventoryAction inventoryAction, @NonNull Slot slot, int i, Player entityPlayer) {
        return IntList.of();
    }
}
