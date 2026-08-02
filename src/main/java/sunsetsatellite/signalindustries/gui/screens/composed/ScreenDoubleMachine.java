package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenDoubleMachine<T extends TileEntityTieredMachineBase> extends ScreenMachine<T> {
	public ScreenDoubleMachine(ContainerInventory playerInv, T inv) {
		super(playerInv, inv, "simple_double_machine");
	}
}
