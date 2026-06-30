package sunsetsatellite.signalindustries;

import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.sound.BlockSounds;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.blocks.logic.*;
import sunsetsatellite.signalindustries.blocks.logic.base.*;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityGreenhouse;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityLaserDrill;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingPlateFormer;
import sunsetsatellite.signalindustries.tiles.machines.simple.*;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityItemBus;
import sunsetsatellite.signalindustries.util.MachineTextures;
import sunsetsatellite.signalindustries.util.MultiblockPart;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.util.VerticalMachineTextures;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static sunsetsatellite.signalindustries.SIConfig.block;
import static sunsetsatellite.signalindustries.SIConfig.config;
import static sunsetsatellite.signalindustries.SignalIndustries.*;
import static sunsetsatellite.signalindustries.SignalIndustries.ORE_BLOCK;

public class SIBlocks extends DataInitializer {

	public static HashMap<Block<? extends BlockLogic>, MachineTextures> blockTextures = new HashMap<>();
	public static HashMap<Block<? extends BlockLogic>, VerticalMachineTextures> blockVerticalTextures = new HashMap<>();

	public static Block<? extends BlockLogic> signalumOre;
	public static Block<? extends BlockLogic> dilithiumOre;
	public static Block<? extends BlockLogic> dimensionalShardOre;

	public static Block<? extends BlockLogic> dilithiumBlock;
	public static Block<? extends BlockLogic> emptyCrystalBlock;
	public static Block<? extends BlockLogic> rawCrystalBlock;
	public static Block<? extends BlockLogic> awakenedSignalumCrystalBlock;

	public static Block<? extends BlockLogic> dilithiumCrystalBlock;
	public static Block<? extends BlockLogic> dimensionalCrystalBlock;

	public static Block<? extends BlockLogic> prototypeMachineCore;
	public static Block<? extends BlockLogic> basicMachineCore;
	public static Block<? extends BlockLogic> reinforcedMachineCore;
	public static Block<? extends BlockLogic> awakenedMachineCore;

	public static Block<? extends BlockLogic> basicCasing;
	public static Block<? extends BlockLogic> reinforcedCasing;
	public static Block<? extends BlockLogic> reinforcedCasing2;
	public static Block<? extends BlockLogic> reinforcedGrate;
	public static Block<? extends BlockLogic> reinforcedFrame;
	public static Block<? extends BlockLogic> awakenedCasing;
	public static Block<? extends BlockLogic> awakenedSocketCasing;
	public static Block<? extends BlockLogic> awakenedCasing2;
	public static Block<? extends BlockLogic> awakenedGrate;
	public static Block<? extends BlockLogic> basicCasing2;
	//public static Block<? extends BlockLogic> connectedTexture;

	public static Block<? extends BlockLogic> reinforcedGlass;

	public static Block<? extends BlockLogic> prototypeConduit;
	public static Block<? extends BlockLogic> basicConduit;
	public static Block<? extends BlockLogic> reinforcedConduit;
	public static Block<? extends BlockLogic> awakenedConduit;

	public static Block<? extends BlockLogic> prototypeFluidConduit;
	public static Block<? extends BlockLogic> basicFluidConduit;
	public static Block<? extends BlockLogic> reinforcedFluidConduit;

	public static Block<? extends BlockLogic> prototypeItemConduit;
	public static Block<? extends BlockLogic> basicItemConduit;
	public static Block<? extends BlockLogic> basicRestrictItemConduit;
	public static Block<? extends BlockLogic> basicSensorItemConduit;

	public static Block<? extends BlockLogic> multiConduit;

	public static Block<? extends BlockLogic> basicCatalystConduit;
	public static Block<? extends BlockLogic> reinforcedCatalystConduit;
	public static Block<? extends BlockLogic> awakenedCatalystConduit;

	public static Block<? extends BlockLogic> infiniteEnergyCell;
	public static Block<? extends BlockLogic> prototypeEnergyCell;
	public static Block<? extends BlockLogic> basicEnergyCell;
	public static Block<? extends BlockLogic> reinforcedEnergyCell;

	public static Block<? extends BlockLogic> prototypeFluidTank;
	public static Block<? extends BlockLogic> infiniteFluidTank;
	public static Block<? extends BlockLogic> basicFluidTank;

	public static Block<? extends BlockLogic> prototypeExtractor;
	public static Block<? extends BlockLogic> basicExtractor;
	public static Block<? extends BlockLogic> reinforcedExtractor;

	public static Block<? extends BlockLogic> basicCollector;
	public static Block<? extends BlockLogic> reinforcedCollector;

	public static Block<? extends BlockLogic> prototypeCrusher;
	public static Block<? extends BlockLogic> basicCrusher;
	public static Block<? extends BlockLogic> reinforcedCrusher;
	public static Block<? extends BlockLogic> wakingCrusher;

	public static Block<? extends BlockLogic> prototypeAlloySmelter;
	public static Block<? extends BlockLogic> basicAlloySmelter;
	public static Block<? extends BlockLogic> reinforcedAlloySmelter;
	public static Block<? extends BlockLogic> wakingAlloySmelter;

	public static Block<? extends BlockLogic> basicInductionSmelter;
	public static Block<? extends BlockLogic> reinforcedInductionSmelter;

	public static Block<? extends BlockLogic> prototypePlateFormer;
	public static Block<? extends BlockLogic> basicPlateFormer;
	public static Block<? extends BlockLogic> reinforcedPlateFormer;
	public static Block<? extends BlockLogic> wakingPlateFormer;

	public static Block<? extends BlockLogic> prototypeCrystalCutter;
	public static Block<? extends BlockLogic> basicCrystalCutter;
	public static Block<? extends BlockLogic> reinforcedCrystalCutter;

	public static Block<? extends BlockLogic> basicCrystalChamber;
	public static Block<? extends BlockLogic> reinforcedCrystalChamber;

	public static Block<? extends BlockLogic> basicInfuser;
	public static Block<? extends BlockLogic> reinforcedInfuser;
	public static Block<? extends BlockLogic> wakingInfuser;

	public static Block<? extends BlockLogic> basicAssembler;

	public static Block<? extends BlockLogic> basicTrommel;
	public static Block<? extends BlockLogic> reinforcedTrommel;

	public static Block<? extends BlockLogic> prototypeStorageContainer;
	public static Block<? extends BlockLogic> infiniteStorageContainer;
	public static Block<? extends BlockLogic> basicStorageContainer;
	public static Block<? extends BlockLogic> reinforcedStorageContainer;

	public static Block<? extends BlockLogic> basicWrathBeacon;
	public static Block<? extends BlockLogic> reinforcedWrathBeacon;

	public static Block<? extends BlockLogic> pulsarBlock;

	public static Block<? extends BlockLogic> dimensionalAnchor;


	public static Block<? extends BlockLogic> dilithiumStabilizer;

	public static Block<? extends BlockLogic> redstoneBooster;
	public static Block<? extends BlockLogic> dilithiumBooster;
	public static Block<? extends BlockLogic> awakenedBooster;

