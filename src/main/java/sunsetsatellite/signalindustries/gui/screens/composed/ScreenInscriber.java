package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityInfuser;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityPropertyInscriber;

public class ScreenInscriber extends ScreenMachine<TileEntityPropertyInscriber> {
	public ScreenInscriber(ContainerInventory playerInv, TileEntityPropertyInscriber inv) {
		super(playerInv, inv, "inscriber");
	}
}
