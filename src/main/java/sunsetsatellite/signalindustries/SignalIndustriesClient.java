package sunsetsatellite.signalindustries;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.entry.TileGuiEntry;
import sunsetsatellite.catalyst.screens.util.GuiComponents;
import sunsetsatellite.signalindustries.gui.component.BlockRenderComponent;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.gui.screens.*;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityExtractor;
import sunsetsatellite.signalindustries.tiles.machines.simple.*;
import turniplabs.halplibe.event.defs.ClientEvents;

import java.awt.*;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class SignalIndustriesClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("signalindustries|client");

	@Override
	public void onInitializeClient() {
		LOGGER.info("SI Client is being initialized...");
		ClientEvents.BEFORE_CLIENT_START.listen(this::beforeClientStart);
		ClientEvents.AFTER_CLIENT_START.listen(this::afterClientStart);
		ClientEvents.BLOCK_MODEL_RELOAD.listen((t)->new SIModels().initBlockModels(t));
		ClientEvents.ITEM_MODEL_RELOAD.listen((t)->new SIModels().initItemModels(t));

		//GuiComponents.register("blockRender", BlockRenderComponent.class);
		Catalyst.GUIS.register(key("gui/crusher"), new TileGuiEntry<>(TileEntityCrusher.class, MenuMachine.class, ScreenMachine::new));
		Catalyst.GUIS.register(key("gui/extractor"), new TileGuiEntry<>(TileEntityExtractor.class, MenuMachine.class, ScreenFuelMachine::new));
		Catalyst.GUIS.register(key("gui/alloy_smelter"), new TileGuiEntry<>(TileEntityAlloySmelter.class, MenuMachine.class, ScreenDoubleMachine::new));
		Catalyst.GUIS.register(key("gui/plate_former"), new TileGuiEntry<>(TileEntityPlateFormer.class, MenuMachine.class, ScreenMachine::new));
		Catalyst.GUIS.register(key("gui/crystal_cutter"), new TileGuiEntry<>(TileEntityCrystalCutter.class, MenuMachine.class, ScreenCrystalCutter::new));
		Catalyst.GUIS.register(key("gui/crystal_chamber"), new TileGuiEntry<>(TileEntityCrystalChamber.class, MenuMachine.class, ScreenCrystalChamber::new));
	}

	public void beforeClientStart() {
		LOGGER.info("Beginning client pre-init.");
	}

	public void afterClientStart() {
		LOGGER.info("Beginning client post-init.");
	}
}
