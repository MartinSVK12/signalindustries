package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenCrystalChamber extends ScreenMachine {
	public ScreenCrystalChamber(ContainerInventory playerInv, TileEntityTieredMachineBase inv) {
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
