package sunsetsatellite.signalindustries;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.entry.TileGuiEntry;
import sunsetsatellite.catalyst.screens.util.GuiComponents;
import sunsetsatellite.signalindustries.dim.WorldTypeFXEternity;
import sunsetsatellite.signalindustries.gui.component.BlockRenderComponent;
import sunsetsatellite.signalindustries.gui.menus.MenuMachine;
import sunsetsatellite.signalindustries.gui.screens.*;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingPlateFormer;
import sunsetsatellite.signalindustries.tiles.machines.simple.*;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityItemBus;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

import java.awt.*;

import static sunsetsatellite.signalindustries.SignalIndustries.MOD_ID;
import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class SignalIndustriesClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("signalindustries|client");

	@Override
	public void onInitializeClient() {
		LOGGER.info("SI Client is being initialized...");
		ClientEvents.BEFORE_CLIENT_START.listen(Key.of(MOD_ID),this::beforeClientStart);
		ClientEvents.AFTER_CLIENT_START.listen(Key.of(MOD_ID),this::afterClientStart);
		ClientEvents.BLOCK_MODEL_RELOAD.listen(Key.of(MOD_ID),(t)->new SIModels().initBlockModels(t));
		ClientEvents.ITEM_MODEL_RELOAD.listen(Key.of(MOD_ID),(t)->new SIModels().initItemModels(t));
		ClientEvents.TILE_ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID),(t)->new SIModels().initTileEntityModels(t));
		ClientEvents.ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID),(t)->new SIModels().initEntityModels(t));

		//GuiComponents.register("blockRender", BlockRenderComponent.class);
		Catalyst.GUIS.register(key("gui/crusher"), new TileGuiEntry<>(TileEntityCrusher.class, MenuMachine.class, ScreenMachine::new));
		Catalyst.GUIS.register(key("gui/extractor"), new TileGuiEntry<>(TileEntityExtractor.class, MenuMachine.class, ScreenFuelMachine::new));
		Catalyst.GUIS.register(key("gui/collector"), new TileGuiEntry<>(TileEntityCollector.class, MenuMachine.class, ScreenCollector::new));
		Catalyst.GUIS.register(key("gui/alloy_smelter"), new TileGuiEntry<>(TileEntityAlloySmelter.class, MenuMachine.class, ScreenDoubleMachine::new));
		Catalyst.GUIS.register(key("gui/plate_former"), new TileGuiEntry<>(TileEntityPlateFormer.class, MenuMachine.class, ScreenMachine::new));
		Catalyst.GUIS.register(key("gui/crystal_cutter"), new TileGuiEntry<>(TileEntityCrystalCutter.class, MenuMachine.class, ScreenCrystalCutter::new));
		Catalyst.GUIS.register(key("gui/crystal_chamber"), new TileGuiEntry<>(TileEntityCrystalChamber.class, MenuMachine.class, ScreenCrystalChamber::new));
		Catalyst.GUIS.register(key("gui/booster"), new TileGuiEntry<>(TileEntityBooster.class, MenuMachine.class, ScreenBooster::new));
		Catalyst.GUIS.register(key("gui/infuser"), new TileGuiEntry<>(TileEntityInfuser.class, MenuMachine.class, ScreenInfuser::new));
		Catalyst.GUIS.register(key("gui/anchor"), new TileGuiEntry<>(TileEntityDimensionalAnchor.class, MenuMachine.class, ScreenAnchor::new));
		Catalyst.GUIS.register(key("gui/stabilizer"), new TileGuiEntry<>(TileEntityStabilizer.class, MenuMachine.class, ScreenStabilizer::new));
		Catalyst.GUIS.register(key("gui/item_bus"), new TileGuiEntry<>(TileEntityItemBus.class, MenuMachine.class, ScreenItemBus::new));
		Catalyst.GUIS.register(key("gui/fluid_hatch"), new TileGuiEntry<>(TileEntityFluidHatch.class, MenuMachine.class, ScreenFluidHatch::new));
		Catalyst.GUIS.register(key("gui/energy_connector"), new TileGuiEntry<>(TileEntityEnergyConnector.class, MenuMachine.class, ScreenEnergyConnector::new));
		Catalyst.GUIS.register(key("gui/waking_alloy_smelter"), new TileGuiEntry<>(TileEntityWakingAlloySmelter.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_crusher"), new TileGuiEntry<>(TileEntityWakingCrusher.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_plate_former"), new TileGuiEntry<>(TileEntityWakingPlateFormer.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_infuser"), new TileGuiEntry<>(TileEntityWakingInfuser.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/energy_injector"), new TileGuiEntry<>(TileEntityEnergyInjector.class, MenuMachine.class, ScreenEnergyInjector::new));
	}

	public void beforeClientStart() {
		LOGGER.info("Beginning client pre-init.");
	}

	public void afterClientStart() {
		LOGGER.info("Beginning client post-init.");
	}
}
