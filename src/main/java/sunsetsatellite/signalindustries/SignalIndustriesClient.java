package sunsetsatellite.signalindustries;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.entry.ItemGuiEntry;
import sunsetsatellite.catalyst.core.util.mp.entry.TileGuiEntry;
import sunsetsatellite.catalyst.screens.util.GuiComponents;
import sunsetsatellite.signalindustries.dim.WorldTypeFXEternity;
import sunsetsatellite.signalindustries.gui.component.BlockRenderComponent;
import sunsetsatellite.signalindustries.gui.menus.*;
import sunsetsatellite.signalindustries.gui.screens.*;
import sunsetsatellite.signalindustries.invs.InventoryAbilityModule;
import sunsetsatellite.signalindustries.invs.InventoryBackpack;
import sunsetsatellite.signalindustries.invs.InventoryHarness;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.MenuPowerSuit;
import sunsetsatellite.signalindustries.powersuit.ScreenPowerSuit;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityInductionSmelter;
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
import java.util.Arrays;
import java.util.HashMap;

import static sunsetsatellite.signalindustries.SignalIndustries.MOD_ID;
import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class SignalIndustriesClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("signalindustries|client");

	public static final HashMap<String, KeyBinding> attachmentKeybinds = new HashMap<>();

	@Override
	public void onInitializeClient() {
		LOGGER.info("SI Client is being initialized...");
		LOGGER.info("Binding to client events...");
		ClientEvents.BEFORE_CLIENT_START.listen(Key.of(MOD_ID),this::beforeClientStart);
		ClientEvents.AFTER_CLIENT_START.listen(Key.of(MOD_ID),this::afterClientStart);
		ClientEvents.BLOCK_MODEL_RELOAD.listen(Key.of(MOD_ID),(t)->new SIModels().initBlockModels(t));
		ClientEvents.ITEM_MODEL_RELOAD.listen(Key.of(MOD_ID),(t)->new SIModels().initItemModels(t));
		ClientEvents.TILE_ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID),(t)->new SIModels().initTileEntityModels(t));
		ClientEvents.ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID),(t)->new SIModels().initEntityModels(t));

		LOGGER.info("Registering GUIs...");
		//GuiComponents.register("blockRender", BlockRenderComponent.class);
		Catalyst.GUIS.register(key("gui/crusher"), new TileGuiEntry<>(TileEntityCrusher.class, MenuMachine.class, ScreenMachine::new));
		Catalyst.GUIS.register(key("gui/extractor"), new TileGuiEntry<>(TileEntityExtractor.class, MenuMachine.class, ScreenFuelMachine::new));
		Catalyst.GUIS.register(key("gui/collector"), new TileGuiEntry<>(TileEntityCollector.class, MenuMachine.class, ScreenCollector::new));
		Catalyst.GUIS.register(key("gui/alloy_smelter"), new TileGuiEntry<>(TileEntityAlloySmelter.class, MenuMachine.class, ScreenDoubleMachine::new));
		Catalyst.GUIS.register(key("gui/plate_former"), new TileGuiEntry<>(TileEntityPlateFormer.class, MenuMachine.class, ScreenMachine::new));
		Catalyst.GUIS.register(key("gui/crystal_cutter"), new TileGuiEntry<>(TileEntityCrystalCutter.class, MenuMachine.class, ScreenCrystalCutter::new));
		Catalyst.GUIS.register(key("gui/crystal_chamber"), new TileGuiEntry<>(TileEntityCrystalChamber.class, MenuMachine.class, ScreenCrystalChamber::new));
		Catalyst.GUIS.register(key("gui/infuser"), new TileGuiEntry<>(TileEntityInfuser.class, MenuMachine.class, ScreenInfuser::new));
		Catalyst.GUIS.register(key("gui/booster"), new TileGuiEntry<>(TileEntityBooster.class, MenuMachine.class, ScreenBooster::new));
		Catalyst.GUIS.register(key("gui/assembler"), new TileGuiEntry<>(TileEntityAssembler.class, MenuAssembler.class, ScreenAssembler::new));
		Catalyst.GUIS.register(key("gui/stabilizer"), new TileGuiEntry<>(TileEntityStabilizer.class, MenuMachine.class, ScreenStabilizer::new));
		Catalyst.GUIS.register(key("gui/item_bus"), new TileGuiEntry<>(TileEntityItemBus.class, MenuMachine.class, ScreenItemBus::new));
		Catalyst.GUIS.register(key("gui/fluid_hatch"), new TileGuiEntry<>(TileEntityFluidHatch.class, MenuMachine.class, ScreenFluidHatch::new));
		Catalyst.GUIS.register(key("gui/energy_connector"), new TileGuiEntry<>(TileEntityEnergyConnector.class, MenuMachine.class, ScreenEnergyConnector::new));
		Catalyst.GUIS.register(key("gui/energy_cell"), new TileGuiEntry<>(TileEntityEnergyCell.class, MenuMachine.class, ScreenEnergyCell::new));
		Catalyst.GUIS.register(key("gui/fluid_tank"), new TileGuiEntry<>(TileEntitySIFluidTank.class, MenuMachine.class, ScreenSIFluidTank::new));
		Catalyst.GUIS.register(key("gui/pump"), new TileGuiEntry<>(TileEntityPump.class, MenuMachine.class, ScreenPump::new));

		Catalyst.GUIS.register(key("gui/anchor"), new TileGuiEntry<>(TileEntityDimensionalAnchor.class, MenuMachine.class, ScreenAnchor::new));
		Catalyst.GUIS.register(key("gui/induction_smelter"), new TileGuiEntry<>(TileEntityInductionSmelter.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_alloy_smelter"), new TileGuiEntry<>(TileEntityWakingAlloySmelter.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_crusher"), new TileGuiEntry<>(TileEntityWakingCrusher.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_plate_former"), new TileGuiEntry<>(TileEntityWakingPlateFormer.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_infuser"), new TileGuiEntry<>(TileEntityWakingInfuser.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/energy_injector"), new TileGuiEntry<>(TileEntityEnergyInjector.class, MenuMachine.class, ScreenEnergyInjector::new));

		Catalyst.GUIS.register(key("gui/harness"), new ItemGuiEntry<>(InventoryHarness.class, MenuHarness.class, ScreenHarness::new));
		Catalyst.GUIS.register(key("gui/power_suit"), new ItemGuiEntry<>(InventoryPowerSuit.class, MenuPowerSuit.class, ScreenPowerSuit::new));
		Catalyst.GUIS.register(key("gui/backpack"), new ItemGuiEntry<>(InventoryBackpack.class, MenuBackpack.class, ScreenBackpack::new));
		Catalyst.GUIS.register(key("gui/ability_module"), new ItemGuiEntry<>(InventoryAbilityModule.class, MenuAbilityModule.class, ScreenAbilityModule::new));
		Catalyst.GUIS.register(key("gui/pulsar"), new ItemGuiEntry<>(InventoryPulsar.class, MenuPulsar.class, ScreenPulsar::new));
	}

	public void beforeClientStart() {
		LOGGER.info("Beginning client pre-init.");
	}

	public void afterClientStart() {
		LOGGER.info("Beginning client post-init.");
		new SIAchievements().initClient();

		LOGGER.info("Registering attachment keybinds...");
		Arrays.stream(SIKeybinds.class.getDeclaredFields()).filter((F) -> F.getName().contains("Attachment")).forEach((F) -> {
			try {
				attachmentKeybinds.put(F.getName(), (KeyBinding) F.get(null));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});

		LOGGER.info("Registering options and their pages...");
		OptionsPage optionsPage = new OptionsPage("gui.options.page.signalindustries", SIItems.signalumCrystal.getDefaultStack());
		optionsPage.withComponent(new BooleanOptionComponent(SIKeybinds.showSuitBackground));
		optionsPage.withComponent(new BooleanOptionComponent(SIKeybinds.renderFluidInsideConduits));
		OptionsPages.register(optionsPage);

		OptionsCategory category = new OptionsCategory("gui.options.page.controls.category.signalindustries");
		Arrays.stream(SIKeybinds.class.getDeclaredFields()).filter((F) -> F.getType() == KeyBinding.class).forEach((F) -> {
			try {
				category.withComponent(new KeyBindingComponent((KeyBinding) F.get(null)));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});
		OptionsPages.CONTROLS
			.withComponent(category);
	}
}
