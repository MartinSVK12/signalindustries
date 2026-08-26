package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.client.gui.TooltipElement;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.input.Keyboard;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.screens.component.ButtonComponent;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageRecipeIdChange;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityCrystalCutter;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenCrystalCutter extends ScreenMachine<TileEntityCrystalCutter> {

	public TooltipElement tooltip = new TooltipElement(mc);

	public ScreenCrystalCutter(ContainerInventory playerInv, TileEntityCrystalCutter inv) {
		super(playerInv, inv, "crystal_cutter");
		ButtonComponent button = get("recipeId");
		button.text.text = String.valueOf(tile.recipeId);
		button.buttonClicked.connect((signal, clicked) -> {
			if(tile.recipeId > 0 && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))){
				if(EnvironmentHelper.isMultiplayerClient()){
					NetworkHandler.sendToServer(new NetworkMessageRecipeIdChange(tile.recipeId - 1, new Vec3i(tile.tilePos), tile.getClass()));
				}
				tile.recipeId--;
			} else {
				if(EnvironmentHelper.isMultiplayerClient()){
					NetworkHandler.sendToServer(new NetworkMessageRecipeIdChange(tile.recipeId + 1, new Vec3i(tile.tilePos), tile.getClass()));
				}
				tile.recipeId++;
			}
			button.text.text = String.valueOf(tile.recipeId);
		});
		button.onHover.connect((signal, hovered) -> {
			int centerX = (this.width - this.xSize) / 2;
			int centerY = (this.height - this.ySize) / 2;
			tooltip.render("Click to increment recipe ID.\nShift+click to decrement.",
				hovered.mx()-centerX, hovered.my()-centerY, 8, -8);
		});
	}
}
