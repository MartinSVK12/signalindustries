package sunsetsatellite.signalindustries;

import net.minecraft.client.render.block.model.BlockModelFluid;
import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.useless.dragonfly.model.block.DFBlockModelBuilder;
import sunsetsatellite.signalindustries.blocks.*;
import sunsetsatellite.signalindustries.blocks.base.BlockTiered;
import sunsetsatellite.signalindustries.blocks.base.BlockTransparent;
import sunsetsatellite.signalindustries.blocks.base.BlockUndroppable;
import sunsetsatellite.signalindustries.blocks.machines.*;
import sunsetsatellite.signalindustries.blocks.models.*;
import sunsetsatellite.signalindustries.blocks.states.*;
import sunsetsatellite.signalindustries.util.DataInitializer;
import sunsetsatellite.signalindustries.util.PipeType;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.BlockBuilder;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIBlocks extends DataInitializer {
    public static Block signalumOre;
    public static Block dilithiumOre;
    public static Block dimensionalShardOre;
    public static Block dilithiumBlock;
    public static Block emptyCrystalBlock;
    public static Block rawCrystalBlock;
    public static Block awakenedSignalumCrystalBlock;
    public static Block dilithiumCrystalBlock;
    public static Block prototypeMachineCore;
    public static Block basicMachineCore;
    public static Block reinforcedMachineCore;
    public static Block awakenedMachineCore;
    public static Block basicCasing;
    public static Block reinforcedCasing;
    public static Block reinforcedGlass;
    public static Block prototypeConduit;
    public static Block basicConduit;
    public static Block reinforcedConduit;
    public static Block awakenedConduit;
    public static Block prototypeFluidConduit;
    public static Block basicFluidConduit;
    public static Block reinforcedFluidConduit;
    public static Block prototypeItemConduit;
    public static Block basicItemConduit;
    public static Block basicRestrictItemConduit;
    public static Block basicSensorItemConduit;
    public static Block multiConduit;
    public static Block basicCatalystConduit;
    public static Block reinforcedCatalystConduit;
    public static Block awakenedCatalystConduit;
    public static Block infiniteEnergyCell;
    public static Block prototypeEnergyCell;
    public static Block basicEnergyCell;
    public static Block prototypeFluidTank;
    public static Block infiniteFluidTank;
    public static Block basicFluidTank;
    public static Block prototypeExtractor;
    public static Block basicExtractor;
    public static Block reinforcedExtractor;
    public static Block prototypeCrusher;
    public static Block basicCrusher;
    public static Block reinforcedCrusher;
    public static Block prototypeAlloySmelter;
    public static Block basicAlloySmelter;
    public static Block basicInductionSmelter;
    public static Block prototypePlateFormer;
    public static Block basicPlateFormer;
    public static Block reinforcedPlateFormer;
    public static Block prototypeCrystalCutter;
    public static Block basicCrystalCutter;
    public static Block reinforcedCrystalCutter;
    public static Block basicCrystalChamber;
    public static Block reinforcedCrystalChamber;
    public static Block basicInfuser;
    public static Block basicAssembler;
    public static Block prototypeStorageContainer;
    public static Block infiniteStorageContainer;
    public static Block basicStorageContainer;
    public static Block reinforcedStorageContainer;
    public static Block basicWrathBeacon;
    public static Block reinforcedWrathBeacon;
    public static Block dimensionalAnchor;
    public static Block dilithiumStabilizer;
    public static Block redstoneBooster;
    public static Block dilithiumBooster;//TODO: W.I.P.
    public static Block awakenedBooster;
    public static Block prototypePump;
    public static Block basicPump;
    public static Block prototypeInserter;
    public static Block basicInserter;
    public static Block prototypeFilter;
    public static Block basicAutomaticMiner;
    public static Block externalIo;
    public static Block reinforcedExternalIo;
    public static Block reinforcedCentrifuge;
    public static Block reinforcedIgnitor;
    public static Block signalumReactorCore;
    public static Block reinforcedEnergyConnector;
    public static Block basicEnergyConnector;
    public static Block basicFluidInputHatch;
    public static Block basicFluidOutputHatch;
    public static Block basicItemInputBus;
    public static Block basicItemOutputBus;
    public static Block reinforcedFluidInputHatch;
    public static Block reinforcedFluidOutputHatch;
    public static Block reinforcedItemInputBus;
    public static Block reinforcedItemOutputBus;
    public static Block basicEnergyInjector;
    public static Block basicSignalumDynamo;
    public static Block basicProgrammer;
    public static Block cobblestoneBricks;
    public static Block crystalAlloyBricks;
    public static Block reinforcedCrystalAlloyBricks;
    public static Block signalumAlloyCoil;
    public static Block dilithiumCoil;
    public static Block awakenedAlloyCoil;

    public static Block portalEternity;
    public static Block realityFabric;
    public static Block rootedFabric;
    public static Block dilithiumRail;
    public static Block eternalTreeLog;
    public static Block fueledEternalTreeLog;
    public static Block glowingObsidian;
    public static Block uvLamp;

    public static BlockFluid energyStill;
    public static BlockFluid burntSignalumFlowing;
    public static BlockFluid burntSignalumStill;
    public static BlockFluid energyFlowing;

    public void init() {
        if(initialized) return;
        LOGGER.info("Initializing blocks...");
        signalumOre = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/signalum_ore")
                .setLuminance(3)
                .setBlockSound(BlockSounds.STONE)
                .setHardness(3)
                .setResistance(25)
                .build(new BlockOreSignalum("signalumOre", config.getInt("BlockIDs.signalumOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE).withOverbright());
        dilithiumOre = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/dilithium_ore")
                .setLuminance(3)
                .setBlockSound(BlockSounds.STONE)
                .setHardness(75)
                .setResistance(100)
                .build(new BlockOreDilithium("dilithiumOre", config.getInt("BlockIDs.dilithiumOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        dimensionalShardOre = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/dimensional_shard_ore")
                .setLuminance(3)
                .setBlockSound(BlockSounds.STONE)
                .setHardness(200)
                .setResistance(50000)
                .build(new BlockOreDimensionalShard("dimensionalShardOre", config.getInt("BlockIDs.dimensionalShardOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        dilithiumBlock = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/dilithium_block")
                .setLuminance(1)
                .setBlockSound(BlockSounds.GLASS)
                .setHardness(20)
                .setResistance(1000)
                .build(new Block("dilithiumBlock", config.getInt("BlockIDs.dilithiumBlock"), Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        emptyCrystalBlock = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/empty_crystal_block")
                .setLuminance(1)
                .setBlockSound(BlockSounds.GLASS)
                .setHardness(12)
                .setResistance(1000)
                .build(new Block("emptyCrystalBlock", config.getInt("BlockIDs.emptyCrystalBlock"), Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        rawCrystalBlock = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/saturated_crystal_block")
                .setLuminance(15).setBlockSound(BlockSounds.GLASS)
                .setHardness(24).setResistance(50000)
                .build(new Block("rawCrystalBlock", config.getInt("BlockIDs.rawCrystalBlock"), Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        awakenedSignalumCrystalBlock = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/awakened_crystal_block")
                .setLuminance(15)
                .setBlockSound(BlockSounds.GLASS)
                .setHardness(50)
                .setResistance(1000000)
                .build(new Block("awakenedSignalumCrystalBlock", config.getInt("BlockIDs.awakenedSignalumCrystalBlock"), Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        dilithiumCrystalBlock = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/dilithium_crystal_block")
                .setLuminance(1)
                .setBlockSound(BlockSounds.GLASS)
                .setHardness(20)
                .setResistance(1000)
                .build(new BlockDilithiumCrystal("dilithiumCrystalBlock", config.getInt("BlockIDs.dilithiumCrystalBlock"), Material.glass, false)).withTags(BlockTags.MINEABLE_BY_PICKAXE);
        prototypeMachineCore = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/machine_prototype")
                .setBlockSound(BlockSounds.STONE)
                .setLuminance(0)
                .setHardness(1)
                .setResistance(3)
                .build(new BlockTiered("prototype.machine", config.getInt("BlockIDs.prototypeMachineCore"), Tier.PROTOTYPE, Material.stone));
        basicMachineCore = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/machine_basic")
                .setBlockSound(BlockSounds.METAL)
                .setLuminance(0)
                .setHardness(3)
                .setResistance(8)
                .build(new BlockTiered("basic.machine", config.getInt("BlockIDs.basicMachineCore"), Tier.BASIC, Material.metal));
        reinforcedMachineCore = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/machine_reinforced")
                .setLuminance(0)
                .setHardness(4)
                .setResistance(15)
                .build(new BlockTiered("reinforced.machine", config.getInt("BlockIDs.reinforcedMachineCore"), Tier.REINFORCED, Material.metal));
        awakenedMachineCore = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/machine_awakened")
                .setLuminance(1)
                .setHardness(1)
                .setResistance(50)
                .build(new BlockTiered("awakened.machine", config.getInt("BlockIDs.awakenedMachineCore"), Tier.AWAKENED, Material.metal));
        basicCasing = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/basic_casing")
                .setLuminance(0)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(10)
                .setResistance(2000)
                .build(new Block("basic.casing", config.getInt("BlockIDs.basicCasing"), Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        reinforcedCasing = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/reinforced_casing")
                .setLuminance(0)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(10)
                .setResistance(2000)
                .build(new Block("reinforced.casing", config.getInt("BlockIDs.reinforcedCasing"), Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        reinforcedGlass = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/reinforced_glass_0")
                .setLuminance(0)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(4)
                .setResistance(2000)
                .setBlockModel((block)->new BlockModelConnectedTexture(block,"signalindustries:block/reinforced_glass"))
                .build(new BlockTransparent("reinforced.glass", config.getInt("BlockIDs.reinforcedGlass"), Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
        prototypeConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/conduit_prototype")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/energy/prototype/conduit_all.json")
                                        .setBlockState("prototype_conduit.json")
                                        .setMetaStateInterpreter(new ConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockConduit("prototype.conduit", config.getInt("BlockIDs.prototypeConduit"), Tier.PROTOTYPE, Material.glass));
        basicConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/conduit_basic")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/energy/basic/conduit_all.json")
                                        .setBlockState("basic_conduit.json")
                                        .setMetaStateInterpreter(new ConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockConduit("basic.conduit", config.getInt("BlockIDs.basicConduit"), Tier.BASIC, Material.glass));
        reinforcedConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/conduit_reinforced")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/energy/reinforced/conduit_all.json")
                                        .setBlockState("reinforced_conduit.json")
                                        .setMetaStateInterpreter(new ConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockConduit("reinforced.conduit", config.getInt("BlockIDs.reinforcedConduit"), Tier.REINFORCED, Material.glass));
        awakenedConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/conduit_awakened")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/energy/awakened/conduit_all.json")
                                        .setBlockState("awakened_conduit.json")
                                        .setMetaStateInterpreter(new CatalystConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockConduit("awakened.conduit", config.getInt("BlockIDs.awakenedConduit"), Tier.AWAKENED, Material.glass));
        basicCatalystConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/catalyst_energy_conduit_basic")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/catalyst/basic/conduit_all.json")
                                        .setBlockState("basic_catalyst_conduit.json")
                                        .setMetaStateInterpreter(new CatalystConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockCatalystConduit("basic.conduit.catalyst", config.getInt("BlockIDs.basicCatalystConduit"), Tier.BASIC, Material.glass));
        reinforcedCatalystConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/catalyst_energy_conduit_reinforced")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/catalyst/reinforced/conduit_all.json")
                                        .setBlockState("reinforced_catalyst_conduit.json")
                                        .setMetaStateInterpreter(new CatalystConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockCatalystConduit("reinforced.conduit.catalyst", config.getInt("BlockIDs.reinforcedCatalystConduit"), Tier.REINFORCED, Material.glass));
        awakenedCatalystConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/catalyst_energy_conduit_awakened")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/catalyst/awakened/conduit_all.json")
                                        .setBlockState("awakened_catalyst_conduit.json")
                                        .setMetaStateInterpreter(new CatalystConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockCatalystConduit("awakened.conduit.catalyst", config.getInt("BlockIDs.awakenedCatalystConduit"), Tier.AWAKENED, Material.glass));
        prototypeFluidConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/fluid_conduit_prototype")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/fluid/prototype/conduit_all.json")
                                        .setBlockState("prototype_fluid_conduit.json")
                                        .setMetaStateInterpreter(new ConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockFluidConduit("prototype.conduit.fluid", config.getInt("BlockIDs.prototypeFluidConduit"), Tier.PROTOTYPE, Material.glass));
        basicFluidConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/fluid_conduit_basic")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/fluid/basic/conduit_all.json")
                                        .setBlockState("basic_fluid_conduit.json")
                                        .setMetaStateInterpreter(new ConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockFluidConduit("basic.conduit.fluid", config.getInt("BlockIDs.basicFluidConduit"), Tier.BASIC, Material.glass));
        reinforcedFluidConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/fluid_conduit_reinforced")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/fluid/reinforced/conduit_all.json")
                                        .setBlockState("reinforced_fluid_conduit.json")
                                        .setMetaStateInterpreter(new ConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockFluidConduit("reinforced.conduit.fluid", config.getInt("BlockIDs.reinforcedFluidConduit"), Tier.REINFORCED, Material.glass));
        prototypeItemConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/item_conduit_prototype")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/item/prototype/conduit_all.json")
                                        .setBlockState("prototype_item_conduit.json")
                                        .setMetaStateInterpreter(new ItemConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockItemConduit("prototype.conduit.item", config.getInt("BlockIDs.prototypeItemConduit"), Tier.PROTOTYPE, Material.glass, PipeType.NORMAL));
        basicItemConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/item_conduit_basic")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/item/basic/conduit_all.json")
                                        .setBlockState("basic_item_conduit.json")
                                        .setMetaStateInterpreter(new ItemConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockItemConduit("basic.conduit.item", config.getInt("BlockIDs.basicItemConduit"), Tier.BASIC, Material.glass, PipeType.NORMAL));
        basicRestrictItemConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/item_conduit_basic")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/item/basic/restrict/conduit_all.json")
                                        .setBlockState("basic_item_conduit_restrict.json")
                                        .setMetaStateInterpreter(new ItemConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockItemConduit("basic.conduit.item.restrict", config.getInt("BlockIDs.basicRestrictItemConduit"), Tier.BASIC, Material.glass, PipeType.RESTRICT));
        basicSensorItemConduit = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/item_conduit_basic_sensor_off")
                .setLuminance(0)
                .setResistance(1)
                .setHardness(1)
                .setBlockSound(BlockSounds.GLASS)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("conduit/item/basic/sensor/off/conduit_all.json")
                                        .setBlockState("basic_item_conduit_sensor.json")
                                        .setMetaStateInterpreter(new ItemConduitStateInterpreter())
                                        .build(block)
                )
                .build(new BlockItemConduit("basic.conduit.item.sensor", config.getInt("BlockIDs.basicSensorItemConduit"), Tier.BASIC, Material.glass, PipeType.SENSOR));
        multiConduit = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setBlockModel((block)->new DFBlockModelBuilder(MOD_ID)
                        .setBlockModel("multi_conduit/frame.json")
                        .setBlockState("multi_conduit.json")
                        .setMetaStateInterpreter(new MultiConduitStateInterpreter())
                        .build(block)
                )
                .build(new BlockMultiConduit("reinforced.conduit.multi", config.getInt("BlockIDs.multiConduit"), Tier.REINFORCED, Material.metal ).withTags(SIGNALUM_CONDUITS_CONNECT,ITEM_CONDUITS_CONNECT,FLUID_CONDUITS_CONNECT));
        infiniteEnergyCell = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/cell_prototype")
                .setLuminance(1)
                .setHardness(-1)
                .setResistance(1000000)
                .setBlockSound(BlockSounds.GLASS)
                .build(new BlockEnergyCell("infinite.energyCell", config.getInt("BlockIDs.infiniteEnergyCell"), Tier.INFINITE, Material.glass));
        prototypeEnergyCell = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/cell_prototype")
                .setLuminance(1)
                .setHardness(1)
                .setResistance(5)
                .setBlockSound(BlockSounds.GLASS)
                .build(new BlockEnergyCell("prototype.energyCell", config.getInt("BlockIDs.prototypeEnergyCell"), Tier.PROTOTYPE, Material.glass));
        basicEnergyCell = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/cell_basic")
                .setLuminance(1)
                .setHardness(1)
                .setResistance(5)
                .setBlockSound(BlockSounds.GLASS)
                .build(new BlockEnergyCell("basic.energyCell", config.getInt("BlockIDs.basicEnergyCell"), Tier.BASIC, Material.glass));
        prototypeFluidTank = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/fluid_tank_prototype")
                .setLuminance(0)
                .setHardness(1)
                .setResistance(5)
                .setBlockSound(BlockSounds.GLASS)
                .build(new BlockSIFluidTank("prototype.fluidTank", config.getInt("BlockIDs.prototypeFluidTank"), Tier.PROTOTYPE, Material.glass));
        infiniteFluidTank = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/fluid_tank_prototype")
                .setLuminance(0)
                .setHardness(1)
                .setResistance(5)
                .setBlockSound(BlockSounds.GLASS)
                .build(new BlockSIFluidTank("infinite.fluidTank", config.getInt("BlockIDs.infiniteFluidTank"), Tier.INFINITE, Material.glass));
        basicFluidTank = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/fluid_tank_basic")
                .setLuminance(0)
                .setHardness(1)
                .setResistance(5)
                .setBlockSound(BlockSounds.GLASS)
                .build(new BlockSIFluidTank("basic.fluidTank", config.getInt("BlockIDs.basicFluidTank"), Tier.BASIC, Material.glass));
        prototypeExtractor = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTextures("signalindustries:block/prototype_blank")
                .setSideTextures("signalindustries:block/extractor_prototype_side_empty")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.PROTOTYPE)
                        .withDefaultSideTextures("extractor_prototype_side_empty")
                        .withActiveSideTextures("extractor_prototype_side_active")
                        .withOverbrightSideTextures("extractor_overlay")
                )
                .build(new BlockExtractor("prototype.extractor", config.getInt("BlockIDs.prototypeExtractor"), Tier.PROTOTYPE, Material.stone));
        basicExtractor = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSideTextures("signalindustries:block/extractor_basic_side_empty")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultSideTextures("extractor_basic_side_empty")
                        .withActiveSideTextures("extractor_basic_side_active")
                        .withOverbrightSideTextures("extractor_overlay")
                )
                .build(new BlockExtractor("basic.extractor", config.getInt("BlockIDs.basicExtractor"), Tier.BASIC, Material.metal));
        reinforcedExtractor = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSideTextures("signalindustries:block/extractor_reinforced_side_empty")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.REINFORCED)
                        .withDefaultSideTextures("extractor_reinforced_side_empty")
                        .withActiveSideTextures("extractor_reinforced_side_active")
                        .withOverbrightSideTextures("extractor_overlay")
                )
                .build(new BlockExtractor("reinforced.extractor", config.getInt("BlockIDs.reinforcedExtractor"), Tier.REINFORCED, Material.metal));
        prototypeCrusher = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTextures("signalindustries:block/prototype_blank")
                .setTopTexture("signalindustries:block/crusher_prototype_top_inactive")
                .setSouthTexture("signalindustries:block/crusher_prototype_side")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.PROTOTYPE)
                        .withDefaultTopTexture("crusher_prototype_top_inactive")
                        .withDefaultNorthTexture("crusher_prototype_side")
                        .withActiveTopTexture("crusher_prototype_top_active")
                        .withActiveNorthTexture("crusher_prototype_side")
                        .withOverbrightTopTexture("crusher_overlay")
                )
                .build(new BlockCrusher("prototype.crusher", config.getInt("BlockIDs.prototypeCrusher"), Tier.PROTOTYPE, Material.stone));
        basicCrusher = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setTopTexture("signalindustries:block/crusher_basic_top_inactive")
                .setSouthTexture("signalindustries:block/crusher_basic_side")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultTopTexture("crusher_basic_top_inactive")
                        .withDefaultNorthTexture("crusher_basic_side")
                        .withActiveTopTexture("crusher_basic_top_active")
                        .withActiveNorthTexture("crusher_basic_side")
                        .withOverbrightTopTexture("crusher_overlay")
                )
                .build(new BlockCrusher("basic.crusher", config.getInt("BlockIDs.basicCrusher"), Tier.BASIC, Material.metal));
        reinforcedCrusher = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setTopTexture("signalindustries:block/crusher_reinforced_top_inactive")
                .setSouthTexture("signalindustries:block/crusher_reinforced_side")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.REINFORCED)
                        .withDefaultTopTexture("crusher_reinforced_top_inactive")
                        .withDefaultNorthTexture("crusher_reinforced_side")
                        .withActiveTopTexture("crusher_reinforced_top_active")
                        .withActiveNorthTexture("crusher_reinforced_side")
                        .withOverbrightTopTexture("crusher_overlay")
                )
                .build(new BlockCrusher("reinforced.crusher", config.getInt("BlockIDs.reinforcedCrusher"), Tier.REINFORCED, Material.metal));
        prototypeAlloySmelter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTextures("signalindustries:block/prototype_blank")
                .setSouthTexture("signalindustries:block/alloy_smelter_prototype_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.PROTOTYPE)
                        .withDefaultNorthTexture("alloy_smelter_prototype_inactive")
                        .withActiveNorthTexture("alloy_smelter_prototype_active")
                        .withOverbrightNorthTexture("alloy_smelter_overlay")
                )
                .build(new BlockAlloySmelter("prototype.alloySmelter", config.getInt("BlockIDs.prototypeAlloySmelter"), Tier.PROTOTYPE, Material.stone));
        basicAlloySmelter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSouthTexture("signalindustries:block/alloy_smelter_basic_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultNorthTexture("alloy_smelter_basic_inactive")
                        .withActiveNorthTexture("alloy_smelter_basic_active")
                        .withOverbrightNorthTexture("alloy_smelter_overlay")
                )
                .build(new BlockAlloySmelter("basic.alloySmelter", config.getInt("BlockIDs.basicAlloySmelter"), Tier.BASIC, Material.metal));
        basicInductionSmelter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSouthTexture("signalindustries:block/basic_induction_smelter_front_inactive")
                .setTopTexture("signalindustries:block/basic_induction_smelter_top_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultTopTexture("basic_induction_smelter_top_inactive")
                        .withDefaultNorthTexture("basic_induction_smelter_front_inactive")
                        .withActiveTopTexture("basic_induction_smelter_top_active")
                        .withActiveNorthTexture("basic_induction_smelter_front_active")
                        .withOverbrightTopTexture("induction_smelter_top_overlay")
                        .withOverbrightNorthTexture("induction_smelter_front_overlay")
                )
                .build(new BlockInductionSmelter("basic.inductionSmelter", config.getInt("BlockIDs.basicInductionSmelter"), Tier.BASIC, Material.metal));
        prototypePlateFormer = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTextures("signalindustries:block/prototype_blank")
                .setSouthTexture("signalindustries:block/plate_former_prototype_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.PROTOTYPE)
                        .withDefaultNorthTexture("plate_former_prototype_inactive")
                        .withActiveNorthTexture("plate_former_prototype_active")
                        .withOverbrightNorthTexture("plate_former_overlay")
                )
                .build(new BlockPlateFormer("prototype.plateFormer", config.getInt("BlockIDs.prototypePlateFormer"), Tier.PROTOTYPE, Material.stone));
        basicPlateFormer = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSouthTexture("signalindustries:block/plate_former_basic_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultNorthTexture("plate_former_basic_inactive")
                        .withActiveNorthTexture("plate_former_basic_active")
                        .withOverbrightNorthTexture("plate_former_overlay")
                )
                .build(new BlockPlateFormer("basic.plateFormer", config.getInt("BlockIDs.basicPlateFormer"), Tier.BASIC, Material.metal));
        reinforcedPlateFormer = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSouthTexture("signalindustries:block/plate_former_reinforced_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.REINFORCED)
                        .withDefaultNorthTexture("plate_former_reinforced_inactive")
                        .withActiveNorthTexture("plate_former_reinforced_active")
                        .withOverbrightNorthTexture("plate_former_overlay")
                )
                .build(new BlockPlateFormer("reinforced.plateFormer", config.getInt("BlockIDs.reinforcedPlateFormer"), Tier.REINFORCED, Material.metal));
        prototypeCrystalCutter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTextures("signalindustries:block/prototype_blank")
                .setSouthTexture("signalindustries:block/crystal_cutter_prototype_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.PROTOTYPE)
                        .withDefaultNorthTexture("crystal_cutter_prototype_inactive")
                        .withActiveNorthTexture("crystal_cutter_prototype_active")
                        .withOverbrightNorthTexture("cutter_overlay")
                )
                .build(new BlockCrystalCutter("prototype.crystalCutter", config.getInt("BlockIDs.prototypeCrystalCutter"), Tier.PROTOTYPE, Material.stone));
        basicCrystalCutter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSouthTexture("signalindustries:block/crystal_cutter_basic_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultNorthTexture("crystal_cutter_basic_inactive")
                        .withActiveNorthTexture("crystal_cutter_basic_active")
                        .withOverbrightNorthTexture("cutter_overlay")
                )
                .build(new BlockCrystalCutter("basic.crystalCutter", config.getInt("BlockIDs.basicCrystalCutter"), Tier.BASIC, Material.metal));
        reinforcedCrystalCutter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSouthTexture("signalindustries:block/crystal_cutter_reinforced_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.REINFORCED)
                        .withDefaultNorthTexture("crystal_cutter_reinforced_inactive")
                        .withActiveNorthTexture("crystal_cutter_reinforced_active")
                        .withOverbrightNorthTexture("reinforced_cutter_overlay")
                )
                .build(new BlockCrystalCutter("reinforced.crystalCutter", config.getInt("BlockIDs.reinforcedCrystalCutter"), Tier.REINFORCED, Material.stone));
        basicCrystalChamber = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSouthTexture("signalindustries:block/basic_crystal_chamber_side_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultNorthTexture("basic_crystal_chamber_side_inactive")
                        .withActiveNorthTexture("basic_crystal_chamber_side_active")
                        .withOverbrightNorthTexture("chamber_overlay")
                )
                .build(new BlockCrystalChamber("basic.crystalChamber", config.getInt("BlockIDs.basicCrystalChamber"), Tier.BASIC, Material.metal));
        reinforcedCrystalChamber = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSouthTexture("signalindustries:block/reinforced_crystal_chamber_side_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.REINFORCED)
                        .withDefaultNorthTexture("reinforced_crystal_chamber_side_inactive")
                        .withActiveNorthTexture("reinforced_crystal_chamber_side_active")
                        .withOverbrightNorthTexture("reinforced_chamber_overlay")
                )
                .build(new BlockCrystalChamber("reinforced.crystalChamber", config.getInt("BlockIDs.reinforcedCrystalChamber"), Tier.REINFORCED, Material.metal));
        basicInfuser = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSideTextures("signalindustries:block/infuser_basic_side_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultSideTextures("infuser_basic_side_inactive")
                        .withActiveSideTextures("infuser_basic_side_active")
                        .withOverbrightSideTextures("infuser_overlay")
                )
                .build(new BlockInfuser("basic.infuser", config.getInt("BlockIDs.basicInfuser"), Tier.BASIC, Material.metal));
        basicAssembler = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_assembler_side")
                .setSouthTexture("signalindustries:block/basic_assembler_front")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultTexture("basic_assembler_side")
                        .withDefaultNorthTexture("basic_assembler_front")
                        .withActiveTexture("basic_assembler_side_active")
                        .withActiveNorthTexture("basic_assembler_front_active")
                        .withOverbrightTexture("assembler_overlay_side")
                        .withOverbrightNorthTexture("assembler_overlay_front")
                )
                .build(new BlockAssembler("basic.assembler", config.getInt("BlockIDs.basicAssembler"), Tier.BASIC, Material.metal));
        prototypeStorageContainer = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setBlockModel(BlockModelHorizontalRotation::new)
                .setTextures("signalindustries:block/prototype_blank")
                .setNorthTexture("signalindustries:block/container_prototype_front")
                .build(new BlockStorageContainer("prototype.storageContainer", config.getInt("BlockIDs.prototypeStorageContainer"), Tier.PROTOTYPE, Material.stone));
        infiniteStorageContainer = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTextures("signalindustries:block/prototype_blank")
                .setNorthTexture("signalindustries:block/container_prototype_front")
                .setBlockModel(BlockModelHorizontalRotation::new)
                .build(new BlockStorageContainer("infinite.storageContainer", config.getInt("BlockIDs.infiniteStorageContainer"), Tier.INFINITE, Material.stone));
        basicStorageContainer = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setNorthTexture("signalindustries:block/container_basic_front")
                .setBlockModel(BlockModelHorizontalRotation::new)
                .build(new BlockStorageContainer("basic.storageContainer", config.getInt("BlockIDs.basicStorageContainer"), Tier.BASIC, Material.metal));
        reinforcedStorageContainer = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setNorthTexture("signalindustries:block/container_reinforced_front")
                .setBlockModel(BlockModelHorizontalRotation::new)
                .build(new BlockStorageContainer("reinforced.storageContainer", config.getInt("BlockIDs.reinforcedStorageContainer"), Tier.REINFORCED, Material.metal));
        basicWrathBeacon = new BlockBuilder(MOD_ID)
                .setHardness(2)
                .setResistance(500)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSideTextures("signalindustries:block/wrath_beacon")
                .build(new BlockWrathBeacon("basic.wrathBeacon", config.getInt("BlockIDs.basicWrathBeacon"), Tier.BASIC, Material.metal));
        reinforcedWrathBeacon = new BlockBuilder(MOD_ID)
                .setHardness(2)
                .setResistance(500)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSideTextures("signalindustries:block/reinforced_wrath_beacon")
                .build(new BlockWrathBeacon("reinforced.wrathBeacon", config.getInt("BlockIDs.reinforcedWrathBeacon"), Tier.REINFORCED, Material.metal));
        dimensionalAnchor = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setTopTexture("signalindustries:block/dimensional_anchor_top_inactive")
                .setSideTextures("signalindustries:block/dimensional_anchor_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.REINFORCED)
                        .withDefaultSideTextures("dimensional_anchor_inactive")
                        .withDefaultTopTexture("dimensional_anchor_top_inactive")
                        .withActiveSideTextures("dimensional_anchor")
                        .withActiveTopTexture("dimensional_anchor_top")
                        .withActiveBottomTexture("dimensional_anchor_bottom")
                        .withOverbrightTexture("anchor_blank_overlay")
                        .withOverbrightSideTextures("anchor_overlay")
                        .withOverbrightTopTexture("anchor_top_overlay")
                )
                .build(new BlockDimensionalAnchor("reinforced.dimensionalAnchor", config.getInt("BlockIDs.dimensionalAnchor"), Tier.REINFORCED, Material.metal));
        dilithiumStabilizer = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSideTextures("signalindustries:block/dilithium_stabilizer_side_inactive")
                .setSouthTexture("signalindustries:block/dilithium_top_inactive")
                .setBlockModel((block) -> new BlockModelVerticalMachine(block, Tier.REINFORCED)
                        .withDefaultSideTextures("dilithium_stabilizer_side_inactive")
                        .withDefaultNorthTexture("dilithium_top_inactive")
                        .withActiveSideTextures("dilithium_stabilizer_side_active")
                        .withActiveNorthTexture("dilithium_top_active")
                        .withOverbrightSideTextures("stabilizer_overlay")
                        .withOverbrightNorthTexture("dilithium_machine_overlay")
                        .withVerticalDefaultSideTextures("dilithium_stabilizer_side_inactive")
                        .withVerticalDefaultTopTexture("dilithium_top_inactive")
                        .withVerticalActiveSideTextures("dilithium_stabilizer_side_active")
                        .withVerticalActiveTopTexture("dilithium_top_active")
                        .withVerticalOverbrightSideTextures("stabilizer_overlay")
                        .withVerticalOverbrightTopTexture("dilithium_machine_overlay")
                )
                .build(new BlockDilithiumStabilizer("reinforced.dilithiumStabilizer", config.getInt("BlockIDs.dilithiumStabilizer"), Tier.REINFORCED, Material.metal));
        redstoneBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank","reinforced_blank","dilithium_top_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive",BlockSounds.METAL,5f,20f,1);
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setSideTextures("signalindustries:block/basic_booster_side_inactive")
                .setSouthTexture("signalindustries:block/basic_booster_top_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultSideTextures("basic_booster_side_inactive")
                        .withDefaultNorthTexture("basic_booster_top_inactive")
                        .withActiveSideTextures("basic_booster_side_active")
                        .withActiveNorthTexture("basic_booster_top_active")
                        .withOverbrightSideTextures("basic_booster_overlay")
                        .withOverbrightNorthTexture("basic_booster_overlay_top")
                )
                .build(new BlockDilithiumBooster("basic.booster", config.getInt("BlockIDs.redstoneBooster"), Tier.BASIC, Material.metal));
        dilithiumBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank","reinforced_blank","dilithium_top_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive",BlockSounds.METAL,5f,20f,1);
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSideTextures("signalindustries:block/dilithium_booster_side_inactive")
                .setSouthTexture("signalindustries:block/dilithium_top_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.REINFORCED)
                        .withDefaultSideTextures("dilithium_booster_side_inactive")
                        .withDefaultNorthTexture("dilithium_top_inactive")
                        .withActiveSideTextures("dilithium_booster_side_active")
                        .withActiveNorthTexture("dilithium_top_active")
                        .withOverbrightSideTextures("booster_overlay")
                        .withOverbrightNorthTexture("dilithium_machine_overlay")
                )
                .build(new BlockDilithiumBooster("reinforced.booster", config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED, Material.metal));
        awakenedBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank","reinforced_blank","dilithium_top_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive",BlockSounds.METAL,5f,20f,1);
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/awakened_blank")
                .setSideTextures("signalindustries:block/awakened_booster_side_inactive")
                .setSouthTexture("signalindustries:block/awakened_booster_top_inactive")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.AWAKENED)
                        .withDefaultSideTextures("awakened_booster_side_inactive")
                        .withDefaultNorthTexture("awakened_booster_top_inactive")
                        .withActiveSideTextures("awakened_booster_side_active")
                        .withActiveNorthTexture("awakened_booster_top_active")
                        .withOverbrightSideTextures("awakened_booster_overlay")
                        .withOverbrightNorthTexture("dilithium_machine_overlay")
                )
                .build(new BlockDilithiumBooster("awakened.booster", config.getInt("BlockIDs.awakenedBooster"), Tier.AWAKENED, Material.metal));
        prototypePump = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTextures("signalindustries:block/prototype_blank")
                .setTopTexture("signalindustries:block/prototype_pump_top_empty")
                .setSideTextures("signalindustries:block/prototype_pump_side_empty")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.PROTOTYPE)
                        .withDefaultSideTextures("prototype_pump_side_empty")
                        .withDefaultTopTexture("prototype_pump_top_empty")
                        .withActiveSideTextures("prototype_pump_side")
                        .withActiveTopTexture("prototype_pump_top")
                )
                .build(new BlockPump("prototype.pump", config.getInt("BlockIDs.prototypePump"), Tier.PROTOTYPE, Material.stone));
        basicPump = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setTopTexture("signalindustries:block/basic_pump_top_empty")
                .setSideTextures("signalindustries:block/basic_pump_side_empty")
                .build(new BlockPump("basic.pump", config.getInt("BlockIDs.basicPump"), Tier.BASIC, Material.metal));
        prototypeInserter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTextures("signalindustries:block/prototype_blank")
                .setNorthTexture("signalindustries:block/inserter_output")
                .setSouthTexture("signalindustries:block/inserter_input")
                .setBlockModel((block) -> new BlockModelVerticalMachine(block,Tier.PROTOTYPE)
                        .withDefaultNorthTexture("inserter_input")
                        .withDefaultSouthTexture("inserter_output")
                        .withVerticalDefaultTopTexture("inserter_input")
                        .withVerticalDefaultBottomTexture("inserter_output")
                )
                .build(new BlockInserter("prototype.inserter", config.getInt("BlockIDs.prototypeInserter"), Tier.PROTOTYPE, Material.stone));
        basicInserter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.METAL)
                .setTextures("signalindustries:block/basic_blank")
                .setNorthTexture("signalindustries:block/basic_inserter_output")
                .setSouthTexture("signalindustries:block/basic_inserter_input")
                .setBlockModel((block) -> new BlockModelVerticalMachine(block,Tier.BASIC)
                        .withDefaultNorthTexture("basic_inserter_input")
                        .withDefaultSouthTexture("basic_inserter_output")
                        .withVerticalDefaultTopTexture("basic_inserter_input")
                        .withVerticalDefaultBottomTexture("basic_inserter_output")
                )
                .build(new BlockInserter("basic.inserter", config.getInt("BlockIDs.basicInserter"), Tier.BASIC, Material.metal));
        prototypeFilter = new BlockBuilder(MOD_ID)
                .setHardness(1)
                .setResistance(3)
                .setBlockSound(BlockSounds.STONE)
                .setTopTexture("signalindustries:block/filter_red")
                .setSouthTexture("signalindustries:block/filter_green")
                .setEastTexture("signalindustries:block/filter_blue")
                .setWestTexture("signalindustries:block/filter_yellow")
                .setNorthTexture("signalindustries:block/filter_magenta")
                .setBottomTexture("signalindustries:block/filter_cyan")
                .build(new BlockFilter("prototype.filter", config.getInt("BlockIDs.prototypeFilter"), Tier.PROTOTYPE, Material.stone));
        basicAutomaticMiner = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .setTextures("signalindustries:block/basic_blank")
                .setSouthTexture("signalindustries:block/basic_automatic_miner")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.BASIC)
                        .withDefaultNorthTexture("basic_automatic_miner")
                        .withActiveNorthTexture("basic_automatic_miner")
                        .withOverbrightNorthTexture("auto_miner_overlay")
                )
                .build(new BlockAutoMiner("basic.automaticMiner", config.getInt("BlockIDs.basicAutomaticMiner"), Tier.BASIC, Material.metal));
        externalIo = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .setTextures("signalindustries:block/external_io_blank")
                .build(new BlockExternalIO("basic.externalIO", config.getInt("BlockIDs.externalIo"), Tier.BASIC, Material.metal));
        reinforcedExternalIo = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .setTextures("signalindustries:block/reinforced_external_io_blank")
                .build(new BlockExternalIO("reinforced.externalIO", config.getInt("BlockIDs.reinforcedExternalIo"), Tier.REINFORCED, Material.metal));
        reinforcedCentrifuge = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(1)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSouthTexture("signalindustries:block/reinforced_centrifuge_front_inactive")
                .setTopTexture("signalindustries:block/reinforced_centrifuge_empty")
                .setBlockModel((block) -> new BlockModelMachine(block, Tier.REINFORCED)
                        .withDefaultNorthTexture("reinforced_centrifuge_front_inactive")
                        .withDefaultTopTexture("reinforced_centrifuge_empty")
                        .withActiveNorthTexture("reinforced_centrifuge_front_active")
                        .withActiveTopTexture("reinforced_centrifuge_closed")
                        .withOverbrightNorthTexture("centrifuge_overlay")
                )
                .build(new BlockCentrifuge("reinforced.centrifuge", config.getInt("BlockIDs.reinforcedCentrifuge"), Tier.REINFORCED, Material.metal));
        reinforcedIgnitor = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(1)
                .setSideTextures("signalindustries:block/reinforced_ignitor_inactive")
                .setTopTexture("signalindustries:block/reinforced_ignitor_top_inactive")
                .setBottomTexture("signalindustries:block/reinforced_ignitor_bottom_inactive")
                .setBlockModel(BlockModelIgnitor::new)
                .build(new BlockIgnitor("reinforced.ignitor", config.getInt("BlockIDs.reinforcedIgnitor"), Tier.REINFORCED, Material.metal));
        signalumReactorCore = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(1)
                .setTextures("signalindustries:block/reinforced_blank")
                .setSideTextures("signalindustries:block/signalum_reactor_side_inactive")
                .setSouthTexture("signalindustries:block/signalum_reactor_front_inactive")
                .build(new BlockSignalumReactorCore("reinforced.signalumReactorCore", config.getInt("BlockIDs.signalumReactorCore"), Tier.REINFORCED, Material.metal));
        reinforcedEnergyConnector = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(1)
                .setTextures("signalindustries:block/reinforced_energy_connector")
                .build(new BlockEnergyConnector("reinforced.energyConnector", config.getInt("BlockIDs.reinforcedEnergyConnector"), Tier.REINFORCED, Material.metal));
        basicEnergyConnector = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(1)
                .setTextures("signalindustries:block/basic_energy_connector")
                .build(new BlockEnergyConnector("basic.energyConnector", config.getInt("BlockIDs.basicEnergyConnector"), Tier.BASIC, Material.metal));
        basicFluidInputHatch = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setTextures("signalindustries:block/basic_fluid_input_hatch")
                .build(new BlockFluidInputHatch("basic.fluidInputHatch", config.getInt("BlockIDs.basicFluidInputHatch"), Tier.BASIC, Material.metal));
        basicFluidOutputHatch = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setTextures("signalindustries:block/basic_fluid_output_hatch")
                .build(new BlockFluidOutputHatch("basic.fluidOutputHatch", config.getInt("BlockIDs.basicFluidOutputHatch"), Tier.BASIC, Material.metal));
        basicItemInputBus = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setTextures("signalindustries:block/basic_input_bus")
                .build(new BlockInputBus("basic.itemInputBus", config.getInt("BlockIDs.basicItemInputBus"), Tier.BASIC, Material.metal));
        basicItemOutputBus = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setTextures("signalindustries:block/basic_output_bus")
                .build(new BlockOutputBus("basic.itemOutputBus", config.getInt("BlockIDs.basicItemOutputBus"), Tier.BASIC, Material.metal));
        reinforcedFluidInputHatch = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setTextures("signalindustries:block/reinforced_fluid_input_hatch")
                .build(new BlockFluidInputHatch("reinforced.fluidInputHatch", config.getInt("BlockIDs.reinforcedFluidInputHatch"), Tier.REINFORCED, Material.metal));
        reinforcedFluidOutputHatch = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setTextures("signalindustries:block/reinforced_fluid_output_hatch")
                .build(new BlockFluidOutputHatch("reinforced.fluidOutputHatch", config.getInt("BlockIDs.reinforcedFluidOutputHatch"), Tier.REINFORCED, Material.metal));
        reinforcedItemInputBus = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setTextures("signalindustries:block/reinforced_input_bus")
                .build(new BlockInputBus("reinforced.itemInputBus", config.getInt("BlockIDs.reinforcedItemInputBus"), Tier.REINFORCED, Material.metal));
        reinforcedItemOutputBus = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setTextures("signalindustries:block/reinforced_output_bus")
                .build(new BlockOutputBus("reinforced.itemOutputBus", config.getInt("BlockIDs.reinforcedItemOutputBus"), Tier.REINFORCED, Material.metal));
        basicEnergyInjector = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setBlockModel(block ->
                        new DFBlockModelBuilder(MOD_ID)
                                .setBlockModel("basic_energy_injector.json")
                                .build(block))
                .build(new BlockEnergyInjector("basic.energyInjector", config.getInt("BlockIDs.basicEnergyInjector"), Tier.BASIC, Material.metal));
        basicSignalumDynamo = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setBlockModel(block ->
                        new DFBlockModelBuilder(MOD_ID)
                                .setBlockModel("signalum_dynamo.json")
                                .build(block))
                .build(new BlockSignalumDynamo("basic.dynamo", config.getInt("BlockIDs.basicSignalumDynamo"), Tier.BASIC, Material.metal));
        basicProgrammer = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(20)
                .setLuminance(0)
                .setBlockModel(
                        block ->
                                new DFBlockModelBuilder(MOD_ID)
                                        .setBlockModel("eeprom_programmer.json")
                                        .setBlockState("eeprom_programmer.json")
                                        .setMetaStateInterpreter(new EEPROMProgrammerStateInterpreter())
                                        .build(block)
                )
                .build(new BlockProgrammer("basic.programmer", config.getInt("BlockIDs.basicProgrammer"), Tier.BASIC, Material.metal));
        cobblestoneBricks = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.STONE)
                .setHardness(1)
                .setResistance(3)
                .setLuminance(0)
                .setTextures("signalindustries:block/cobblestone_bricks")
                .build(new Block("prototype.bricks", config.getInt("BlockIDs.cobblestoneBricks"), Material.stone));
        crystalAlloyBricks = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .setLuminance(0)
                .setTextures("signalindustries:block/crystal_alloy_bricks")
                .build(new Block("basic.bricks", config.getInt("BlockIDs.crystalAlloyBricks"), Material.metal));
        reinforcedCrystalAlloyBricks = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .setLuminance(0)
                .setTextures("signalindustries:block/reinforced_alloy_bricks")
                .build(new Block("reinforced.bricks", config.getInt("BlockIDs.reinforcedCrystalAlloyBricks"), Material.metal));
        signalumAlloyCoil = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .setLuminance(0)
                .setSouthTexture("signalindustries:block/signalum_alloy_coil")
                .setNorthTexture("signalindustries:block/signalum_alloy_coil")
                .setEastTexture("signalindustries:block/signalum_alloy_coil_2")
                .setWestTexture("signalindustries:block/signalum_alloy_coil_2")
                .setTopBottomTextures("signalindustries:block/signalum_alloy_coil_top")
                .build(new Block("signalumAlloyCoil", config.getInt("BlockIDs.signalumAlloyCoil"), Material.metal));
        dilithiumCoil = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .setLuminance(0)
                .setSideTextures("signalindustries:block/dilithium_coil")
                .setTopBottomTextures("signalindustries:block/dilithium_coil_top")
                .build(new Block("dilithiumCoil", config.getInt("BlockIDs.dilithiumCoil"), Material.metal));
        awakenedAlloyCoil = new BlockBuilder(MOD_ID)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .setLuminance(0)
                .setSouthTexture("signalindustries:block/awakened_alloy_coil")
                .setNorthTexture("signalindustries:block/awakened_alloy_coil")
                .setEastTexture("signalindustries:block/awakened_alloy_coil_2")
                .setWestTexture("signalindustries:block/awakened_alloy_coil_2")
                .setTopBottomTextures("signalindustries:block/awakened_alloy_coil_top")
                .build(new Block("awakenedAlloyCoil", config.getInt("BlockIDs.awakenedAlloyCoil"), Material.metal));

        portalEternity = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/reality_fabric")
                .setBlockSound(BlockSounds.GLASS)
                .setLuminance(1)
                .build(new BlockPortal("eternityPortal", config.getInt("BlockIDs.portalEternity"), config.getInt("Other.eternityDimId"),Block.bedrock.id,Block.fire.id));

        realityFabric = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/reality_fabric")
                .setBlockSound(BlockSounds.STONE)
                .setHardness(150)
                .setResistance(50000)
                .setLuminance(0)
                .build(new BlockUndroppable("realityFabric", config.getInt("BlockIDs.realityFabric"),Material.stone));

        rootedFabric = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/rooted_fabric")
                .setBlockSound(BlockSounds.STONE)
                .setHardness(50)
                .setResistance(50000)
                .setLuminance(0)
                .build(new Block("rootedFabric", config.getInt("BlockIDs.rootedFabric"),Material.stone).withTags(BlockTags.MINEABLE_BY_PICKAXE));

        dilithiumRail = new BlockBuilder(MOD_ID)
                .setLuminance(0)
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(50)
                .setTextures("signalindustries:block/dilithium_rail_unpowered")
                .setBlockModel(BlockModelDilithiumRail::new)
                .build(new BlockDilithiumRail("dilithiumRail", config.getInt("BlockIDs.dilithiumRail"),true));

        eternalTreeLog = new BlockBuilder(MOD_ID)
                .setHardness(75f)
                .setResistance(50000)
                .setLuminance(12)
                .setTopBottomTextures("signalindustries:block/eternal_tree_log_top")
                .setSideTextures("signalindustries:block/eternal_tree_log")
                .setBlockSound(BlockSounds.WOOD)
                .build(new BlockEternalTreeLog("eternalTreeLog", config.getInt("BlockIDs.eternalTreeLog"),Material.wood));

        fueledEternalTreeLog = new BlockBuilder(MOD_ID)
                .setUnbreakable()
                .setResistance(18000000)
                .setLuminance(15)
                .setTopBottomTextures("signalindustries:block/fueled_eternal_tree_log_top")
                .setSideTextures("signalindustries:block/fueled_eternal_tree_log")
                .setBlockSound(BlockSounds.WOOD)
                .build(new BlockEternalTreeLog("fueledEternalTreeLog", config.getInt("BlockIDs.fueledEternalTreeLog"),Material.wood));

        glowingObsidian = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/glowing_obsidian")
                .setBlockSound(BlockSounds.STONE)
                .setHardness(2)
                .setResistance(1200)
                .setLuminance(10)
                .build(new BlockGlowingObsidian("glowingObsidian", config.getInt("BlockIDs.glowingObsidian"),Material.stone));

        uvLamp = new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/uv_lamp_inactive")
                .setBlockSound(BlockSounds.METAL)
                .setHardness(1)
                .setResistance(3)
                .build(new BlockUVLamp("uvLamp", config.getInt("BlockIDs.uvLamp"),Material.metal));

            /*public static final Block recipeMaker = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/prototype_connection")
            .setLuminance(0)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.STONE)
            .build(new BlockRecipeMaker("recipeMaker",config.getInt("BlockIDs.recipeMaker"),Material.stone));*/

        energyFlowing = (BlockFluid) new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/signalum_energy_transparent")
                .setBlockModel(BlockModelFluid::new)
                .build(new BlockFluidFlowing("signalumEnergy", config.getInt("BlockIDs.energyFlowing"), Material.water).withTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.PLACE_OVERWRITES));
        energyStill = (BlockFluid) new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/signalum_energy_transparent")
                .setBlockModel(BlockModelFluid::new)
                .build(new BlockFluidFlowing("signalumEnergy", config.getInt("BlockIDs.energyStill"), Material.water).withTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.PLACE_OVERWRITES));
        burntSignalumFlowing = (BlockFluid) new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/burnt_signalum")
                .setBlockModel(BlockModelFluid::new)
                .build(new BlockFluidFlowing("burntSignalum", config.getInt("BlockIDs.burntSignalumFlowing"), Material.water).withTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.PLACE_OVERWRITES));
        burntSignalumStill = (BlockFluid) new BlockBuilder(MOD_ID)
                .setTextures("signalindustries:block/burnt_signalum")
                .setBlockModel(BlockModelFluid::new)
                .build(new BlockFluidStill("burntSignalum", config.getInt("BlockIDs.burntSignalumStill"), Material.water).withTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.PLACE_OVERWRITES));

        setInitialized(true);
    }
}