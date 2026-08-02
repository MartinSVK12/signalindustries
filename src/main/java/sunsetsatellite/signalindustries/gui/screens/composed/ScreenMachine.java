package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.catalyst.screens.component.TextComponent;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class ScreenMachine<T extends TileEntityTieredMachineBase> extends ScreenTiered<T> {

	public T tile;

	public ScreenMachine(ContainerInventory playerInv, T inv) {
		this(playerInv, inv, "simple_machine");
	}

	public ScreenMachine(ContainerInventory playerInv, T inv, String scene) {
		super(playerInv, inv, scene);
		this.tile = inv;
		ProgressBarComponent progress = get("progressBar");
		ProgressBarComponent energy = get("energyBar");
		progress.progress = 0;
		energy.progress = 0;
	}

	@Override
	public void tick() {
		super.tick();
		ProgressBarComponent energy = get("energyBar");
		ProgressBarComponent progress = get("progressBar");
		energy.max = tile.fuelMaxBurnTicks;
		energy.setProgress(tile.fuelBurnTicks);
		progress.max = tile.progressMaxTicks;
		progress.setProgress(tile.progressTicks);
		TextComponent speed = get("speedModifier");
		if(speed == null) return;
		if(tile.speedMultiplier > 1) {
			speed.visible = true;
			speed.text = tile.speedMultiplier + "x";
			speed.color = tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080);
		} else {
			speed.visible = false;
		}
	}
}
