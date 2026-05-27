package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.screens.component.ImageComponent;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.catalyst.screens.component.TextComponent;
import sunsetsatellite.catalyst.screens.screen.ScreenComposedContainer;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

public class ScreenMachine extends ScreenComposedContainer {

	public TileEntityTieredMachineSimple tile;

	public ScreenMachine(ContainerInventory playerInv, TileEntityTieredMachineSimple inv) {
		this(playerInv, inv, "simple_machine");
	}

	public ScreenMachine(ContainerInventory playerInv, TileEntityTieredMachineSimple inv, String scene) {
		super(new MenuMachine(playerInv, inv), Catalyst.compoundOf(new String[]{"scene"},"signalindustries:"+scene));
		this.tile = inv;
		TextComponent name = (TextComponent) components.get("machineName");
		ImageComponent background = (ImageComponent) components.get("background");
		name.text = Catalyst.translateNameKey((inv.getNameTranslationKey()));
		name.color = inv.getTier().getAltColor();
		switch (inv.getTier()) {
			case PROTOTYPE, INFINITE -> {
				background.changeImage("/assets/signalindustries/textures/gui/container/prototype_gui.png");
			}
			case BASIC -> {
				background.changeImage("/assets/signalindustries/textures/gui/container/basic_gui.png");
			}
			case REINFORCED -> {
				background.changeImage("/assets/signalindustries/textures/gui/container/reinforced_gui.png");
			}
			case AWAKENED -> {
				background.changeImage("/assets/signalindustries/textures/gui/container/awakened_gui.png");
			}
		}

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
