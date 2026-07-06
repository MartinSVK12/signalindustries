package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityCrystalCutter;

public class ScreenCrystalCutter extends ScreenMachine<TileEntityCrystalCutter> {
	public ScreenCrystalCutter(ContainerInventory playerInv, TileEntityCrystalCutter inv) {
		super(playerInv, inv, "crystal_cutter");
	}
}
