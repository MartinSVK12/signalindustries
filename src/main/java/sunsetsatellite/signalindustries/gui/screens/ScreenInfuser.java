package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityInfuser;

public class ScreenInfuser extends ScreenMachine<TileEntityInfuser> {
	public ScreenInfuser(ContainerInventory playerInv, TileEntityInfuser inv) {
		super(playerInv, inv, "infuser");
	}
}
