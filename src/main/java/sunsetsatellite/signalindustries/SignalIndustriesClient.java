package sunsetsatellite.signalindustries;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.MpGuiEntryClient;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.gui.screens.ScreenDoubleMachine;
import sunsetsatellite.signalindustries.gui.screens.ScreenFuelMachine;
import sunsetsatellite.signalindustries.gui.screens.ScreenMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityExtractor;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityCrusher;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityPlateFormer;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.ClientStartEntrypoint;

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

		Catalyst.GUIS.register(key("gui/crusher"), new MpGuiEntryClient(TileEntityCrusher.class, ScreenMachine.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/extractor"), new MpGuiEntryClient(TileEntityExtractor.class, ScreenFuelMachine.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/alloy_smelter"), new MpGuiEntryClient(TileEntityAlloySmelter.class, ScreenDoubleMachine.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/plate_former"), new MpGuiEntryClient(TileEntityPlateFormer.class, ScreenMachine.class, MenuMachine.class));
	}

	public void beforeClientStart() {
		LOGGER.info("Beginning client pre-init.");
	}

	public void afterClientStart() {
		LOGGER.info("Beginning client post-init.");
	}
}
