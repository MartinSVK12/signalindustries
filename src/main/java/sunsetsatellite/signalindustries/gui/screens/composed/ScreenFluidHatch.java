package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.SlotGridComponent;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityFluidHatch;

public class ScreenFluidHatch extends ScreenTiered<TileEntityFluidHatch> {

	public ScreenFluidHatch(ContainerInventory playerInv, TileEntityFluidHatch inv) {
		super(playerInv, inv, "fluid_hatch");
		SlotGridComponent slots = get("slotGrid");
		slots.resize(1,1);
	}
}
