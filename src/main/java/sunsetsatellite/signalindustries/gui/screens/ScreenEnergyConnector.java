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
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.util.IO;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenEnergyConnector extends ScreenTiered<TileEntityEnergyConnector> {

	public ScreenEnergyConnector(ContainerInventory playerInv, TileEntityEnergyConnector inv) {
		super(playerInv, inv, "energy_connector");
		ProgressBarComponent energy = get("energyBar");
		energy.setProgress(0);
		energy.max = 100;
	}

	@Override
	public void tick() {
		super.tick();
		ProgressBarComponent energy = (ProgressBarComponent) components.get("energyBar");
		if(tile.connectedTo instanceof TileEntityTieredMultiblock multiblock){
			energy.max = multiblock.fuelMaxBurnTicks;
			energy.setProgress(multiblock.fuelBurnTicks);
		} else if (tile.connectedTo instanceof IActiveForm activeForm) {
			energy.max = 100;
			energy.setProgress(activeForm.isBurning() ? 100 : 0);
		} else {
			energy.setProgress(0);
			energy.max = 0;
		}
	}
}
