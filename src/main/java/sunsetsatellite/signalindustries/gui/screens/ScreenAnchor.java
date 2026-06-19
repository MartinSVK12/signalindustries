package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenAnchor extends ScreenMachine {
	public ScreenAnchor(ContainerInventory playerInv, TileEntityTieredMachineBase inv) {
		super(playerInv, inv, "anchor");
	}
}
