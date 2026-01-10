package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.invs.InventoryBlueprint;
import sunsetsatellite.signalindustries.invs.InventoryItemFluid;

import java.util.ArrayList;
import java.util.List;

public class MenuBlueprint extends MenuFluid {
    public MenuBlueprint(ContainerInventory playerInv, int slotIndex, boolean isArmor) {
        super(new InventoryBlueprint(playerInv.getItem(slotIndex)));
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return new ArrayList<>();
    }
}