	public static Block<? extends BlockLogic> prototypePump;
	public static Block<? extends BlockLogic> basicPump;
	public static Block<? extends BlockLogic> reinforcedPump;

	public static Block<? extends BlockLogic> basicStoneworks;
	//public static Block<? extends BlockLogic> reinforcedStoneworks;

	/*public static Block<? extends BlockLogic> basicThermalChamber;
	public static Block<? extends BlockLogic> reinforcedThermalChamber;*/
	public static Block<? extends BlockLogic> basicHeatPump;

	public static Block<? extends BlockLogic> prototypeInserter;
	public static Block<? extends BlockLogic> basicInserter;

	public static Block<? extends BlockLogic> prototypeFilter;

	public static Block<? extends BlockLogic> basicAutomaticMiner;
	public static Block<? extends BlockLogic> reinforcedAutomaticMiner;
	public static Block<? extends BlockLogic> reinforcedLaserDrill;

	public static Block<? extends BlockLogic> externalIo;
	public static Block<? extends BlockLogic> reinforcedExternalIo;

	public static Block<? extends BlockLogic> reinforcedCentrifuge;

	public static Block<? extends BlockLogic> reinforcedIgnitor;

	public static Block<? extends BlockLogic> signalumReactorCore;

	public static Block<? extends BlockLogic> awakenedEnergyConnector;
	public static Block<? extends BlockLogic> reinforcedEnergyConnector;
	public static Block<? extends BlockLogic> basicEnergyConnector;

	public static Block<? extends BlockLogic> basicFluidInputHatch;
	public static Block<? extends BlockLogic> basicFluidOutputHatch;
	public static Block<? extends BlockLogic> basicItemInputBus;
	public static Block<? extends BlockLogic> basicItemOutputBus;

	public static Block<? extends BlockLogic> reinforcedFluidInputHatch;
	public static Block<? extends BlockLogic> reinforcedFluidOutputHatch;
	public static Block<? extends BlockLogic> reinforcedItemInputBus;
	public static Block<? extends BlockLogic> reinforcedItemOutputBus;

	public static Block<? extends BlockLogic> basicEnergyInjector;

	public static Block<? extends BlockLogic> basicSignalumDynamo;

	public static Block<? extends BlockLogic> basicProgrammer;
	//public static Block<? extends BlockLogic> reinforcedProgrammer;

	public static Block<? extends BlockLogic> basicBonsai;
	public static Block<? extends BlockLogic> reinforcedBonsai;

	public static Block<? extends BlockLogic> basicGreenhouse;

	public static Block<? extends BlockLogic> reinforcedChunkloader;

	public static Block<? extends BlockLogic> basicMarker;
	public static Block<? extends BlockLogic> reinforcedBuilder;
	public static Block<? extends BlockLogic> spatialEncapsulator;

	public static Block<? extends BlockLogic> creationAltar;

	public static Block<? extends BlockLogic> warpGate;

	public static Block<? extends BlockLogic> reinforcedParallelProcessor;
	public static Block<? extends BlockLogic> awakenedParallelProcessor;
	public static Block<? extends BlockLogic> awakenedParallelProcessor8x;

	public static Block<? extends BlockLogic> cobblestoneBricks;
	public static Block<? extends BlockLogic> crystalAlloyBricks;
	public static Block<? extends BlockLogic> reinforcedCrystalAlloyBricks;
	public static Block<? extends BlockLogic> awakenedAlloyBricks;
	public static Block<? extends BlockLogic> signalumAlloyCoil;
	public static Block<? extends BlockLogic> dilithiumCoil;
	public static Block<? extends BlockLogic> awakenedAlloyCoil;

	public static Block<? extends BlockLogicPortal> portalEternity;
	public static Block<? extends BlockLogic> realityFabric;
	public static Block<? extends BlockLogic> rootedFabric;
	//public static Block<? extends BlockLogic> unraveledFabric;
	public static Block<? extends BlockLogic> dilithiumRail;
	public static Block<? extends BlockLogic> eternalTreeLog;
	public static Block<? extends BlockLogic> etherealLeaves;
	public static Block<? extends BlockLogic> ashenTreeSapling;
	public static Block<? extends BlockLogic> fueledEternalTreeLog;
	public static Block<? extends BlockLogic> glowingObsidian;
	public static Block<? extends BlockLogic> uvLamp;
	public static Block<? extends BlockLogic> voidContainer;
	public static Block<? extends BlockLogic> redstoneClock;

	public static Block<? extends BlockLogic> lunarTotem;
	public static Block<? extends BlockLogic> solarTotem;
	public static Block<? extends BlockLogic> pedestal;

	public static Block<? extends BlockLogic> energyStill;
	public static Block<? extends BlockLogic> energyFlowing;
	public static Block<? extends BlockLogic> burntSignalumFlowing;
	public static Block<? extends BlockLogic> burntSignalumStill;
	public static Block<? extends BlockLogic> worldResinStill;
	public static Block<? extends BlockLogic> worldResinFlowing;

