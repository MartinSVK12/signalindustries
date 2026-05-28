package sunsetsatellite.signalindustries.gui.screens;

import com.mojang.nbt.tags.CompoundTag;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.FluidItemContainer;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.catalyst.screens.screen.ScreenComposedContainer;
import sunsetsatellite.signalindustries.util.IO;

public class ScreenIO extends ScreenComposedContainer {

	public TileEntityFluidItemContainer tile;

	public ScreenIO(MenuComposed menuComposed, CompoundTag tag, IO io) {
		super(new MenuComposed(menuComposed.playerInventory, (FluidItemContainer) menuComposed.inventory), tag);
		tile = (TileEntityFluidItemContainer) menuComposed.itemInventory;
	}
}
