package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityCrystalChamber;

public class ScreenCrystalChamber extends ScreenMachine<TileEntityCrystalChamber> {
	public ScreenCrystalChamber(ContainerInventory playerInv, TileEntityCrystalChamber inv) {
		super(playerInv, inv, "crystal_chamber");
	}

	@Override
	public void tick() {
		super.tick();
		ProgressBarComponent secondProgress = (ProgressBarComponent) components.get("progressBar1");
		secondProgress.max = tile.progressMaxTicks;
		secondProgress.setProgress(tile.progressTicks);
	}
}