	@Override
	public void init() {
		if (initialized) return;
		LOGGER.info("Initializing blocks...");

		energyFlowing = customBlock(new BlockBuilder(MOD_ID),
			"signalumEnergy.flowing",
			"signaling_energy_flowing",
			"energyFlowing",
			0,
			(block) -> new BlockLogicFluidFlowing(block, Materials.WATER, new FluidWater(), energyStill),
			new MachineTextures().withDefaultTexture("signalum_energy_transparent"))
			.withTags(BlockTags.NOT_IN_CREATIVE_MENU);
		energyStill = customBlock(new BlockBuilder(MOD_ID),
			"signalumEnergy.still",
			"signaling_energy_still",
			"energyStill",
			0,
			(block) -> new BlockLogicFluidFlowing(block, Materials.WATER, new FluidWater(), energyStill),
			new MachineTextures().withDefaultTexture("signalum_energy_transparent"))
			.withTags(BlockTags.NOT_IN_CREATIVE_MENU);

		burntSignalumFlowing = customBlock(new BlockBuilder(MOD_ID),
			"burntSignalum.flowing",
			"burnt_signaling_energy_flowing",
			"burntSignalumFlowing",
			0,
			(block) -> new BlockLogicFluidFlowing(block, Materials.WATER, new FluidWater(), energyStill),
			new MachineTextures().withDefaultTexture("burnt_signalum"))
			.withTags(BlockTags.NOT_IN_CREATIVE_MENU);
		burntSignalumStill = customBlock(new BlockBuilder(MOD_ID),
			"burntSignalum.still",
			"burnt_signaling_energy_still",
			"burntSignalumStill",
			0,
			(block) -> new BlockLogicFluidFlowing(block, Materials.WATER, new FluidWater(), energyStill),
			new MachineTextures().withDefaultTexture("burnt_signalum"))
			.withTags(BlockTags.NOT_IN_CREATIVE_MENU);

		worldResinFlowing = customBlock(new BlockBuilder(MOD_ID),
			"worldResin.flowing",
			"world_resin_flowing",
			"worldResinFlowing",
			0,
			(block) -> new BlockLogicFluidFlowing(block, Materials.WATER, new FluidWater(), worldResinStill),
			new MachineTextures().withDefaultTexture("world_resin_transparent"))
			.withTags(BlockTags.NOT_IN_CREATIVE_MENU);

		worldResinStill = customBlock(new BlockBuilder(MOD_ID),
			"worldResin.still",
			"world_resin_still",
			"worldResinStill",
			0,
			(block) -> new BlockLogicFluidFlowing(block, Materials.WATER, new FluidWater(), worldResinStill),
			new MachineTextures().withDefaultTexture("world_resin_transparent"))
			.withTags(BlockTags.NOT_IN_CREATIVE_MENU);

		signalumOre = customBlock(new BlockBuilder(MOD_ID)
				.setLuminance(3)
				.setBlockSound(BlockSounds.STONE)
				.setHardness(3)
				.setResistance(25).addTags(BlockTags.MINEABLE_BY_PICKAXE),
			"signalumOre",
			"signalite_ore",
			"signalumOre",
			3,
			BlockLogicOreSignalite::new,
			new MachineTextures().withDefaultTexture("signalum_ore").withOverbrightTextures("signalum_ore_overlay")
		);

		dilithiumOre = customBlock(new BlockBuilder(MOD_ID)
				.setLuminance(3)
				.setBlockSound(BlockSounds.STONE)
				.setHardness(75)
				.setResistance(100).addTags(BlockTags.MINEABLE_BY_PICKAXE),
			"dilithiumOre",
			"dilithium_ore",
			"dilithiumOre",
			config.getInt("Other.dilithiumMiningLevel"),
			BlockLogicOreDilithium::new,
			new MachineTextures().withDefaultTexture("dilithium_ore")
		);

		dilithiumCrystalBlock = customBlock(new BlockBuilder(MOD_ID)
				.setLuminance(1)
				.setHardness(20)
				.setResistance(1000).addTags(BlockTags.MINEABLE_BY_PICKAXE),
			"dilithiumCrystalBlock",
			"dilithium_crystal_block",
			"dilithiumCrystalBlock",
			config.getInt("Other.dilithiumMiningLevel"),
			BlockLogicDilithiumCrystal::new,
			new MachineTextures().withDefaultTexture("dilithium_crystal_block")
		);

		dimensionalCrystalBlock = customBlock(new BlockBuilder(MOD_ID)
				.setLuminance(1)
				.setHardness(20)
				.setResistance(1000).addTags(BlockTags.MINEABLE_BY_PICKAXE),
			"dimensionalCrystalBlock",
			"dimensional_crystal_block",
			"dimensionalCrystalBlock",
			config.getInt("Other.dilithiumMiningLevel"),
			(block) -> new BlockLogicTransparent(block, Materials.GLASS),
			new MachineTextures().withDefaultTexture("dimensional_crystal_block")
		);

		dimensionalShardOre = customBlock(new BlockBuilder(MOD_ID)
				.setLuminance(3)
				.setBlockSound(BlockSounds.STONE)
				.setHardness(200)
				.setResistance(50000).addTags(BlockTags.MINEABLE_BY_PICKAXE),
			"dimensionalShardOre",
			"dimensional_shard_ore",
			"dimensionalShardOre",
			config.getInt("Other.dilithiumMiningLevel"),
			BlockLogicOreDimensionalShard::new,
			new MachineTextures().withDefaultTexture("dimensional_shard_ore")
		);

		emptyCrystalBlock = simpleBlock(
			new BlockBuilder(MOD_ID)
				.setLuminance(1)
				.setBlockSound(BlockSounds.GLASS)
				.setHardness(12)
				.setResistance(1000)
				.addTags(BlockTags.MINEABLE_BY_PICKAXE),
			"emptyCrystalBlock",
			"empty_crystal_block",
			"emptyCrystalBlock",
			3,
			Materials.METAL,
			new MachineTextures().withDefaultTexture("empty_crystal_block")
		);

		rawCrystalBlock = simpleBlock(
			new BlockBuilder(MOD_ID)
				.setLuminance(15)
				.setBlockSound(BlockSounds.GLASS)
				.setHardness(24)
				.setResistance(50000)
				.addTags(BlockTags.MINEABLE_BY_PICKAXE),
			"rawCrystalBlock",
			"saturated_crystal_block",
			"rawCrystalBlock",
			3,
			Materials.METAL,
			new MachineTextures().withDefaultTexture("saturated_crystal_block")
		);

		awakenedSignalumCrystalBlock = simpleBlock(
			new BlockBuilder(MOD_ID)
				.setLuminance(15)
				.setBlockSound(BlockSounds.GLASS)
				.setHardness(50)
				.setResistance(1000000)
				.addTags(BlockTags.MINEABLE_BY_PICKAXE),
			"awakenedSignalumCrystalBlock",
			"awakened_crystal_block",
			"awakenedSignalumCrystalBlock",
			3,
			Materials.METAL,
			new MachineTextures().withDefaultTexture("awakened_crystal_block")
		);

		prototypeMachineCore = customBlock(defaultBuilder(Tier.PROTOTYPE),
			"prototype.machine",
			"prototype_machine_block",
			"prototypeMachineCore",
			2,
			(block) -> new BlockLogicTiered(block, Materials.STONE, Tier.PROTOTYPE),
			new MachineTextures().withDefaultTexture("machine_prototype"));

		basicMachineCore = customBlock(defaultBuilder(Tier.BASIC),
			"basic.machine",
			"basic_machine_block",
			"basicMachineCore",
			3,
			(block) -> new BlockLogicTiered(block, Materials.METAL, Tier.BASIC),
			new MachineTextures().withDefaultTexture("machine_basic"));

		reinforcedMachineCore = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.machine",
			"reinforced_machine_block",
			"reinforcedMachineCore",
			3,
			(block) -> new BlockLogicTiered(block, Materials.METAL, Tier.REINFORCED),
			new MachineTextures().withDefaultTexture("machine_reinforced"));

		awakenedMachineCore = customBlock(defaultBuilder(Tier.AWAKENED),
			"awakened.machine",
			"awakened_machine_block",
			"awakenedMachineCore",
			3,
			(block) -> new BlockLogicTiered(block, Materials.METAL, Tier.AWAKENED),
			new MachineTextures().withDefaultTexture("machine_awakened"));

		prototypeExtractor = customBlock(defaultBuilder(Tier.PROTOTYPE),
			"prototype.extractor",
			"prototype_extractor",
			"prototypeExtractor",
			2,
			(block) -> new BlockLogicMachine(block, Materials.STONE, Tier.PROTOTYPE, TileEntityExtractor::new, "extractor"),
			new MachineTextures(Tier.PROTOTYPE)
				.withDefaultSideTextures("extractor_prototype_side_empty")
				.withActiveSideTextures("extractor_prototype_side_active")
				.withOverbrightSideTextures("extractor_overlay")
		);

