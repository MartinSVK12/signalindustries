package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.signalindustries.gui.menus.MenuCreationAltar;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityCreationAltar;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityCentrifuge;

public class ScreenCreationAltar extends ScreenFluid {

	public Player player;
	public TileEntityCreationAltar tile;

	public ScreenCreationAltar(ContainerInventory inv, TileEntityCreationAltar tile) {
		super(new MenuCreationAltar(inv, tile));
		this.tile = tile;
		this.player = inv.player;
		this.xSize = mc.resolution.getScaledWidthScreenCoords();
		this.ySize = mc.resolution.getScaledHeightScreenCoords();
	}

	@Override
	public void opened(int width, int height) {
		super.opened(width, height);
		this.xSize = width;
		this.ySize = height;
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		super.drawGuiContainerForegroundLayer();
		drawStringCenteredShadow(fontRenderer, Catalyst.translateNameKey(tile.getNameTranslationKey()), xSize / 2, 6, tile.tier.getAltColor());
	}
}
