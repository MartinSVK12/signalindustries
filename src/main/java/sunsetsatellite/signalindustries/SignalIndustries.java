package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.SnowballRenderer;
import net.minecraft.client.render.model.ModelZombie;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.fx.EntityFX;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.mixin.accessors.PacketAccessor;
import sunsetsatellite.signalindustries.abilities.powersuit.*;
import sunsetsatellite.signalindustries.blocks.*;
import sunsetsatellite.signalindustries.dim.WorldTypeEternity;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;
import sunsetsatellite.signalindustries.entities.mob.EntityInfernal;
import sunsetsatellite.signalindustries.gui.*;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.inventories.*;
import sunsetsatellite.signalindustries.items.*;
import sunsetsatellite.signalindustries.items.abilities.ItemWithAbility;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;
import sunsetsatellite.signalindustries.render.RenderAutoMiner;
import sunsetsatellite.signalindustries.render.RenderFluidInBlock;
import sunsetsatellite.signalindustries.render.RenderFluidInConduit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.BlockTexture;
import sunsetsatellite.signalindustries.util.Mode;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.weather.WeatherBloodMoon;
import sunsetsatellite.signalindustries.weather.WeatherEclipse;
import sunsetsatellite.signalindustries.weather.WeatherSolarApocalypse;
import sunsetsatellite.sunsetutils.util.Config;
import sunsetsatellite.sunsetutils.util.NBTEditCommand;
import sunsetsatellite.sunsetutils.util.multiblocks.Multiblock;
import sunsetsatellite.sunsetutils.util.multiblocks.RenderMultiblock;
import sunsetsatellite.sunsetutils.util.multiblocks.Structure;
import sunsetsatellite.sunsetutils.util.multiblocks.StructureCommand;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.achievements.AchievementPage;

import java.util.*;

public class SignalIndustries implements ModInitializer {

    private static int availableBlockId = 1200;
    private static int availableItemId = 17100;

    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Config config = new Config(MOD_ID, mapOf(new String[]{"PacketOpenMachineGUI_ID","PacketPipeItemSpawn_ID","GuiID"},new String[]{"113","114","9"}), new Class[]{SignalIndustries.class});

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();
    public static final Block signalumOre = BlockHelper.createBlock(MOD_ID,new BlockOreSignalum(key("signalumOre"),config.getFromConfig("signalumOre",availableBlockId++)).withTags(BlockTags.MINEABLE_BY_PICKAXE),"signalum_ore.png",BlockSounds.STONE,3.0f,25.0f,1);
    public static final Block dilithiumOre = BlockHelper.createBlock(MOD_ID,new BlockOreDilithium(key("dilithiumOre"),config.getFromConfig("dilithiumOre",availableBlockId++)).withTags(BlockTags.MINEABLE_BY_PICKAXE),"dilithium_ore.png",BlockSounds.STONE,75.0f,100.0f,1);
    public static final Block dimensionalShardOre = BlockHelper.createBlock(MOD_ID,new BlockOreDimensionalShard(key("dimensionalShardOre"),config.getFromConfig("dimensionalShardOre",availableBlockId++)).withTags(BlockTags.MINEABLE_BY_PICKAXE),"dimensional_shard_ore.png",BlockSounds.STONE,200f,50000f,1);

