package sunsetsatellite.signalindustries;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.entry.ItemGuiEntry;
import sunsetsatellite.catalyst.core.util.mp.entry.TileDataGuiEntry;
import sunsetsatellite.catalyst.core.util.mp.entry.TileGuiEntry;
import sunsetsatellite.signalindustries.api.impl.vintagequesting.VintageQuestingSIPlugin;
import sunsetsatellite.signalindustries.dim.WorldTypeFXEternity;
import sunsetsatellite.signalindustries.gui.menus.*;
import sunsetsatellite.signalindustries.gui.screens.*;
import sunsetsatellite.signalindustries.gui.screens.composed.*;
import sunsetsatellite.signalindustries.gui.screens.cover.ScreenRedstoneCoverConfig;
import sunsetsatellite.signalindustries.gui.screens.cover.ScreenSwitchCoverConfig;
import sunsetsatellite.signalindustries.gui.screens.cover.ScreenVoidCoverConfig;
import sunsetsatellite.signalindustries.invs.InventoryAbilityModule;
import sunsetsatellite.signalindustries.invs.InventoryBackpack;
import sunsetsatellite.signalindustries.invs.InventoryHarness;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.MenuPowerSuit;
import sunsetsatellite.signalindustries.powersuit.ScreenPowerSuit;
import sunsetsatellite.signalindustries.tiles.TileEntityExternalIO;
import sunsetsatellite.signalindustries.tiles.TileEntityFilter;
import sunsetsatellite.signalindustries.tiles.TileEntityRedstoneClock;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityMultiConduit;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.*;
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

