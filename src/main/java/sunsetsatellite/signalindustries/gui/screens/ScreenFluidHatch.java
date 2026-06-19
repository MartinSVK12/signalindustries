package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.screens.component.ButtonComponent;
import sunsetsatellite.catalyst.screens.component.ImageComponent;
import sunsetsatellite.catalyst.screens.component.SlotGridComponent;
import sunsetsatellite.catalyst.screens.component.TextComponent;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.catalyst.screens.screen.ScreenComposedContainer;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.util.IO;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenFluidHatch extends ScreenComposedContainer {

	public TileEntityFluidHatch tile;

	public ScreenFluidHatch(ContainerInventory playerInv, TileEntityFluidHatch inv) {
		super(new MenuMachine(playerInv, inv), scene("fluid_hatch"));
		this.tile = inv;
		TextComponent name = get("machineName");
		ImageComponent background = get("background");
		ButtonComponent fluidIo = get("fluidIo");
		ButtonComponent itemIo = get("itemIo");
		SlotGridComponent slots = get("slotGrid");
		name.text = Catalyst.translateNameKey((inv.getNameTranslationKey()));
		name.color = inv.getTier().getAltColor();
		slots.resize(1,1);
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
	}

	@Override
	public void tick() {
		super.tick();
	}
}
