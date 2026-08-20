package sunsetsatellite.signalindustries;

import com.mojang.nbt.tags.CompoundTag;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.net.entity.NetEntityHandler;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.CustomStructure;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.signalindustries.api.impl.vintagequesting.VintageQuestingSIPlugin;
import sunsetsatellite.signalindustries.entities.*;
import sunsetsatellite.signalindustries.items.ItemBlueprint;
import sunsetsatellite.signalindustries.mp.entity.*;
import sunsetsatellite.signalindustries.mp.message.*;
import sunsetsatellite.signalindustries.tiles.*;
import sunsetsatellite.signalindustries.tiles.conduit.*;
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
import sunsetsatellite.signalindustries.util.MeteorLocation;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.helper.ArmorHelper;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.dependency.Key;

import java.lang.reflect.Field;
import java.util.*;

import static sunsetsatellite.signalindustries.SIConfig.config;

public class SignalIndustries implements ModInitializer {
	public static final String MOD_ID = HalpLibe.registerMod("signalindustries", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean DEBUG = false;

	public static List<MeteorLocation> meteorLocations = new ArrayList<>();
	public static Set<BlockInstance> uvLamps = new HashSet<>();
	public static HashMap<String, CustomStructure> customStructures = new HashMap<>();
	public static final Map<Block<?>, Integer> ORE_BLOCK_COUNT = new HashMap<>();
	public static boolean worldSavedIDs = false;

	public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID, "harness", 1200, 10, 10, 10, 10);
	public static final ArmorMaterial armorSignalumPowerSuit = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID, "power_suit", 9999, 50, 50, 50, 50);

	public static final ToolMaterial toolMaterialBasic = new ToolMaterial().setDurability(9999).setMiningLevel(3).setEfficiency(25, 50);
	public static final ToolMaterial toolMaterialReinforced = new ToolMaterial().setDurability(9999).setMiningLevel(config.getInt("Other.dilithiumMiningLevel")).setEfficiency(45, 80);
	public static final ToolMaterial toolMaterialAwakened = new ToolMaterial().setDurability(9999).setMiningLevel(config.getInt("Other.awakenedMiningLevel")).setEfficiency(60, 100);

	public static final Tag<Block<?>> SIGNALUM_CONDUITS_CONNECT = Tag.of("signalum_conduits_connect");
	public static final Tag<Block<?>> FLUID_CONDUITS_CONNECT = Tag.of("fluid_conduits_connect");
	public static final Tag<Block<?>> ITEM_CONDUITS_CONNECT = Tag.of("item_conduits_connect");
	public static final Tag<Block<?>> CASING = Tag.of("casing");
	public static final Tag<Block<?>> REPLACEABLE_CASING = Tag.of("replaceable_casing");
	public static final Tag<Block<?>> PROTOTYPE_CASING = Tag.of("prototype_casing");
	public static final Tag<Block<?>> BASIC_CASING = Tag.of("basic_casing");
	public static final Tag<Block<?>> REINFORCED_CASING = Tag.of("reinforced_casing");
	public static final Tag<Block<?>> AWAKENED_CASING = Tag.of("awakened_casing");
	public static final Tag<Block<?>> ORE_BLOCK = Tag.of("ore_block");

	public static boolean bloodMoonsDisabled = false;

	@SuppressWarnings("InstantiationOfUtilityClass")
	@Override
	public void onInitialize() {
		LOGGER.info("Signal Industries is loading... Shine!");
		LOGGER.info("Loading SI config...");
		new SIConfig();
		new SIArt().init();

		LOGGER.info("Binding to events...");
		CommonEvents.BEFORE_GAME_START.listen(Key.of(MOD_ID), this::beforeGameStart);
		CommonEvents.AFTER_GAME_START.listen(Key.of(MOD_ID),this::afterGameStart);
		CommonEvents.AFTER_BLOCK_INIT.listen(Key.of(MOD_ID),()->new SIBlocks().afterBlockInit());
		CommonEvents.AFTER_ITEM_INIT.listen(Key.of(MOD_ID),()->new SIItems().afterItemInit());
		CommonEvents.RECIPES_NAMESPACE_INIT.listen(Key.of(MOD_ID),()->new SIRecipes().initNamespaces());
		CommonEvents.RECIPES_READY.listen(Key.of(MOD_ID),()->new SIRecipes().onRecipesReady());

		LOGGER.info("Registering tile entities...");
		TileEntityDispatcher.addMapping(TileEntityExtractor.class, id("extractor"));
		TileEntityDispatcher.addMapping(TileEntityCollector.class, id("collector"));
		TileEntityDispatcher.addMapping(TileEntitySIFluidTank.class, id("fluid_tank"));
		TileEntityDispatcher.addMapping(TileEntityEnergyCell.class, id("energy_cell"));
		TileEntityDispatcher.addMapping(TileEntityCrusher.class, id("crusher"));
		TileEntityDispatcher.addMapping(TileEntityAlloySmelter.class, id("alloy_smelter"));
		TileEntityDispatcher.addMapping(TileEntityPlateFormer.class, id("plate_former"));
		TileEntityDispatcher.addMapping(TileEntityCrystalChamber.class, id("crystal_chamber"));
		TileEntityDispatcher.addMapping(TileEntityCrystalCutter.class, id("crystal_cutter"));
		TileEntityDispatcher.addMapping(TileEntityInfuser.class, id("infuser"));
		TileEntityDispatcher.addMapping(TileEntityStoneworks.class, id("stoneworks"));
		TileEntityDispatcher.addMapping(TileEntityPump.class, id("pump"));
		TileEntityDispatcher.addMapping(TileEntityAssembler.class, id("assembler"));
		TileEntityDispatcher.addMapping(TileEntityAutoMiner.class, id("auto_miner"));
		TileEntityDispatcher.addMapping(TileEntitySignalumDynamo.class, id("dynamo"));
		TileEntityDispatcher.addMapping(TileEntityEnergyInjector.class, id("injector"));
		TileEntityDispatcher.addMapping(TileEntityBooster.class, id("booster"));
		TileEntityDispatcher.addMapping(TileEntityStabilizer.class, id("stabilizer"));
		TileEntityDispatcher.addMapping(TileEntityExternalIO.class, id("external_io"));
		TileEntityDispatcher.addMapping(TileEntityEnergyConnector.class, id("energy_connector"));
		TileEntityDispatcher.addMapping(TileEntityItemBus.class, id("item_bus"));
		TileEntityDispatcher.addMapping(TileEntityFluidHatch.class, id("fluid_hatch"));
		TileEntityDispatcher.addMapping(TileEntityInductionSmelter.class, id("induction_smelter"));
		TileEntityDispatcher.addMapping(TileEntityWakingAlloySmelter.class, id("waking_alloy_smelter"));
		TileEntityDispatcher.addMapping(TileEntityWakingCrusher.class, id("waking_crusher"));
		TileEntityDispatcher.addMapping(TileEntityWakingPlateFormer.class, id("waking_plate_former"));
		TileEntityDispatcher.addMapping(TileEntityWakingInfuser.class, id("waking_infuser"));
		TileEntityDispatcher.addMapping(TileEntityCentrifuge.class, id("centrifuge"));
		TileEntityDispatcher.addMapping(TileEntityDimensionalAnchor.class, id("dimensional_anchor"));
		TileEntityDispatcher.addMapping(TileEntityReinforcedExtractor.class, id("reinforced_extractor"));
		TileEntityDispatcher.addMapping(TileEntitySignalumReactor.class, id("reactor"));
		TileEntityDispatcher.addMapping(TileEntityIgnitor.class, id("ignitor"));
		TileEntityDispatcher.addMapping(TileEntityFluidConduit.class, id("fluid_conduit"));
		TileEntityDispatcher.addMapping(TileEntityConduit.class, id("conduit"));
		TileEntityDispatcher.addMapping(TileEntityCatalystConduit.class, id("catalyst_conduit"));
		TileEntityDispatcher.addMapping(TileEntityItemConduit.class, id("item_conduit"));
		TileEntityDispatcher.addMapping(TileEntityFilter.class, id("filter"));
		TileEntityDispatcher.addMapping(TileEntityInserter.class, id("inserter"));
		TileEntityDispatcher.addMapping(TileEntityStorageContainer.class, id("storage_container"));
		TileEntityDispatcher.addMapping(TileEntityVoidContainer.class, id("void_container"));
		TileEntityDispatcher.addMapping(TileEntityBuilder.class, id("builder"));
		//TileEntityDispatcher.addMapping(TileEntityChunkloader.class, id("chunkloader"));
		TileEntityDispatcher.addMapping(TileEntityWarpGate.class, id("warp_gate"));
		TileEntityDispatcher.addMapping(TileEntityMultiConduit.class, id("multi_conduit"));
		TileEntityDispatcher.addMapping(TileEntityProgrammer.class, id("programmer"));
		TileEntityDispatcher.addMapping(TileEntityReinforcedWrathBeacon.class, id("reinforced_wrath_beacon"));
		TileEntityDispatcher.addMapping(TileEntityWrathBeacon.class, id("wrath_beacon"));
		TileEntityDispatcher.addMapping(TileEntityUVLamp.class, id("uv_lamp"));
		TileEntityDispatcher.addMapping(TileEntityPulsar.class, id("pulsar"));
		TileEntityDispatcher.addMapping(TileEntityBonsaiPot.class, id("bonsai"));
		TileEntityDispatcher.addMapping(TileEntityLaserDrill.class, id("laser_drill"));
		TileEntityDispatcher.addMapping(TileEntityGreenhouse.class, id("greenhouse"));
		TileEntityDispatcher.addMapping(TileEntityEncapsulator.class, id("encapsulator"));
		TileEntityDispatcher.addMapping(TileEntityPedestal.class, id("pedestal"));
		TileEntityDispatcher.addMapping(TileEntitySITrommel.class, id("trommel"));
		TileEntityDispatcher.addMapping(TileEntityRedstoneClock.class, id("redstone_clock"));
		TileEntityDispatcher.addMapping(TileEntityHeatPump.class, id("heat_pump"));

		LOGGER.info("Registering entities...");
		EntityDispatcher.getInstance().addMapping(ProjectileCrystal.class, id("volatile_crystal"), ProjectileCrystal::new, "entity.signalindustries.volatileCrystal");
		EntityDispatcher.getInstance().addMapping(ProjectileFallingMeteor.class, id("falling_meteor"), ProjectileFallingMeteor::new,"entity.signalindustries.fallingMeteor");
		EntityDispatcher.getInstance().addMapping(ProjectileEnergyOrb.class, id("energy_orb"), ProjectileEnergyOrb::new,"entity.signalindustries.energyOrb");
		EntityDispatcher.getInstance().addMapping(ProjectileSunbeam.class, id("sunbeam"), ProjectileSunbeam::new, "entity.signalindustries.sunbeam");
		EntityDispatcher.getInstance().addMapping(MobInfernal.class, id("infernal"), MobInfernal::new, "entity.signalindustries.infernal");
		EntityDispatcher.getInstance().addMapping(EntityRealityTear.class, id("reality_tear"), EntityRealityTear::new, "entity.signalindustries.realityTear");
		EntityDispatcher.getInstance().addMapping(EntityShockwave.class, id("shockwave"), EntityShockwave::new, "entity.signalindustries.shockwave");

		NetEntityHandler.registerNetworkEntry(new NetEntryVolatileCrystal(), config.getInt("EntityIDs.volatileCrystalId"));
		NetEntityHandler.registerNetworkEntry(new NetEntryFallingMeteor(), config.getInt("EntityIDs.fallingMeteorId"));
		NetEntityHandler.registerNetworkEntry(new NetEntryEnergyOrb(), config.getInt("EntityIDs.energyOrbId"));
		NetEntityHandler.registerNetworkEntry(new NetEntrySunbeam(), config.getInt("EntityIDs.sunbeamId"));
		NetEntityHandler.registerNetworkEntry(new NetEntryShockwave(), config.getInt("EntityIDs.shockwaveId"));

		LOGGER.info("Registering packets...");
		NetworkHandler.registerNetworkMessage(NetworkMessageRecipeIdChange::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageIOChange::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageIOPreview::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageBuilderConfig::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageReactorStart::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageDrillModeChange::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageOpenSuit::new);
		NetworkHandler.registerNetworkMessage(NetworkMessagePowerSuitAction::new);
		NetworkHandler.registerNetworkMessage(NetworkMessagePowerSuitSync::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageExternalIOLinkBreak::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageFilterConfig::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageAutoMinerStart::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageMeteorLocationSync::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageSensorPipeSetFilter::new);
		NetworkHandler.registerNetworkMessage(NetworkMessagePowerSuitRemoteSync::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageRedstoneCoverSetFilter::new);

		LOGGER.info("Registering tags...");
		BlockTags.TAG_LIST.add(SIGNALUM_CONDUITS_CONNECT);
		BlockTags.TAG_LIST.add(FLUID_CONDUITS_CONNECT);
		BlockTags.TAG_LIST.add(ITEM_CONDUITS_CONNECT);
		BlockTags.TAG_LIST.add(CASING);
		BlockTags.TAG_LIST.add(REPLACEABLE_CASING);
		BlockTags.TAG_LIST.add(PROTOTYPE_CASING);
		BlockTags.TAG_LIST.add(BASIC_CASING);
		BlockTags.TAG_LIST.add(REINFORCED_CASING);
		BlockTags.TAG_LIST.add(AWAKENED_CASING);
		BlockTags.TAG_LIST.add(ORE_BLOCK);
		//GameSettings.register(SIKeybinds.renderFluidInsideConduits);
		//GameSettings.register(SIKeybinds.showSuitBackground);
	}

	public void beforeGameStart() {

	}

	public void afterGameStart() {
		LOGGER.info("Beginning core post-init.");
		SIRecipes.loadSpecial();
		LOGGER.info("Beginning core pre-init.");
		if (FabricLoaderImpl.INSTANCE.isModLoaded("vintagequesting")) {
			if (SIConfig.config.getBoolean("Other.enableQuests")) {
				new VintageQuestingSIPlugin().initializePlugin();
			}
		}
	}

	public static NamespaceID id(String id) {
		return NamespaceID.fromPool(MOD_ID, id);
	}

	public static String key(String key) {
		return MOD_ID + ":" + key;
	}

	public static String langKey(String key) {
		return MOD_ID + "." + key;
	}

	public static CompoundTag scene(String scene){
		return Catalyst.compoundOf(new String[]{"scene"},"signalindustries:"+scene);
	}

	public static void addMeteorLocation(MeteorLocation location) {
		meteorLocations.add(location);
		if (EnvironmentHelper.isMultiplayerServer()) {
			NetworkHandler.sendToAllPlayers(new NetworkMessageMeteorLocationSync(location));
		}
	}

	public static int getEnergyBurnTime(FluidStack stack) {
		if (stack == null) {
			return 0;
		} else {
			return stack.isFluidEqual(new FluidStack(SIFluids.ENERGY)) ? 200 : 0;
		}
	}

	public static Structure getStructureFromBlueprint(ItemStack blueprint, World world) {
		if (blueprint != null && blueprint.getItem() instanceof ItemBlueprint) {
			String key = blueprint.getData().getStringOrDefault("multiblock", "");
			String customKey = blueprint.getData().getStringOrDefault("structure", "");
			if (!key.isEmpty()) {
				return Multiblock.multiblocks.get(key.replace("multiblock.signalindustries.", ""));
			} else if (!customKey.isEmpty()) {
				if (SignalIndustries.customStructures.containsKey(customKey)) {
					return SignalIndustries.customStructures.get(customKey);
				} else {
					CustomStructure structure = new CustomStructure(customKey, world, false, false);
					SignalIndustries.customStructures.put(customKey, structure);
					return structure;
				}
			}
			return null;
		}
		return null;
	}

}
