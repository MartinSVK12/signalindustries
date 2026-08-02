package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenFuelMachine<T extends TileEntityTieredMachineBase> extends ScreenMachine<T> {
	public ScreenFuelMachine(ContainerInventory playerInv, T inv) {
		super(playerInv, inv, "fuel_machine");
	}
}
