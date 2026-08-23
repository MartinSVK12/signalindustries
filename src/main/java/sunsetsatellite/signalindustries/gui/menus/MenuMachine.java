package sunsetsatellite.signalindustries.gui.menus;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;

public class MenuMachine extends MenuComposed {

	public TileEntityFluidItemContainer tile;

	public MenuMachine(ContainerInventory playerInventory, TileEntityFluidItemContainer inventory) {
		super(playerInventory, inventory);
		this.tile = inventory;
	}

	@Override
	public IntList getTargetSlots(@NotNull InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
		int firstDeviceSlot = entityPlayer.inventory.mainInventory.length;
		if(slot.index < firstDeviceSlot) {
			return getSlots(firstDeviceSlot, tile.getContainerSize(), false);
		}
		return getSlots(0, 36, true);
		/*if (slot.index <= lastDeviceSlot) {
			return getSlots(lastDeviceSlot + 1, 36, true);
		}
		return getSlots(0, Math.max(lastDeviceSlot + 1, 1), false);*/
	}

	@Override
	public boolean stillValid(@NonNull Player player) {
		return tile.stillValid(player);
	}
}
