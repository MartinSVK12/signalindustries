package sunsetsatellite.signalindustries;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.MpGuiEntry;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityExtractor;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityCrusher;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityPlateFormer;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class SignalIndustriesServer implements DedicatedServerModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("signalindustries|server");

	@Override
	public void onInitializeServer() {
		LOGGER.info("SI Server is being initialized...");
		Catalyst.GUIS.register(key("gui/crusher"), new MpGuiEntry(TileEntityCrusher.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/extractor"), new MpGuiEntry(TileEntityExtractor.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/alloy_smelter"), new MpGuiEntry(TileEntityAlloySmelter.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/plate_former"), new MpGuiEntry(TileEntityPlateFormer.class, MenuMachine.class));
	}
}
