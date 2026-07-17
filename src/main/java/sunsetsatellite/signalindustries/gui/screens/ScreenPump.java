package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPump;

public class ScreenPump extends ScreenMachine<TileEntityPump> {
	public ScreenPump(ContainerInventory playerInv, TileEntityPump inv) {
		super(playerInv, inv, "pump");
	}
}
