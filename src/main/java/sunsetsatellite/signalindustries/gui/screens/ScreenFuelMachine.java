package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenFuelMachine extends ScreenMachine {
	public ScreenFuelMachine(ContainerInventory playerInv, TileEntityTieredMachineBase inv) {
		super(playerInv, inv, "fuel_machine");
	}
}
