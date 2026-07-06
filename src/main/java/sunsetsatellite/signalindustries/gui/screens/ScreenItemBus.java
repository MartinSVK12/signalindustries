package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.screens.component.*;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.catalyst.screens.screen.ScreenComposedContainer;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityItemBus;
import sunsetsatellite.signalindustries.util.IO;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenItemBus extends ScreenTiered<TileEntityItemBus> {


	public ScreenItemBus(ContainerInventory playerInv, TileEntityItemBus inv) {
		super(playerInv, inv,"item_bus");
		SlotGridComponent slots = get("slotGrid");
		switch (inv.getTier()) {
			case PROTOTYPE, INFINITE -> {
				slots.resize(1,1);
			}
			case BASIC -> {
				slots.resize(2,2);
			}
			case REINFORCED -> {
				slots.resize(3,3);
			}
			case AWAKENED -> {
				slots.resize(3,6);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
	}
}