		basicExtractor = customBlock(defaultBuilder(Tier.BASIC),
			"basic.extractor",
			"basic_extractor",
			"basicExtractor",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityExtractor::new, "extractor"),
			new MachineTextures(Tier.BASIC)
				.withDefaultSideTextures("extractor_basic_side_empty")
				.withActiveSideTextures("extractor_basic_side_active")
				.withOverbrightSideTextures("extractor_overlay")
		);

		basicCollector = customBlock(defaultBuilder(Tier.BASIC),
			"basic.collector",
			"basic_collector",
			"basicCollector",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityCollector::new, "collector"),
			new MachineTextures(Tier.BASIC)
				.withDefaultTopTexture("basic_collector_top_inactive")
				.withActiveTopTexture("basic_collector_top")
				.withOverbrightTopTexture("collector_overlay")
				.withDefaultSideTextures("basic_collector_side_inactive")
				.withActiveSideTextures("basic_collector_side")
				.withOverbrightSideTextures("crystal_overlay")
		);

		reinforcedCollector = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.collector",
			"reinforced_collector",
			"reinforcedCollector",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityCollector::new, "collector"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultTopTexture("reinforced_collector_top_inactive")
				.withActiveTopTexture("reinforced_collector_top")
				.withOverbrightTopTexture("collector_overlay")
				.withDefaultSideTextures("reinforced_collector_side_inactive")
				.withActiveSideTextures("reinforced_collector_side")
				.withOverbrightSideTextures("crystal_overlay")
		);

		prototypeCrusher = customBlock(defaultBuilder(Tier.PROTOTYPE),
			"prototype.crusher",
			"prototype_crusher",
			"prototypeCrusher",
			2,
			(block) -> new BlockLogicMachine(block, Materials.STONE, Tier.PROTOTYPE, TileEntityCrusher::new, "crusher"),
			new MachineTextures(Tier.PROTOTYPE)
				.withDefaultTopTexture("crusher_prototype_top_inactive")
				.withDefaultNorthTexture("crusher_prototype_side")
				.withActiveTopTexture("crusher_prototype_top_active")
				.withActiveNorthTexture("crusher_prototype_side")
				.withOverbrightTopTexture("crusher_overlay")
		);

		basicCrusher = customBlock(defaultBuilder(Tier.BASIC),
			"basic.crusher",
			"basic_crusher",
			"basicCrusher",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityCrusher::new, "crusher"),
			new MachineTextures(Tier.BASIC)
				.withDefaultTopTexture("crusher_basic_top_inactive")
				.withDefaultNorthTexture("crusher_basic_side")
				.withActiveTopTexture("crusher_basic_top_active")
				.withActiveNorthTexture("crusher_basic_side")
				.withOverbrightTopTexture("crusher_overlay")
		);

		reinforcedCrusher = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.crusher",
			"reinforced_crusher",
			"reinforcedCrusher",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityCrusher::new, "crusher"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultTopTexture("crusher_reinforced_top_inactive")
				.withDefaultNorthTexture("crusher_reinforced_side")
				.withActiveTopTexture("crusher_reinforced_top_active")
				.withActiveNorthTexture("crusher_reinforced_side")
				.withOverbrightTopTexture("crusher_overlay")
		);

		prototypeAlloySmelter = customBlock(defaultBuilder(Tier.PROTOTYPE),
			"prototype.alloySmelter",
			"prototype_alloy_smelter",
			"prototypeAlloySmelter",
			2,
			(block) -> new BlockLogicMachine(block, Materials.STONE, Tier.PROTOTYPE, TileEntityAlloySmelter::new, "alloy_smelter"),
			new MachineTextures(Tier.PROTOTYPE)
				.withDefaultNorthTexture("alloy_smelter_prototype_inactive")
				.withActiveNorthTexture("alloy_smelter_prototype_active")
				.withOverbrightNorthTexture("alloy_smelter_overlay")
		);

		basicAlloySmelter = customBlock(defaultBuilder(Tier.BASIC),
			"basic.alloySmelter",
			"basic_alloy_smelter",
			"basicAlloySmelter",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityAlloySmelter::new, "alloy_smelter"),
			new MachineTextures(Tier.BASIC)
				.withDefaultNorthTexture("alloy_smelter_basic_inactive")
				.withActiveNorthTexture("alloy_smelter_basic_active")
				.withOverbrightNorthTexture("alloy_smelter_overlay")
		);

		reinforcedAlloySmelter = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.alloySmelter",
			"reinforced_alloy_smelter",
			"reinforcedAlloySmelter",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityAlloySmelter::new, "alloy_smelter"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultNorthTexture("alloy_smelter_reinforced_inactive")
				.withActiveNorthTexture("alloy_smelter_reinforced_active")
				.withOverbrightNorthTexture("alloy_smelter_reinforced_overlay")
		);

		prototypePlateFormer = customBlock(defaultBuilder(Tier.PROTOTYPE),
			"prototype.plateFormer",
			"prototype_plate_former",
			"prototypePlateFormer",
			2,
			(block) -> new BlockLogicMachine(block, Materials.STONE, Tier.PROTOTYPE, TileEntityPlateFormer::new, "plate_former"),
			new MachineTextures(Tier.PROTOTYPE)
				.withDefaultNorthTexture("plate_former_prototype_inactive")
				.withActiveNorthTexture("plate_former_prototype_active")
				.withOverbrightNorthTexture("plate_former_overlay")
		);

		basicPlateFormer = customBlock(defaultBuilder(Tier.BASIC),
			"basic.plateFormer",
			"basic_plate_former",
			"basicPlateFormer",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityPlateFormer::new, "plate_former"),
			new MachineTextures(Tier.BASIC)
				.withDefaultNorthTexture("plate_former_basic_inactive")
				.withActiveNorthTexture("plate_former_basic_active")
				.withOverbrightNorthTexture("plate_former_overlay")
		);

		reinforcedPlateFormer = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.plateFormer",
			"reinforced_plate_former",
			"reinforcedPlateFormer",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityPlateFormer::new, "plate_former"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultNorthTexture("plate_former_reinforced_inactive")
				.withActiveNorthTexture("plate_former_reinforced_active")
				.withOverbrightNorthTexture("plate_former_overlay")
		);

		basicCrystalChamber = customBlock(defaultBuilder(Tier.BASIC),
			"basic.crystalChamber",
			"basic_crystal_chamber",
			"basicCrystalChamber",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityCrystalChamber::new, "crystal_chamber"),
			new MachineTextures(Tier.BASIC)
				.withDefaultNorthTexture("basic_crystal_chamber_side_inactive")
				.withActiveNorthTexture("basic_crystal_chamber_side_active")
				.withOverbrightNorthTexture("chamber_overlay")
		);

		reinforcedCrystalChamber = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.crystalChamber",
			"reinforced_crystal_chamber",
			"reinforcedCrystalChamber",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityCrystalChamber::new, "crystal_chamber"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultNorthTexture("reinforced_crystal_chamber_side_inactive")
				.withActiveNorthTexture("reinforced_crystal_chamber_side_active")
				.withOverbrightNorthTexture("reinforced_chamber_overlay")
		);

		prototypeCrystalCutter = customBlock(defaultBuilder(Tier.PROTOTYPE),
			"prototype.crystalCutter",
			"prototype_crystal_cutter",
			"prototypeCrystalCutter",
			2,
			(block) -> new BlockLogicMachine(block, Materials.STONE, Tier.PROTOTYPE, TileEntityCrystalCutter::new, "crystal_cutter"),
			new MachineTextures(Tier.PROTOTYPE)
				.withDefaultNorthTexture("crystal_cutter_prototype_inactive")
				.withActiveNorthTexture("crystal_cutter_prototype_active")
				.withOverbrightNorthTexture("cutter_overlay")
		);

		basicCrystalCutter = customBlock(defaultBuilder(Tier.BASIC),
			"basic.crystalCutter",
			"basic_crystal_cutter",
			"basicCrystalCutter",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityCrystalCutter::new, "crystal_cutter"),
			new MachineTextures(Tier.BASIC)
				.withDefaultNorthTexture("crystal_cutter_basic_inactive")
				.withActiveNorthTexture("crystal_cutter_basic_active")
				.withOverbrightNorthTexture("cutter_overlay")
		);

		reinforcedCrystalCutter = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.crystalCutter",
			"reinforced_crystal_cutter",
			"reinforcedCrystalCutter",
			3,
			(block) -> new BlockLogicMachine(block, Materials.STONE, Tier.REINFORCED, TileEntityCrystalCutter::new, "crystal_cutter"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultNorthTexture("crystal_cutter_reinforced_inactive")
				.withActiveNorthTexture("crystal_cutter_reinforced_active")
				.withOverbrightNorthTexture("reinforced_cutter_overlay")
		);

		basicInfuser = customBlock(defaultBuilder(Tier.BASIC),
			"basic.infuser",
			"basic_infuser",
			"basicInfuser",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityInfuser::new, "infuser"),
			new MachineTextures(Tier.BASIC)
				.withDefaultSideTextures("infuser_basic_side_inactive")
				.withActiveSideTextures("infuser_basic_side_active")
				.withOverbrightSideTextures("infuser_overlay")
		);

		reinforcedInfuser = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.infuser",
			"reinforced_infuser",
			"reinforcedInfuser",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityInfuser::new, "infuser"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultSideTextures("infuser_reinforced_side_inactive")
				.withActiveSideTextures("infuser_reinforced_side_active")
				.withOverbrightSideTextures("reinforced_infuser_overlay")
		);

		basicEnergyInjector = customBlock(defaultBuilder(Tier.BASIC),
			"basic.energyInjector",
			"basic_energy_injector",
			"basicEnergyInjector",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityEnergyInjector::new, "energy_injector").setNonSolid(),
			new MachineTextures()
				.withDefaultTexture("basic_energy_injector_bottom")
		);

		redstoneBooster = customBlock(defaultBuilder(Tier.BASIC),
			"basic.booster",
			"basic_booster",
			"redstoneBooster",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityBooster::new, "booster"),
			new MachineTextures(Tier.BASIC)
				.withDefaultSideTextures("basic_booster_side_inactive")
				.withDefaultNorthTexture("basic_booster_top_inactive")
				.withActiveSideTextures("basic_booster_side_active")
				.withActiveNorthTexture("basic_booster_top_active")
				.withOverbrightSideTextures("basic_booster_overlay")
				.withOverbrightNorthTexture("basic_booster_overlay_top")
		);

		dilithiumBooster = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.booster",
			"reinforced_booster",
			"dilithiumBooster",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityBooster::new, "booster"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultSideTextures("dilithium_booster_side_inactive")
				.withDefaultNorthTexture("dilithium_top_inactive")
				.withActiveSideTextures("dilithium_booster_side_active")
				.withActiveNorthTexture("dilithium_top_active")
				.withOverbrightSideTextures("booster_overlay")
				.withOverbrightNorthTexture("dilithium_machine_overlay")
		);

		awakenedBooster = customBlock(defaultBuilder(Tier.AWAKENED),
			"awakened.booster",
			"awakened_booster",
			"awakenedBooster",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.AWAKENED, TileEntityBooster::new, "booster"),
			new MachineTextures(Tier.AWAKENED)
				.withDefaultSideTextures("awakened_booster_side_inactive")
				.withDefaultNorthTexture("awakened_booster_top_inactive")
				.withActiveSideTextures("awakened_booster_side_active")
				.withActiveNorthTexture("awakened_booster_top_active")
				.withOverbrightSideTextures("awakened_booster_overlay")
				.withOverbrightNorthTexture("dilithium_machine_overlay")
		);

		dilithiumStabilizer = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.stabilizer",
			"reinforced_stabilizer",
			"dilithiumStabilizer",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityStabilizer::new, "stabilizer").setVertical(),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultSideTextures("dilithium_stabilizer_side_inactive")
				.withDefaultNorthTexture("dilithium_top_inactive")
				.withActiveSideTextures("dilithium_stabilizer_side_active")
				.withActiveNorthTexture("dilithium_top_active")
				.withOverbrightSideTextures("stabilizer_overlay")
				.withOverbrightNorthTexture("dilithium_machine_overlay"),
			new VerticalMachineTextures(Tier.REINFORCED)
				.withVerticalDefaultSideTextures("dilithium_stabilizer_side_inactive")
				.withVerticalDefaultTopTexture("dilithium_top_inactive")
				.withVerticalActiveSideTextures("dilithium_stabilizer_side_active")
				.withVerticalActiveTopTexture("dilithium_top_active")
				.withVerticalOverbrightSideTextures("stabilizer_overlay")
				.withVerticalOverbrightTopTexture("dilithium_machine_overlay")
		);

		basicFluidInputHatch = customBlock(defaultBuilder(Tier.BASIC),
			"basic.fluidInputHatch",
			"basic_fluid_input_hatch",
			"basicFluidInputHatch",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.BASIC, TileEntityFluidHatch::new, "fluid_hatch", MultiblockPart.Type.FLUID, MultiblockPart.IO.INPUT),
			new MachineTextures(Tier.BASIC)
				.withDefaultTexture("basic_fluid_input_hatch")
		);

		basicFluidOutputHatch = customBlock(defaultBuilder(Tier.BASIC),
			"basic.fluidOutputHatch",
			"basic_fluid_output_hatch",
			"basicFluidOutputHatch",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.BASIC, TileEntityFluidHatch::new, "fluid_hatch", MultiblockPart.Type.FLUID, MultiblockPart.IO.OUTPUT),
			new MachineTextures(Tier.BASIC)
				.withDefaultTexture("basic_fluid_output_hatch")
		);

		basicItemInputBus = customBlock(defaultBuilder(Tier.BASIC),
			"basic.itemInputBus",
			"basic_item_input_bus",
			"basicItemInputBus",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.BASIC, TileEntityItemBus::new, "item_bus", MultiblockPart.Type.ITEM, MultiblockPart.IO.INPUT),
			new MachineTextures(Tier.BASIC)
				.withDefaultTexture("basic_input_bus")
		);

		basicItemOutputBus = customBlock(defaultBuilder(Tier.BASIC),
			"basic.itemOutputBus",
			"basic_item_output_bus",
			"basicItemOutputBus",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.BASIC, TileEntityItemBus::new, "item_bus", MultiblockPart.Type.ITEM, MultiblockPart.IO.OUTPUT),
			new MachineTextures(Tier.BASIC)
				.withDefaultTexture("basic_output_bus")
		);

		reinforcedFluidInputHatch = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.fluidInputHatch",
			"reinforced_fluid_input_hatch",
			"reinforcedFluidInputHatch",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.REINFORCED, TileEntityFluidHatch::new, "fluid_hatch", MultiblockPart.Type.FLUID, MultiblockPart.IO.INPUT),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultTexture("reinforced_fluid_input_hatch")
		);

		reinforcedFluidOutputHatch = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.fluidOutputHatch",
			"reinforced_fluid_output_hatch",
			"reinforcedFluidOutputHatch",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.REINFORCED, TileEntityFluidHatch::new, "fluid_hatch", MultiblockPart.Type.FLUID, MultiblockPart.IO.OUTPUT),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultTexture("reinforced_fluid_output_hatch")
		);

		reinforcedItemInputBus = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.itemInputBus",
			"reinforced_item_input_bus",
			"reinforcedItemInputBus",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.REINFORCED, TileEntityItemBus::new, "item_bus", MultiblockPart.Type.ITEM, MultiblockPart.IO.INPUT),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultTexture("reinforced_input_bus")
		);

		reinforcedItemOutputBus = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.itemOutputBus",
			"reinforced_item_output_bus",
			"reinforcedItemOutputBus",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.REINFORCED, TileEntityItemBus::new, "item_bus", MultiblockPart.Type.ITEM, MultiblockPart.IO.OUTPUT),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultTexture("reinforced_output_bus")
		);

		basicEnergyConnector = customBlock(defaultBuilder(Tier.BASIC),
			"basic.energyConnector",
			"basic_energy_connector",
			"basicEnergyConnector",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.BASIC, TileEntityEnergyConnector::new, "energy_connector", MultiblockPart.Type.ENERGY, MultiblockPart.IO.N_A),
			new MachineTextures(Tier.BASIC)
				.withDefaultTexture("basic_energy_connector")
		);

		reinforcedEnergyConnector = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.energyConnector",
			"reinforced_energy_connector",
			"reinforcedEnergyConnector",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.REINFORCED, TileEntityEnergyConnector::new, "energy_connector", MultiblockPart.Type.ENERGY, MultiblockPart.IO.N_A),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultTexture("reinforced_energy_connector")
		);

		awakenedEnergyConnector = customBlock(defaultBuilder(Tier.AWAKENED),
			"awakened.energyConnector",
			"awakened_energy_connector",
			"awakenedEnergyConnector",
			3,
			(block) -> new BlockLogicMultiblockPart(block, Materials.METAL, Tier.AWAKENED, TileEntityEnergyConnector::new, "energy_connector", MultiblockPart.Type.ENERGY, MultiblockPart.IO.N_A),
			new MachineTextures(Tier.AWAKENED)
				.withDefaultTexture("awakened_energy_connector")
		);

		dimensionalAnchor = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.dimensionalAnchor",
			"dimensional_anchor",
			"dimensionalAnchor",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityDimensionalAnchor::new, "anchor"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultSideTextures("dimensional_anchor_inactive")
				.withDefaultTopTexture("dimensional_anchor_top_inactive")
				.withActiveSideTextures("dimensional_anchor")
				.withActiveTopTexture("dimensional_anchor_top")
				.withActiveBottomTexture("dimensional_anchor_bottom")
				.withOverbrightTextures("anchor_blank_overlay")
				.withOverbrightSideTextures("anchor_overlay")
				.withOverbrightTopTexture("anchor_top_overlay")
		);

		reinforcedLaserDrill = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.laserDrill",
			"reinforced_laser_drill",
			"reinforcedLaserDrill",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityLaserDrill::new, "laser_drill"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultNorthTexture("reinforced_laser_drill")
				.withActiveNorthTexture("reinforced_laser_drill_active")
				.withOverbrightNorthTexture("laser_drill_overlay")
		);

		basicGreenhouse = customBlock(defaultBuilder(Tier.BASIC),
			"basic.greenhouse",
			"basic_greenhouse",
			"basicGreenhouse",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.BASIC, TileEntityGreenhouse::new, "greenhouse"),
			new MachineTextures(Tier.BASIC)
				.withDefaultNorthTexture("basic_greenhouse_front_inactive")
				.withActiveNorthTexture("basic_greenhouse_front_active")
		);

		wakingAlloySmelter = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.wakingAlloySmelter",
			"waking_alloy_smelter",
			"wakingAlloySmelter",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityWakingAlloySmelter::new, "waking_alloy_smelter"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultNorthTexture("alloy_smelter_reinforced_inactive")
				.withActiveNorthTexture("waking_alloy_smelter_reinforced_active")
				.withOverbrightNorthTexture("waking_alloy_smelter_overlay")
		);

		wakingCrusher = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.wakingCrusher",
			"waking_crusher",
			"wakingCrusher",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityWakingCrusher::new, "waking_crusher"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultTopTexture("crusher_reinforced_top_inactive")
				.withDefaultNorthTexture("waking_crusher_reinforced_side")
				.withActiveTopTexture("crusher_reinforced_top_active")
				.withActiveNorthTexture("waking_crusher_reinforced_side")
				.withOverbrightTopTexture("crusher_overlay")
		);

		wakingPlateFormer = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.wakingPlateFormer",
			"waking_plate_former",
			"wakingPlateFormer",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityWakingPlateFormer::new, "waking_plate_former"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultNorthTexture("plate_former_reinforced_inactive")
				.withActiveNorthTexture("waking_plate_former_reinforced_active")
				.withOverbrightNorthTexture("waking_plate_former_overlay")
		);

		wakingInfuser = customBlock(defaultBuilder(Tier.REINFORCED),
			"reinforced.wakingInfuser",
			"waking_infuser",
			"wakingInfuser",
			3,
			(block) -> new BlockLogicMachine(block, Materials.METAL, Tier.REINFORCED, TileEntityWakingInfuser::new, "waking_infuser"),
			new MachineTextures(Tier.REINFORCED)
				.withDefaultSideTextures("infuser_reinforced_side_inactive")
				.withActiveSideTextures("waking_infuser_side_active")
				.withOverbrightSideTextures("waking_infuser_overlay")
		);

		basicCasing = customBlock(
			defaultBuilder(Tier.BASIC),
			"basic.casing",
			"basic_casing",
			"basicCasing",
			3,
			(block) -> new BlockLogicCasing(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("basic_casing")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE, CASING, BASIC_CASING, REPLACEABLE_CASING);

		reinforcedCasing = customBlock(
			defaultBuilder(Tier.REINFORCED),
			"reinforced.casing",
			"reinforced_casing",
			"reinforcedCasing",
			3,
			(block) -> new BlockLogicCasing(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("reinforced_casing")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE, CASING, REPLACEABLE_CASING, REINFORCED_CASING);

		awakenedCasing = customBlock(
			defaultBuilder(Tier.AWAKENED),
			"awakened.casing",
			"awakened_casing",
			"awakenedCasing",
			3,
			(block) -> new BlockLogicCasing(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("awakened_casing_0")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE, CASING, AWAKENED_CASING, REPLACEABLE_CASING);

		awakenedSocketCasing = customBlock(
			defaultBuilder(Tier.AWAKENED),
			"awakened.casing.socket",
			"awakened_socket_casing",
			"awakenedSocketCasing",
			3,
			(block) -> new BlockLogicCasing(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("awakened_socket_casing_0")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE, CASING, AWAKENED_CASING);

		awakenedCasing2 = customBlock(
			defaultBuilder(Tier.AWAKENED),
			"awakened.casing2",
			"awakened_casing_2",
			"awakenedCasing2",
			3,
			(block) -> new BlockLogicCasing(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("awakened_casing_2")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE, CASING, AWAKENED_CASING, REPLACEABLE_CASING);

		basicCasing2 = customBlock(
			defaultBuilder(Tier.BASIC),
			"basic.casing2",
			"basic_casing_2",
			"basicCasing2",
			3,
			(block) -> new BlockLogicCasing(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("basic_casing_2")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE, CASING, BASIC_CASING, REPLACEABLE_CASING);

		reinforcedCasing2 = customBlock(
			defaultBuilder(Tier.REINFORCED),
			"reinforced.casing2",
			"reinforced_casing_2",
			"reinforcedCasing2",
			3,
			(block) -> new BlockLogicCasing(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("reinforced_casing_2")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE, CASING, REPLACEABLE_CASING);

		reinforcedGrate = customBlock(
			defaultBuilder(Tier.REINFORCED),
			"reinforced.grate",
			"reinforced_grate",
			"reinforcedGrate",
			3,
			(block) -> new BlockLogicNonSolid(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("reinforced_grate")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE);

		awakenedGrate = customBlock(
			defaultBuilder(Tier.AWAKENED),
			"awakened.grate",
			"awakened_grate",
			"awakenedGrate",
			3,
			(block) -> new BlockLogicNonSolid(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("awakened_grate")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE);

		reinforcedFrame = customBlock(
			defaultBuilder(Tier.REINFORCED),
			"reinforced.frame",
			"reinforced_frame",
			"reinforcedFrame",
			3,
			(block) -> new BlockLogicNonSolid(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("reinforced_frame")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE);

		reinforcedGlass = customBlock(
			defaultBuilder(Tier.REINFORCED),
			"reinforced.glass",
			"reinforced_glass",
			"reinforcedGlass",
			3,
			(block) -> new BlockLogicNonSolid(block, Materials.METAL),
			new MachineTextures().withDefaultTexture("reinforced_glass_0")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE);

		cobblestoneBricks = simpleBlock(
			defaultBuilder(Tier.PROTOTYPE),
			"prototype.bricks",
			"cobblestone_bricks",
			"cobblestoneBricks",
			1,
			Materials.STONE,
			new MachineTextures().withDefaultTexture("cobblestone_bricks")
		);

		crystalAlloyBricks = simpleBlock(
			defaultBuilder(Tier.BASIC),
			"basic.bricks",
			"crystal_alloy_bricks",
			"crystalAlloyBricks",
			3,
			Materials.METAL,
			new MachineTextures().withDefaultTexture("crystal_alloy_bricks")
		);

		reinforcedCrystalAlloyBricks = simpleBlock(
			defaultBuilder(Tier.REINFORCED),
			"reinforced.bricks",
			"reinforced_alloy_bricks",
			"reinforcedCrystalAlloyBricks",
			3,
			Materials.METAL,
			new MachineTextures().withDefaultTexture("reinforced_alloy_bricks")
		);

		awakenedAlloyBricks = simpleBlock(
			defaultBuilder(Tier.AWAKENED),
			"awakened.bricks",
			"awakened_alloy_bricks",
			"awakenedAlloyBricks",
			3,
			Materials.METAL,
			new MachineTextures().withDefaultTexture("awakened_alloy_bricks")
		);

		signalumAlloyCoil = customBlock(
			defaultBuilder(Tier.BASIC),
			"signalumAlloyCoil",
			"signalite_alloy_coil",
			"signalumAlloyCoil",
			3,
			(block) -> new BlockLogicFullyRotatable(block, Materials.METAL),
			new MachineTextures()
				.withDefaultSouthTexture("signalum_alloy_coil")
				.withDefaultNorthTexture("signalum_alloy_coil")
				.withDefaultEastTexture("signalum_alloy_coil_2")
				.withDefaultWestTexture("signalum_alloy_coil_2")
				.withDefaultTopBottomTextures("signalum_alloy_coil_top")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE);

		dilithiumCoil = customBlock(
			defaultBuilder(Tier.REINFORCED),
			"dilithiumCoil",
			"dilithium_coil",
			"dilithiumCoil",
			3,
			(block) -> new BlockLogicFullyRotatable(block, Materials.METAL),
			new MachineTextures()
				.withDefaultSideTextures("dilithium_coil")
				.withDefaultTopBottomTextures("dilithium_coil_top")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE);

		awakenedAlloyCoil = customBlock(
			defaultBuilder(Tier.REINFORCED).setLuminance(15),
			"awakenedAlloyCoil",
			"awakened_alloy_coil",
			"awakenedAlloyCoil",
			3,
			(block) -> new BlockLogicFullyRotatable(block, Materials.METAL),
			new MachineTextures()
				.withDefaultSouthTexture("awakened_alloy_coil")
				.withDefaultNorthTexture("awakened_alloy_coil")
				.withDefaultEastTexture("awakened_alloy_coil_2")
				.withDefaultWestTexture("awakened_alloy_coil_2")
				.withDefaultTopBottomTextures("awakened_alloy_coil_top")
		).withTags(BlockTags.MINEABLE_BY_PICKAXE);

		glowingObsidian = simpleBlock(
			new BlockBuilder(MOD_ID)
				.setLuminance(10)
				.setBlockSound(BlockSounds.STONE)
				.setHardness(2)
				.setResistance(1200),
			"glowingObsidian",
			"glowing_obsidian",
			"glowingObsidian",
			3,
			Materials.STONE,
			new MachineTextures().withDefaultTexture("glowing_obsidian").withOverbrightTextures("glowing_obsidian_overlay")
		);

		rootedFabric = simpleBlock(defaultBuilder(Tier.PROTOTYPE).setHardness(50).setResistance(50000),
			"rootedFabric",
			"rooted_fabric",
			"rootedFabric",
			config.getInt("Other.dilithiumMiningLevel"),
			Materials.STONE,
			new MachineTextures()
				.withDefaultTexture("rooted_fabric")
		);

		realityFabric = customBlock(new BlockBuilder(MOD_ID).setBlockSound(BlockSounds.STONE).setHardness(150).setResistance(50000),
			"realityFabric",
			"reality_fabric",
			"realityFabric",
			config.getInt("Other.awakenedMiningLevel"),
			(block) -> new BlockLogicUndroppable(block, Materials.STONE),
			new MachineTextures()
				.withDefaultTexture("reality_fabric")
		);

		eternalTreeLog = customBlock(new BlockBuilder(MOD_ID).setBlockSound(BlockSounds.WOOD).setHardness(75).setResistance(50000).setLuminance(12),
			"eternalTreeLog",
			"ashen_tree_log",
			"eternalTreeLog",
			3,
			(block) -> new BlockLogicEternalTreeLog(block, Materials.WOOD),
			new MachineTextures()
				.withDefaultTexture("eternal_tree_log")
				.withDefaultTopBottomTextures("eternal_tree_log_top")
		).withTags(BlockTags.MINEABLE_BY_AXE, BlockTags.MINEABLE_BY_PICKAXE);

		etherealLeaves = customBlock(new BlockBuilder(MOD_ID),
			"leaves.ethereal",
			"ethereal_leaves",
			"etherealLeaves",
			0,
			(block) -> new BlockLogicLeavesEthereal(block, Materials.GRASS),
			new MachineTextures()
				.withDefaultTexture("ethereal_leaves")
				.withOverbrightTextures("ethereal_leaves")
		).withSound(BlockSounds.GRASS).withHardness(0.2F).withLightBlock(1).withTags(BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.MINEABLE_BY_AXE, BlockTags.MINEABLE_BY_HOE, BlockTags.MINEABLE_BY_SWORD, BlockTags.MINEABLE_BY_SHEARS);

		ashenTreeSapling = customBlock(new BlockBuilder(MOD_ID),
			"sapling.ashen",
			"ashen_tree_sapling",
			"ashenTreeSapling",
			0,
			(block) -> new BlockLogicTransparent(block, Materials.PLANT),
			new MachineTextures()
				.withDefaultTexture("ashen_tree_sapling")
		).withSound(BlockSounds.GRASS).withHardness(0.0F).withTags(BlockTags.PLANTABLE_IN_JAR);

		fueledEternalTreeLog = customBlock(new BlockBuilder(MOD_ID).setBlockSound(BlockSounds.WOOD).setUnbreakable().setResistance(18000000).setLuminance(15),
			"fueledEternalTreeLog",
			"charged_ashen_tree_log",
			"fueledEternalTreeLog",
			3,
			(block) -> new BlockLogicEternalTreeLog(block, Materials.WOOD),
			new MachineTextures()
				.withDefaultTexture("fueled_eternal_tree_log")
				.withDefaultTopBottomTextures("fueled_eternal_tree_log_top")
		);

		dilithiumOre.withTags(ORE_BLOCK);
		signalumOre.withTags(ORE_BLOCK);
		dimensionalShardOre.withTags(ORE_BLOCK);

		List<Field> blockFields = Arrays.stream(SIBlocks.class.getDeclaredFields()).filter((F) -> Block.class.isAssignableFrom(F.getType())).toList();

		int unknownAmount = 0;
		int itemAmount = blockFields.size();
		for (Field field : blockFields) {
			try {
				Block<?> block = (Block<?>) field.get(null);
				if(block == null) {
					unknownAmount++;
					field.set(null,simpleBlock(
						defaultBuilder(Tier.PROTOTYPE),
						field.getName(),
						field.getName(),
						field.getName(),
						3,
						Materials.STONE,
						new MachineTextures().withDefaultTexture("unknown")
					));
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		LOGGER.info("Block progress: {}/{} ({}% complete)",itemAmount-unknownAmount,itemAmount,((float)(itemAmount-unknownAmount)/itemAmount)*100);

		setInitialized(true);
	}

	public BlockBuilder defaultBuilder(Tier tier) {
		BlockBuilder builder = new BlockBuilder(MOD_ID);
		builder = switch (tier) {
			case PROTOTYPE -> builder.setBlockSound(BlockSounds.STONE);
			case BASIC, REINFORCED, AWAKENED, INFINITE -> builder.setBlockSound(BlockSounds.METAL);
		};
		builder = builder.setHardness(1).setResistance(3).addTags(BlockTags.MINEABLE_BY_PICKAXE);
		if (tier == Tier.INFINITE) builder = builder.setUnbreakable();
		return builder;
	}

	public Block<? extends BlockLogic> simpleBlock(BlockBuilder builder, String lang, String name, String configId, int miningLevel, Material material, MachineTextures blockTextures) {
		Block<BlockLogic> block = builder
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
			.build(lang, name, block(configId), (b) -> new BlockLogic(b, material));
		SIBlocks.blockTextures.put(block, blockTextures);
		ItemToolPickaxe.miningLevels.put(block, miningLevel);
		//LOGGER.info("Registering block '{}'.", block.namespaceId());
		return block;
	}

	public <T extends BlockLogic> Block<T> customBlock(BlockBuilder builder, String lang, String name, String configId, int miningLevel, BlockLogicSupplier<T> blockLogicSupplier, MachineTextures blockTextures) {
		Block<T> block = builder
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
			.build(lang, name, block(configId), blockLogicSupplier);
		SIBlocks.blockTextures.put(block, blockTextures);
		ItemToolPickaxe.miningLevels.put(block, miningLevel);
		//LOGGER.info("Registering block '{}'.", block.namespaceId());
		return block;
	}

	public <T extends BlockLogic> Block<T> customBlock(BlockBuilder builder, String lang, String name, String configId, int miningLevel, BlockLogicSupplier<T> blockLogicSupplier, MachineTextures blockTextures, VerticalMachineTextures verticalBlockTextures) {
		Block<T> block = builder
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
			.build(lang, name, block(configId), blockLogicSupplier);
		SIBlocks.blockTextures.put(block, blockTextures);
		SIBlocks.blockVerticalTextures.put(block, verticalBlockTextures);
		ItemToolPickaxe.miningLevels.put(block, miningLevel);
		//LOGGER.info("Registering block '{}'.", block.namespaceId());
		return block;
	}

	public void afterBlockInit() {
		init();
		new SIFluids().init();
		new SIWeather().init();
		new SIBiomes().init();
		new SIWorldTypes().init();
		new SIDimensions().init();
	}
}
