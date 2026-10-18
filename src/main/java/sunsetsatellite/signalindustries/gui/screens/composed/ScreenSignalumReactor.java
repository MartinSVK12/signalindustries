package sunsetsatellite.signalindustries.gui.screens.composed;

import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.screens.component.ButtonComponent;
import sunsetsatellite.catalyst.screens.component.ProgressBarComponent;
import sunsetsatellite.catalyst.screens.component.TextComponent;
import sunsetsatellite.catalyst.screens.screen.ScreenComposed;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageReactorStart;
import sunsetsatellite.signalindustries.tiles.TileEntityIgnitor;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntitySignalumReactor;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.List;

public class ScreenSignalumReactor extends ScreenComposed {

	public TileEntitySignalumReactor tile;
	TooltipElement tooltip = new TooltipElement(mc);

	public ScreenSignalumReactor(ContainerInventory inventory, TileEntitySignalumReactor tile) {
		super("signalindustries:reactor");
		this.tile = tile;
		TextComponent name = get("machineName");
		name.text = Catalyst.translateNameKey(tile.getName());
		name.color = Tier.REINFORCED.getAltColor();
		ButtonComponent button = get("startStop");
		button.buttonClicked.connect(((signal, clicked) -> {
			tile.start();
			if(EnvironmentHelper.isMultiplayerClient()){
				NetworkHandler.sendToServer(new NetworkMessageReactorStart(tile.getPosition(), TileEntitySignalumReactor.class));
			}
		}));
		ProgressBarComponent s1e = get("stabilizer1energy");
		ProgressBarComponent s1f = get("stabilizer1fuel");
		ProgressBarComponent s2e = get("stabilizer2energy");
		ProgressBarComponent s2f = get("stabilizer2fuel");
		ProgressBarComponent s3e = get("stabilizer3energy");
		ProgressBarComponent s3f = get("stabilizer3fuel");
		ProgressBarComponent s4e = get("stabilizer4energy");
		ProgressBarComponent s4f = get("stabilizer4fuel");
		List<ProgressBarComponent> stabilizersEnergy = List.of(s1e, s2e, s3e, s4e);
		List<ProgressBarComponent> stabilizersFuel = List.of(s1f, s2f, s3f, s4f);
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		renderBackground();
		float capacity = ((float) (tile.getFuel() + tile.getDepletedFuel()) / (4000 * 9)) * 100;
		float fill = 100 - ((float) tile.getDepletedFuel() / (tile.getFuel() + tile.getDepletedFuel())) * 100;
		if (Float.isNaN(fill)) {
			fill = 0;
		}
		int color = switch (tile.state) {
			case INACTIVE -> 0xFF404040;
			case IGNITING -> 0xFFFF8000;
			case RUNNING -> 0xFF00FF00;
		};
		TextComponent fillText = get("fillText");
		fillText.text = String.format("%.0f%%", fill);
		TextComponent fuelText = get("fuelText");
		fuelText.text = String.format("%.0f%%", capacity);
		TextComponent status = get("status");
		status.text = tile.state.toString();
		status.color = color;
		ProgressBarComponent reactorFuel = get("reactorFuel");
		reactorFuel.max = 4000*9;
		reactorFuel.setProgress(tile.getFuel());
		ProgressBarComponent s1e = get("stabilizer1energy");
		ProgressBarComponent s1f = get("stabilizer1fuel");
		ProgressBarComponent s2e = get("stabilizer2energy");
		ProgressBarComponent s2f = get("stabilizer2fuel");
		ProgressBarComponent s3e = get("stabilizer3energy");
		ProgressBarComponent s3f = get("stabilizer3fuel");
		ProgressBarComponent s4e = get("stabilizer4energy");
		ProgressBarComponent s4f = get("stabilizer4fuel");
		List<ProgressBarComponent> stabilizersEnergy = List.of(s1e, s2e, s3e, s4e);
		List<ProgressBarComponent> stabilizersFuel = List.of(s1f, s2f, s3f, s4f);
		for (ProgressBarComponent c : stabilizersEnergy) {
			c.setProgress(0);
		}
		for (ProgressBarComponent c : stabilizersFuel) {
			c.setProgress(0);
		}
		for (int i = 0; i < tile.stabilizers.size(); i++) {
			TileEntityStabilizer stabilizer = tile.stabilizers.get(i);
			stabilizersEnergy.get(i).max = stabilizer.fluidCapacity[0];
			if(stabilizer.fluidContents[0] != null){
				stabilizersEnergy.get(i).setProgress(stabilizer.fluidContents[0].amount);
			}
			if(stabilizer.itemContents[0] != null){
				stabilizersFuel.get(i).max = stabilizer.itemContents[0].getMaxStackSize(stabilizer);
				stabilizersFuel.get(i).setProgress(stabilizer.itemContents[0].stackSize);
			}
		}
		ProgressBarComponent ignitors = get("ignitors");
		ignitors.max = tile.ignitors.size();
		ignitors.setProgress(0);
		int i = 0;
		for (TileEntityIgnitor ignitor : tile.ignitors) {
			if(ignitor.isReady()){
				i++;
			}
		}
		ignitors.setProgress(i);
		ProgressBarComponent ignitorsEnergy = get("ignitorsEnergy");
		ignitorsEnergy.setProgress(0);
		if(tile.connector != null){
			ignitorsEnergy.max = tile.connector.fluidCapacity[0];
			if(tile.connector.fluidContents[0] != null){
				ignitorsEnergy.setProgress(tile.connector.fluidContents[0].amount);
			}
		}
		ButtonComponent button = get("startStop");
		button.text.text = tile.state == TileEntitySignalumReactor.State.INACTIVE ? "START" : "STOP";
		super.render(mx, my, partialTick);
	}
}
