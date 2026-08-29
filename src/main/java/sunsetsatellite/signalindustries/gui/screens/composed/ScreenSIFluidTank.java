package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ButtonComponent;
import sunsetsatellite.catalyst.screens.component.SlotGridComponent;
import sunsetsatellite.signalindustries.tiles.machines.TileEntitySIFluidTank;
import sunsetsatellite.signalindustries.util.Tier;

public class ScreenSIFluidTank extends ScreenTiered<TileEntitySIFluidTank> {

	public ScreenSIFluidTank(ContainerInventory playerInv, TileEntitySIFluidTank inv) {
		super(playerInv, inv, "tank");
		SlotGridComponent slots = get("slotGrid");
		slots.resize(1,1);
		ButtonComponent button = get("infButton");
		if(inv.tier != Tier.INFINITE){
			button.visible = false;
		}
		button.text.text = inv.isInfiniteSource ? "INF" : "VOID";
		button.buttonClicked.connect( (signal, clicked) -> {
			inv.isInfiniteSource = !inv.isInfiniteSource;
			button.text.text = inv.isInfiniteSource ? "INF" : "VOID";
		});
	}
}
