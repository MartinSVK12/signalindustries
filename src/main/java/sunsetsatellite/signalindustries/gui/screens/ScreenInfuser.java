package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenInfuser extends ScreenMachine {
	public ScreenInfuser(ContainerInventory playerInv, TileEntityTieredMachineBase inv) {
		super(playerInv, inv, "infuser");
	}
}
