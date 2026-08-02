package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.util.Tier;

public class ScreenBooster extends ScreenTiered<TileEntityBooster> {

	public ScreenBooster(ContainerInventory playerInv, TileEntityBooster inv) {
		super(playerInv, inv, inv.tier == Tier.BASIC ? "redstone_booster" : "booster");
		ProgressBarComponent progress = get("progressBar");
		ProgressBarComponent energy = get("energyBar");
		progress.progress = 0;
		energy.progress = 0;
	}

	@Override
	public void tick() {
		super.tick();
		ProgressBarComponent energy = (ProgressBarComponent) components.get("energyBar");
		ProgressBarComponent progress = (ProgressBarComponent) components.get("progressBar");
		energy.max = tile.fuelMaxBurnTicks;
		energy.setProgress(tile.fuelBurnTicks);
		progress.max = tile.progressMaxTicks;
		progress.setProgress(tile.progressTicks);
	}
}
