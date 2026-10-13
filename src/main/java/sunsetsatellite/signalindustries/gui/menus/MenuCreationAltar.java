package sunsetsatellite.signalindustries.gui.menus;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;

public class MenuCreationAltar extends MenuMachine {
	public MenuCreationAltar(ContainerInventory playerInventory, TileEntityFluidItemContainer inventory) {
		super(playerInventory, inventory);
		initialized = true;

		for (int i = 0; i < 3; ++i) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + i * 9 + 9, k * 18, i * 18));
			}
		}
		for (int j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, j * 18, 58));
		}
	}
}
