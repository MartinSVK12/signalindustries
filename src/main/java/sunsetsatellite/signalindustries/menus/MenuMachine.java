package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;

import java.util.List;

public abstract class MenuMachine extends MenuFluid {

    public TileEntityFluidItemContainer tile;
    public ContainerInventory playerInv;

    public MenuMachine(ContainerInventory inv, TileEntityFluidItemContainer tile) {
        super(tile);
        this.tile = tile;
        this.playerInv = inv;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        int lastDeviceSlot = tile.getContainerSize() - 1;
        if (slot.index <= lastDeviceSlot) {
            return getSlots(lastDeviceSlot + 1, 36, true);
        }
        return getSlots(0, Math.max(lastDeviceSlot + 1, 1), false);
    }

    @Override
    public boolean stillValid(Player player) {
        return tile.stillValid(player);
    }
}
