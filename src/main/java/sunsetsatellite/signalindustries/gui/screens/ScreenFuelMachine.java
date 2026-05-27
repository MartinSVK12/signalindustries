package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

public class ScreenFuelMachine extends ScreenMachine {
	public ScreenFuelMachine(ContainerInventory playerInv, TileEntityTieredMachineSimple inv) {
		super(playerInv, inv, "fuel_machine");
	}
}
