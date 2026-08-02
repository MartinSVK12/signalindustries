package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.catalyst.screens.component.TextComponent;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityHeatPump;

public class ScreenHeatPump extends ScreenTiered<TileEntityHeatPump> {

	public ScreenHeatPump(ContainerInventory playerInv, TileEntityHeatPump inv) {
		super(playerInv, inv, "heat_pump");
		ProgressBarComponent energy = get("energyBar");
		energy.progress = 0;
	}

	@Override
	public void tick() {
		super.tick();
		ProgressBarComponent energy = (ProgressBarComponent) components.get("energyBar");
		energy.max = tile.fuelMaxBurnTicks;
		energy.setProgress(tile.fuelBurnTicks);
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
