package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenCrystalChamber extends ScreenMachine {
	public ScreenCrystalChamber(ContainerInventory playerInv, TileEntityTieredMachineBase inv) {
		super(playerInv, inv, "crystal_chamber");
	}
}
