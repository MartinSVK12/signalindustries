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
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEnergyInjector;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.util.IO;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenEnergyInjector extends ScreenComposedContainer {

	public TileEntityEnergyInjector tile;

	public ScreenEnergyInjector(ContainerInventory playerInv, TileEntityEnergyInjector inv) {
		super(new MenuMachine(playerInv, inv), scene("energy_injector"));
		this.tile = inv;
		TextComponent name = get("machineName");
		ImageComponent background = get("background");
		ButtonComponent fluidIo = get("fluidIo");
		ButtonComponent itemIo = get("itemIo");
		ProgressBarComponent energy = get("energyBar");
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
		fluidIo.buttonClicked.connect((s, t)->{
			mc.displayScreen(new ScreenIO((MenuComposed) inventorySlots, scene("configure"), IO.FLUID));
		});
		itemIo.buttonClicked.connect((s, t)->{
			mc.displayScreen(new ScreenIO((MenuComposed) inventorySlots, scene("configure"), IO.ITEM));
		});
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
