package sunsetsatellite.signalindustries;

import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.sound.BlockSounds;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.util.MachineTextures;
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
import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;
import static sunsetsatellite.signalindustries.SignalIndustries.MOD_ID;

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
	}
}