    public static final Block dilithiumBlock = new BlockBuilder(MOD_ID)
            .setTextures("dilithium_block.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(20)
            .setResistance(1000)
            .build(new Block("dilithiumBlock",config.getFromConfig("dilithiumBlock",availableBlockId++),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block emptyCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("empty_crystal_block.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(12)
            .setResistance(1000)
            .build(new Block("emptyCrystalBlock",config.getFromConfig("emptyCrystalBlock",availableBlockId++),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block rawCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("saturated_crystal_block.png")
            .setLuminance(1).setBlockSound(BlockSounds.GLASS)
            .setHardness(24).setResistance(50000)
            .build(new Block("rawCrystalBlock",config.getFromConfig("rawCrystalBlock",availableBlockId++),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block awakenedSignalumCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("awakened_crystal_block.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(50)
            .setResistance(1000000)
            .build(new Block("awakenedSignalumCrystalBlock",config.getFromConfig("awakenedSignalumCrystalBlock",availableBlockId++),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block dilithiumCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("dilithium_crystal_block.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(20)
            .setResistance(1000)
            .build(new BlockDilithiumCrystal("dilithiumCrystalBlock",config.getFromConfig("dilithiumCrystalBlock",availableBlockId++),Material.glass,false)).withTags(BlockTags.MINEABLE_BY_PICKAXE);

    public static final Block prototypeMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(key("prototype.machine"),config.getFromConfig("prototypeMachineCore",availableBlockId++), Tier.PROTOTYPE,Material.stone),"machine_prototype.png",BlockSounds.STONE,2.0f,3.0f,0);
    public static final Block basicMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(key("basic.machine"),config.getFromConfig("basicMachineCore",availableBlockId++), Tier.BASIC,Material.stone),"machine_basic.png",BlockSounds.STONE,3.0f,8.0f,1.0f/2.0f);
    public static final Block reinforcedMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(key("reinforced.machine"),config.getFromConfig("reinforcedMachineCore",availableBlockId++), Tier.REINFORCED,Material.stone),"machine_reinforced.png",BlockSounds.STONE,4.0f,15.0f,1.0f/1.50f);
    public static final Block awakenedMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(key("awakened.machine"),config.getFromConfig("awakenedMachineCore",availableBlockId++), Tier.AWAKENED,Material.stone),"machine_awakened.png",BlockSounds.STONE,5.0f,50.0f,1);

    public static final Block reinforcedCasing = new BlockBuilder(MOD_ID)
            .setTextures("reinforced_casing.png")
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(10)
            .setResistance(2000)
            .build(new Block("reinforced.casing",config.getFromConfig("reinforcedCasing",availableBlockId++),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block reinforcedGlass = new BlockBuilder(MOD_ID)
            .setTextures("reinforced_glass.png")
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(4)
            .setResistance(2000)
            .build(new BlockConnectedTextureCursed("reinforced.glass",config.getFromConfig("reinforcedGlass",availableBlockId++),Material.metal,"reinforced_glass").withTags(BlockTags.MINEABLE_BY_PICKAXE));



    public static final Block prototypeConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(key("prototype.conduit"),config.getFromConfig("prototypeConduit",availableBlockId++), Tier.PROTOTYPE, Material.glass),"conduit_prototype.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block basicConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(key("basic.conduit"),config.getFromConfig("basicConduit",availableBlockId++), Tier.BASIC,Material.glass),"conduit_basic.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block reinforcedConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(key("reinforced.conduit"),config.getFromConfig("reinforcedConduit",availableBlockId++), Tier.REINFORCED,Material.glass),"conduit_reinforced.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block awakenedConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(key("awakened.conduit"),config.getFromConfig("awakenedConduit",availableBlockId++), Tier.AWAKENED,Material.glass),"conduit_awakened.png",BlockSounds.GLASS,1.0f,1.0f,0);

    public static final Block prototypeFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(key("prototype.conduit.fluid"),config.getFromConfig("prototypeFluidConduit",availableBlockId++), Tier.PROTOTYPE,Material.glass),"fluid_pipe_prototype.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block basicFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(key("basic.conduit.fluid"),config.getFromConfig("basicFluidConduit",availableBlockId++), Tier.BASIC,Material.glass),"fluid_pipe_basic.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block reinforcedFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(key("reinforced.conduit.fluid"),config.getFromConfig("reinforcedFluidConduit",availableBlockId++), Tier.REINFORCED,Material.glass),"fluid_pipe_reinforced.png",BlockSounds.GLASS,1.0f,1.0f,0);

    public static final Block infiniteEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(key("infinite.energyCell"),config.getFromConfig("infiniteEnergyCell",availableBlockId++), Tier.INFINITE,Material.glass),"cell_prototype.png",BlockSounds.GLASS,-1.0f,1000000.0f,0);
    public static final Block prototypeEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(key("prototype.energyCell"),config.getFromConfig("prototypeEnergyCell",availableBlockId++), Tier.PROTOTYPE,Material.glass),"cell_prototype.png",BlockSounds.GLASS,2.0f,5.0f,0);
    public static final Block basicEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(key("basic.energyCell"),config.getFromConfig("basicEnergyCell",availableBlockId++), Tier.BASIC,Material.glass),"cell_basic.png",BlockSounds.GLASS,2.0f,5.0f,0);

    public static final Block prototypeFluidTank = BlockHelper.createBlock(MOD_ID,new BlockSIFluidTank(key("prototype.fluidTank"),config.getFromConfig("prototypeFluidTank",availableBlockId++), Tier.PROTOTYPE,Material.glass),"fluid_tank_prototype.png",BlockSounds.GLASS,2.0f,5.0f,0);

    public static final Block recipeMaker = BlockHelper.createBlock(MOD_ID,new BlockRecipeMaker(key("recipeMaker"),config.getFromConfig("recipeMaker",availableBlockId++),Material.stone),"prototype_connection.png",BlockSounds.STONE,2.0f,5.0f,0);


    public static final Block prototypeExtractor = BlockHelper.createBlock(MOD_ID,new BlockExtractor(key("prototype.extractor"),config.getFromConfig("prototypeExtractor",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototype_blank.png","extractor_prototype_side_empty.png",BlockSounds.STONE,2,3,0);
    public static final Block basicExtractor = BlockHelper.createBlock(MOD_ID,new BlockExtractor(key("basic.extractor"),config.getFromConfig("basicExtractor",availableBlockId++), Tier.BASIC,Material.stone),"basic_blank.png","extractor_basic_side_empty.png",BlockSounds.STONE,2,3,0);

    public static final Block prototypeCrusher = BlockHelper.createBlock(MOD_ID,new BlockCrusher(key("prototype.crusher"),config.getFromConfig("prototypeCrusher",availableBlockId++), Tier.PROTOTYPE,Material.stone),"crusher_prototype_top_inactive.png","prototype_blank.png","crusher_prototype_side.png","prototype_blank.png","prototype_blank.png","prototype_blank.png",BlockSounds.STONE,2,3,0);
    public static final Block basicCrusher = BlockHelper.createBlock(MOD_ID,new BlockCrusher(key("basic.crusher"),config.getFromConfig("basicCrusher",availableBlockId++), Tier.BASIC,Material.metal),"crusher_basic_top_inactive.png","basic_blank.png","crusher_basic_side.png","basic_blank.png","basic_blank.png","basic_blank.png",BlockSounds.METAL,2,3,0);

    public static final Block prototypeAlloySmelter = BlockHelper.createBlock(MOD_ID,new BlockAlloySmelter(key("prototype.alloySmelter"),config.getFromConfig("prototypeAlloySmelter",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototype_blank.png","prototype_blank.png","alloy_smelter_prototype_inactive.png","prototype_blank.png","prototype_blank.png","prototype_blank.png",BlockSounds.STONE,2,3,0);
    public static final Block basicAlloySmelter = BlockHelper.createBlock(MOD_ID,new BlockAlloySmelter(key("basic.alloySmelter"),config.getFromConfig("basicAlloySmelter",availableBlockId++), Tier.BASIC,Material.metal),"basic_blank.png","basic_blank.png","alloy_smelter_basic_inactive.png","basic_blank.png","basic_blank.png","basic_blank.png",BlockSounds.STONE,2,3,0);

    public static final Block prototypePlateFormer = BlockHelper.createBlock(MOD_ID,new BlockPlateFormer(key("prototype.plateFormer"),config.getFromConfig("prototypePlateFormer",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototype_blank.png","prototype_blank.png","plate_former_prototype_inactive.png","prototype_blank.png","prototype_blank.png","prototype_blank.png",BlockSounds.STONE,2,3,0);

    public static final Block prototypeCrystalCutter = BlockHelper.createBlock(MOD_ID,new BlockCrystalCutter(key("prototype.crystalCutter"),config.getFromConfig("prototypeCrystalCutter",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototype_blank.png","prototype_blank.png","crystal_cutter_prototype_inactive.png","prototype_blank.png","prototype_blank.png","prototype_blank.png",BlockSounds.STONE,2,3,0);

    public static final Block basicCrystalChamber = BlockHelper.createBlock(MOD_ID,new BlockCrystalChamber(key("basic.crystalChamber"),config.getFromConfig("basicCrystalChamber",availableBlockId++), Tier.BASIC,Material.stone),"basic_blank.png","basic_blank.png","basic_crystal_chamber_side_inactive.png","basic_blank.png","basic_blank.png","basic_blank.png",BlockSounds.STONE,2,3,0);


    public static final Block basicInfuser = BlockHelper.createBlock(MOD_ID,new BlockInfuser(key("basic.infuser"),config.getFromConfig("basic.infuser",availableBlockId++), Tier.BASIC,Material.metal),"basic_blank.png","infuser_basic_side_inactive.png",BlockSounds.METAL,2,3,0);

    public static final Block basicWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(key("basic.wrathBeacon"),config.getFromConfig("basicWrathBeacon",availableBlockId++), Tier.BASIC,Material.metal),"basic_blank.png","wrath_beacon.png",BlockSounds.METAL,10f,500f,1);
    //public static final Block reinforcedWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(""),config.getFromConfig("reinforcedWrathBeacon",availableBlockId++),Tiers.REINFORCED,Material.metal),"reinforced.wrathBeacon","reinforced_blank.png","reinforced_wrath_beacon_active.png",BlockSounds.METAL,25f,500f,1);
    //public static final Block awakenedWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(""),config.getFromConfig("awakenedWrathBeacon",availableBlockId++),Tiers.AWAKENED,Material.metal),"awakened.wrathBeacon","reinforced_blank.png","awakened_wrath_beacon_active.png",BlockSounds.METAL,25f,500f,1);
    public static final int[][] wrathBeaconTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"wrath_beacon.png"),TextureHelper.registerBlockTexture(MOD_ID,"wrath_beacon_active.png")};

    public static final Block dimensionalAnchor = BlockHelper.createBlock(MOD_ID,new BlockDimensionalAnchor(key("reinforced.dimensionalAnchor"),config.getFromConfig("dimensionalAnchor",availableBlockId++), Tier.REINFORCED,Material.metal),"dimensional_anchor_top_inactive.png","reinforced_blank.png","dimensional_anchor_inactive.png",BlockSounds.METAL,5f,20f,1);

    public static final Block dilithiumStabilizer = BlockHelper.createBlock(MOD_ID,new BlockDilithiumStabilizer(key("reinforced.dilithiumStabilizer"),config.getFromConfig("dilithiumStabilizer",availableBlockId++), Tier.REINFORCED,Material.metal),"reinforced_blank.png","reinforced_blank.png","dilithium_top_inactive.png","dilithium_stabilizer_side_inactive.png","dilithium_stabilizer_side_inactive.png","dilithium_stabilizer_side_inactive.png",BlockSounds.METAL,5f,20f,1);

    public static final Block dilithiumBooster = BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster(key("reinforced.dilithiumBooster"),config.getFromConfig("dilithiumBooster",availableBlockId++), Tier.REINFORCED,Material.metal),"reinforced_blank.png","reinforced_blank.png","dilithium_top_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png",BlockSounds.METAL,5f,20f,1);

    public static final Block prototypePump = BlockHelper.createBlock(MOD_ID,new BlockPump(key("prototype.pump"),config.getFromConfig("prototypePump",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototype_pump_top_empty.png","prototype_blank.png","prototype_pump_side_empty.png","prototype_pump_side_empty.png","prototype_pump_side_empty.png","prototype_pump_side_empty.png",BlockSounds.STONE,2,3,0);


    public static final Block prototypeBlockBreaker = BlockHelper.createBlock(MOD_ID, new BlockBreaker(key("prototype.blockBreaker"),config.getFromConfig("prototypeBlockBreaker",availableBlockId++),Tier.PROTOTYPE,Material.stone), "prototype_block_breaker_side_2.png", "prototype_block_breaker_side_2.png", "prototype_block_breaker.png", "prototype_block_breaker_side.png", "prototype_blank.png", "prototype_block_breaker_side.png", BlockSounds.STONE, 2f,3f,0f);
    public static final int[][] breakerTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"prototype_block_breaker.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototype_block_breaker_active.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototype_block_breaker_side.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototype_block_breaker_side_active.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototype_block_breaker_side_2.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototype_block_breaker_side_2_active.png"),TextureHelper.registerBlockTexture(MOD_ID,"inserteroutput.png")};
    public static final Block basicAutomaticMiner = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(2)
            .setResistance(3)
            .setTextures("basic_blank.png")
            .setNorthTexture("basic_automatic_miner.png")
            .build(new BlockAutoMiner("basic.automaticMiner",config.getFromConfig("basicAutomaticMiner",availableBlockId++),Tier.BASIC,Material.metal));
    public static final Block externalIo = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(2)
            .setResistance(3)
            .setTextures("external_io_blank.png")
            .build(new BlockExternalIO("basic.externalIO",config.getFromConfig("externalIo",availableBlockId++),Tier.BASIC,Material.metal));

    public static final Block reinforcedCentrifuge = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(5)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("reinforced_blank.png")
            .setNorthTexture("reinforced_centrifuge_front_inactive.png")
            .setTopTexture("reinforced_centrifuge_empty.png")
            .build(new BlockCentrifuge("reinforced.centrifuge",config.getFromConfig("reinforcedCentrifuge",availableBlockId++),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedIgnitor = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(5)
            .setResistance(20)
            .setLuminance(1)
            .setSideTextures("reinforced_ignitor_inactive.png")
            .setTopTexture("reinforced_ignitor_top_inactive.png")
            .setBottomTexture("reinforced_ignitor_bottom_inactive.png")
            .build(new BlockIgnitor("reinforced.ignitor",config.getFromConfig("reinforcedIgnitor",availableBlockId++),Tier.REINFORCED,Material.metal));

    public static final Block signalumReactorCore = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(5)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("reinforced_blank.png")
            .setSideTextures("signalum_reactor_side_inactive.png")
            .setNorthTexture("signalum_reactor_front_inactive.png")
            .build(new BlockSignalumReactorCore("reinforced.signalumReactorCore",config.getFromConfig("signalumReactorCore",availableBlockId++),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedEnergyContainer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(5)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("reinforced_energy_connector.png")
            .build(new BlockEnergyConnector("reinforced.energyConnector",config.getFromConfig("reinforcedEnergyConnector",availableBlockId++),Tier.REINFORCED,Material.metal));

    //this has to be after any other block
    public static final int[] energyTex = TextureHelper.registerBlockTexture(MOD_ID,"signalum_energy.png");
    public static final int[] burntSignalumTex = TextureHelper.registerBlockTexture(MOD_ID,"burnt_signalum.png");//registerFluidTexture(MOD_ID,"signalum_energy.png",0,4);
    public static final Block energyFlowing = BlockHelper.createBlock(MOD_ID,new BlockFluidFlowing(key("signalumEnergy"),config.getFromConfig("signalumEnergy",availableBlockId++),Material.water),"signalum_energy.png",BlockSounds.DEFAULT,1.0f,1.0f,0).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES);
    public static final Block energyStill = BlockHelper.createBlock(MOD_ID,new BlockFluidStill(key("signalumEnergy"),config.getFromConfig("signalumEnergy",availableBlockId++),Material.water),"signalum_energy.png",BlockSounds.DEFAULT,1.0f,1.0f,0).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES);

    public static final Block burntSignalumFlowing = new BlockBuilder(MOD_ID)
            .setTextures("burnt_signalum.png")
            .build(new BlockFluidFlowing(key("burntSignalum"),config.getFromConfig("burntSignalum",availableBlockId++),Material.water).withTexCoords(burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));
    public static final Block burntSignalumStill = new BlockBuilder(MOD_ID)
            .setTextures("burnt_signalum.png")
            .build(new BlockFluidStill(key("burntSignalum"),config.getFromConfig("burntSignalum",availableBlockId++),Material.water).withTexCoords(burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));

    public static final Item signalumCrystalEmpty = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(config.getFromConfig("signalumCrystalEmpty",availableItemId++)),"signalumCrystalEmpty","signalumcrystalempty.png").setMaxStackSize(1);
    public static final Item signalumCrystal = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(config.getFromConfig("signalumCrystal",availableItemId++)),"signalumCrystal","signalumcrystal.png").setMaxStackSize(1);
    public static final Item rawSignalumCrystal = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("rawSignalumCrystal",availableItemId++)),"rawSignalumCrystal","rawsignalumcrystal.png");

    public static final Item awakenedSignalumCrystal = ItemHelper.createItem(MOD_ID, new Item(config.getFromConfig("awakenedSignalumCrystal",availableItemId++)),"awakenedSignalumCrystal","awakenedsignalumcrystal.png").setMaxStackSize(1);
    public static final Item awakenedSignalumFragment = ItemHelper.createItem(MOD_ID, new Item(config.getFromConfig("awakenedSignalumFragment",availableItemId++)),"awakenedSignalumFragment","awakenedsignalumfragment.png");

    public static final Item coalDust = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("coalDust",availableItemId++)),"coalDust","coaldust.png");
    public static final Item netherCoalDust = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("netherCoalDust",availableItemId++)),"netherCoalDust","nethercoaldust.png");
    public static final Item emptySignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("emptySignalumCrystalDust",availableItemId++)),"signalumCrystalDust","emptysignalumdust.png");
    public static final Item saturatedSignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("saturatedSignalumCrystalDust",availableItemId++)),"saturatedSignalumCrystalDust","saturatedsignalumdust.png");

    public static final Item ironPlateHammer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("ironPlateHammer",availableItemId++)),"ironPlateHammer","platehammer.png");

    public static final Item cobblestonePlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("cobblestonePlate",availableItemId++)),"cobblestonePlate","cobblestoneplate.png");
    public static final Item stonePlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("stonePlate",availableItemId++)),"stonePlate","stoneplate.png");
    public static final Item crystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("crystalAlloyPlate",availableItemId++)),"crystalAlloyPlate","crystalalloyplate.png");
    public static final Item steelPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("steelPlate",availableItemId++)),"steelPlate","steelplate.png");
    public static final Item reinforcedCrystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("reinforcedCrystalAlloyPlate",availableItemId++)),"reinforcedCrystalAlloyPlate","reinforcedcrystalalloyplate.png");
    public static final Item saturatedSignalumAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("saturatedSignalumAlloyPlate",availableItemId++)),"saturatedSignalumAlloyPlate","saturatedsignalumalloyplate.png");
    public static final Item dilithiumPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("dilithiumPlate",availableItemId++)),"dilithiumPlate","dilithiumplate.png");


    public static final Item crystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("crystalAlloyIngot",availableItemId++)),"crystalAlloyIngot","crystalalloy.png");
    public static final Item reinforcedCrystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("reinforcedCrystalAlloyIngot",availableItemId++)),"reinforcedCrystalAlloyIngot","reinforcedcrystalalloy.png");
    public static final Item saturatedSignalumAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("saturatedSignalumAlloyIngot",availableItemId++)),"saturatedSignalumAlloyIngot","saturatedsignalumalloy.png");

    public static final Item diamondCuttingGear = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("diamondCuttingGear",availableItemId++)),"diamondCuttingGear","diamondcuttinggear.png");

    public static final Block portalEternity = BlockHelper.createBlock(MOD_ID,new BlockPortal(key("eternityPortal"),availableBlockId++,3,Block.bedrock.id,Block.fire.id),"reality_fabric.png",BlockSounds.GLASS,1.0f,1.0f,1);
    public static final Block realityFabric = BlockHelper.createBlock(MOD_ID,new BlockUndroppable(key("realityFabric"),config.getFromConfig("realityFabric",availableBlockId++),Material.dirt),"reality_fabric.png",BlockSounds.STONE,150f,50000f,0);
    public static final Block rootedFabric = BlockHelper.createBlock(MOD_ID,new Block(key("rootedFabric"),config.getFromConfig("rootedFabric",availableBlockId++),Material.dirt),"rooted_fabric.png",BlockSounds.STONE,50f,50000f,0);

    public static final Item monsterShard = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("monsterShard",availableItemId++)),"monsterShard","monstershard.png");
    public static final Item infernalFragment = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("infernalFragment",availableItemId++)),"infernalFragment","infernalfragment.png");
    public static final Item evilCatalyst = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("evilCatalyst",availableItemId++)),"evilCatalyst","evilcatalyst.png").setMaxStackSize(4);
    public static final Item infernalEye = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("infernalEye",availableItemId++)),"infernalEye","infernaleye.png").setMaxStackSize(4);
    public static final Item dimensionalShard = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("dimensionalShard",availableItemId++)),"dimensionalShard","dimensionalshard.png");
    public static final Item warpOrb = ItemHelper.createItem(MOD_ID,new ItemWarpOrb(config.getFromConfig("warpOrb",availableItemId++)),"warpOrb","warporb.png").setMaxStackSize(1);
    public static final Item realityString = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("realityString",availableItemId++)),"realityString","stringofreality.png");
    public static final Item dilithiumShard = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("dilithiumShard",availableItemId++)),"dilithiumShard","dilithiumshard.png");

    public static final Block eternalTreeLog = new BlockBuilder(MOD_ID)
            .setHardness(75f)
            .setResistance(50000)
            .setLuminance(15)
            .setTopBottomTexture("eternal_tree_log_top.png")
            .setSides("eternal_tree_log.png")
            .setBlockSound(BlockSounds.WOOD)
            .build(new BlockEternalTreeLog("eternalTreeLog",config.getFromConfig("eternalTreeLog",availableBlockId++),Material.wood));//BlockHelper.createBlock(MOD_ID,new BlockEternalTreeLog(key("eternalTreeLog"),config.getFromConfig("eternalTreeLog",availableBlockId++),Material.wood),"eternal_tree_log_top.png","eternal_tree_log.png",BlockSounds.WOOD, 75f,50000f,1);

    public static final Block glowingObsidian = BlockHelper.createBlock(MOD_ID,new Block(key("glowingObsidian"),config.getFromConfig("glowingObsidian",availableBlockId++),Material.stone),"glowing_obsidian.png",BlockSounds.STONE, 50f,1200f,1.0f/2.0f);

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial("signalumprototypeharness",1200,10,10,10,10);
    public static final ArmorMaterial armorSignalumPowerSuit = ArmorHelper.createArmorMaterial("signalumpowersuit",9999,50,50,50,50);

    public static final ItemArmorTiered signalumPrototypeHarness = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness("basic.prototypeHarness",config.getFromConfig("prototypeHarness",availableItemId++),armorPrototypeHarness,1, Tier.BASIC),"basic.prototypeHarness","harness.png");
    public static final ItemArmorTiered signalumPrototypeHarnessGoggles = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness("basic.prototypeHarnessGoggles",config.getFromConfig("prototypeHarnessGoggles",availableItemId++),armorPrototypeHarness,0, Tier.BASIC),"basic.prototypeHarnessGoggles","goggles.png");

    public static final ToolMaterial toolMaterialBasic = new ToolMaterial().setDurability(9999).setMiningLevel(3).setEfficiency(25,50);
    public static final ToolMaterial toolMaterialReinforced = new ToolMaterial().setDurability(9999).setMiningLevel(4).setEfficiency(45,80);
    public static final ToolMaterial toolMaterialAwakened = new ToolMaterial().setDurability(9999).setMiningLevel(5).setEfficiency(60,100);

    public static final Item basicSignalumDrill = ItemHelper.createItem(MOD_ID,new ItemSignalumDrill("basic.signalumDrill",config.getFromConfig("basicSignalumDrill",availableItemId++),Tier.BASIC,toolMaterialBasic),"basic.signalumDrill","signalum_drill.png");
    public static final Item reinforcedSignalumDrill = ItemHelper.createItem(MOD_ID,new ItemSignalumDrill("reinforced.signalumDrill",config.getFromConfig("reinforcedSignalumDrill",availableItemId++),Tier.REINFORCED,toolMaterialReinforced),"reinforced.signalumDrill","signalum_drill_reinforced.png");

    public static final Item fuelCell = ItemHelper.createItem(MOD_ID,new ItemFuelCell(config.getFromConfig("basicSignalumDrill",availableItemId++)),"fuelCell","fuelcellempty.png").setMaxStackSize(1);
    public static final int[][] fuelCellTex = new int[][]{TextureHelper.getOrCreateItemTexture(MOD_ID,"fuelcellempty.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"fuelcellfilled.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"fuelcelldepleted.png")};


    public static final Item nullTrigger = ItemHelper.createItem(MOD_ID,new ItemTrigger(config.getFromConfig("triggerNull",availableItemId++)),"triggerNull","trigger.png").setMaxStackSize(1);

    public static final Item romChipProjectile = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("romChipProjectile",availableItemId++)),"romChipProjectile","chip1.png");
    public static final Item romChipBoost = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("romChipBoost",availableItemId++)),"romChipBoost","chip2.png");

    public static final Item energyCatalyst = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("energyCatalyst",availableItemId++)),"energyCatalyst","energycatalyst.png");

    public static final Item signalumSaber = ItemHelper.createItem(MOD_ID, new ItemSignalumSaber("signalumSaber",config.getFromConfig("signalumSaber",availableItemId++), Tier.REINFORCED, ToolMaterial.stone), "signalumSaber", "signalumsaberunpowered.png");
    public static final int[][] saberTex = new int[][]{TextureHelper.getOrCreateItemTexture(MOD_ID,"signalumsaberunpowered.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"signalumsaber.png")};

    public static final Item pulsar = ItemHelper.createItem(MOD_ID,new ItemPulsar(config.getFromConfig("pulsar",availableItemId++), Tier.REINFORCED),"pulsar","pulsaractive.png").setMaxStackSize(1);
    public static final int[][] pulsarTex = new int[][]{TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsarinactive.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsaractive.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsarcharged.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsarwarpactive.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsarwarpcharged.png")};

    public static final ItemSignalumPowerSuit signalumPowerSuitHelmet = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.helmet",config.getFromConfig("signalumPowerSuitHelmet",availableItemId++),armorSignalumPowerSuit,0,Tier.REINFORCED),"reinforced.signalumpowersuit.helmet","signalumpowersuit_helmet.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitChestplate = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.chestplate",config.getFromConfig("signalumPowerSuitChestplate",availableItemId++),armorSignalumPowerSuit,1,Tier.REINFORCED),"reinforced.signalumpowersuit.chestplate","signalumpowersuit_chestplate.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitLeggings = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.leggings",config.getFromConfig("signalumPowerSuitChestplate",availableItemId++),armorSignalumPowerSuit,2,Tier.REINFORCED),"reinforced.signalumpowersuit.leggings","signalumpowersuit_leggings.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitBoots = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.boots",config.getFromConfig("signalumPowerSuitChestplate",availableItemId++),armorSignalumPowerSuit,3,Tier.REINFORCED),"reinforced.signalumpowersuit.boots","signalumpowersuit_boots.png");

    public static final Item testingAttachment = ItemHelper.createItem(MOD_ID,new ItemAttachment(config.getFromConfig("testingAttachment",availableItemId++), listOf(AttachmentPoint.ANY)),"attachment.testingAttachment","energyorb.png");

    public static SuitBaseAbility testAbility = new TestingAbility();
    public static SuitBaseEffectAbility testEffectAbility = new TestingEffectAbility();
    public static SuitBaseEffectAbility clockworkAbility = new ClockworkAbility();

    public static final Item testingAbility = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getFromConfig("testingAbilityItem",availableItemId++),testEffectAbility),"testingAbilityItem","testingability.png");
    public static final Item clockworkAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getFromConfig("clockworkAbilityContainer",availableItemId++),clockworkAbility),"clockworkAbilityContainer","clockworkability.png");


    public static final Item abilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule(config.getFromConfig("abilityModule",availableItemId++),Mode.NORMAL),"abilityModule","abilitymodule.png");
    /*public static final Item normalAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getFromConfig("normalAbilityModule",availableItemId++),Mode.NORMAL),"normalAbilityModule","normalmodule.png");
    public static final Item attackAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getFromConfig("attackAbilityModule",availableItemId++),Mode.ATTACK),"attackAbilityModule","attackmodule.png");
    public static final Item defenseAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getFromConfig("defenseAbilityModule",availableItemId++),Mode.DEFENSE),"defenseAbilityModule","defensemodule.png");
    public static final Item pursuitAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getFromConfig("pursuitAbilityModule",availableItemId++),Mode.PURSUIT),"pursuitAbilityModule","pursuitmodule.png");*/
    public static final Item awakenedAbilityModuel = ItemHelper.createItem(MOD_ID,new ItemAbilityModule(config.getFromConfig("awakenedAbilityModuel",availableItemId++),Mode.AWAKENED),"awakenedAbilityModule","awakenedmodule.png");

    public static final int[][] railTex = new int[][]{TextureHelper.getOrCreateBlockTexture(MOD_ID,"dilithium_rail_unpowered.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithium_rail.png")};
    public static final Block dilithiumRail = BlockHelper.createBlock(MOD_ID,new BlockDilithiumRail(key("dilithiumRail"),config.getFromConfig("dilithiumRail",availableBlockId++),true),"dilithium_rail_unpowered.png", BlockSounds.METAL,1,50f,0);

    public static final int[] energyOrbTex = TextureHelper.getOrCreateItemTexture(MOD_ID,"energyorb.png");

    public static final Weather weatherBloodMoon = new WeatherBloodMoon(10).setLanguageKey("bloodMoon");
    public static final Weather weatherEclipse = new WeatherEclipse(11).setLanguageKey("solarEclipse");
    public static final Weather weatherSolarApocalypse = new WeatherSolarApocalypse(12).setLanguageKey("solarApocalypse");

    public static final AchievementPage ACHIEVEMENTS = new SignalIndustriesAchievementPage();


    public static final Biome biomeEternity = Biomes.register("signalindustries:eternity",new Biome().setFillerBlock(realityFabric.id).setTopBlock(realityFabric.id).setColor(0x808080));
    public static final WorldType eternityWorld = WorldTypes.register("signalindustries:eternity",new WorldTypeEternity(key("eternity")));
    public static final Dimension dimEternity = new Dimension(key("eternity"),Dimension.overworld,1,portalEternity.id).setDefaultWorldType(eternityWorld);

    public static final Multiblock dimAnchorMultiblock = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"dimensionalAnchor","dimensionalAnchor",false);

    public static Map<String, BlockTexture> textures = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.info("Signal Industries initialized.");
    }

    static {
        try {
            Class.forName("net.minecraft.core.block.Block");
            Class.forName("net.minecraft.core.item.Item");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        textures.put(Tier.PROTOTYPE.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("prototype_blank.png").setSides("extractor_prototype_side_active.png"));
        textures.put(Tier.BASIC.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("basic_blank.png").setSides("extractor_basic_side_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setTopTexture("crusher_prototype_top_active.png").setNorthTexture("crusher_prototype_side.png"));
        textures.put(Tier.BASIC.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setTopTexture("crusher_basic_top_active.png").setNorthTexture("crusher_basic_side.png"));

        textures.put(Tier.PROTOTYPE.name()+".alloySmelter.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("alloy_smelter_prototype_active.png"));
        textures.put(Tier.BASIC.name()+".alloySmelter.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("alloy_smelter_basic_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".plateFormer.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("plate_former_prototype_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("crystal_cutter_prototype_active.png"));

        textures.put(Tier.BASIC.name()+".crystalChamber.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("basic_crystal_chamber_side_active.png"));

        textures.put(Tier.BASIC.name()+".infuser.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setSides("infuser_basic_side_active.png"));

        textures.put(Tier.BASIC.name()+".wrathBeacon.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setSides("wrath_beacon_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".pump.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setSides("prototype_pump_side.png").setTopTexture("prototype_pump_top.png"));

        textures.put("dimensionalAnchor.active",new BlockTexture(MOD_ID).setSides("dimensional_anchor.png").setBottomTexture("dimensional_anchor_bottom.png").setTopTexture("dimensional_anchor_top.png"));

        textures.put("dilithiumStabilizer.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_active.png").setNorthTexture("dilithium_top_active.png"));
        textures.put("dilithiumStabilizer.vertical",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_inactive.png").setTopTexture("dilithium_top_inactive.png"));
        textures.put("dilithiumStabilizer.vertical.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_active.png").setTopTexture("dilithium_top_active.png"));

        textures.put("dilithiumBooster.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_booster_side_active.png").setNorthTexture("dilithium_top_active.png"));

        textures.put(Tier.BASIC.name()+".externalIo",new BlockTexture(MOD_ID).setAll("external_io_blank.png").setTopTexture("external_io_input.png").setBottomTexture("external_io_output.png").setNorthTexture("external_io_both.png"));

        textures.put(Tier.REINFORCED.name()+".centrifuge.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setTopTexture("reinforced_centrifuge_closed.png").setNorthTexture("reinforced_centrifuge_front_active.png"));
        textures.put(Tier.REINFORCED.name()+".centrifuge.loaded",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setTopTexture("reinforced_centrifuge_loaded.png").setNorthTexture("reinforced_centrifuge_front_inactive.png"));

        textures.put(Tier.REINFORCED.name()+".ignitor.inverted",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_inactive_inverted.png").setTopTexture("reinforced_ignitor_bottom_inactive.png").setBottomTexture("reinforced_ignitor_top_inactive.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.active",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_active.png").setTopTexture("reinforced_ignitor_top_active.png").setBottomTexture("reinforced_ignitor_bottom_active.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.active",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_active_inverted.png").setTopTexture("reinforced_ignitor_bottom_active.png").setBottomTexture("reinforced_ignitor_top_active.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.ready",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_ready.png").setTopTexture("reinforced_ignitor_top_ready.png").setBottomTexture("reinforced_ignitor_bottom_ready.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.ready",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_ready_inverted.png").setTopTexture("reinforced_ignitor_bottom_ready.png").setBottomTexture("reinforced_ignitor_top_ready.png"));


        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_center.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom_left.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom_right.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_left.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_right.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_bottom.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_left_right.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_right.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_left.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_bottom.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_top.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_left.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_right.png");

        Dimension.registerDimension(config.getFromConfig("eternityDimId",3),dimEternity);
    }


    public SignalIndustries(){
        AchievementHelper.addPage(ACHIEVEMENTS);
        //RecipeFIleLoader.load("/assets/signalindustries/recipes/recipes.txt",mapOf(new String[]{"SignalIndustries"},new String[]{"sunsetsatellite.signalindustries.SignalIndustries"}));
        BlockModelDispatcher.getInstance().addDispatch(dilithiumRail,new BlockModelRenderBlocks(9));
        PacketAccessor.callAddIdClassMapping(config.getFromConfig("PacketOpenMachineGUI_ID",113),true,false, PacketOpenMachineGUI.class);

        ItemToolPickaxe.miningLevels.put(signalumOre,3);
        ItemToolPickaxe.miningLevels.put(dilithiumBlock,4);
        ItemToolPickaxe.miningLevels.put(dilithiumCrystalBlock,4);
        ItemToolPickaxe.miningLevels.put(emptyCrystalBlock,3);
        ItemToolPickaxe.miningLevels.put(rawCrystalBlock,3);
        ItemToolPickaxe.miningLevels.put(awakenedSignalumCrystalBlock,4);
        ItemToolPickaxe.miningLevels.put(rootedFabric,4);
        ItemToolPickaxe.miningLevels.put(dimensionalShardOre,4);
        ItemToolPickaxe.miningLevels.put(dilithiumOre,4);
        ItemToolPickaxe.miningLevels.put(realityFabric,5);
        ItemToolPickaxe.miningLevels.put(reinforcedGlass,3);
        ItemToolPickaxe.miningLevels.put(reinforcedCasing,3);

        CommandHelper.createCommand(new NBTEditCommand());
        CommandHelper.createCommand(new StructureCommand("structure","struct"));
        EntityHelper.createSpecialTileEntity(TileEntityConduit.class, new RenderFluidInConduit(),"Conduit");
        EntityHelper.createSpecialTileEntity(TileEntityFluidConduit.class, new RenderFluidInConduit(),"Fluid Conduit");
        EntityHelper.createEntity(EntityCrystal.class,new SnowballRenderer(signalumCrystal.getIconFromDamage(0)),47,"signalumCrystal");
        EntityHelper.createEntity(EntityEnergyOrb.class,new SnowballRenderer(Block.texCoordToIndex(energyOrbTex[0],energyOrbTex[1])),49,"energyOrb");

        EntityHelper.createSpecialTileEntity(TileEntityEnergyCell.class,new RenderFluidInBlock(),"Energy Cell");
        addToNameGuiMap("Energy Cell", GuiEnergyCell.class, TileEntityEnergyCell.class);

        EntityHelper.createSpecialTileEntity(TileEntitySIFluidTank.class,new RenderFluidInBlock(),"SI Fluid Tank");
        addToNameGuiMap("SI Fluid Tank", GuiSIFluidTank.class, TileEntitySIFluidTank.class);

        EntityHelper.createTileEntity(TileEntityExtractor.class,"Extractor");
        addToNameGuiMap("Extractor", GuiExtractor.class, TileEntityExtractor.class);

        EntityHelper.createTileEntity(TileEntityCrusher.class,"Crusher");
        addToNameGuiMap("Crusher", GuiCrusher.class, TileEntityCrusher.class);

        EntityHelper.createTileEntity(TileEntityAlloySmelter.class,"Alloy Smelter");
        addToNameGuiMap("Alloy Smelter", GuiAlloySmelter.class, TileEntityAlloySmelter.class);

        EntityHelper.createTileEntity(TileEntityPlateFormer.class,"Plate Former");
        addToNameGuiMap("Plate Former", GuiPlateFormer.class, TileEntityPlateFormer.class);

        EntityHelper.createTileEntity(TileEntityCrystalCutter.class,"Crystal Cutter");
        addToNameGuiMap("Crystal Cutter", GuiCrystalCutter.class, TileEntityCrystalCutter.class);

        EntityHelper.createTileEntity(TileEntityInfuser.class,"Infuser");
        addToNameGuiMap("Infuser", GuiInfuser.class, TileEntityInfuser.class);

        EntityHelper.createTileEntity(TileEntityBooster.class,"Dilithium Booster");
        addToNameGuiMap("Dilithium Booster", GuiBooster.class, TileEntityBooster.class);

        EntityHelper.createTileEntity(TileEntityStabilizer.class,"Dilithium Stabilizer");
        addToNameGuiMap("Dilithium Stabilizer", GuiStabilizer.class, TileEntityStabilizer.class);

        EntityHelper.createTileEntity(TileEntityCrystalChamber.class,"Crystal Chamber");
        addToNameGuiMap("Crystal Chamber", GuiCrystalChamber.class, TileEntityCrystalChamber.class);

        EntityHelper.createTileEntity(TileEntityPump.class,"Pump");
        addToNameGuiMap("Pump", GuiPump.class, TileEntityCrystalChamber.class);

        EntityHelper.createSpecialTileEntity(TileEntityDimensionalAnchor.class,new RenderMultiblock(),"Dimensional Anchor");
        addToNameGuiMap("Dimensional Anchor", GuiDimAnchor.class, TileEntityDimensionalAnchor.class);

        EntityHelper.createSpecialTileEntity(TileEntityAutoMiner.class, new RenderAutoMiner(),"Automatic Miner");
        addToNameGuiMap("Automatic Miner", GuiAutoMiner.class, TileEntityAutoMiner.class);

        EntityHelper.createTileEntity(TileEntityExternalIO.class,"External I/O");
        addToNameGuiMap("External I/O", GuiExternalIO.class, TileEntityExternalIO.class);

        EntityHelper.createTileEntity(TileEntityCentrifuge.class,"Separation Centrifuge");
        addToNameGuiMap("Separation Centrifuge", GuiCentrifuge.class, TileEntityCentrifuge.class);

        EntityHelper.createTileEntity(TileEntitySignalumReactor.class,"Signalum Reactor");
        addToNameGuiMap("Signalum Reactor", GuiSignalumReactor.class, TileEntitySignalumReactor.class);

        EntityHelper.createTileEntity(TileEntityEnergyConnector.class,"Energy Connector");
        addToNameGuiMap("Energy Connector", GuiEnergyConnector.class, TileEntityEnergyConnector.class);

        EntityHelper.createTileEntity(TileEntityIgnitor.class,"Signalum Ignitor");

        addToNameGuiMap("The Pulsar", GuiPulsar.class, InventoryPulsar.class);
        addToNameGuiMap("Signalum Prototype Harness", GuiHarness.class, InventoryHarness.class);

        EntityHelper.createTileEntity(TileEntityRecipeMaker.class,"Recipe Maker");
        EntityHelper.createTileEntity(TileEntityWrathBeacon.class,"Wrath Beacon");
        EntityHelper.createTileEntity(TileEntityBlockBreaker.class,"Block Breaker");

        Multiblock.multiblocks.put("dimensionalAnchor",dimAnchorMultiblock);
        SignalIndustries.LOGGER.info(String.format("Loaded %d multiblocks..",Multiblock.multiblocks.size()));
        SignalIndustries.LOGGER.info(String.format("Loaded %d internal structures.", Structure.internalStructures.size()));

        EntityHelper.createEntity(EntityInfernal.class,new MobRenderer<EntityInfernal>(new ModelZombie(), 0.5F),config.getFromConfig("infernalId",50),"Infernal");
        //crafting recipes in RecipeHandlerCraftingSI

    }

    public static <K,V> Map<K,V> mapOf(K[] keys, V[] values){
        if(keys.length != values.length){
            throw new IllegalArgumentException("Arrays differ in size!");
        }
        HashMap<K,V> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i],values[i]);
        }
        return map;
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... values){
        return new ArrayList<>(Arrays.asList(values));
    }

    public static void debug(String string, Object... args){
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        String trace = element.getFileName() + ":" + element.getLineNumber();
        LOGGER.info("["+trace+"] "+String.format(string,args));
    }

    public static void error(String string, Object... args){
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        String trace = element.getFileName() + ":" + element.getLineNumber();
        LOGGER.error("["+trace+"] "+String.format(string,args));
    }


    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, Container container, IInventory tile, int x, int y, int z) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen_si(guiScreen,container,tile,x,y,z);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(guiScreen);
        }
    }

    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, TileEntityWithName tile, int x, int y, int z) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen_si(guiScreen,tile,x,y,z);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(guiScreen);
        }
    }

    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, Container container, IInventory tile, ItemStack stack) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayItemGuiScreen_si(guiScreen,container,tile,stack);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(guiScreen);
        }
    }

    public static void addToNameGuiMap(String name, Class<? extends Gui> guiClass, Class<?> tileEntityClass){
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(guiClass);
        list.add(tileEntityClass);
        nameToGuiMap.put(name,list);
    }

    public static int getEnergyBurnTime(FluidStack stack) {
        if(stack == null) {
            return 0;
        } else {
            return stack.isFluidEqual(new FluidStack((BlockFluid) SignalIndustries.energyFlowing,1)) ? 100 : 0;
        }
    }

    public static void spawnParticle(EntityFX particle){
        if (Minecraft.getMinecraft(Minecraft.class) == null || Minecraft.getMinecraft(Minecraft.class).thePlayer == null || Minecraft.getMinecraft(Minecraft.class).effectRenderer == null)
            return;
        double d6 = Minecraft.getMinecraft(Minecraft.class).thePlayer.x - particle.x;
        double d7 = Minecraft.getMinecraft(Minecraft.class).thePlayer.y - particle.y;
        double d8 = Minecraft.getMinecraft(Minecraft.class).thePlayer.z - particle.z;
        double d9 = 16.0D;
        if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)
            return;
        Minecraft.getMinecraft(Minecraft.class).effectRenderer.addEffect(particle);
    }

    public static String key(String key){
        return HalpLibe.addModId(MOD_ID,key);
    }

    public static void usePortal(int dim) {
        Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
        Dimension lastDim = Dimension.getDimensionList().get(mc.thePlayer.dimension);
        Dimension newDim = Dimension.getDimensionList().get(dim);
        System.out.println("Switching to dimension \"" + newDim.getTranslatedName() + "\"!!");
        mc.thePlayer.dimension = dim;
        mc.theWorld.setEntityDead(mc.thePlayer);
        mc.thePlayer.removed = false;
        double d = mc.thePlayer.x;
        double d1 = mc.thePlayer.z;
        double newY = mc.thePlayer.y;
        d *= Dimension.getCoordScale(lastDim, newDim);
        d1 *= Dimension.getCoordScale(lastDim, newDim);
        mc.thePlayer.moveTo(d, newY, d1, mc.thePlayer.yRot, mc.thePlayer.xRot);
        if (mc.thePlayer.isAlive()) {
            mc.theWorld.updateEntityWithOptionalForce(mc.thePlayer, false);
        }

        World world;
        world = new World(mc.theWorld, newDim);
        if (newDim == lastDim.homeDim) {
            mc.changeWorld(world, "Leaving " + lastDim.getTranslatedName(), mc.thePlayer);
        } else {
            mc.changeWorld(world, "Entering " + newDim.getTranslatedName(), mc.thePlayer);
        }

        mc.thePlayer.world = mc.theWorld;
        if (mc.thePlayer.isAlive()) {
            mc.thePlayer.moveTo(d, newY, d1, mc.thePlayer.yRot, mc.thePlayer.xRot);
            mc.theWorld.updateEntityWithOptionalForce(mc.thePlayer, false);
        }
    }

}
