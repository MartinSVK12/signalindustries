package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenCrystalCutter extends ScreenMachine {
	public ScreenCrystalCutter(ContainerInventory playerInv, TileEntityTieredMachineBase inv) {
		super(playerInv, inv, "crystal_cutter");
	}
}
