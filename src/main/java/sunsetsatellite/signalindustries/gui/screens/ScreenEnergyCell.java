package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ButtonComponent;
import sunsetsatellite.catalyst.screens.component.SlotGridComponent;
import sunsetsatellite.signalindustries.gui.screens.ScreenTiered;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEnergyCell;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.util.Tier;

public class ScreenEnergyCell extends ScreenTiered<TileEntityEnergyCell> {

	public ScreenEnergyCell(ContainerInventory playerInv, TileEntityEnergyCell inv) {
		super(playerInv, inv, "tank");
		SlotGridComponent slots = get("slotGrid");
		slots.resize(1,1);
		ButtonComponent button = get("infButton");
		if(inv.tier != Tier.INFINITE){
			button.visible = false;
		}
		button.text.text = inv.isInfiniteSource ? "INF" : "VOID";
	}
}
