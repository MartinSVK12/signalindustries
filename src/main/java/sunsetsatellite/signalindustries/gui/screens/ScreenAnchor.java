package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityDimensionalAnchor;

public class ScreenAnchor extends ScreenMachine<TileEntityDimensionalAnchor> {
	public ScreenAnchor(ContainerInventory playerInv, TileEntityDimensionalAnchor inv) {
		super(playerInv, inv, "anchor");
	}
}
