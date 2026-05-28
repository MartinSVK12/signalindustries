package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenDoubleMachine extends ScreenMachine {
	public ScreenDoubleMachine(ContainerInventory playerInv, TileEntityTieredMachineBase inv) {
		super(playerInv, inv, "simple_double_machine");
	}
}
