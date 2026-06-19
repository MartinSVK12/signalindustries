package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.screens.component.ImageComponent;
import sunsetsatellite.catalyst.screens.screen.ScreenComposedContainer;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenMultiblock extends ScreenComposedContainer {

	public String name;
	public TileEntityTieredMultiblock tile;

	public ScreenMultiblock(ContainerInventory playerInv, TileEntityTieredMultiblock inv) {
		super(new MenuMachine(playerInv, inv), scene("multiblock"));
		this.tile = inv;
		this.name = tile.multiblock.data.getTranslatedName();
		ImageComponent background = get("background");
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
	protected void drawGuiContainerForegroundLayer() {
		super.drawGuiContainerForegroundLayer();
		drawStringShadow(fontRenderer, name, 10, 10, tile.tier.getAltColor());
		if (tile.isBurning()) {
			drawStringShadow(fontRenderer, "Current Parallel: " + TextFormatting.ORANGE + tile.parallel, 10, 20, 0xFFFFFFFF);
		} else {
			drawStringShadow(fontRenderer, "Max Parallel: " + TextFormatting.ORANGE + tile.parallel, 10, 20, 0xFFFFFFFF);
		}
		drawStringShadow(fontRenderer, "Speed Multiplier: " + TextFormatting.MAGENTA + tile.speedMultiplier + "x", 10, 30, 0xFFFFFFFF);
		if (tile.isDisabled()) {
			drawStringShadow(fontRenderer, "Disabled", 10, 50, 0xFFFF0000);
		} else if (tile.isBurning()) {
			drawStringShadow(fontRenderer, String.format("Processing: %d%%", tile.getProgressScaled(100)), 10, 50, 0xFF00FF00);
		} else {
			drawStringShadow(fontRenderer, TextFormatting.LIGHT_GRAY + "Idling..", 10, 50, 0xFFFFFFFF);
		}
	}
}
