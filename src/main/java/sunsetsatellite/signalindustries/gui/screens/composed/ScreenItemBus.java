package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.*;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityItemBus;

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
