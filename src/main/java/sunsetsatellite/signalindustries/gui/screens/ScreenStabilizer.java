package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.screens.component.ButtonComponent;
import sunsetsatellite.catalyst.screens.component.ImageComponent;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.catalyst.screens.component.TextComponent;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.catalyst.screens.screen.ScreenComposedContainer;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.util.IO;
import sunsetsatellite.signalindustries.util.Tier;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenStabilizer extends ScreenTiered<TileEntityStabilizer> {

	public TileEntityStabilizer tile;

	public ScreenStabilizer(ContainerInventory playerInv, TileEntityStabilizer inv) {
		super(playerInv, inv, "stabilizer");
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
		TextComponent con = get("connected");
		if (tile.connectedTo != null) {
			con.text = "Connected!";
			con.color = 0x00FF00;
		} else {
			con.text = "Nothing to stabilize.";
			con.color = 0xFF0000;
		}
	}
}
