package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityCollector;

public class ScreenCollector extends ScreenTiered<TileEntityCollector> {

	public ScreenCollector(ContainerInventory playerInv, TileEntityCollector inv) {
		super(playerInv,inv,"collector");
		ProgressBarComponent energy = get("energyBar");
		energy.setProgress(0);
		energy.max = 100;
	}

	@Override
	public void tick() {
		super.tick();
		ProgressBarComponent energy = (ProgressBarComponent) components.get("energyBar");
		energy.max = tile.progressMaxTicks;
		energy.setProgress(tile.progressTicks);
	}
}
