package sunsetsatellite.signalindustries;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.GuiEntry;
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
		Catalyst.GUIS.register(key("gui/crusher"), new GuiEntry(TileEntityCrusher.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/extractor"), new GuiEntry(TileEntityExtractor.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/alloy_smelter"), new GuiEntry(TileEntityAlloySmelter.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/plate_former"), new GuiEntry(TileEntityPlateFormer.class, MenuMachine.class));
	}
}