import java.lang.reflect.Field;
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
		WorldTypeFXDispatcher.getInstance().addDispatch(SIWorldTypes.ETERNITY_WORLD, new WorldTypeFXEternity(SIWorldTypes.ETERNITY_WORLD));

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
		Catalyst.GUIS.register(key("gui/centrifuge"), new TileGuiEntry<>(TileEntityCentrifuge.class, MenuCentrifuge.class, ScreenCentrifuge::new));
		Catalyst.GUIS.register(key("gui/trommel"), new TileGuiEntry<>(TileEntitySITrommel.class, MenuSITrommel.class, ScreenSITrommel::new));
		Catalyst.GUIS.register(key("gui/stoneworks"), new TileGuiEntry<>(TileEntityStoneworks.class, MenuMachine.class, ScreenStoneworks::new));
		Catalyst.GUIS.register(key("gui/heat_pump"), new TileGuiEntry<>(TileEntityHeatPump.class, MenuMachine.class, ScreenHeatPump::new));
		Catalyst.GUIS.register(key("gui/energy_injector"), new TileGuiEntry<>(TileEntityEnergyInjector.class, MenuMachine.class, ScreenEnergyInjector::new));
		Catalyst.GUIS.register(key("gui/external_io"), new TileGuiEntry<>(TileEntityExternalIO.class, MenuExternalIO.class, ScreenExternalIO::new));
		Catalyst.GUIS.register(key("gui/bonsai_pot"), new TileGuiEntry<>(TileEntityBonsaiPot.class, MenuBonsaiPot.class, ScreenBonsaiPot::new));
		Catalyst.GUIS.register(key("gui/redstone_clock"), new TileGuiEntry<>(TileEntityRedstoneClock.class, null, ScreenRedstoneClock::new));
		Catalyst.GUIS.register(key("gui/filter"), new TileGuiEntry<>(TileEntityFilter.class, MenuFilter.class, ScreenFilter::new));
		Catalyst.GUIS.register(key("gui/dynamo"), new TileGuiEntry<>(TileEntitySignalumDynamo.class, MenuSignalumDynamo.class, ScreenSignalumDynamo::new));
		Catalyst.GUIS.register(key("gui/builder"), new TileGuiEntry<>(TileEntityBuilder.class, MenuBuilder.class, ScreenBuilder::new));
		Catalyst.GUIS.register(key("gui/auto_miner"), new TileGuiEntry<>(TileEntityAutoMiner.class, MenuAutoMiner.class, ScreenAutoMiner::new));
		Catalyst.GUIS.register(key("gui/programmer"), new TileGuiEntry<>(TileEntityProgrammer.class, MenuProgrammer.class, ScreenProgrammer::new));
		Catalyst.GUIS.register(key("gui/pulsar_block"), new TileGuiEntry<>(TileEntityPulsar.class, MenuPulsarBlock.class, ScreenPulsarBlock::new));
		Catalyst.GUIS.register(key("gui/multi_conduit"), new TileGuiEntry<>(TileEntityMultiConduit.class, null, ScreenMultiConduitConfig::new));

		Catalyst.GUIS.register(key("gui/restrict_item_conduit"), new TileGuiEntry<>(TileEntityItemConduit.class, null, ScreenRestrictPipeConfig::new));
		Catalyst.GUIS.register(key("gui/sensor_item_conduit"), new TileGuiEntry<>(TileEntityItemConduit.class, MenuSensorPipe.class, ScreenSensorPipeConfig::new));

		Catalyst.GUIS.register(key("gui/switch_cover"), new TileDataGuiEntry<>(TileEntityCoverable.class, MenuCover.class, ScreenSwitchCoverConfig::new));
		Catalyst.GUIS.register(key("gui/void_cover"), new TileDataGuiEntry<>(TileEntityCoverable.class, MenuCover.class, ScreenVoidCoverConfig::new));
		Catalyst.GUIS.register(key("gui/redstone_cover"), new TileDataGuiEntry<>(TileEntityCoverable.class, MenuCover.class, ScreenRedstoneCoverConfig::new));

		Catalyst.GUIS.register(key("gui/induction_smelter"), new TileGuiEntry<>(TileEntityInductionSmelter.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/r_extractor"), new TileGuiEntry<>(TileEntityReinforcedExtractor.class, MenuReinforcedExtractor.class, ScreenReinforcedExtractor::new));
		Catalyst.GUIS.register(key("gui/anchor"), new TileGuiEntry<>(TileEntityDimensionalAnchor.class, MenuMachine.class, ScreenAnchor::new));
		Catalyst.GUIS.register(key("gui/reactor"), new TileGuiEntry<>(TileEntitySignalumReactor.class, MenuSignalumReactor.class, ScreenSignalumReactor::new));
		Catalyst.GUIS.register(key("gui/waking_alloy_smelter"), new TileGuiEntry<>(TileEntityWakingAlloySmelter.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_crusher"), new TileGuiEntry<>(TileEntityWakingCrusher.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_plate_former"), new TileGuiEntry<>(TileEntityWakingPlateFormer.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/waking_infuser"), new TileGuiEntry<>(TileEntityWakingInfuser.class, MenuMachine.class, ScreenMultiblock::new));
		Catalyst.GUIS.register(key("gui/warp_gate"), new TileGuiEntry<>(TileEntityWarpGate.class, MenuWarpGate.class, ScreenWarpGate::new));

		Catalyst.GUIS.register(key("gui/harness"), new ItemGuiEntry<>(InventoryHarness.class, MenuHarness.class, ScreenHarness::new));
		Catalyst.GUIS.register(key("gui/power_suit"), new ItemGuiEntry<>(InventoryPowerSuit.class, MenuPowerSuit.class, ScreenPowerSuit::new));
		Catalyst.GUIS.register(key("gui/backpack"), new ItemGuiEntry<>(InventoryBackpack.class, MenuBackpack.class, ScreenBackpack::new));
		Catalyst.GUIS.register(key("gui/ability_module"), new ItemGuiEntry<>(InventoryAbilityModule.class, MenuAbilityModule.class, ScreenAbilityModule::new));
		Catalyst.GUIS.register(key("gui/pulsar"), new ItemGuiEntry<>(InventoryPulsar.class, MenuPulsar.class, ScreenPulsar::new));
		Catalyst.GUIS.register(key("gui/pulsar_attch"), new ItemGuiEntry<>(InventoryPulsar.class, MenuPulsarAttachment.class, ScreenPulsarAttachment::new));

		LOGGER.info("Registering options...");
		for (Field field : SIKeybinds.class.getDeclaredFields()) {
			try {
				Object o = field.get(null);
				if(o instanceof KeyBinding key){
					GameSettings.register(key);
				} else {
					GameSettings.register((Option<?>) o);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void movePlayerToDimension(Player player, int dimension) {
		Minecraft mc = Minecraft.getMinecraft();
		Dimension lastDim = Dimension.getDimensionList().get(player.dimension);
		Dimension newDim = Dimension.getDimensionList().get(dimension);
		System.out.println("Switching to dimension \"" + newDim.getTranslatedName() + "\"!!");
		player.dimension = dimension;
		mc.currentWorld.setEntityDead(player);
		mc.thePlayer.removed = false;
		double x = player.x;
		double y = player.y + 64;
		double z = player.z;
		player.moveTo(x *= Dimension.getCoordScale(lastDim, newDim), y, z *= Dimension.getCoordScale(lastDim, newDim), player.yRot, player.xRot);
		if (player.isAlive()) {
			mc.currentWorld.updateEntityWithOptionalForce(player, false);
		}
		WorldClient world = new WorldClient(mc.currentWorld, newDim);
		if (newDim == lastDim.homeDim) {
			mc.changeWorld(world, "Leaving " + lastDim.getTranslatedName(), player);
		} else {
			mc.changeWorld(world, "Entering " + newDim.getTranslatedName(), player);
		}
		player.world = mc.currentWorld;
		if (player.isAlive()) {
			player.moveTo(x, y, z, player.yRot, player.xRot);
			mc.currentWorld.updateEntityWithOptionalForce(player, false);
		}
	}

	public void beforeClientStart() {
		LOGGER.info("Beginning client pre-init.");
	}

	public void afterClientStart() {
		LOGGER.info("Beginning client post-init.");
		new SIAchievements().initClient();
		if (FabricLoaderImpl.INSTANCE.isModLoaded("vintagequesting")) {
			if (SIConfig.config.getBoolean("Other.enableQuests")) {
				new VintageQuestingSIPlugin().reloadClient();
			}
		}

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
